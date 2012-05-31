/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

public class BdComponentBinder extends AbstractPropertyBinder {
    

    public BdComponentBinder() {
        initBinder();
    }       
    
    protected final void initBinder() {
        converter = new DefaultBinderConvertor(this);
    }
    
    @Override
    protected void setComponentValue(Object componentValue) {
    }


    @Override
    protected Object componentValueOf(Object propertyValue) {
        if ( converter == null ) {
            converter = new DefaultBinderConvertor(this);
        }
        return converter.componentValueOf(propertyValue);

    }

    @Override
    protected Object propertyValueOf(Object componentValue) {
        if ( converter == null ) {
            converter = new DefaultBinderConvertor(this);
        }
        return converter.componentValueOf(componentValue);
    }

    @Override
    public Object getComponentValue() {
        return null;
    }

    @Override
    public void initComponentDefault() {
    }


}
    

