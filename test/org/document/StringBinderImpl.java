/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import org.document.binding.AbstractBinder;

/**
 *
 * @author V. Shyshkin
 */
public class StringBinderImpl  extends AbstractBinder{
    
    Object componentValue;
    
    public StringBinderImpl(String propName) {
        this.propertyName = propName;
    }   
    
    @Override
    public Object getComponentValue() {
        return this.componentValue;
    }

    @Override
    protected void setComponentValue(Object compValue) {
        componentValue = compValue;
    }

    @Override
    protected Object componentValueOf(Object dataValue) {
        return dataValue == null ? "" : dataValue.toString();
    }

    @Override
    protected Object dataValueOf(Object compValue) {
        return compValue == null ? null : compValue.toString();
    }
}
