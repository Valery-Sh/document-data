/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public abstract class AbstractBinder implements Binder {

    protected String propertyPath;
    protected BinderRegistry registry;

    @Override
    public String getPath() {
        return this.propertyPath;
    }
    
    @Override
    public void dataChanged(Object oldValue, Object newValue) {
        if (oldValue == null && newValue == null) {
            return;
        }
        if (oldValue != null) {
            if (oldValue.equals(newValue)) {
                return;
            }
        } else if (newValue.equals(oldValue)) {
            return;
        }
        
        Object convValue = this.componentValueOf(newValue);
        
        Object currentValue = getComponentValue();
        if ( currentValue == null && convValue == null ) {
            return;
        } else if ( currentValue != null  ) {
            if ( currentValue.equals(convValue)) {
                return;
            }
        } else {
            if ( convValue.equals(currentValue)) {
                return;
            }
        }
        
        setCompValue(convValue);
    }

    @Override
    public void setRegistry(BinderRegistry registry) {
        this.registry = registry;
    }

    protected void componentChanged(Object oldValue, Object newValue) {
        if (oldValue == null && newValue == null) {
            return;
        }
        if (oldValue != null) {
            if (oldValue.equals(newValue)) {
                return;
            }
        } else if (newValue.equals(oldValue)) {
            return;
        }
        try {
            Object convValue = this.dataValueOf(newValue);
            registry.validate(propertyPath, convValue);
            setDataValue(convValue);
        } catch(Exception e) {
            registry.notifyError(this, e);
        }
    }

    protected abstract void setCompValue(Object compValue);

    protected abstract void setDataValue(Object dataValue);
    
    protected abstract Object componentValueOf(Object dataValue);
    /**
     * return current component value 
     */
    protected abstract Object getComponentValue();

    protected abstract Object dataValueOf(Object compValue);
    
}
