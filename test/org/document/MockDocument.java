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
public class MockDocument implements PropertyDataStore{
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
        if ( key instanceof PropertyBinder ) {
            data.put( ((PropertyBinder)key).getPropertyName(),value);
        } else {
            data.put(key, value);
        }
    }


    @Override
    public void addDocumentChangeListener(DocumentChangeListener listener) {
        
    }

    @Override
    public void removeDocumentChangeListener(DocumentChangeListener listener) {
        
    }
    
}
