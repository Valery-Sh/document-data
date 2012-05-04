package org.document;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractBinder implements Binder {

    protected String propertyName;
    protected Document document;
//    protected DocumentBinding binding;
    protected List<BinderListener> binderListeners;
    
    @Override
    public void addBinderListener(BinderListener l) {
        if ( this.binderListeners == null) {
            this.binderListeners = new ArrayList<BinderListener>(1);
        }
        this.binderListeners.add(l);
    }
    @Override
    public void removeBinderListener(BinderListener l) {
        if ( this.binderListeners == null) {
            return;
        }
        this.binderListeners.remove(l);
    }   
    
    @Override
    public void react(DocumentEvent event) {
        switch(event.getAction()) {
            case documentChange :
                this.document = (Document)event.getNewValue();
                break;
        }//switch
    }
    @Override
    public String getPropertyName() {
        return this.propertyName;
    }
    
    @Override
    public void dataChanged(Object newValue) {
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
    private boolean needChangeComponent(Object value) {
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
/*    protected boolean needChangeData(Object value) {
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
*/
    @Override
    public void componentChanged(Object newValue) {
        //binding.notifyPropertyError(this.getPropertyName(), null);
        fireClearPropertyError();
        Object convValue;
        try {
            convValue = this.dataValueOf(newValue);
            //  changePropertyValueRequest,
            fireChangeDataValue(convValue,newValue);
/*            if ( ! needChangeData(convValue) ) {
                return;
            }
            
            setDataValue(convValue);
 */
        } catch(ValidationException e) {
            throw e;
        } catch(Exception e) {
            firePropertyError(e);
            //binding.notifyPropertyError(this.getPropertyName(), e);
        }
    }
    
    private void fireChangeDataValue(Object dataValue, Object componentValue) {
        BinderEvent.Action action = BinderEvent.Action.changePropertyValueRequest;
        BinderEvent event = new BinderEvent(this,action,dataValue,componentValue);
        notifyListeners(event);
    }
    private void fireClearPropertyError() {
        BinderEvent.Action action = BinderEvent.Action.clearPropertyErrorRequest;
        BinderEvent event = new BinderEvent(this,action,null);
        notifyListeners(event);
    }
    private void firePropertyError(Exception e) {
        BinderEvent.Action action = BinderEvent.Action.notifyPropertyErrorRequest;
        BinderEvent event = new BinderEvent(this,action,e);
        notifyListeners(event);
    }
    
    private void notifyListeners(BinderEvent event) {
        for ( BinderListener l : binderListeners) {
            l.react(event);
        }
    }
    //@Override
/*    protected Object getDataValue() {
        return binding.getDocument().get(this.propertyName);
    }
*/
    /**
     * return current component value 
     */
    @Override
    public abstract Object getComponentValue();
    
    @Override
    public void init(Object dataValue){
        
    }
/*    protected void setDataValue(Object dataValue) {
        //this.binding.getDocument().put(propertyName, dataValue);
        this.binding.getDocument().put(this, dataValue);
    }
*/    
    protected abstract void setComponentValue(Object compValue);
    
    //protected abstract void restoreComponentValue(Object compValue);

    
    
    protected abstract Object componentValueOf(Object dataValue);

    
    protected abstract Object dataValueOf(Object compValue);
    
}
