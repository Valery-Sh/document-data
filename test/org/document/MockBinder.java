/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public class MockBinder implements Binder{
    
    String propName;
    Object component;
    BinderRegistry registry;
    
    boolean dataChanged;
    
    
    public static Binder create(String propName, Object component) {
        return new MockBinder(propName,component);
    }
    protected MockBinder(String propName, Object component) {
        this.component = component;
        this.propName = propName;
    }
    
    @Override
    public String getPath() {
        return this.propName;
    }

    @Override
    public void dataChanged(Object oldValue, Object newValue) {
        this.dataChanged = true;
    }

    @Override
    public void setRegistry(BinderRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Object getDataValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getComponentValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void componentChanged(Object oldValue, Object newValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
