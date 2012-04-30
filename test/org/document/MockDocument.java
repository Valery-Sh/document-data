/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.util.HashMap;

/**
 *
 * @author Valery
 */
public class MockDocument implements Document{
    protected HashMap data;
    public MockDocument() {
        data = new HashMap();
    }
    
    @Override
    public Object get(Object key) {
        return data.get(key);
    }

    @Override
    public void put(Object key, Object value) {
        if ( key instanceof Binder ) {
            data.put( ((Binder)key).getPropertyName(),value);
        } else {
            data.put(key, value);
        }
    }


    @Override
    public void addDocumentListener(DocumentListener listener) {
        
    }

    @Override
    public void removeDocumentListener(DocumentListener listener) {
        
    }
    
}
