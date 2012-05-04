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
    //protected DocumentStore document;
    protected Document objectDocument;
    
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
                // document error binder
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
                // document error binder
                remove(binder, documentErrorBinders);
            } else {
                remove(binder, errorBinders);
            }
        } else {
            remove(binder, binders);
        }
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
    

    public Document getObjectDocument() {
        return this.objectDocument;
    }

    public DocumentStore getDocument() {
        //return this.document;
        return this.objectDocument.getDocumentStore();
    }

    /*
     * @Override public void setObjectDocument(Document hasDocument, boolean
     * completeChanges) { this.setObjectDocument(hasDocument.getDocumentStore(),
     * completeChanges); }
     */
    //@Override
    /*public void setDocument(Document hasDocument) {
        this.setObjectDocument(hasDocument.getDocumentStore());
    }
*/
    @Override
    public void setObjectDocument(Document object) {
        DocumentStore oldDocument = null;
        if ( this.objectDocument != null ) { 
           oldDocument = getDocument();
        }   
        
        if (oldDocument != null) {
            completeChanges();
            if (oldDocument instanceof HasDocumentState) {
                DocumentState state = ((HasDocumentState) oldDocument).getDocumentState();
                state.setEditing(false);
            }
        }
        Document oldObjectDocument = this.objectDocument;
        if ( object == null ) {
            this.objectDocument = null;
        } 
        
        this.objectDocument = object;

        if (this.objectDocument != null) {
            getDocument().addDocumentListener(this);
        } 
        if (oldObjectDocument != null && oldObjectDocument !=  objectDocument ) {
            oldDocument.removeDocumentListener(this);
        }

        if ( objectDocument == null ) {
            return;
        }

        refresh();
        
        DocumentStore document = getDocument();
        
        if (document instanceof HasDocumentState) {
            DocumentState state = ((HasDocumentState) document).getDocumentState();
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
                this.notifyDocumentError(null);
                try {
                    getValidators().validate(document);
                } catch (ValidationException e) {
                    this.notifyDocumentError(e);
                }
            }
        }
        
        notifyDocumentListeners(oldObjectDocument, objectDocument);
        
        for (DocumentBinding child : childs) {
            Object d = document.get(child.getChildName());
            if (d == null) {
                child.setObjectDocument((Document) null);
            } else if (d instanceof Document) {
                child.setObjectDocument((Document) d);
            }
        }
    }
    private void notifyDocumentListeners(Document oldDoc, Document newDoc ) {
        if ( this.documentListeners == null || documentListeners.isEmpty()) {
            return;
        }

        DocumentEvent event = new DocumentEvent(objectDocument, DocumentEvent.Action.documentChange);
        event.setOldValue(oldDoc);
        event.setNewValue(newDoc);
        
        for ( DocumentListener l : documentListeners) {
            l.react(event);
        }
    }
    protected void refresh() {
        for (Map.Entry<String, List<Binder>> ent : this.binders.entrySet()) {
            for (Binder b : ent.getValue()) {
                //b.dataChanged(null, b.getDataValue());
                b.init(getDocument().get(b.getPropertyName()));
                notifyPropertyError(b.getPropertyName(), null);
            }
        }
        notifyDocumentError(null);

    }

    protected void resolvePendingChanges() {
        for (Map.Entry<String, List<Binder>> ent : this.binders.entrySet()) {
            for (Binder b : ent.getValue()) {
                b.componentChanged(b.getComponentValue());
            }
        }
    }

    @Override
    public void completeChanges() {
        if (objectDocument.getDocumentStore() == null) {
            return;
        }

        this.resolvePendingChanges();

        /*
         * for (DocumentBinding child : childs) { Object d =
         * this.document.get(child.getChildName()); if (d != null && (d
         * instanceof DocumentStore)) { child.completeChanges(); } }
         *
         */
    }

    /**
     * For test purpose
     *
     * @param path
     * @return
     */
    protected List<Binder> getBinders(String path) {
        return this.binders.get(path);
    }

    /*
     * @Override public void notifyDocumentError(Binder source, Exception e) {
     * List<Binder> blist = this.errorBinders.get(source.getPropertyName()); if
     * (blist != null) { for (Binder b : blist) { //((ErrorBinder)
     * b).notifyPropertyError(source, e); ((ErrorBinder)
     * b).notifyPropertyError(e); } } }
     */
    //@Override
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

    @Override
    public ValidatorCollection getValidators() {
        return validators;
    }

    @Override
    public void validate(String propPath, Object value) throws ValidationException {
        getValidators().validate(propPath, getDocument(), value);
    }

    protected void validate() throws ValidationException {
        getValidators().validate(getDocument());
    }

    @Override
    public DocumentBinding createChild(String childName) {
        DocumentBinding binding = new ObjectDocumentBinding(childName);
        childs.add(binding);
        if (objectDocument.getDocumentStore() != null) {
            binding.setObjectDocument( (Document)getDocument().get(childName));
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
                // document error
                notifyDocumentError(event.getException());
            } else {
                notifyPropertyError(event.getPropertyName(), event.getException());
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
                getDocument().put((Binder) event.getSource(), event.getDataValue());
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
        Object currentValue = getDocument().get(propertyName);

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
