package org.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author V. Shyshkin
 */
public class MapBasedDocument1 implements Document, HasDocumentState {

    //protected SimpleMapBasedDocument baseDocument;
    protected transient DocumentState state;
    protected Map source;
    protected List<DocumentListener> docListeners;

    public MapBasedDocument1(Map source) {
        //baseDocument = new SimpleMapBasedDocument();
        this.state = new MapBasedDocument1.MapDocumentState(this);
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

        if (! state.isEditing()) {
            state.setEditing(true);
        }

        Object oldValue = this.get(key);
        
//        try {
//            validate(key, value);
//        } catch (ValidationException e) {
//        }
        
        source.put(key, value);

        if (!docListeners.isEmpty()) {
            DocumentEvent event = new DocumentEvent(this, DocumentEvent.Action.propertyChange);
            event.setPropertyName(key.toString());
            event.setOldValue(oldValue);
            event.setNewValue(value);
            fireDocumentEvent(event);
        }
        validate(key, value);
        

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
            DocumentEvent event = new DocumentEvent(this, DocumentEvent.Action.validate);
            event.setPropertyName(key.toString());
            event.setNewValue(value);
            fireDocumentEvent(event);
        }

    }

    protected void validateProperties() {
        if (!docListeners.isEmpty()) {
            DocumentEvent event = new DocumentEvent(this, DocumentEvent.Action.validateProperties);
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
        private Document unchangedDocument;
        private Map editingData;

        public MapDocumentState(Document document) {
            this.unchangedDocument = document;
            editingData = new HashMap();
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
            if (this.editing && !editing) {
                try {
                    ((MapBasedDocument1) unchangedDocument).validateProperties();
                    ((MapBasedDocument1) unchangedDocument).source.clear();
                    ((MapBasedDocument1) unchangedDocument).source = editingData;

                    this.editing = editing;
                } catch (ValidationException e) {
                }

            } else if (!this.editing) {
//                editingDocument = new SimpleMapBasedDocument();
                editingData.clear();
                editingData.putAll(((MapBasedDocument1) unchangedDocument).source);
                this.editing = editing;
            }


        }

        @Override
        public Document getCurrent() {
            return null;
        }
    }
}
