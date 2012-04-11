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
//    protected String propertyPath;

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

        Object newConvValue = this.componentValueOf(newValue);
        setCompValue(newConvValue);
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

        Object newConvValue = this.dataValueOf(newValue);
        setDataValue(newConvValue);
    }

    protected abstract void setCompValue(Object compValue);

    protected abstract void setDataValue(Object dataValue);
    
    protected abstract Object componentValueOf(Object dataValue);

    protected abstract Object dataValueOf(Object compValue);
    
}
