package org.document.swing.binders;

import javax.swing.JLabel;
import org.document.ValidationException;
import org.document.binding.AbstractErrorBinder;

/**
 *
 * @author V. Shyshkin
 */
public class PropertyErrorBinder extends AbstractErrorBinder{
    protected JLabel textField;
    
    public PropertyErrorBinder(String propName, JLabel textField) {
        this.textField = textField;
        this.propertyName = propName;
    }   
    @Override
    public void notifyError(ValidationException e) {
        //super.notifyError(e);
        if ( e == null ) {
            textField.setVisible(false);
            propertyChanged(false);
        } else {
            textField.setVisible(true);
            propertyChanged(true);
        }
    }
    @Override
    public boolean isPropertyError() {
        return true;
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
        boolean dv = (Boolean)propertyValue;
        String result = "";
        return dv ? exception.getMessage() : ""; 
    }

    @Override
    protected Object propertyValueOf(Object componentValue) {
        if ( componentValue == null ) {
            return false;
        }
        String s = componentValue.toString().trim();
        if ( s.isEmpty() ) {
            return false;
        }
        return true;
    }
    @Override
    public void initComponentDefault() {
        this.textField.setText("");
    }

    @Override
    public void notifyError(String propertyName, ValidationException e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void notifyFixed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void notifyFixed(String propertyName, ValidationException e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear(String propertyName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
