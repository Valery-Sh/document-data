package org.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author V. Shyshkin
 */
public class SimpleMapBasedDocument implements Document {

    protected Map values = new HashMap();
    protected List<DocumentListener> docListeners;

    protected SimpleMapBasedDocument() {
        this(new HashMap());
    }

    protected SimpleMapBasedDocument(Map values) {
        this.values = values;
        docListeners = new ArrayList<DocumentListener>();
    }

    public static SimpleMapBasedDocument create() {
        return new SimpleMapBasedDocument();
    }

    public static SimpleMapBasedDocument create(Map values) {
        return new SimpleMapBasedDocument(values);
    }

    @Override
    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException("The 'key' parameter cannot be null");
        }
        return values.get(key);
    }

    @Override
    public void put(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException("The 'key' parameter cannot be null");
        }
        Object oldValue = this.get(key);
        values.put(key, value);
        
        this.validate(key, value);
        
        if (!docListeners.isEmpty()) {
            DocumentEvent event = new DocumentEvent(this, DocumentEvent.Action.propertyChange);
            event.setPropertyName(key.toString());
            event.setOldValue(oldValue);
            event.setNewValue(value);
            fireDocumentEvent(event);
        }

    }
    protected void validate(Object key, Object value) {
        if (! docListeners.isEmpty()) {
            DocumentEvent event = new DocumentEvent(this, DocumentEvent.Action.validate);
            event.setPropertyName(key.toString());
            event.setNewValue(value);
            fireDocumentEvent(event);
        }

    }

    private void fireDocumentEvent(DocumentEvent event) {
        for (DocumentListener l : docListeners  ) {
            l.react(event);
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
}
