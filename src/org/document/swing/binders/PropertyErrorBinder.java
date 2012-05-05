package org.document.swing.binders;

import javax.swing.JLabel;
import org.document.AbstractPropertyErrorBinder;

/**
 *
 * @author V. Shyshkin
 */
public class PropertyErrorBinder extends AbstractPropertyErrorBinder{
    protected JLabel textField;
    
    public PropertyErrorBinder(String propName, JLabel textField) {
        this.textField = textField;
        this.propertyName = propName;
    }   
    @Override
    public void notifyError(Exception e) {
        super.notifyError(e);
        if ( ! errorFound ) {
            textField.setVisible(false);
            dataChanged(false);
        } else {
            textField.setVisible(true);
            dataChanged(true);            
        }
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

}
