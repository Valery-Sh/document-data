/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.util.HashMap;
import java.util.Map;

public class DocumentImpl implements DocumentStore {

    Map values = new HashMap();

    DocumentListener docListener;

    @Override
    public void addDocumentListener(DocumentListener listener) {
        this.docListener = listener;
    }

    @Override
    public void removeDocumentListener(DocumentListener listener) {
        this.docListener = null;
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
        String pname;
        Binder binder = null;
        
        if ( key instanceof Binder) {
            pname = ((Binder)key).getPropertyName();
            binder = (Binder)key;
        } else {
            pname = key.toString();
        }
        
        Object oldValue = this.get(pname);
        values.put(pname, value);
        if (docListener != null) {
            DocumentEvent event = new DocumentEvent(this, DocumentEvent.Action.propertyChangeNotify);
            event.setPropertyName(pname);
            event.setBinder(binder);
            event.setOldValue(oldValue);
            event.setNewValue(value);
            docListener.react(event);

        }

    }
}
