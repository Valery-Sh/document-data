package org.document.binding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.document.*;

/**
 *
 * @author V. Shishkin
 */
public abstract class AbstractBindingManager<T extends Document> implements BinderListener {

    protected static final String DEFAULT_ALIAS = "default alias";
    private BinderSet binders;
//    protected Map<Object, DocumentBindings> documentBindings;
//    protected ListBindings listBinding;
    protected T selected;
    protected ValidatorCollection validators;
    protected BindingRecognizer recognizer;
    private List<DocumentSelectListener> selectDocumentListeners;
    private List<T> sourceList;
    
    public AbstractBindingManager(List<T> sourceList) {
        this();
        this.sourceList = sourceList;
    }

    protected AbstractBindingManager() {

        binders = new BinderSet(this);

//        documentBindings = new HashMap<Object, DocumentBindings>();
        validators = new ValidatorCollection();
        selectDocumentListeners = new ArrayList<DocumentSelectListener>();
//        listBinding = new DocumentListBindings();
//        listBinding.addDocumentChangeListener(this);
    }
    
    protected AbstractBindingManager(BindingRecognizer recognizer) {
        this();
        this.recognizer = recognizer;
    }
    protected BinderSet getBinders() {
        return this.binders;
    }
    public T getSelected() {
        return selected;
    }

    public void setSelected(T selected) {
        //this.update(selected);
        T old = this.selected;
        
        boolean markedNew = false;
        this.selected = selected;
        this.binders.setDocument(selected);
        
        afterSetSelected(old);
        
        
        fireSelectDocument(old, selected);
    }
    
//    protected abstract void beforeSetSelected(T oldSelected);
    protected abstract void afterSetSelected(T oldSelected);
    //protected abstract boolean isNew(T doc);
    
    private void fireSelectDocument(T oldSelected, T newSelected) {
        DocumentSelectEvent event = new DocumentSelectEvent(this, oldSelected, newSelected);
        for (DocumentSelectListener l : selectDocumentListeners) {
            l.documentSelect(event);
        }
    }

    /*
     * public void addBinder(Object docTypeId, Binder binder) { assert(docTypeId
     * != null); assert(binder != null);
     *
     * if ( binder instanceof PropertyBinder) { addPropertyBinder(docTypeId,
     * binder); } else if ( binder instanceof ErrorBinder) {
     * addErrorBinder(docTypeId, binder); } else if ( binder instanceof
     * ListBinder) { addListBinder(docTypeId, binder); } }
     */
    public void addBinder(Binder binder) {
        if (binder instanceof ListBinder) {
            binder.addBinderListener(this);
        }
        this.binders.add(binder);
        //addBinder("default doc type",binder);
    }

    public void removeBinder(Binder binder) {
        if (binder instanceof ListBinder) {
            binder.removeBinderListener(this);
        }
        this.binders.add(binder);
        //addBinder("default doc type",binder);
    }

    /*
     * protected void addPropertyBinder(Object docTypeId, Binder binder) { if (
     * ! documentBindings.containsKey(docTypeId)) { DocumentBindings db = new
     * DocumentBindingHandler(); documentBindings.put(docTypeId, db); }
     * documentBindings.get(docTypeId).add(binder); } protected void
     * addErrorBinder(Object docTypeId, Binder binder) {
     * addPropertyBinder(docTypeId, binder); }
     *
     * protected void addListBinder(Object docTypeId, Binder binder) {
     * listBinding.add(binder); }
     */
    /*
     * public ValidatorCollection getValidators(Object docTypeId) {
     * DocumentBindings b = documentBindings.get(docTypeId); if ( b == null ) {
     * return null; } return b.getValidators(); }
     *
     * public ValidatorCollection getValidators() { return
     * getValidators("default doc type"); }
     */
    protected PropertyStore getPropertyDataStore() {
        return this.selected.getPropertyStore();
    }

    public void setRecognizer(BindingRecognizer recognizer) {
        this.recognizer = recognizer;
    }
    //
    // ===========================================================
    //

    public Object getAlias(Document doc) {
        Object result;
        if (doc == null) {
            return null;
        }

        if (recognizer != null) {
            result = recognizer.getBindingId(doc);
        } else {
            result = DEFAULT_ALIAS;
        }
        return result;

    }

    public DocumentBinder getDocumentBinder(Object alias) {
        Object a = DEFAULT_ALIAS;
        if (alias != null) {
            a = alias;
        }

        Set<Binder> set = binders.getSubset(a);
        DocumentBinder result = null;
        for (Binder b : set) {
            if (b instanceof DocumentBinder) {
                result = (DocumentBinder) b;
                break;
            }
        }
        if (result == null) {
            result = new DocumentBinder(a);
            binders.add(result);
        }
        return result;
    }

    /*
     * protected DocumentBindings getBinding(Document doc) { if (doc == null) {
     * return null; }
     *
     * DocumentBindings result; if (recognizer != null) { result =
     * documentBindings.get(recognizer.getBindingId(doc)); } else { result =
     * documentBindings.get("default doc type"); } return result; }
     */
    @Override
    public void react(BinderEvent event) {
        switch (event.getAction()) {
            case componentSelectChange:
                T newDoc = (T) event.getDataValue();
                if (newDoc == this.selected) {
                    return;
                }
                setSelected(newDoc);
                break;

        }
    }

    /**
     * Prepends cyclic data modifications.
     *
     * @param value a new value to be assigned
     * @return
     */
    protected boolean needChangeSelected(Document document) {
        if (document == this.selected) {
            return false;
        }
        return true;
    }

    public void addSelectDocumentListener(DocumentSelectListener l) {
        this.selectDocumentListeners.add(l);
    }

    public void removeSelectDocumentListener(DocumentSelectListener l) {
        this.selectDocumentListeners.remove(l);
    }

    public static class DefaultBindingRecognizerNew<T> implements BindingRecognizer<T> {

        @Override
        public Object getBindingId(T document) {
            return document.getClass();
        }
    }

    public static class BinderSet<T extends Binder> implements BinderCollection<T> {

        private AbstractBindingManager bindingManager;
        private Document selected;
        private Set<T> binders;

        public BinderSet(AbstractBindingManager bindingManager) {
            this.binders = new HashSet<T>();
            this.bindingManager = bindingManager;
        }

        @Override
        public void add(T binder) {
            binders.add(binder);
        }

        @Override
        public void remove(T binder) {
            binders.remove(binder);
        }

        public Set<T> getSubset(Object alias) {
            Set<T> subset = new HashSet<T>();
            for (T b : binders) {
                if (b instanceof HasDocumentAlias) {
                    Object a = ((HasDocumentAlias) b).getAlias();
                    if (a == alias) {
                        subset.add(b);
                    } else if (a != null && a.equals(alias)) {
                        subset.add(b);
                    } else if (alias != null && alias.equals(a)) {
                        subset.add(b);
                    }
                }
            }
            return subset;
        }

        @Override
        public void setDocument(Document newDocument) {

            Document oldSelected = this.selected;
            this.selected = newDocument;

            for (T b : binders) {
                DocumentChangeEvent e = new DocumentChangeEvent(this, DocumentChangeEvent.Action.documentChange);
                e.setOldValue(oldSelected);
                e.setNewValue(newDocument);
                b.react(e);
            }
        }

        @Override
        public Document getDocument() {
            return this.selected;
        }

        @Override
        public void addDocumentChangeListener(DocumentChangeListener l) {
        }
        
        @Override
        public void removeDocumentChangeListener(DocumentChangeListener l) {
        }

        @Override
        public void react(DocumentChangeEvent event) {
        }

        @Override
        public void listChanged(ListChangeEvent event) {
            for (T b : binders) {
                if ( b instanceof ListChangeListener) {
                    ((ListChangeListener)b).listChanged(event);
                }            
            }

        }
    }
}//class AbstractBindingManager