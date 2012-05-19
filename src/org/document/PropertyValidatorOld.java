package org.document;

/**
 *
 * @author Valery
 */
public interface PropertyValidatorOld {
    void validate(String propPath,PropertyStore doc, Object value) throws ValidationException;
}
