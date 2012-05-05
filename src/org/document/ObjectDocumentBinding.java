/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Valery
 */
public class ObjectDocumentBinding implements DocumentBinding {
    
    protected List<DocumentListener> documentListeners;
    
    protected Object id;
    protected String childName;
    protected List<DocumentBinding> childs;
    protected Map<String, List<Binder>> binders;
    protected Map<String, List<Binder>> errorBinders;
    protected List<Binder> documentErrorBinders;
    //protected DocumentStore documentStore;
    protected Document document;
    
    protected ValidatorCollection validators;

    public ObjectDocumentBinding(Object id) {
        this();
        this.id = id;
    }

    public ObjectDocumentBinding() {
        binders = new HashMap<String, List<Binder>>();
        errorBinders = new HashMap<String, List<Binder>>();
        documentErrorBinders = new ArrayList<Binder>();
        validators = new ValidatorCollection();
        childs = new ArrayList<DocumentBinding>();
    }

    protected ObjectDocumentBinding(String childName) {
        this();
        this.childName = childName;
    }

    /**
     * The
     * <code>id</code> is a user defined identifier.
     *
     * @return user defined identifier
     */
    @Override
    public Object getId() {
        return id;
    }

    protected void add(Binder binder, Map<String, List<Binder>> binderMap) {
        String propPath = binder.getPropertyName();
        List<Binder> blist = binderMap.get(propPath);
        if (blist == null) {
            blist = new ArrayList<Binder>();
        }
        binder.addBinderListener(this);
        addDocumentListener(binder);
        blist.add(binder);
        binderMap.put(propPath, blist);
        //binder.setDocumentBinding(this);

    }

    protected void add(Binder binder, List<Binder> binderList) {
        binderList.add(binder);
        binder.addBinderListener(this);
        addDocumentListener(binder);        
//        binder.setDocumentBinding(this);

    }

    @Override
    public void add(Binder binder) {
        if (binder instanceof ErrorBinder) {
            if (binder.getPropertyName() == null) {
                // documentStore error binder
                add(binder, documentErrorBinders);
            } else {
                add(binder, errorBinders);
            }
        } else {
            add(binder, binders);
        }
    }

    protected void remove(Binder binder, Map<String, List<Binder>> binderMap) {
        String propPath = binder.getPropertyName();
        List<Binder> blist = binderMap.get(propPath);
        if (blist == null || blist.isEmpty()) {
            return;
        }
        binder.removeBinderListener(this);
        removeDocumentListener(binder);
        blist.remove(binder);
        if (blist.isEmpty()) {
            binderMap.remove(binder.getPropertyName());
        }

    }
    protected void remove(Binder binder, List<Binder> binderList) {
        binderList.remove(binder);
        binder.removeBinderListener(this);
        removeDocumentListener(binder);
//        binder.setDocumentBinding(this);

    }

    @Override
    public void remove(Binder binder) {
        if (binder instanceof ErrorBinder) {
            if (binder.getPropertyName() == null) {
                // documentStore error binder
                remove(binder, documentErrorBinders);
            } else {
                remove(binder, errorBinders);
            }
        } else {
            remove(binder, binders);
        }
    }
    public Map<String, List<Binder>> getBinders() {
        return this.binders;
    }
    public List<Binder> getBinders(String propertyName) {
        return this.binders.get(propertyName);
    }
    public Map<String, List<Binder>> getErrorBinders() {
        return this.errorBinders;
    }
    
    public List<Binder> getErrorBinders(String propertyName) {
        return this.errorBinders.get(propertyName);
    }
    
    public List<Binder> getDocumentErrorBinders() {
        return this.documentErrorBinders;
    }
    
    protected void firePropertyChange(String propName, Object oldValue, Object newValue) {

        List<Binder> blist = binders.get(propName);
        if (blist == null) {
            return;
        }
        for (Binder b : blist) {
            //b.dataChanged(oldValue, newValue);
            b.dataChanged(newValue);
        }
    }

    protected void firePropertyChange(Binder binder, Object oldValue, Object newValue) {
        List<Binder> blist = binders.get(binder.getPropertyName());
        if (blist == null) {
            return;
        }
        for (Binder b : blist) {
            if (b == binder) {
                continue;
            }
//            b.dataChanged(oldValue, newValue);
            b.dataChanged(newValue);
        }
    }
    

    public Document getDocument() {
        return this.document;
    }

    public DocumentStore getDocumentStore() {
        //return this.documentStore;
        return this.document.getDocumentStore();
    }

    @Override
    public void setDocument(Document object) {
        DocumentStore oldDocumentStore = null;
        if ( this.document != null ) { 
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
        if ( object == null ) {
            this.document = null;
        } 
        
        this.document = object;

        if (this.document != null) {
            getDocumentStore().addDocumentListener(this);
        } 
        if (oldDocument != null && oldDocument !=  document ) {
            oldDocumentStore.removeDocumentListener(this);
        }

        if ( document == null ) {
            return;
        }
        
        fireDocumentChanged(oldDocument, document);
        //refresh();
        
        DocumentStore documentStore = getDocumentStore();
        
        if (documentStore instanceof HasDocumentState) {
            DocumentState state = ((HasDocumentState) documentStore).getDocumentState();
            if (state.isEditing()) {
                for (Map.Entry<String, List<Binder>> e : binders.entrySet()) {
                    List<Binder> l = e.getValue();
                    if (l == null) {
                        continue;
                    }
                    for (Binder b : l) {
                        b.init(state.getDirtyValues().get(b.getPropertyName()));
                    }
                }
                this.completeChanges();
                //this.notifyDocumentError(null);
                fireDocumentError(null);
                try {
                    getValidators().validate(document);
                } catch (ValidationException e) {
                    //this.notifyDocumentError(e);
                    fireDocumentError(e);
                }
            }
        }
        
        
        
        for (DocumentBinding child : childs) {
            Object d = documentStore.get(child.getChildName());
            if (d == null) {
                child.setDocument((Document) null);
            } else if (d instanceof Document) {
                child.setDocument((Document) d);
            }
        }
    }
    private void fireDocumentChanged(Document oldDoc, Document newDoc ) {
        if ( this.documentListeners == null || documentListeners.isEmpty()) {
            return;
        }

        DocumentEvent event = new DocumentEvent(document, DocumentEvent.Action.documentChange);
        event.setOldValue(oldDoc);
        event.setNewValue(newDoc);
        
        for ( DocumentListener l : documentListeners) {
            l.react(event);
        }
    }
/*    protected void refresh() {
        for (Map.Entry<String, List<Binder>> ent : this.binders.entrySet()) {
            for (Binder b : ent.getValue()) {
                //b.init(getDocumentStore().get(b.getPropertyName()));
                notifyPropertyError(b.getPropertyName(), null);
            }
        }
        notifyDocumentError(null);

    }
*/
    protected void resolvePendingChanges() {
        for (Map.Entry<String, List<Binder>> ent : this.binders.entrySet()) {
            for (Binder b : ent.getValue()) {
                b.componentChanged(b.getComponentValue());
            }
        }
    }

    @Override
    public void completeChanges() {
        if (document.getDocumentStore() == null) {
            return;
        }

        this.resolvePendingChanges();

        /*
         * for (DocumentBinding child : childs) { Object d =
         * this.documentStore.get(child.getChildName()); if (d != null && (d
         * instanceof DocumentStore)) { child.completeChanges(); } }
         *
         */
    }


    protected void firePropertyError(String propertyName, Exception e) {
        if ( this.documentListeners == null || documentListeners.isEmpty()) {
            return;
        }

        DocumentEvent event = new DocumentEvent(document, DocumentEvent.Action.propertyErrorNotify);
        event.setPropertyName(propertyName);
        event.setException(e);
        
        for ( DocumentListener l : documentListeners) {
            if ( (l instanceof PropertyErrorBinder) && propertyName.equals(((Binder)l).getPropertyName()) ) {
                l.react(event);
            }
        }
        
    }
    
    protected void fireDocumentError(Exception e) {
        if ( this.documentListeners == null || documentListeners.isEmpty()) {
            return;
        }

        DocumentEvent event = new DocumentEvent(document, DocumentEvent.Action.documentErrorNotify);
        event.setException(e);
        
        for ( DocumentListener l : documentListeners) {
            if ( ! (l instanceof DocumentErrorBinder) ) {
                continue;
            }
            l.react(event);
        }
        
    }
    
/*    
    protected void notifyPropertyError(String propertyName, Exception e) {
        
        List<Binder> blist = this.errorBinders.get(propertyName);
        if (blist != null) {
            for (Binder b : blist) {
                //((ErrorBinder) b).notifyPropertyError(null, e);
                ((ErrorBinder) b).notifyPropertyError(e);
            }
        }
    }

    protected void notifyDocumentError(Exception e) {
        for (Binder b : documentErrorBinders) {
            ((ErrorBinder) b).notifyDocumentError(e);
        }
    }
*/
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

    @Override
    public DocumentBinding createChild(String childName) {
        DocumentBinding binding = new ObjectDocumentBinding(childName);
        childs.add(binding);
        if (document.getDocumentStore() != null) {
            binding.setDocument( (Document)getDocumentStore().get(childName));
        }
        return binding;
    }

    @Override
    public String getChildName() {
        return this.childName;
    }

    @Override
    public void react(DocumentEvent event) {
        if (event.getAction().equals(DocumentEvent.Action.propertyChangeNotify)) {
            if (event.getBinder() == null) {
                firePropertyChange(event.getPropertyName(), event.getOldValue(), event.getNewValue());
            } else {
                firePropertyChange(event.getBinder(), event.getOldValue(), event.getNewValue());
            }
        } else if (event.getAction().equals(DocumentEvent.Action.validateProperty)) {
            this.validate(event.getPropertyName(), event.getNewValue());
        } else if (event.getAction().equals(DocumentEvent.Action.validateErrorNotify)) {
            if (((ValidationException) event.getException()).getPropertyName() == null) {
                // documentStore error
                //(event.getException());
                fireDocumentError(event.getException());
            } else {
                //notifyPropertyError(event.getPropertyName(), event.getException());
                firePropertyError(event.getPropertyName(), event.getException());
            }
        } else if (event.getAction().equals(DocumentEvent.Action.validateAllProperties)) {
            //validators.validateProperties((DocumentStore) event.getSource());
            for (Map.Entry<String, List<Binder>> e : binders.entrySet()) {
                List<Binder> l = e.getValue();
                if (l == null) {
                    continue;
                }
                for (Binder b : l) {
                    this.validate(b.getPropertyName(), b.getComponentValue());
                }
            }
        } else if (event.getAction().equals(DocumentEvent.Action.validateDocument)) {
            this.validate();
        }
    }

    @Override
    public void react(BinderEvent event) {

        switch (event.getAction()) {
            case changePropertyValueRequest:
                if (!needChangeData(event.getPropertyName(),event.getDataValue())) {
                    return;
                }
                getDocumentStore().put((Binder) event.getSource(), event.getDataValue());
                break;
            case notifyPropertyErrorRequest:

                break;
            case clearPropertyErrorRequest:

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
    public void addDocumentListener(DocumentListener l) {
        if ( documentListeners == null ) {
            documentListeners = new ArrayList<DocumentListener>();
        }
        documentListeners.add(l);
    }
    @Override
    public void removeDocumentListener(DocumentListener l) {
        if ( documentListeners == null ) {
            return;
        }
        documentListeners.remove(l);
    }



}
