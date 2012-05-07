/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public class MockBinder implements PropertyBinder{
    
    String propName;
    Object component;
    DocumentBindings registry;
    Object componentValue;
    Object dataValue;
    boolean dataChanged;
    
    
    public static PropertyBinder create(String propName, Object component) {
        return new MockBinder(propName,component);
    }
    protected MockBinder(String propName, Object component) {
        this.component = component;
        this.propName = propName;
    }
    
    @Override
    public String getPropertyName() {
        return this.propName;
    }

    @Override
    public void dataChanged(Object oldValue, Object newValue) {
        dataChanged = true;
        componentValue = newValue;
    }

    @Override
    public void setDocumentBinding(DocumentBindings registry) {
        this.registry = registry;
    }

    @Override
    public Object getDataValue() {
        return dataValue;
    }

    @Override
    public Object getComponentValue() {
        return componentValue;
    }

    @Override
    public void componentChanged(Object oldValue, Object newValue) {
        dataValue = newValue;
    }
    
}
