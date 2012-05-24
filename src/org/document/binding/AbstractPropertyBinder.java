/*
 * 
 */
package org.document.binding;

import java.util.ArrayList;
import java.util.List;
import org.document.Document;
import org.document.DocumentChangeEvent;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractPropertyBinder implements PropertyBinder {
    
    
    
    private Object alias;
    protected String propertyName;
    protected Document document;
    protected List<BinderListener> binderListeners;
    protected BinderConverter converter;

    @Override
    public Object getAlias() {
        return alias;
    }

    @Override
    public BinderConverter getConverter() {
        return converter;
    }
    
    @Override
    public void setConverter(BinderConverter converter) {
        this.converter = converter;
    }

    @Override
    public Document getDocument() {
        return document;
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
        if (binderListeners == null || binderListeners.isEmpty()) {
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
                    propertyChanged(document.propertyStore().get(getPropertyName()));
                } else if (document == null) {
                    initComponentDefault();
                }
                return;
            case propertyChange:
                this.propertyChanged(event.getNewValue());
                return;
        }//switch
    }

    @Override
    public String getPropertyName() {
        return this.propertyName;
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


    /**
     * The method is called when a data value (for example a property value) is
     * changed, and in response, it is necessary to change the value in the
     * associated component. First the method converts data to a component
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
    @Override
    public void propertyChanged(Object propertyValue) {
        Object convertedValue = this.componentValueOf(propertyValue);
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
    
    protected abstract void setComponentValue(Object compValue);

    protected abstract Object componentValueOf(Object dataValue);

    protected abstract Object propertyValueOf(Object compValue);
    

}
