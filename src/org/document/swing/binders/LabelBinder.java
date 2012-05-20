package org.document.swing.binders;

import javax.swing.JLabel;
import org.document.binding.AbstractPropertyBinder;

/**
 *
 * @author V. Shyshkin
 */
public class LabelBinder extends AbstractPropertyBinder {
    
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
    protected Object propertyValueOf(Object compValue) {
        return compValue == null ? "" : compValue;
    }

    @Override
    public Object getComponentValue() {
        return this.textField.getText();
    }

    @Override
    public void initComponentDefault() {
        this.textField.setText("");
    }


}
