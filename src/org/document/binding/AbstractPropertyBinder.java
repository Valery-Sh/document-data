/*
 * 
 */
package org.document.binding;

import java.util.ArrayList;
import java.util.List;
import org.document.*;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractPropertyBinder implements PropertyBinder {

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
        if (this.binderListeners == null) {
            this.binderListeners = new ArrayList<BinderListener>(1);
        }
        binderListeners.add(l);
    }

    @Override
    public void removeBinderListener(BinderListener l) {
        if (this.binderListeners == null) {
            return;
        }
        this.binderListeners.remove(l);
    }


    @Override
    public void react(DocumentChangeEvent event) {
        switch (event.getAction()) {
            case documentChange:
                this.document = (Document) event.getNewValue();
                if (document != null && getPropertyName() != null) {
                    initComponent(document.getPropertyStore().get(getPropertyName()));
                } else if (document == null) {
                    initComponentDefault();
                }
                return;
            case propertyChange:
                this.initComponent(event.getNewValue());
                return;
        }//switch
    }

    @Override
    public String getPropertyName() {
        return this.propertyName;
    }

    /**
     * The method is called when a data value (for example a property value) is
     * changed, and in response, it is necessary to change the value in the
     * associated component. First? the method converts data to a component
     * value by calling an abstract method
     * {@link #componentValueOf(java.lang.Object) }. The method checks whether
     * or not to actually change the value of the component (possibly a
     * component already has the same meaning) and, if so, then the new value
     * assigned to the component by calling a protected method {@link #setComponentValue(java.lang.Object).
     * Usually, the method is not overriden by subclasses. Instead, you
     * might to override the method
     * <code>setComponentValue</code>.
     *
     * @param newValue
     */
    protected void dataChanged(Object newValue) {
        Object convValue = this.componentValueOf(newValue);
        if (!needChangeComponent(convValue)) {
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
        if (value == null && currentValue == null) {
            result = false;
        }
        if (value != null && value.equals(currentValue)) {
            result = false;
        } else if (currentValue != null && currentValue.equals(value)) {
            result = false;
        }
        return result;
    }

/*    private void firePropertyChanging(Object dataValue, Object componentValue) {
        BinderEvent.Action action = BinderEvent.Action.propertyChanging;
        BinderEvent event = new BinderEvent(this, action, dataValue, componentValue);
        notifyListeners(event);
    }

    private void fireComponentValueChange(Object dataValue, Object componentValue) {
        BinderEvent.Action action =
                BinderEvent.Action.componentValueChange;
        BinderEvent event = new BinderEvent(this, action, dataValue, componentValue);
        notifyListeners(event);
    }

    private void fireClearPropertyError() {
        BinderEvent.Action action = BinderEvent.Action.clearComponentChangeError;
        BinderEvent event = new BinderEvent(this, action, null);
        notifyListeners(event);
    }

    private void firePropertyError(Exception e) {
        BinderEvent.Action action = BinderEvent.Action.componentChangeValueError;
        BinderEvent event = new BinderEvent(this, action, e);
        notifyListeners(event);
    }

    private void notifyListeners(BinderEvent event) {
        for (BinderListener l : binderListeners) {
            l.react(event);
        }
    }
*/
    /**
     * It is assumed that this method should be called when you want to set the
     * value of the component and do not want that in response a component
     * generated an event. In this implementation the method does nothing.
     *
     * @param dataValue a data value. Before assign it to a component it should
     * be converted to a component value.
     */
    @Override
    public void initComponent(Object dataValue) {
        Object convertedValue = this.componentValueOf(dataValue);
        if (!needChangeComponent(convertedValue)) {
            return;
        }
        setComponentValue(convertedValue);
    }

    /**
     * return current component value
     */
    @Override
    public abstract Object getComponentValue();
    
    //protected abstract void componentChanged(Object componentValue);
    
    protected abstract void setComponentValue(Object compValue);

    protected abstract Object componentValueOf(Object dataValue);

    protected abstract Object dataValueOf(Object compValue);
    

}
