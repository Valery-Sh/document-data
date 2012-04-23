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
    void componentChanged(Object oldValue, Object newValue);
    Object getDataValue();
    Object getComponentValue();
    void setRegistry(BinderRegistry registry);
}
