package org.document.swing.binders;

import javax.swing.JLabel;
import org.document.ValidationException;
import org.document.binding.AbstractErrorBinder;

/**
 *
 * @author V. Shyshkin
 */
public class JDocumentErrorBinder extends AbstractErrorBinder{
    
    protected JLabel textField;
    
    public JDocumentErrorBinder(JLabel textField) {
        this.textField = textField;
    }   
    
    @Override
    protected void setComponentValue(Object compValue) {
        String s = compValue == null ? "" : compValue.toString();
        textField.setText(s);
    }
    
    @Override
    public Object getComponentValue() {
        return textField.getText();
    }


    @Override
    protected Object componentValueOf(Object dataValue) {
        boolean dv = (Boolean)dataValue;
        String result = "";
        return dv ? exception.getMessage() : ""; 
    }

    @Override
    protected Object propertyValueOf(Object compValue) {
        if ( compValue == null ) {
            return false;
        }
        String s = compValue.toString().trim();
        if ( s.isEmpty() ) {
            return false;
        }
        return true;
    }
    
    @Override
    public void notifyError(ValidationException e) {
        //super.notifyError(e);
        exception = e;
        if ( e == null ) {
            textField.setVisible(false);
            propertyChanged(false);
        } else {
            textField.setVisible(true);
            propertyChanged(true);
        }
    }

    @Override
    public void initComponentDefault() {
        this.textField.setText("");
    }

    @Override
    public boolean isPropertyError() {
        return false;
    }

    @Override
    public void notifyError(String propertyName, ValidationException e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public void notifyFixed() {
        notifyError(null);
    }

    @Override
    public void notifyFixed(String propertyName, ValidationException e) {
        notifyError(e);
    }

    @Override
    public void clear(String propertyName) {
        this.notifyError(null);
    }

}
