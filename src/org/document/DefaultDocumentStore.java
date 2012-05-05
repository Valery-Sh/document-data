package org.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author V. Shyshkin
 */
public class DefaultDocumentStore<T> implements DocumentStore, HasDocumentState {

    protected transient BeanDocumentState state;
    protected T source;
    protected List<DocumentListener> docListeners;

    public DefaultDocumentStore(T source) {
        this.state = new BeanDocumentState(this);
        this.source = source;
        this.docListeners = new ArrayList<DocumentListener>();
//        this.state.fillValidEditValues();
    }
    public T getObject() {
        return source;
    }
    @Override
    public Object get(Object key) {
        return DataUtils.getValue(key.toString(), source);
    }
    /**
     * The <code>key</code> parameter may of type {@link PropertyBinder} or any type
     * whose <code>key.toString</code> value represents a property name.
     * When the given property changes then the method notifies all 
     * registered binders. But when the <code>key</code> parameter is instance
     * of <code>PropertyBinder</code> then the method doesn't notify that binder.
     * Calling a method with a parameter of type <code>PropertyBinder</code> 
     * is used by binders. The latter pass themselves as a first parameter
     * of the method.
     * 
     * @param key a property name or an instance of the property binder.
     * @param value the value to be saved by a documentStore.
     */
    @Override
    public void put(Object key, Object value) {

        if (key == null) {
            throw new NullPointerException("The 'key' parameter cannot be null");
        }
        String propertyName;
        PropertyBinder binder = null;
        
        if ( key instanceof PropertyBinder ) {
            propertyName = ((PropertyBinder) key).getPropertyName();            
            binder = (PropertyBinder)key;
        } else {
            propertyName = key.toString();
            
        }
        
        
        Object oldValue = get(propertyName);        
        /**
         * To avoid cyclic 'put' method invocation we do nothing
         * when an old value equals to a new one
         */
        if ( DataUtils.equals(oldValue,value)) {
            return;
        }
        
        if (!state.isEditing()) {
            state.setEditing(true);
        }
        if ( binder != null ) {
            state.dirtyEditValues.put(propertyName, binder.getComponentValue());
        }

        try {
            validate(propertyName, value);
        } catch (ValidationException e) {
            if (!docListeners.isEmpty()) {
                DocumentEvent event = new DocumentEvent(this, DocumentEvent.Action.validateErrorNotify);
                event.setPropertyName(propertyName);
                event.setBinder(binder);
                event.setOldValue(oldValue);
                event.setNewValue(value);
                event.setException(e);
                fireDocumentEvent(event);
            }
            return;
            
        }
        /**
         * Here just calls
         * DataUtils.setValue(propertyName.toString(), source, value);
         */
        setPropertyValue(propertyName.toString(), value);

        if (!docListeners.isEmpty()) {
            DocumentEvent event = new DocumentEvent(this, DocumentEvent.Action.propertyChangeNotify);
            event.setPropertyName(propertyName);
            event.setBinder(binder);
            event.setOldValue(oldValue);
            event.setNewValue(value);
            fireDocumentEvent(event);
        }

    }

    @Override
    public void addDocumentListener(DocumentListener listener) {
        this.docListeners.add(listener);
    }

    @Override
    public void removeDocumentListener(DocumentListener listener) {
        this.docListeners.remove(listener);
    }

    @Override
    public DocumentState getDocumentState() {
        return state;
    }
    /**
     * 
     * @param key
     * @param value
     * @throws ValidationException 
     */
    public void validate(Object key, Object value) throws ValidationException {
        if (!docListeners.isEmpty()) {
            DocumentEvent event = new DocumentEvent(this, DocumentEvent.Action.validateProperty);
            event.setPropertyName(key.toString());
            event.setNewValue(value);
            fireDocumentEvent(event);
        }
    }
    /**
     * 
     */
    protected void validateProperties() {
        if (!docListeners.isEmpty()) {
            DocumentEvent event = new DocumentEvent(this, DocumentEvent.Action.validateAllProperties);
            fireDocumentEvent(event);
        }
    }
    protected void validateDocument() {
        if (!docListeners.isEmpty()) {
            DocumentEvent event = new DocumentEvent(this, DocumentEvent.Action.validateDocument);
            fireDocumentEvent(event);
        }

    }
    
    /**
     * The method is defined in order to easy override in a subclass
     * without a need to override the <code>put</code> method.
     * @param name string property name
     * @param value a value to be set
     */
    protected void setPropertyValue(String name, Object value) {
        DataUtils.setValue(name, source, value);        
    }
    /**
     * Notifies all registered listeners of type 
     * {@link org.documentStore.DocumentListener } when an event of type
     * {@link org.documentStore.DocumentEvent } arises.
     * @param event an event of type <code>DocumentEvent</code>
     */
    private void fireDocumentEvent(DocumentEvent event) {
        for (DocumentListener l : docListeners) {
            l.react(event);
        }
    }
    
    /**
     * 
     */
    protected static class BeanDocumentState implements DocumentState {

        private boolean editing;
        private DocumentStore documentStore;
        private Map beforeEditValues;
        protected Map dirtyEditValues;
        protected Map<String,DocumentEvent> propertyErrors;
        //protected Map validEditValues;

        public BeanDocumentState(DocumentStore documentStore) {
            this.documentStore = documentStore;
            beforeEditValues = new HashMap();
            dirtyEditValues = new HashMap();
            propertyErrors = new HashMap<String,DocumentEvent>();
            //validEditValues = new HashMap();
        }
        
        
        @Override
        public boolean isEditing() {
            return editing;
        }

        @Override
        public void setEditing(boolean editing) {
            if (this.editing == editing) {
                return;
            }

            DefaultDocumentStore d = (DefaultDocumentStore) documentStore;

            if (this.editing && !editing) {
                try {
                    d.validateProperties();
                    d.validateDocument();
                    this.editing = editing;
                } catch (ValidationException e) {
                    if (!d.docListeners.isEmpty()) {
                        DocumentEvent event = new DocumentEvent(d, DocumentEvent.Action.validateErrorNotify);
                        event.setPropertyName("");
                        event.setException(e);
                        d.fireDocumentEvent(event);
                    }
                }
            } else if (!this.editing) {
                beforeEditValues.clear();
                DataUtils.putAll(beforeEditValues, d.source);
                dirtyEditValues.clear();
                dirtyEditValues.putAll(beforeEditValues);
                this.editing = editing;
            }
        }

        @Override
        public Map<String, Object> getDirtyValues() {
            return this.dirtyEditValues;
        }

        @Override
        public Map<String, DocumentEvent> getPropertyErrors() {
            return this.getPropertyErrors();
        }
    }//class BeanDocumentState
}