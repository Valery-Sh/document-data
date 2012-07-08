package org.document;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @param <T> 
 * @author V. Shyshkin
 */
public class DocumentPropertyStore<T extends Document> implements PropertyStore<String,Object>, HasState, BoundPropertyListener {

    private List<PropertyChangeListener> propertyChangeListeners;
    
    /**
     * 
     */
    protected transient DocumentStateImpl state;
    /**
     * 
     */
    protected Object owner;
    /**
     * 
     */
//    protected List<DocumentChangeListener> documentChangeListeners;
    /**
     * 
     */
    //protected DocumentSchema localSchema;
    /**
     * 
     */
    //protected DocumentGroup group;
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
     * @param owner
     */
    public DocumentPropertyStore(Object source) {
        this();
        if ( source == null ) {
            throw new IllegalArgumentException("Constructor DocumentPropertyStore cannot accept null parameter value");
        }
        this.owner = source;
        //localSchema = createSchema(source.getClass(),Object.class);
//        localSchema = getSchema(source.getClass(),Object.class);
        alias = null;
    }
    
/*    protected DocumentSchema createSchema(Class sourceClass, Class restrictSuper) {
        return SchemaUtils.createSchema(sourceClass,restrictSuper);        
    }
*/    
    private List<PropertyChangeListener> saveListeners;
    
    protected void removePropertyListeners() {
        saveListeners = propertyChangeListeners;
        propertyChangeListeners = null;

    }
    protected void addPropertyListeners() {
        propertyChangeListeners = saveListeners;
    }
    
    /**
     * 
     * @return
     */
    @Override
    public Object getOwner() {
        return owner;
    }
    
    /**
     * 
     * @return
     */
/*    @Override
    public DocumentSchema getSchema() {
        //DocumentSchema ds;
        return Registry.getSchema(owner.getClass());
        if (group != null) {
            ds = group.getSchema(owner);
        } else if (localSchema != null) {
            ds = localSchema;
        } else {
            localSchema = createSchema(owner.getClass(), Object.class);
            ds = localSchema;
        }

        return ds;
  
    }
*/
    /**
     * 
     * @param key
     * @return
     */
    @Override
    public Object getValue(String key) {
        Object result;
        if ( owner == null ) {
            return null;
        }
        if (owner instanceof KeyValueMap) {
            //result = DataUtils.getValue(key.toString(), ((KeyValueMap) owner).getMap());
            result = ((KeyValueMap) owner).getMap().get(key.toString());            
        } else {
            if ( key.equals("country")) {
                System.out.println("");
            }
            result = DataUtils.getValue(key.toString(), owner);
        }
        return result;
    }
    protected Object putSilent(String key, Object value) {
        Object result;
        removePropertyListeners();
        try {
            result = putValue(key, value);
        } catch(Exception e) {
            throw e;
        } finally {
            addPropertyListeners();
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
    public Object putValue(String key, Object value) {

        if (key == null) {
            throw new NullPointerException("The 'key' parameter cannot be null");
        }
        String propertyName;

        propertyName = key.toString();
        Object oldValue = getValue(propertyName);
        /**
         * To avoid cyclic 'putValue' method invocation we do nothing when an old
         * value equals to a new one
         */
        if (DataUtils.equals(oldValue, value)) {
            return oldValue;
        }

        if (!state.isEditing()) {
            state.setEditing(true);
        }
        /**
         * Here just calls DataUtils.setValue(propertyName.toString(), owner,
         * value);
         */
        setPropertyValue(propertyName, value);
        return oldValue;

    }

//    @Override
    
    /**
     * 
     * @param key
     * @param value
     */
    @Override
    public void bind(String key, Object value) {
        if ( propertyChangeListeners == null ) {
            return;
        }
        if (key == null) {
            throw new NullPointerException("The 'key' parameter cannot be null");
        }
        String propertyName;
        propertyName = key.toString();
        Object oldValue;
        oldValue = getValue(propertyName);
        if (!state.isEditing()) {
             state.setEditing(true);
        }
        for (PropertyChangeListener l  : propertyChangeListeners ) {
            PropertyChangeEvent event = new PropertyChangeEvent(this.owner,propertyName,oldValue, value);
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
     * <code>putValue</code> method.
     *
     * @param name string property name
     * @param value a value to be bind
     */
    protected void setPropertyValue(String name, Object value) {
        if ( owner instanceof KeyValueMap ) {
            ((KeyValueMap)owner).put(name, value);
        } else {
            DataUtils.setValue(name, owner, value);
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
    protected List<PropertyChangeListener> getPropertyChangeListeners() {
        return propertyChangeListeners;
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
         * @param documentStore
         */
        public DocumentStateImpl(PropertyStore documentStore) {
            this.documentStore = documentStore;
            beforeEditValues = new HashMap();
            dirtyEditValues = new HashMap();
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
            if ( documentStore.getOwner() == null ) {
                return;
            }

            if (this.editing && !editing) {
            } else if (!this.editing) {
                beforeEditValues.clear();
                DataUtils.putAll(beforeEditValues, documentStore.getOwner());
                dirtyEditValues.clear();
                dirtyEditValues.putAll(beforeEditValues);
                this.editing = editing;
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
                dirtyEditValues.putValue(event.getBoundProperty(), event.getComponentValue());
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