package org.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author V. Shyshkin
 */
public class MapBasedDocument implements Document, HasDocumentState {

    //protected SimpleMapBasedDocument baseDocument;
    protected transient DocumentState state;
    protected Map source;
    protected List<DocumentListener> docListeners;

    public MapBasedDocument(Map source) {
        //baseDocument = new SimpleMapBasedDocument();
        this.state = new MapBasedDocument.MapDocumentState(this);
        this.source = source;
        this.docListeners = new ArrayList<DocumentListener>();
    }

    @Override
    public Object get(Object key) {
        return source.get(key);
    }

    @Override
    public void put(Object key, Object value) {
        
        if (key == null) {
            throw new NullPointerException("The 'key' parameter cannot be null");
        }

        if (!state.isEditing()) {
            state.setEditing(true);
        }

        Object oldValue = this.get(key);
        ((MapDocumentState)state).dirtyEditValues.put(key, value);
        
        try {
            validate(key, value);
        } catch (ValidationException e) {
            if (!docListeners.isEmpty()) {
                DocumentEvent event = new DocumentEvent(this, DocumentEvent.Action.validateErrorNotify);
                event.setPropertyName(key.toString());
                event.setOldValue(oldValue);
                event.setNewValue(value);
                event.setException(e);
                fireDocumentEvent(event);
            }
            return;
        }

        source.put(key, value);

        if (!docListeners.isEmpty()) {
            DocumentEvent event = new DocumentEvent(this, DocumentEvent.Action.propertyChangeNotify);
            event.setPropertyName(key.toString());
            event.setOldValue(oldValue);
            event.setNewValue(value);
            fireDocumentEvent(event);
        }
        return;
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

    protected void validate(Object key, Object value) {
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

    protected static class MapDocumentState implements DocumentState {

        private boolean editing;
        private Document document;
        private Map beforeEditValues;
        protected Map dirtyEditValues;

        public MapDocumentState(Document document) {
            this.document = document;
            beforeEditValues = new HashMap();
            dirtyEditValues = new HashMap();
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
            
            MapBasedDocument d = (MapBasedDocument) document;
            
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
                beforeEditValues.putAll(d.source);
                dirtyEditValues.clear();
                dirtyEditValues.putAll(d.source);
                this.editing = editing;
            }
        }

        @Override
        public Map<String, Object> getDirtyValues() {
            throw new UnsupportedOperationException("Not supported yet.");
        }


        @Override
        public Map<String, DocumentEvent> getPropertyErrors() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
}
