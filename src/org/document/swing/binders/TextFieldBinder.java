package org.document.swing.binders;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.document.binding.AbstractEditablePropertyBinder;
import org.document.binding.DefaultBinderConvertor;

/**
 *
 * @author Valery
 */
public class TextFieldBinder extends AbstractEditablePropertyBinder implements DocumentListener {

    protected JTextField textField;

    public TextFieldBinder(String propName, JTextField textField) {
        this.textField = textField;
        this.propertyName = propName;
        initBinder();
    }

    protected final void initBinder() {
        textField.getDocument().removeDocumentListener(this);
        textField.getDocument().addDocumentListener(this);
        converter = new DefaultBinderConvertor(this);
    }
    
    @Override
    protected void setComponentValue(Object compValue) {
        String s = compValue == null ? "" : compValue.toString();
        String oldValue = textField.getText();
        if (s.isEmpty() && (oldValue == null || oldValue.isEmpty())) {
            return;
        }
        if (oldValue != null && oldValue.equals(s)) {
            return;
        }
        textField.setText(s);
    }

    /*************************************************************/
    /*  The JTextField component specific methods implementation */
    /*************************************************************/
    
    @Override
    protected Object componentValueOf(Object dataValue) {
        return dataValue == null ? null : dataValue.toString();
    }
    @Override
    protected Object propertyValueOf(Object compValue) {
        if ( converter == null ) {
            converter = new DefaultBinderConvertor(this);
        }
        return converter.propertyValueOf(compValue);
    }

/*    @Override
    protected Object propertyValueOf(Object compValue) {
        String sv = compValue == null ? null : compValue.toString();
        if (sv != null && sv.trim().isEmpty()) {
            sv = null;
        }
        return sv;
    }
*/

    @Override
    public Object getComponentValue() {
        return this.textField.getText();
    }

    @Override
    public void initComponentDefault() {
        this.textField.setText("");
    }

    @Override
    protected void addComponentListeners() {
        textField.getDocument().addDocumentListener(this);
    }

    @Override
    protected void removeComponentListeners() {
        textField.getDocument().removeDocumentListener(this);

    }
    /*************************************************************/
    /*   javax.swing.event.DocumentListener implementation       */
    /*************************************************************/
    @Override
    public void insertUpdate(DocumentEvent e) {
        this.componentChanged(textField.getText());
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        this.componentChanged(textField.getText());
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        System.out.println("*** NEWVALUE 3 = " + textField.getText());
    }
    
}
