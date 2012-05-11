package org.document.swing.binders;

import javax.swing.JLabel;
import org.document.binding.AbstractDocumentErrorBinder;

/**
 *
 * @author V. Shyshkin
 */
public class DefaultDocumentErrorBinder extends AbstractDocumentErrorBinder{
    
    protected JLabel textField;
    
    public DefaultDocumentErrorBinder(JLabel textField) {
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
    protected Object dataValueOf(Object compValue) {
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
    public void notifyError(Exception e) {
        super.notifyError(e);
        if ( ! isErrorFound() ) {
            textField.setVisible(false);
            dataChanged(false);
        } else {
            textField.setVisible(true);
            dataChanged(true);            
        }
    }

    @Override
    public void initComponentDefault() {
        this.textField.setText("");
    }

    
}
