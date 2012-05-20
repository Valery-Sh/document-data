/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.swing.binders;

import javax.swing.JLabel;
import org.document.binding.AbstractMultiErrorBinder;

/**
 *
 * @author Valery
 */
public class MultiErrorBinder extends AbstractMultiErrorBinder{
    protected JLabel textField;
    
    public MultiErrorBinder(JLabel textField) {
        super();
        this.textField = textField;
    }   
    
    @Override
    protected void setComponentValue(Object componentValue) {
        String s = componentValue == null ? "" : componentValue.toString();
        textField.setText(s);
    }

    @Override
    public Object getComponentValue() {
        return textField.getText();
    }

    @Override
    protected Object componentValueOf(Object propertyValue) {
        boolean dv = (Boolean) propertyValue;
        String result = dv ? exception.getMessage() : "";
        return result;
    }

    @Override
    protected Object propertyValueOf(Object componentValue) {
        if (componentValue == null) {
            return false;
        }
        String s = componentValue.toString().trim();
        if (s.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public void initComponentDefault() {
        this.textField.setText("");
    }

    @Override
    protected void setVisible(boolean visible) {
        textField.setVisible(visible);
    }

    @Override
    public boolean isPropertyError() {
        return true;
    }

    
}
