package org.document.binding;

import java.util.*;
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
    //protected ValidatorCollection validators;
    protected Map<Object,Validator> validators;
    
    protected BindingRecognizer recognizer;
    //private List<DocumentSelectListener> selectDocumentListeners;
    private List<T> sourceList;
    
    private ListState listState;
    private BinderSet listBinders;    
    
    public AbstractBindingManager(List sourceList) {
        this();
        this.sourceList = sourceList;
        this.listState = new ListState();
        listState.setDocumentList(new DocumentList(sourceList));
        listBinders = new BinderSet(this);
        validators = new HashMap<Object,Validator>();
    }

    
    protected AbstractBindingManager() {
        binders = new BinderSet(this);
        
    }
    public void addValidator(Object alias, Validator validator) {
        validators.put(alias, validator);
    }
    
    protected ListState getListState() {
        return this.listState;
    }
    
    //public abstract AbstractBindingManager getBindingManager();
    
    protected BinderSet getBinders() {
        return this.binders;
    }
    public T getSelected() {
        return selected;
    }

    public void setSelected(T selected) {
        if ( sourceList == null ) {
            setDocument(selected);
            return;
        }
        
        T old = this.selected;
        
        boolean markedNew = false;
        this.binders.setDocument(selected);
        Object o = listState.getSelected();
        this.listState.setSelected(selected);
        this.selected = selected;
        
        afterSetSelected(old);
    }

    public void setDocument(T selected) {
        if ( sourceList != null ) {
            setSelected(selected);
            return;
        }

        T old = this.selected;
        
        boolean markedNew = false;
        this.binders.setDocument(selected);
    }
    
    protected abstract void afterSetSelected(T oldSelected);
    

    public void addBinder(Binder binder) {
        if (binder instanceof ListStateBinder) {
            if ( sourceList == null ) {
                throw new IllegalArgumentException("A List Binders are not supported wnen no source list is defined");
            }
            binder.addBinderListener(this);
            listBinders.add(binder);
            ((ListStateBinder)binder).setDocument(getListState());
        } else {
            binders.add(binder);
        }
    }

    public void removeBinder(Binder binder) {
        if (binder instanceof ListStateBinder) {
            binder.removeBinderListener(this);
            listBinders.remove(binder);
        } else {
            this.binders.add(binder);
        }
    }

    protected PropertyStore getPropertyStore() {
        return selected.propertyStore();
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
    public DocumentBinder getDocumentBinder(Document doc) {
        return getDocumentBinder(getAlias(doc));
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

/*        @Override
        public void listChanged(ListChangeEvent event) {
            for (T b : binders) {
                if ( b instanceof ListChangeListener) {
                    ((ListChangeListener)b).listChanged(event);
                }            
            }

        }
*/
    }
}//class AbstractBindingManager