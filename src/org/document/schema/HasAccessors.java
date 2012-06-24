/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.schema;

/**
 *
 * @author Valery
 */
public interface HasAccessors {
    boolean hasGettter();
    boolean hasSetter();
    Object get(Object target) throws Exception;
    void set(Object target,Object value) throws Exception;
}
