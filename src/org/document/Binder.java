/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public interface Binder {
    String getPath();
    void dataChanged(Object oldValue, Object newValue);
    void setRegistry(BinderRegistry registry);
}
