package org.document.swing.binders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import org.document.binding.AbstractEditablePropertyBinder;
import org.document.binding.DefaultBinderConvertor;

/**
 *
 * @author Valery
 */
public class TextFieldBinder extends AbstractEditablePropertyBinder implements DocumentListener, ActionListener {
    
    public String _ID_;
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
        
        textField.addActionListener(this);
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
//        return dataValue == null ? null : dataValue.toString();
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
        //System.out.println("*** insertUpdate = " + textField.getText());        
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        javax.swing.text.Document dd;
        this.componentChanged(textField.getText());
        //System.out.println("*** removeUpdate = " + textField.getText());        
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        //System.out.println("*** changeUpdate = " + textField.getText());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        propertyChanged(document.propertyStore().get(propertyName));
    }
    
}
