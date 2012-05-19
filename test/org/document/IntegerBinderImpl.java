/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import org.document.binding.AbstractPropertyBinder;

/**
 *
 * @author Valery
 */
public class IntegerBinderImpl extends AbstractPropertyBinder{
    
    Object componentValue;
    public IntegerBinderImpl(String propName) {
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
        return compValue == null ? null : Integer.parseInt(compValue.toString().trim());
    }
    
}
