/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

/**
 *
 * @author Valery
 */
public interface HasContext {
    BindingContext getContext();
    void setContext(BindingContext context);
    
}