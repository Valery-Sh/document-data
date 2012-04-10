/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public interface BinderRegistry extends PropertyChangeHandler{
    void add(Binder binder);
    void remove(Binder binder);
}
