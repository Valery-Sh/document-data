/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.util.HashMap;
import java.util.Map;

public class DocumentImpl implements PropertyDataStore {

    Map values = new HashMap();

    DocumentChangeListener docListener;

    @Override
    public void addDocumentChangeListener(DocumentChangeListener listener) {
        this.docListener = listener;
    }

    @Override
    public void removeDocumentChangeListener(DocumentChangeListener listener) {
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
        PropertyBinder binder = null;
        
        if ( key instanceof PropertyBinder) {
            pname = ((PropertyBinder)key).getPropertyName();
            binder = (PropertyBinder)key;
        } else {
            pname = key.toString();
        }
        
        Object oldValue = this.get(pname);
        values.put(pname, value);
        if (docListener != null) {
            DocumentChangeEvent event = new DocumentChangeEvent(this, DocumentChangeEvent.Action.propertyChangeNotify);
            event.setPropertyName(pname);
            event.setBinder(binder);
            event.setOldValue(oldValue);
            event.setNewValue(value);
            docListener.react(event);

        }

    }
}
