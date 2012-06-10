/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

/**
 *
 * @author Valery
 */
public interface BinderEventSubject {
    /**
     * Adds an object that implements <code>BinderListener</code>.
     * @param l 
     */
    void addBinderListener(BinderListener l);
    /**
     * Removes an object that implements <code>BinderListener</code>
     * @param l 
     */
    void removeBinderListener(BinderListener l);
    
}
