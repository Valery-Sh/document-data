package org.document.swing.binders;

import javax.swing.JLabel;
import org.document.binding.AbstractPropertyBinder;
import org.document.binding.DefaultBinderConvertor;

/**
 *
 * @author V. Shyshkin
 */
public class BdLabelBinder extends AbstractPropertyBinder {
    
    protected JLabel textField;
    
    public BdLabelBinder(String propertyName, JLabel textField) {
        this.textField = textField;
        this.propertyName = propertyName;
        initBinder();
    }   
    public BdLabelBinder(JLabel textField) {
        this.textField = textField;
        initBinder();
    }       
    protected final void initBinder() {
        converter = new DefaultBinderConvertor(this);
    }
    
    @Override
    protected void setComponentValue(Object componentValue) {
        textField.setText(componentValue.toString());
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
        return this.textField.getText();
    }

    @Override
    public void initComponentDefault() {
        this.textField.setText("");
    }


}
