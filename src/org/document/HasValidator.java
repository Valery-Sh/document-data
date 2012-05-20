package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface HasValidator {
    Validator getValidator();
    void setValidator(Validator validator);
}
