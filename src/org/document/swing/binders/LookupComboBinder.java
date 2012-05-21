
package org.document.swing.binders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import org.document.binding.AbstractEditablePropertyBinder;

/**
 *
 * @author V. Shyshkin
 */
public class LookupComboBinder<E> extends AbstractEditablePropertyBinder  implements ActionListener{
    
    protected JComboBox component;
    
    public LookupComboBinder(String propName, JComboBox component) {
        this.component = component;
        this.propertyName = propName;
        initBinder();
    }
    protected final void initBinder() {
        
    }
    @Override
    protected void addComponentListeners() {
        component.addActionListener(this);
    }

    @Override
    protected void removeComponentListeners() {
        component.removeActionListener(this);
    }

    @Override
    public Object getComponentValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void setComponentValue(Object compValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Object componentValueOf(Object dataValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Object propertyValueOf(Object compValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void initComponentDefault() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
