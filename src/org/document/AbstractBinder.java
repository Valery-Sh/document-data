/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public abstract class AbstractBinder implements Binder{
    protected String propertyPath;
//    protected String propertyPath;
    
    @Override
    public void bind() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    protected abstract Object componentValueOf(Object dataValue);
    protected abstract Object dataValueOf(Object compValue);    
    
    
    
}
