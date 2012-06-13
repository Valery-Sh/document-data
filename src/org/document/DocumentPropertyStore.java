package org.document;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.document.schema.DocumentGroup;
import org.document.schema.DocumentSchema;
import org.document.schema.HasSchema;
import org.document.schema.SchemaUtils;

/**
 *
 * @param <T> 
 * @author V. Shyshkin
 */
public class DocumentPropertyStore<T extends Document> implements PropertyStore, HasDocumentState, HasSchema {

    /**
     * 
     */
    protected transient DocumentStateImpl state;
    /**
     * 
     */
    protected T source;
    /**
     * 
     */
//    protected List<DocumentChangeListener> documentChangeListeners;
    /**
     * 
     */
    protected DocumentSchema localSchema;
    /**
     * 
     */
    protected DocumentGroup group;
    /**
     * 
     */
    protected Object alias;

    public DocumentPropertyStore() {
        this.state = new DocumentStateImpl(this);
        this.propertyChangeListeners = new ArrayList<PropertyChangeListener>();
    }
    /**
     * 
     * @param source
     */
    public DocumentPropertyStore(T source) {
        this();
        if ( source == null ) {
            throw new IllegalArgumentException("Constructor DocumentPropertyStore cannot accept null parameter value");
        }
        this.source = source;
        localSchema = SchemaUtils.createSchema(source.getClass());
        alias = null;
    }

    /**
     * 
     * @return
     */
    public T getObject() {
        return source;
    }
    
    /**
     * 
     * @return
     */
    @Override
    public DocumentSchema getSchema() {
        DocumentSchema ds;

        if (group != null) {
            ds = group.getSchema(source);
        } else if (localSchema != null) {
            ds = localSchema;
        } else {
            localSchema = SchemaUtils.createSchema(source.getClass());
            ds = localSchema;
        }

        return ds;
    }

    /**
     * 
     * @param key
     * @return
     */
    @Override
    public Object get(Object key) {
        Object result;
        if ( source == null ) {
            return null;
        }
        if (source instanceof KeyValueMap) {
            result = DataUtils.getValue(key.toString(), ((KeyValueMap) source).getMap());
        } else {
            result = DataUtils.getValue(key.toString(), source);
        }
        return result;
    }

    /**
     * The
     * <code>key</code> parameter may of type {@link PropertyBinder} or any type
     * whose
     * <code>key.toString</code> value represents a property name. When the
     * given property changes then the method notifies all registered binders.
     * But when the
     * <code>key</code> parameter is instance of
     * <code>PropertyBinder</code> then the method doesn't notify that binder.
     * Calling a method with a parameter of type
     * <code>PropertyBinder</code> is used by binders. The latter pass
     * themselves as a first parameter of the method.
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

        propertyName = key.toString();
        Object oldValue = get(propertyName);
        /**
         * To avoid cyclic 'put' method invocation we do nothing when an old
         * value equals to a new one
         */
        if (DataUtils.equals(oldValue, value)) {
            return;
        }

        if (!state.isEditing()) {
            state.setEditing(true);
        }
        /**
         * Here just calls DataUtils.setValue(propertyName.toString(), source,
         * value);
         */
        setPropertyValue(propertyName, value);

    }

//    @Override
    
    private List<PropertyChangeListener> propertyChangeListeners;
    /**
     * 
     * @param key
     * @param value
     */
    public void bind(Object key, Object value) {

        if (key == null) {
            throw new NullPointerException("The 'key' parameter cannot be null");
        }
        String propertyName;
        propertyName = key.toString();
        Object oldValue;
        oldValue = get(propertyName);
        if (!state.isEditing()) {
             state.setEditing(true);
        }
        for (PropertyChangeListener l  : propertyChangeListeners ) {
            PropertyChangeEvent event = new PropertyChangeEvent(this.source,propertyName,oldValue, value);
            l.propertyChange(event);
        }
        
    }

    /**
     * 
     * @return
     */
    @Override
    public DocumentState getDocumentState() {
        return state;
    }

    /**
     * The method is defined in order to easy override in a subclass without a
     * need to override the
     * <code>put</code> method.
     *
     * @param name string property name
     * @param value a value to be bind
     */
    protected void setPropertyValue(String name, Object value) {
        if ( source instanceof KeyValueMap ) {
            ((KeyValueMap)source).put(name, value);
        } else {
            DataUtils.setValue(name, source, value);
        }
    }

    /**
     * Notifies all registered listeners of type
     * {@link org.documentStore.DocumentListener } when an event of type
     * {@link org.documentStore.DocumentEvent } arises.
     *
     * @param event an event of type
     * <code>DocumentEvent</code>
     */
/*    private void fireDocumentEvent(DocumentChangeEvent event) {
        for (DocumentChangeListener l : documentChangeListeners) {
            l.react(event);
        }
    }
*/
    /**
     * 
     * @return
     */
    @Override
    public Object getAlias() {
        return alias;
    }
    public void setAlias(Object alias) {
        this.alias = alias;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeListeners.add(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeListeners.remove(listener);
    }
    /**
     *
     */
    protected static class DocumentStateImpl implements DocumentState {
        private boolean attached;
        private boolean editing;
        private PropertyStore documentStore;
        private Map beforeEditValues;
        /**
         * 
         */
        protected Map dirtyEditValues;
        /**
         * 
         */
//        protected Map<String, DocumentChangeEvent> propertyErrors;

        /**
         * 
         * @param documentStore
         */
        public DocumentStateImpl(PropertyStore documentStore) {
            this.documentStore = documentStore;
            beforeEditValues = new HashMap();
            dirtyEditValues = new HashMap();
//            propertyErrors = new HashMap<String, DocumentChangeEvent>();
        }

        @Override
        public boolean isAttached() {
            return attached;
        }

        @Override
        public void setAttached(boolean attached) {
            this.attached = attached;
        }

        /**
         * 
         * @return
         */
        @Override
        public boolean isEditing() {
            return editing;
        }

        /**
         * 
         * @param editing
         */
        @Override
        public void setEditing(boolean editing) {
            if ( ! attached ) {
                return;
            }
            if (this.editing == editing) {
                return;
            }
            DocumentPropertyStore ps = (DocumentPropertyStore) documentStore;
            if ( ps.source == null ) {
                return;
            }

            if (this.editing && !editing) {
                try {
                    if (ps.source instanceof HasValidator) {
                        Validator v = ((HasValidator) ps.source).getValidator();
                        if (v != null) {
                            v.validate((Document)ps.source);
                        }
                    }
                    this.editing = editing;
                } catch (ValidationException e) {
                }
            } else if (!this.editing) {
                beforeEditValues.clear();
                DataUtils.putAll(beforeEditValues, ps.source);
                dirtyEditValues.clear();
                dirtyEditValues.putAll(beforeEditValues);
                this.editing = editing;
                //ps.bind("document.state.editing", editing);
            }
        }

        /**
         * 
         * @return
         */
        @Override
        public Map<String, Object> getDirtyValues() {
            return this.dirtyEditValues;
        }

/*        @Override
        public Map<String, DocumentChangeEvent> getPropertyErrors() {
            return this.getPropertyErrors();
        }
*/
        /**
         * 
         * @param event
         */
        /*@Override
        public void react(BinderEvent event) {
            if (event.getAction() == BinderEvent.Action.boundObjectChange) {
                dirtyEditValues.put(event.getBoundProperty(), event.getComponentValue());
            }
        }
        */ 
        /**
         * Can be called only by a
         * <code>DocumentList</code> object. When a contained
         * <code>DocumentList</code> completes an operation that removes a
         * document that is marked as
         * <code>detached</code> then this method is called.
         *
         * @param event
         */
        /*
         * @Override public void listChanged(ListChangeEvent event) { if (
         * event.getAction() == ListChangeEvent.Action.removeNew) {
         * this.attached = true; this.editing = false; } }
         *
         * @Override public void addListChangeListener(ListChangeListener l) {
         * this.listListener = l; }
         *
         * @Override public void removeListChangeListener(ListChangeListener l)
         * { this.listListener = null; }
         */
    }//class DocumentStateImpl
}