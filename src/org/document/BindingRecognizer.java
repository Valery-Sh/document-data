/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public interface BindingRecognizer<T> {
    Object getBindingId(T document);
}
