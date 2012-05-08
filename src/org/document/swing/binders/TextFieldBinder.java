package org.document.swing.binders;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.document.binding.AbstractBinder;

/**
 *
 * @author Valery
 */
public class TextFieldBinder extends AbstractBinder implements DocumentListener{
    
    protected JTextField textField;
    
    public TextFieldBinder(String propName, JTextField textField) {
        this.textField = textField;
        this.propertyName = propName;
        textField.getDocument().addDocumentListener(this);
    }   
    @Override
    public void init(Object dataValue) {
       textField.getDocument().removeDocumentListener(this);
       dataChanged(dataValue);
       textField.getDocument().addDocumentListener(this);
    }
    @Override
    protected void setComponentValue(Object compValue) {
        String s = compValue == null ? "" : compValue.toString();
        String oldValue = textField.getText();
        if ( s.isEmpty() && (oldValue == null || oldValue.isEmpty()) ) {
            return;
        }
        if ( oldValue != null && oldValue.equals(s) ) {
            return;
        }
        textField.setText(s);
    }


    @Override
    protected Object componentValueOf(Object dataValue) {
        return dataValue == null ? null : dataValue.toString();
    }

    @Override
    protected Object dataValueOf(Object compValue) {
        String sv = compValue == null ? null : compValue.toString();
        if ( sv != null && sv.trim().isEmpty()) {
            sv = null;
        }
        return sv;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
//        System.out.println("*** NEWVALUE 1 = " + textField.getText());
        
        this.componentChanged(textField.getText()); 
/*        try {
            this.componentChanged(null, e.getDocument().getText(0,e.getDocument().getLength()));        
        } catch(Exception ex){
            System.out.println("____ insertUpdate " + ex.getMessage()) ;
        }
*/
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        this.componentChanged(textField.getText());        
        try {
//            this.componentChanged(null, e.getDocument().getText(0,e.getDocument().getLength()));        
        } catch(Exception ex){
            
        }
        
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        System.out.println("*** NEWVALUE 3 = " + textField.getText());
    }

    @Override
    public Object getComponentValue() {
        return this.textField.getText();
    }
    
/*    @Override
    public void setDirtyComponentValue(Object value) {
        this.setComponentValue(value);
    }
*/
}
