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

    protected Object id;
    protected String childName;
    protected List<DocumentBinding> childs;
    protected Map<String, List<Binder>> binders;
    protected Map<String, List<Binder>> errorBinders;
    protected List<Binder> documentErrorBinders;
    protected Document document;
    protected ObjectDocument objectDocument;
    
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
        blist.add(binder);
        binderMap.put(propPath, blist);
        //binder.setDocumentBinding(this);

    }

    protected void add(Binder binder, List<Binder> binderList) {
        binderList.add(binder);
        binder.addBinderListener(this);
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
        blist.remove(binder);
        if (blist.isEmpty()) {
            binderMap.remove(binder.getPropertyName());
        }

    }
    protected void remove(Binder binder, List<Binder> binderList) {
        binderList.remove(binder);
        binder.removeBinderListener(this);
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
    
    @Override
    public ObjectDocument getBoundObject() {
        return this.objectDocument;
    }

    @Override
    public Document getBoundDocument() {
        return this.document;
    }

    /*
     * @Override public void bindTo(ObjectDocument hasDocument, boolean
     * completeChanges) { this.bindTo(hasDocument.getDocument(),
     * completeChanges); }
     */
    //@Override
    /*public void setDocument(ObjectDocument hasDocument) {
        this.bindTo(hasDocument.getDocument());
    }
*/
    @Override
    public void bindTo(Object object) {
        
        Document old = this.document;
        if (old != null) {
            completeChanges();
            if (old instanceof HasDocumentState) {
                DocumentState state = ((HasDocumentState) old).getDocumentState();
                state.setEditing(false);
            }
        }
        
        if ( object == null ) {
            this.document = null;
            this.objectDocument = null;
        } else if ( ! (object instanceof ObjectDocument) ) {
            throw new IllegalArgumentException(" Invalid parameter type. Expected " + ObjectDocument.class + " but found " + object.getClass());
        }
        
        this.document = ((ObjectDocument)object).getDocument();
        this.objectDocument = (ObjectDocument)object;

        if (this.document != null) {
            //this.document.setPropertyChangeHandler(this);
            this.document.addDocumentListener(this);

        } 
        if (old != null && old !=  this.document ) {
            //old.setPropertyChangeHandler(null);
            old.removeDocumentListener(this);
        }

        if ( document == null ) {
            return;
        }

        refresh();
        
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
        for (DocumentBinding child : childs) {
            Object d = this.document.get(child.getChildName());
            if (d == null) {
                child.bindTo((Document) null);
            } else if (d instanceof Document) {
                child.bindTo((Document) d);
            }
        }
    }

    /*
     * @Override public void bindTo(Document document, boolean
     * completeChanges) {
     *
     * Document old = this.document; if (old != null) { if (completeChanges) {
     * completeChanges(); } else { this.resolvePendingChanges(); } }
     *
     * this.document = document;
     *
     * if (this.document != null) {
     * //this.document.setPropertyChangeHandler(this);
     * this.document.addDocumentListener(this);
     *
     * } else if (old != null) { //old.setPropertyChangeHandler(null);
     * old.removeDocumentListener(this); }
     *
     * refresh();
     *
     * for (DocumentBinding child : childs) { Object d =
     * this.document.get(child.getChildName()); if (d == null) {
     * child.bindTo((Document)null, false); } else if (d instanceof
     * Document) { child.bindTo((Document) d, false); } } }
     */
    protected void refresh() {
        for (Map.Entry<String, List<Binder>> ent : this.binders.entrySet()) {
            for (Binder b : ent.getValue()) {
                //b.dataChanged(null, b.getDataValue());
                b.init(document.get(b.getPropertyName()));
                notifyPropertyError(b.getPropertyName(), null);
            }
        }
        notifyDocumentError(null);

    }

    protected void resolvePendingChanges() {
        for (Map.Entry<String, List<Binder>> ent : this.binders.entrySet()) {
            for (Binder b : ent.getValue()) {
                //b.componentChanged(null, b.getComponentValue());
                b.componentChanged(b.getComponentValue());
            }
        }
    }

    @Override
    public void completeChanges() {
        if (document == null) {
            return;
        }

        this.resolvePendingChanges();

        /*
         * for (DocumentBinding child : childs) { Object d =
         * this.document.get(child.getChildName()); if (d != null && (d
         * instanceof Document)) { child.completeChanges(); } }
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
        getValidators().validate(propPath, document, value);
    }

    protected void validate() throws ValidationException {
        getValidators().validate(document);
    }

    @Override
    public DocumentBinding createChild(String childName) {
        DocumentBinding binding = new ObjectDocumentBinding(childName);
        childs.add(binding);
        if (this.document != null) {
            binding.bindTo((Document) document.get(childName));
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
            //validators.validateProperties((Document) event.getSource());
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
        //BinderEvent.Action.changePropertyValueRequest
        switch (event.getAction()) {
            case changePropertyValueRequest:
                if (!needChangeData(event.getPropertyName(),event.getDataValue())) {
                    return;
                }
                document.put((Binder) event.getSource(), event.getDataValue());
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
        Object currentValue = document.get(propertyName);

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

}
