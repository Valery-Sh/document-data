/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.io.Serializable;

/**
 *
 * @author V. Shyshkin
 */
public interface Document extends Serializable {
    
    Object get(Object key);
    void put(Object key,Object value);
    void setPropertyChangeHandler(PropertyChangeHandler handler);
}
