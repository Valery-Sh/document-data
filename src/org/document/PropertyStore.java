package org.document;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

/**
 *
 * @author V. Shyshkin
 */
public interface PropertyStore<K,V> extends Serializable {
    
    Object getOwner();
    
    Object getAlias();
    
    V getValue(K key);

    V putValue(K key, V value);
    
    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);
}
