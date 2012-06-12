
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
        super(component);
        initBinder();
    }
    
    public LookupComboBinder(String propName, JComboBox component) {
        super(component);
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
    public void addBoundObjectListeners() {
        ((JComboBox)boundObject).addActionListener(this);
    }

    @Override
    public void removeBoundObjectListeners() {
        ((JComboBox)boundObject).removeActionListener(this);
    }

    @Override
    public Object getBoundObjectValue() {
        return ((JComboBox)boundObject).getModel().getSelectedItem();
    }

    @Override
    protected void setBoundObjectValue(Object componentValue) {
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
    public void initBoundObjectDefaults() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boundObjectChanged(((JComboBox)boundObject).getModel().getSelectedItem());
    }

    
    
}
