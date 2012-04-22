package org.document;

/**
 *
 * @author Valery
 */
public interface PropertyValidator {
    void validate(String propPath,Document doc, Object value) throws ValidationException;
}
