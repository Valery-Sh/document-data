/*
 * 
 */
package org.document;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractBinder implements PropertyBinder {
    
    private Object alias;
    protected String propertyName;
    protected Document document;

    protected List<BinderListener> binderListeners;
    
    @Override
    public Object getAlias() {
        return alias;
    }
    
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
    public void react(DocumentChangeEvent event) {
        switch(event.getAction()) {
            case documentChange :
                this.document = (Document)event.getNewValue();
                if ( document != null && getPropertyName() != null) {
                    init(document.getPropertyDataStore().get(getPropertyName()));
                }
                break;
        }//switch
    }
    @Override
    public String getPropertyName() {
        return this.propertyName;
    }
    /**
     * The method is called when a data value (for example 
     * a property value)  is changed, 
     * and in response, it is necessary to change the value 
     * in the associated component. First? the method converts data
     * to a component value by calling an abstract method
     * {@link #componentValueOf(java.lang.Object)  }. 
     * The method checks whether or not to actually change the 
     * value of the component 
     * (possibly a component already has the same meaning) and, 
     * if so, then the new value assigned to the component by calling
     * a protected method {@link #setComponentValue(java.lang.Object).
     * Usually, the method is not overriden by subclasses. Instead, you 
     * might to override the method <code>setComponentValue</code>.
     * 
     * @param newValue 
     */
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

    @Override
    public void componentChanged(Object newValue) {
        fireClearPropertyError();
        Object convValue;
        try {
            convValue = this.dataValueOf(newValue);
            fireChangeDataValue(convValue,newValue);
        } catch(ValidationException e) {
            throw e;
        } catch(Exception e) {
            firePropertyError(e);
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
    /**
     * return current component value 
     */
    @Override
    public abstract Object getComponentValue();
    
    /**
     * It is assumed that this method should be called when you want 
     * to set the value of the component and do not want that in response
     * a component generated an event.
     * In this implementation the method does nothing.
     * @param dataValue a data value. Before assign it to a component it should
     * be converted to a component value.
     */

    @Override
    public void init(Object dataValue){
        
    }
    protected abstract void setComponentValue(Object compValue);
    protected abstract Object componentValueOf(Object dataValue);
    protected abstract Object dataValueOf(Object compValue);
    /**
     * For test purpose
     */
    public Object getValueByName() {
        if ( document == null ) {
            return null;
        }
        return document.getPropertyDataStore().get(getPropertyName());
    }
}
