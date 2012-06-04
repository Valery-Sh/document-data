package org.document.swing.binders;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.document.binding.AbstractEditablePropertyBinder;
import org.document.binding.DefaultBinderConvertor;

/**
 *
 * @author V. Shyshkin
 */
public class BdTextFieldBinder extends AbstractEditablePropertyBinder implements DocumentListener {//, ActionListener {
    
    public String _ID_; // for test ppurpose
    
    protected JTextField textField;

    public BdTextFieldBinder(JTextField textField) {
        this.textField = textField;
        initBinder();
    }
    
    public BdTextFieldBinder(String propertyName, JTextField textField) {
        this.textField = textField;
        this.boundProperty = propertyName;
        initBinder();
    }

    protected final void initBinder() {
        textField.getDocument().removeDocumentListener(this);
        textField.getDocument().addDocumentListener(this);
       // textField.removeActionListener(this);
       // textField.addActionListener(this);
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
        //((PlainDocument)textField.getDocument()).getDefaultRootElement()
        textField.setText(s);
    }

    /*************************************************************/
    /*  The JTextField component specific methods implementation */
    /*************************************************************/
    
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
        return converter.propertyValueOf(componentValue);
    }


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
    }


}
