
package org.document.swing.binders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import org.document.binding.AbstractEditablePropertyBinder;
import org.document.binding.DefaultBinderConvertor;

/**
 *
 * @author V. Shyshkin
 */
public class LookupComboBinder<E> extends AbstractEditablePropertyBinder  implements ActionListener{
    
    
    public LookupComboBinder(JComboBox component) {
        this.boundObject = component;
        initBinder();
    }
    
    public LookupComboBinder(String propName, JComboBox component) {
        this.boundObject = component;
        this.boundProperty = propName;
        initBinder();
    }
    @Override
    public void setBoundProperty(String propertyName) {
        this.boundProperty = propertyName;
    }
    
    protected final void initBinder() {
        ((JComboBox)boundObject).removeActionListener(this);
        ((JComboBox)boundObject).addActionListener(this);
        converter = new DefaultBinderConvertor(this);
    }
    
    @Override
    protected void addComponentListeners() {
        ((JComboBox)boundObject).addActionListener(this);
    }

    @Override
    protected void removeComponentListeners() {
        ((JComboBox)boundObject).removeActionListener(this);
    }

    @Override
    public Object getComponentValue() {
        return ((JComboBox)boundObject).getModel().getSelectedItem();
    }

    @Override
    protected void setComponentValue(Object componentValue) {
        ((JComboBox)boundObject).getModel().setSelectedItem(componentValue);
    }

    @Override
    protected Object componentValueOf(Object propertyValue) {
        if (converter != null) {
            return converter.componentValueOf(propertyValue);
        }
        return null;
    }

    @Override
    protected Object propertyValueOf(Object componentValue) {
        if (converter != null) {
            return converter.propertyValueOf(componentValue);
        }
        return null;

    }

    @Override
    public void initComponentDefault() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        componentChanged(((JComboBox)boundObject).getModel().getSelectedItem());
    }

    
    
}
