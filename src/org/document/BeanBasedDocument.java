package org.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author V. Shyshkin
 */
public class BeanBasedDocument<T> implements Document, HasDocumentState {

    protected transient BeanDocumentState state;
    protected T source;
    protected List<DocumentListener> docListeners;

    public BeanBasedDocument(T source) {
        this.state = new BeanDocumentState(this);
        this.source = source;
        this.docListeners = new ArrayList<DocumentListener>();
        this.state.fillValidEditValues();
    }

    @Override
    public Object get(Object key) {
        return DataUtils.getValue(key.toString(), source);
    }

    @Override
    public boolean put(Object key, Object value) {

        if (key == null) {
            throw new NullPointerException("The 'key' parameter cannot be null");
        }
        
        String propertyName = (key instanceof Binder) ? ((Binder) key).getDataEntityName() : key.toString();
        
        Object oldValue = this.get(propertyName);        
        /**
         * To avoid cyclic 'put' method invocation we do nothing
         * when an old value equals to a new one
         */
        if ( DataUtils.equals(oldValue,value)) {
            return false;
        }
        
        if (!state.isEditing()) {
            state.setEditing(true);
        }

        state.dirtyEditValues.put(propertyName, value);

        try {
            validate(propertyName, value);
            state.validEditValues.put(propertyName, value);
        } catch (ValidationException e) {

/*            if (value != null && value.equals(oldValue)
                    || oldValue != null && oldValue.equals(value)
                    || oldValue == null && value == null) {
                Object v = state.validEditValues.get(propertyName);
                try {
                    validate(propertyName, v);
                    DataUtils.setValue(propertyName, source, v);
                } catch (ValidationException e1) {
                }
            } 
*/
            if (!docListeners.isEmpty()) {
                DocumentEvent event = new DocumentEvent(this, DocumentEvent.Action.validateErrorNotify);
                event.setPropertyName(propertyName);
                event.setOldValue(oldValue);
                event.setNewValue(value);
                event.setException(e);
                fireDocumentEvent(event);
            }
            return false;
            
        }
        DataUtils.setValue(propertyName.toString(), source, value);
        
        //Object v = DataUtils.getValue(propertyName.toString(), source);
/*        boolean b = true;
        if (oldValue == null && value == null) {
            b = false;
        } else if (oldValue != null && oldValue.equals(value)) {
            b = false;
        } else if (value != null && value.equals(oldValue)) {
            b = false;
        }
        if (b) {
        } else {
            return false;
        }
        */

        if (!docListeners.isEmpty()) {
            DocumentEvent event = new DocumentEvent(this, DocumentEvent.Action.propertyChangeNotify);
            event.setBinder((Binder) key);
            event.setPropertyName(propertyName.toString());
            event.setOldValue(oldValue);
            event.setNewValue(value);
            fireDocumentEvent(event);
        }
        return true;
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

    public void validate(Object key, Object value) {
        if (!docListeners.isEmpty()) {
            DocumentEvent event = new DocumentEvent(this, DocumentEvent.Action.validateProperty);
            event.setPropertyName(key.toString());
            event.setNewValue(value);
            fireDocumentEvent(event);
        }
    }

    protected void validateProperties() {
        if (!docListeners.isEmpty()) {
            DocumentEvent event = new DocumentEvent(this, DocumentEvent.Action.validateAllProperties);
            fireDocumentEvent(event);
        }

    }

    private void fireDocumentEvent(DocumentEvent event) {
        for (DocumentListener l : docListeners) {
            l.react(event);
        }
    }

    protected static class BeanDocumentState implements DocumentState {

        private boolean editing;
        private Document document;
        private Map beforeEditValues;
        protected Map dirtyEditValues;
        protected Map validEditValues;

        public BeanDocumentState(Document document) {
            this.document = document;
            beforeEditValues = new HashMap();
            dirtyEditValues = new HashMap();
            validEditValues = new HashMap();
        }
        
        protected void fillValidEditValues() {
            DataUtils.putAll(validEditValues, ((BeanBasedDocument) document).source);
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

            BeanBasedDocument d = (BeanBasedDocument) document;

            if (this.editing && !editing) {
                try {
                    d.validateProperties();
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

//                validEditValues.clear();
//                validEditValues.putAll(beforeEditValues);
                this.editing = editing;
            }
        }
    }//class BeanDocumentState
}//class BeanBasedDocument
