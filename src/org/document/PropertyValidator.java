package org.document;

/**
 *
 * @author Valery
 */
public interface PropertyValidator {
    void validate(String propPath,DocumentStore doc, Object value) throws ValidationException;
}
