/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

/**
 *
 * @author Valery
 */
public interface ErrorBinder {
     void notifyError(Exception e);
     boolean isPropertyError();
}
