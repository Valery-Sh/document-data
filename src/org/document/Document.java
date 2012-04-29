package org.document;

import java.io.Serializable;

/**
 *
 * @author V. Shyshkin
 */
public interface Document extends Serializable {
    
    Object get(Object key);
    void put(Object key,Object value);
    void addDocumentListener(DocumentListener listener);
    void removeDocumentListener(DocumentListener listener);    
}
