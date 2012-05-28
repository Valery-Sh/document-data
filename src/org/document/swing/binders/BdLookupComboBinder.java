/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.swing.binders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.document.DataUtils;
import org.document.Document;
import org.document.ValidationException;
import org.document.binding.AbstractEditablePropertyBinder;
import org.document.binding.DefaultBinderConvertor;

/**
 *
 * @author Valery
 */
public class BdLookupComboBinder<E> extends AbstractEditablePropertyBinder implements ActionListener {

    protected JLookupComboBox component;
    //private String[] lookupProperties;

    public BdLookupComboBinder(JLookupComboBox component) {
        this.component = component;
        initBinder();
    }

    public BdLookupComboBinder(String propName, JLookupComboBox component) {
        this.component = component;
        this.propertyName = propName;
        initBinder();
    }

    /*    public BdLookupComboBinder(String propName, JLookupComboBox component, String... lookupProperties) {
     this.component = component;
     this.propertyName = propName;
     //        this.lookupProperties = lookupProperties;
     initBinder();
     }
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /*    public String[] getLookupProperties() {
     return lookupProperties;
     }

     public void setLookupProperties(String[] lookupProperties) {
     this.lookupProperties = lookupProperties;
     }
     */
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
        if ( componentValue == null  ) {
            firePropertyError(new ValidationException("Theris no for"));
        }
    }

    @Override
    protected Object componentValueOf(Object propertyValue) {
        List<? extends Document> l = component.getModelSource();
        String lookupProperty = component.getLookupProperty();
        Document result = null;
        for (Document d : l) {
            Object value = d.propertyStore().get(lookupProperty);
            if (!DataUtils.equals(propertyValue, value)) {
                continue;
            }
            result = d;
            break;

        }
        return result;
    }

    @Override
    protected Object propertyValueOf(Object componentValue) {
        if (componentValue == null) {
            return null;
        }
        return ((Document) componentValue).propertyStore().get(component.getLookupProperty());

    }

    @Override
    public void initComponentDefault() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        componentChanged(component.getModel().getSelectedItem());
    }
}
