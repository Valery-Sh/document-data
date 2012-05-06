package org.document;

/**
 *
 * @author Valery
 */
public interface PropertyValidator {
    void validate(String propPath,PropertyDataStore doc, Object value) throws ValidationException;
}
