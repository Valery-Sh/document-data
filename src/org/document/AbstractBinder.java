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
    protected BindingManager bindingManager;

    @Override
    public String getDataEntityName() {
        return this.propertyPath;
    }
    
    @Override
    public void dataChanged(Object oldValue, Object newValue) {
        Object convValue = this.componentValueOf(newValue);
        if ( ! needChangeComponent(convValue) ) {
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
    protected boolean needChangeComponent(Object value) {
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
    protected boolean needChangeData(Object value) {
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
    public void setBindingManager(BindingManager bindingManager) {
        this.bindingManager = bindingManager;
    }

    @Override
    public void componentChanged(Object oldValue, Object newValue) {
        bindingManager.notifyError(this, null);
        Object convValue = null;
        try {
            convValue = this.dataValueOf(newValue);
            if ( ! needChangeData(convValue) ) {
                return;
            }
            setDataValue(convValue);
        } catch(ValidationException e) {
            throw e;
        } catch(Exception e) {
            bindingManager.notifyError(this, e);
        }
    }

    @Override
    public Object getDataValue() {
        return bindingManager.getDocument().get(this.propertyPath);
    }
    /**
     * return current component value 
     */
    @Override
    public abstract Object getComponentValue();

    protected void setDataValue(Object dataValue) {
        //this.bindingManager.getDocument().put(propertyPath, dataValue);
        this.bindingManager.getDocument().put(this, dataValue);
    }
    
    protected abstract void setComponentValue(Object compValue);

    
    
    protected abstract Object componentValueOf(Object dataValue);

    
    protected abstract Object dataValueOf(Object compValue);
    
}
