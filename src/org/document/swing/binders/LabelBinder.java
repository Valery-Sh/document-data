package org.document.swing.binders;

import javax.swing.JLabel;
import org.document.binding.AbstractBinder;

/**
 *
 * @author Valery
 */
public class LabelBinder extends AbstractBinder {
    
    protected JLabel textField;
    
    public LabelBinder(String propName, JLabel textField) {
        this.textField = textField;
        this.propertyName = propName;
    }   
    
    @Override
    protected void setComponentValue(Object compValue) {
        textField.setText(compValue.toString());
    }


    @Override
    protected Object componentValueOf(Object dataValue) {
        return dataValue == null ? "" : dataValue;
    }

    @Override
    protected Object dataValueOf(Object compValue) {
        return compValue == null ? "" : compValue;
    }

    @Override
    public Object getComponentValue() {
        return this.textField.getText();
    }

}
