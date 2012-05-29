package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface Validator {
    void validate(Document document);
    void validate(Object key, Object value, Document document);
    
}
