package org.document;

import java.io.Serializable;

/**
 *
 * @author V. Shyshkin
 */
public interface DocumentStore extends Serializable {
    
    Object get(Object key);
    void put(Object key,Object value);
    void addDocumentListener(DocumentChangeListener listener);
    void removeDocumentListener(DocumentChangeListener listener);    
}
