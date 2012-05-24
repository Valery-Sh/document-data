package org.document.binding;

import java.util.*;
import org.document.*;

/**
 *
 * @author V. Shishkin
 */
public abstract class AbstractBindingManager<T extends Document> implements BinderListener {

    protected static final String DEFAULT_ALIAS = "default alias";
    private BinderMap documentBinders;
//    protected Map<Object, DocumentBindings> documentBindings;
//    protected ListBindings listBinding;
    protected T selected;
    //protected ValidatorCollection validators;
//    protected Map<Object,Validator> validators;
    
    protected BindingRecognizer recognizer;
    //private List<DocumentSelectListener> selectDocumentListeners;
    private List<T> sourceList;
    
    private ListState listState;
    private BinderMap documentListBinders;    
    
    public AbstractBindingManager(List sourceList) {
        this();
        this.sourceList = sourceList;
        this.listState = new ListState();
        listState.setDocumentList(new DocumentList(sourceList));
        documentListBinders = new BinderMap(this);
        //validators = new HashMap<Object,Validator>();
    }
    
    protected AbstractBindingManager() {
        documentBinders = new BinderMap(this);
    }
/*    public void addValidator(Object alias, Validator validator) {
        validators.put(alias, validator);
    }
*/    
    protected ListState getListState() {
        return this.listState;
    }
    
    protected BinderMap getBinders() {
        return this.documentBinders;
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
        this.documentBinders.setDocument(selected);
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
        this.documentBinders.setDocument(selected);
    }
    
    protected abstract void afterSetSelected(T oldSelected);
    

    public void addBinder(Binder binder) {
        if (binder instanceof ListStateBinder) {
            if ( sourceList == null ) {
                throw new IllegalArgumentException("A List Binders are not supported wnen no source list is defined");
            }
            binder.addBinderListener(this);
            documentListBinders.add(binder);
            ((ListStateBinder)binder).setDocument(getListState());
            
        } else {
            throw new IllegalArgumentException("The 'binder' argument type must be  org.document.ListStateBinder or it's subclass");
        }
        
        
        //else {
          //  binders.add(binder);
        //}
    }

    public void removeBinder(Binder binder) {
        if (binder instanceof ListStateBinder) {
            binder.removeBinderListener(this);
            documentListBinders.remove(binder);
        }// else {
         //   this.binderCollection.add(binder);
        //}
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
        
        DocumentBinder result = (DocumentBinder)documentBinders.get(a);
        if (result == null) {
            result = new DocumentBinder(a);
            documentBinders.add(result);
        }
        return result;
    }

/*    public DocumentBinder getDocumentBinder(Object alias) {
        Object a = DEFAULT_ALIAS;
        if (alias != null) {
            a = alias;
        }

        Set<Binder> set = documentBinders.getSubset(a);
        DocumentBinder result = null;
        for (Binder b : set) {
            if (b instanceof DocumentBinder) {
                result = (DocumentBinder) b;
                break;
            }
        }
        if (result == null) {
            result = new DocumentBinder(a);
            documentBinders.add(result);
        }
        return result;
    }
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

    public static class BinderMap<T extends Binder> implements BinderContainer<T> {

        private AbstractBindingManager bindingManager;
        private Document selected;
        private Map<Object,T> binders;

        public BinderMap(AbstractBindingManager bindingManager) {
            this.binders = new HashMap<Object,T>();
            this.bindingManager = bindingManager;
        }
        /**
         * Now doesn't in use
         * @return 
         */
        @Override
        public Object getAlias() {
            return "documentBinders";
        }
        
        @Override
        public void add(T binder) {
            binders.put(((BinderContainer)binder).getAlias(),binder);
        }

        @Override
        public void remove(T binder) {
            binders.remove(binder);
        }
        public T get(Object alias) {
            return this.binders.get(alias);
        }

/*        public Set<T> getSubset(Object alias) {
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
*/
        @Override
        public void setDocument(Document newDocument) {

            Document oldSelected = this.selected;
            this.selected = newDocument;

            for (T b : binders.values()) {
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

        //@Override
/*        public void addDocumentChangeListener(DocumentChangeListener l) {
        }
        
        //@Override
        public void removeDocumentChangeListener(DocumentChangeListener l) {
        }
*/
        @Override
        public void react(DocumentChangeEvent event) {
        }

    }
}//class AbstractBindingManager