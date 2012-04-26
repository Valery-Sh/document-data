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
    public String getDataEntityName() {
        return this.propertyPath;
    }
    
    @Override
    public void dataChanged(Object oldValue, Object newValue) {
        Object convValue = this.componentValueOf(newValue);
        if ( ! canChangeComponent(convValue) ) {
            return;
        }
        setComponentValue(convValue);
    }
    /**
     * Prepends cyclic component modifications.
     * 
     * @param value a new value to be assigned
     * @return 
     */
    protected boolean canChangeComponent(Object value) {
        boolean result = true;
        
        Object currentValue = getComponentValue();
        if ( value == null && currentValue == null ) {
           result = false; 
        } if ( value != null && value.equals(currentValue) ) {
            result = false;
        } else if ( currentValue != null  && currentValue.equals(value) ) {
            result = false;
        }       
        return result;
    }

    /**
     * Prepends cyclic data modifications.
     * 
     * @param value a new value to be assigned
     * @return 
     */
    protected boolean canChangeData(Object value) {
        boolean result = true;
        
        Object currentValue = getDataValue();
        
        if ( value == null && currentValue == null ) {
           result = false; 
        } if ( value != null && value.equals(currentValue) ) {
            result = false;
        } else if ( currentValue != null  && currentValue.equals(value) ) {
            result = false;
        }       
        return result;
    }
    
    @Override
    public void setRegistry(BinderRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void componentChanged(Object oldValue, Object newValue) {
        registry.notifyError(this, null);
        Object convValue = null;
        try {
            convValue = this.dataValueOf(newValue);
            if ( ! canChangeData(convValue) ) {
                return;
            }
            registry.validate(propertyPath, convValue);
            setDataValue(convValue);
            //registry.notifyError(this, null);
        } catch(Exception e) {
            registry.notifyError(this, e);
            if ( e instanceof ValidationException ) {
                if ( registry.getDocument() instanceof Editable ) {
                     setDataValue(convValue);
                }
            }
        }
    }

    @Override
    public Object getDataValue() {
        return registry.getDocument().get(this.propertyPath);
    }
    /**
     * return current component value 
     */
    @Override
    public abstract Object getComponentValue();

    protected void setDataValue(Object dataValue) {
        this.registry.getDocument().put(propertyPath, dataValue);
    }
    
    protected abstract void setComponentValue(Object compValue);

    
    
    protected abstract Object componentValueOf(Object dataValue);

    
    protected abstract Object dataValueOf(Object compValue);
    
}
