/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public class HasTextBinder extends AbstractBinder {
    
    protected HasText component;
    
    public HasTextBinder(String propertyPath, HasText component) {
        this.propertyPath = propertyPath;
        this.component = component;
    }
    
    @Override
    protected Object componentValueOf(Object dataValue) {
        return  dataValue == null ? "" : dataValue.toString();
    }

    @Override
    protected Object dataValueOf(Object compValue) {
        return component.getText();
    }

    @Override
    public String getPath() {
        return this.propertyPath;
    }

    @Override
    public void dataChanged(Object oldValue, Object newValue) {
        String text = (String)this.componentValueOf(newValue);
        component.setText(text);
    }
    
}
