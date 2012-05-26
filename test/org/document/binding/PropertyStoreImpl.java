package org.document.binding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.document.DocumentChangeListener;
import org.document.PropertyStore;

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
    public Object get(Object key) {
        return this.store.get(key);
    }

    @Override
    public void put(Object key, Object value) {
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
    public Alias getAlias() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
