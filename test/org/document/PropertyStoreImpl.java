/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.util.HashMap;
import java.util.Map;
import org.document.binding.PropertyBinder;

public class PropertyStoreImpl implements PropertyStore {

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
        values.put(key.toString(), value);
    }
}
