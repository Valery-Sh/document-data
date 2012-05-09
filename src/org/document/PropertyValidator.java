package org.document;

/**
 *
 * @author Valery
 */
public interface PropertyValidator {
    void validate(String propPath,PropertyStore doc, Object value) throws ValidationException;
}
