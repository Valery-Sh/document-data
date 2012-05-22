
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
    
    protected JComboBox component;
    
    
    public LookupComboBinder(String propName, JComboBox component) {
        this.component = component;
        this.propertyName = propName;
        initBinder();
    }
    protected final void initBinder() {
        component.removeActionListener(this);
        component.addActionListener(this);
        converter = new DefaultBinderConvertor(this);
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
        return component.getModel().getSelectedItem();
    }

    @Override
    protected void setComponentValue(Object componentValue) {
        component.getModel().setSelectedItem(componentValue);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        componentChanged(component.getModel().getSelectedItem());
    }
    
/*    protected static class DefaultLookupComboConverter extends DefaultBinderConvertor {
        
        public DefaultLookupComboConverter(PropertyBinder binder) {
            super(binder);
        }
        
        public Object componentValueOf(Object propertyValue) {
            
        }
    }
*/
}
