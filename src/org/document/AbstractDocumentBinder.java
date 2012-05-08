package org.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractDocumentBinder<T extends PropertyBinder> implements DocumentBinder<T> {

    private Object alias;
    protected List<DocumentChangeListener> documentListeners;
    protected List<BinderListener> binderListeners;
    protected Object id;
    protected String childName;
    protected List<DocumentBinder> childs;
    protected Map<String, List<T>> binders;
    protected Map<String, List<T>> errorBinders;
    protected List<T> documentErrorBinders;
    //protected DocumentStore documentStore;
    protected Document document;
    protected ValidatorCollection validators;

    protected AbstractDocumentBinder() {
        binders = new HashMap<String, List<T>>();
        binderListeners = new ArrayList<BinderListener>();
        errorBinders = new HashMap<String, List<T>>();
        documentErrorBinders = new ArrayList<T>();
        validators = new ValidatorCollection();
        childs = new ArrayList<DocumentBinder>();

    }

    protected AbstractDocumentBinder(Object alias) {
        this();
        this.alias = alias;
    }

    @Override
    public Object getAlias() {
        return alias;
    }

    @Override
    public Object getComponentValue() {
        return null;
    }

    @Override
    public void addBinderListener(BinderListener l) {
        this.binderListeners.add(l);
    }

    @Override
    public void removeBinderListener(BinderListener l) {
        this.binderListeners.remove(l);
    }

    /**
     * The
     * <code>id</code> is a user defined identifier.
     *
     * @return user defined identifier
     */
//    @Override
//    public Object getId() {
//        return id;
//    }
    protected void add(T binder, Map<String, List<T>> binderMap) {
        String propPath = ((PropertyBinder) binder).getPropertyName();
        List<T> blist = binderMap.get(propPath);
        if (blist == null) {
            blist = new ArrayList<T>();
        }
        binder.addBinderListener(this);
        addDocumentChangeListener(binder);
        blist.add(binder);
        binderMap.put(propPath, blist);
        //binder.setDocumentBinding(this);

    }

    protected void add(T binder, List<T> binderList) {
        binderList.add(binder);
        binder.addBinderListener(this);
        addDocumentChangeListener(binder);
//        binder.setDocumentBinding(this);

    }

    @Override
    public void add(T binder) {
        if (binder instanceof ErrorBinder) {
            if (((ErrorBinder) binder).getPropertyName() == null) {
                // documentStore error binder
                add(binder, documentErrorBinders);
            } else {
                add(binder, errorBinders);
            }
        } else if (binder instanceof PropertyBinder) {
            add(binder, binders);
        }
    }

    protected void remove(T binder, Map<String, List<T>> binderMap) {
        String propPath = ((PropertyBinder) binder).getPropertyName();
        List<T> blist = binderMap.get(propPath);
        if (blist == null || blist.isEmpty()) {
            return;
        }
        binder.removeBinderListener(this);
        removeDocumentChangeListener(binder);
        blist.remove(binder);
        if (blist.isEmpty()) {
            binderMap.remove(((PropertyBinder) binder).getPropertyName());
        }

    }

    protected void remove(T binder, List<T> binderList) {
        binderList.remove(binder);
        binder.removeBinderListener(this);
        removeDocumentChangeListener(binder);
//        binder.setDocumentBinding(this);

    }

    @Override
    public void remove(T binder) {
        if (binder instanceof ErrorBinder) {
            if (((ErrorBinder) binder).getPropertyName() == null) {
                // documentStore error binder
                remove(binder, documentErrorBinders);
            } else {
                remove(binder, errorBinders);
            }
        } else if (binder instanceof PropertyBinder) {
            remove(binder, binders);
        }
    }

    public Map<String, List<T>> getBinders() {
        return this.binders;
    }

    public List<T> getBinders(String propertyName) {
        return this.binders.get(propertyName);
    }

    public Map<String, List<T>> getErrorBinders() {
        return this.errorBinders;
    }

    public List<T> getErrorBinders(String propertyName) {
        return this.errorBinders.get(propertyName);
    }

    public List<T> getDocumentErrorBinders() {
        return this.documentErrorBinders;
    }

    protected void firePropertyChange(DocumentChangeEvent event) {
        String propName = event.getPropertyName();
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        List<T> blist = binders.get(propName);
        if (blist == null) {
            return;
        }
        for (Binder b : blist) {
            //b.dataChanged(oldValue, newValue);
            //b.dataChanged(newValue);
            b.react(event);
        }
    }

    @Override
    public Document getDocument() {
        return document;
    }

    public PropertyDataStore getDocumentStore() {
        //return this.documentStore;
        return document.getPropertyDataStore();
    }

    @Override
    public void setDocument(Document object) {
        PropertyDataStore oldDocumentStore = null;
        if (document != null) {
            oldDocumentStore = getDocumentStore();
        }

        if (oldDocumentStore != null) {
            completeChanges();
            if (oldDocumentStore instanceof HasDocumentState) {
                DocumentState state = ((HasDocumentState) oldDocumentStore).getDocumentState();
                state.setEditing(false);
            }
        }
        Document oldDocument = this.document;
        if (object == null) {
            this.document = null;
        }

        this.document = object;

        if (this.document != null) {
            getDocumentStore().addDocumentChangeListener(this);
        }
        if (oldDocument != null && oldDocument != document) {
            oldDocumentStore.removeDocumentChangeListener(this);
        }

        if (document == null) {
            return;
        }

        fireDocumentChanged(oldDocument, document);
        //refresh();

        PropertyDataStore documentStore = getDocumentStore();

        if (documentStore instanceof HasDocumentState) {
            DocumentState state = ((HasDocumentState) documentStore).getDocumentState();
            if (state.isEditing()) {
                for (Map.Entry<String, List<T>> e : binders.entrySet()) {
                    List<T> l = e.getValue();
                    if (l == null) {
                        continue;
                    }
                    for (T b : l) {
                        b.init(state.getDirtyValues().get(b.getPropertyName()));
                    }
                }
                completeChanges();
                fireDocumentError(null);
                try {
                    getValidators().validate(document);
                } catch (ValidationException e) {
                    //this.notifyDocumentError(e);
                    fireDocumentError(e);
                }
            }
        }



        for (DocumentBinder child : childs) {
            Object d = documentStore.get(child.getChildName());
            if (d == null) {
                child.setDocument((Document) null);
            } else if (d instanceof Document) {
                child.setDocument((Document) d);
            }
        }
    }

    private void fireDocumentChanged(Document oldDoc, Document newDoc) {
        if (this.documentListeners == null || documentListeners.isEmpty()) {
            return;
        }

        DocumentChangeEvent event = new DocumentChangeEvent(document, DocumentChangeEvent.Action.documentChange);
        event.setOldValue(oldDoc);
        event.setNewValue(newDoc);

        for (DocumentChangeListener l : documentListeners) {
            l.react(event);
        }
    }

    
    protected void completeChanges() {
        if (document.getPropertyDataStore() == null) {
            return;
        }
        DocumentChangeEvent event = new DocumentChangeEvent(this, DocumentChangeEvent.Action.completeChanges);
        for (Map.Entry<String, List<T>> ent : this.binders.entrySet()) {
            for (Binder b : ent.getValue()) {
                //b.componentChanged(b.getComponentValue());
                b.react(event);
            }
        }
    }

    protected void firePropertyError(String propertyName, Exception e) {
        if (this.documentListeners == null || documentListeners.isEmpty()) {
            return;
        }

        DocumentChangeEvent event = new DocumentChangeEvent(document, DocumentChangeEvent.Action.propertyError);
        event.setPropertyName(propertyName);
        event.setException(e);

        for (DocumentChangeListener l : documentListeners) {
            if ((l instanceof PropertyErrorBinder) && propertyName.equals(((PropertyBinder) l).getPropertyName())) {
                l.react(event);
            }
        }

    }

    protected void fireDocumentError(Exception e) {
        if (this.documentListeners == null || documentListeners.isEmpty()) {
            return;
        }

        DocumentChangeEvent event = new DocumentChangeEvent(document, DocumentChangeEvent.Action.documentError);
        event.setException(e);

        for (DocumentChangeListener l : documentListeners) {
            if (!(l instanceof DocumentErrorBinder)) {
                continue;
            }
            l.react(event);
        }

    }

    @Override
    public ValidatorCollection getValidators() {
        return validators;
    }

    @Override
    public void validate(String propPath, Object value) throws ValidationException {
        getValidators().validate(propPath, getDocumentStore(), value);
    }

    protected void validate() throws ValidationException {
        getValidators().validate(getDocument());
    }

    protected abstract DocumentBinder create();

    @Override
    public DocumentBinder createChild(String childName) {
        DocumentBinder binder = create();
        binder.setChildName(childName);
        childs.add(binder);
        if (document.getPropertyDataStore() != null) {
            binder.setDocument((Document) getDocumentStore().get(childName));
        }
        return binder;
    }

    @Override
    public String getChildName() {
        return this.childName;
    }

    @Override
    public void setChildName(String childName) {
        this.childName = childName;
    }

    /*
     * protected void update(DocumentChangeEvent event) { Document selected =
     * (Document)event.getNewValue(); if (this.document == selected) { return; }
     * Document old = this.document; if (old != null) { DocumentBinder b =
     * (DocumentBinder)event.getOldValue(); if (b != null) {
     * //b.setDocument(old); b.completeChanges(); } } this.document = selected;
     * if (selected != null) { DocumentBinder b =
     * (DocumentBinder)event.getNewValue(); if (b != null) {
     * b.setDocument(selected); } //listBinding.setDocument(selected); } }
     */
    @Override
    public void react(DocumentChangeEvent event) {
        if (event.getAction().equals(DocumentChangeEvent.Action.editingChange)) {
            if ((Boolean) event.getOldValue() && !(Boolean) event.getNewValue()) {
                this.validateOnEndEditing();
            }
        } else //  this.validate(event.getPropertyName(), event.getNewValue());        
        if (event.getAction().equals(DocumentChangeEvent.Action.documentChange)) {
            setDocument((Document) event.getNewValue());
        } else if (event.getAction().equals(DocumentChangeEvent.Action.propertyChange)) {
            firePropertyChange(event);
            //} else if (event.getAction().equals(DocumentChangeEvent.Action.validateProperty)) {
            //  this.validate(event.getPropertyName(), event.getNewValue());
        } /*else if (event.getAction().equals(DocumentChangeEvent.Action.validateErrorNotify)) {
            if (((ValidationException) event.getException()).getPropertyName() == null) {
                // documentStore error
                //(event.getException());
                fireDocumentError(event.getException());
            } else {
                //notifyPropertyError(event.getPropertyName(), event.getException());
                firePropertyError(event.getPropertyName(), event.getException());
            }
        } 
        */
        
        /*else if (event.getAction().equals(DocumentChangeEvent.Action.validateAllProperties)) {
            //validators.validateProperties((DocumentStore) event.getSource());
            for (Map.Entry<String, List<T>> e : binders.entrySet()) {
                List<T> l = e.getValue();
                if (l == null) {
                    continue;
                }
                for (T b : l) {
                    this.validate(b.getPropertyName(), b.getComponentValue());
                }
            }
        } else if (event.getAction().equals(DocumentChangeEvent.Action.validateDocument)) {
            this.validate();
        }
        */
    }

    protected void validateOnEndEditing() {
        for (Map.Entry<String, List<T>> e : binders.entrySet()) {
            List<T> l = e.getValue();
            if (l == null) {
                continue;
            }
            for (T b : l) {
                try {
                    validate(b.getPropertyName(), b.getComponentValue());
                } catch (ValidationException ex) {
                    firePropertyError(b.getPropertyName(), ex);
                    throw ex;
                }
            }
        }
        try {
            validate();
        } catch (ValidationException ex) {
            fireDocumentError(ex);
            throw ex;
        }


    }

    @Override
    public void react(BinderEvent event) {

        switch (event.getAction()) {
            case componentValueChange:
                if (getDocumentStore() instanceof HasDocumentState) {
                    ((HasDocumentState) getDocumentStore()).getDocumentState().react(event);
                }

                if (!needChangeData(event.getPropertyName(), event.getDataValue())) {
                    return;
                }
                try {
                    validate(event.getPropertyName(), event.getDataValue());
                    getDocumentStore().put(event.getPropertyName(), event.getDataValue());
                } catch (ValidationException ex) {
                    firePropertyError(event.getPropertyName(), ex);
                }

                //fireDocumentError(event.getException());
                break;
            case componentChangeValueError:

                break;
            case clearComponentChangeError:
                firePropertyError(event.getPropertyName(), event.getException());
                break;

        }
    }

    /**
     * Prepends cyclic data modifications.
     *
     * @param value a new value to be assigned
     * @return
     */
    protected boolean needChangeData(String propertyName, Object value) {
        boolean result = true;
        Object currentValue = getDocumentStore().get(propertyName);

        if (value == null && currentValue == null) {
            result = false;
        }
        if (value != null && value.equals(currentValue)) {
            result = false;
        } else if (currentValue != null && currentValue.equals(value)) {
            result = false;
        }
        return result;
    }

    @Override
    public void addDocumentChangeListener(DocumentChangeListener l) {
        if (documentListeners == null) {
            documentListeners = new ArrayList<DocumentChangeListener>();
        }
        documentListeners.add(l);
    }

    @Override
    public void removeDocumentChangeListener(DocumentChangeListener l) {
        if (documentListeners == null) {
            return;
        }
        documentListeners.remove(l);
    }
}
