package org.document;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author V. Shyshkin
 */
public class PropertyStoreImpl implements PropertyStore{
    
    protected Map store; 
    protected List<DocumentChangeListener> documentListeners;
    
    public PropertyStoreImpl() {
        store = new HashMap();
        documentListeners = new ArrayList<DocumentChangeListener>();
    }
    @Override
    public Object getValue(Object key) {
        return this.store.get(key);
    }

    @Override
    public void putValue(Object key, Object value) {
        this.store.put(key, value);
    }

    @Override
    public void addDocumentChangeListener(DocumentChangeListener listener) {
        this.documentListeners.add(listener);
    }

    @Override
    public void removeDocumentChangeListener(DocumentChangeListener listener) {
        this.documentListeners.remove(listener);
    }

    @Override
    public Object getAlias() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
