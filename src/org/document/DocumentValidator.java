/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public interface DocumentValidator {
    //void validate(DocumentStore store) throws ValidationException;
    void validate(Document document) throws ValidationException;
}
