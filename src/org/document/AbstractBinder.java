package org.document;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractBinder implements Binder {

    protected String propertyName;
    protected DocumentBinding binding;

    @Override
    public String getPropertyName() {
        return this.propertyName;
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
    public void setDocumentBinding(DocumentBinding binding) {
        this.binding = binding;
    }

    @Override
    public void componentChanged(Object oldValue, Object newValue) {
        binding.notifyError(this, null);
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
            binding.notifyError(this, e);
        }
    }

    @Override
    public Object getDataValue() {
        return binding.getDocument().get(this.propertyName);
    }
    /**
     * return current component value 
     */
    @Override
    public abstract Object getComponentValue();
    
    @Override
    public void init(Object dataValue){
        
    }
    protected void setDataValue(Object dataValue) {
        //this.binding.getDocument().put(propertyName, dataValue);
        this.binding.getDocument().put(this, dataValue);
    }
    
    protected abstract void setComponentValue(Object compValue);
    
    protected abstract void restoreComponentValue(Object compValue);

    
    
    protected abstract Object componentValueOf(Object dataValue);

    
    protected abstract Object dataValueOf(Object compValue);
    
}
