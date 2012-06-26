/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.schema;

/**
 *
 * @author Valery
 */
public interface PropertyChangeAccessors {
    boolean hasAddPropertyChangeListener();
    boolean hasAddBoundPropertyChangeListener();

    void addPropertyChangeListener(Object target, Object value) throws Exception;
    void addBoundPropertyChangeListener(Object target, Object value) throws Exception;
}
