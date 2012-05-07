package org.document;

import java.io.Serializable;

/**
 *
 * @author V. Shyshkin
 */
public interface PropertyDataStore extends Serializable {
    
    Object get(Object key);
    void put(Object key,Object value);
    void addDocumentChangeListener(DocumentChangeListener listener);
    void removeDocumentChangeListener(DocumentChangeListener listener);    
}
