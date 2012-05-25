package org.document;

import java.io.Serializable;

/**
 *
 * @author V. Shyshkin
 */
public interface PropertyStore extends Serializable {
    
    Object getAlias();
    
    Object get(Object key);

    void put(Object key, Object value);

    //void validate(Object key, Object value) throws ValidationException;
    
    void addDocumentChangeListener(DocumentChangeListener listener);

    void removeDocumentChangeListener(DocumentChangeListener listener);
}
