/*
 * 
 */
package org.document.binding;

import org.document.*;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractEditablePropertyBinder extends AbstractPropertyBinder {

    protected boolean binderIsStillChangingProperty;  //propertyValueChangingInProgressByTheBinder

    /**
     *
     * @param componentValue the new component specific value
     */
    protected void componentChanged(boolean notifyOfErrors, Object componentValue) {
        if (isSuspended()) {
            return;
        }

        if (document == null) {
            return;
        }
        if (binderIsStillChangingProperty) {
            return;
        }
        if (document.propertyStore() instanceof HasDocumentState) {
            DocumentState state = ((HasDocumentState) document.propertyStore()).getDocumentState();
            state.getDirtyValues().put(boundProperty, componentValue);
        }
        //if (notifyOfErrors) {
        fireClearPropertyError();
        //}

        Object convertedValue;
        Object oldDataValue = document.propertyStore().get(boundProperty);
        try {
            convertedValue = this.propertyValueOf(componentValue);
            if (DataUtils.equals(convertedValue, oldDataValue)) {
                return;
            }
            if (document instanceof HasValidator) {
                Validator v = ((HasValidator) document).getValidator();
                if (v != null) {
                    v.validate(boundProperty, convertedValue, document);
                }
            }
            binderIsStillChangingProperty = true;
            document.propertyStore().put(boundProperty, convertedValue);

            fireComponentValueChange(convertedValue, componentValue);
            /*
             * Some error binders may accumulate property error info 
             * until fixed. So we  must notify them that an error fixed.
             */
            fireClearPropertyError();
            //binderIsStillChangingProperty = false;
            //updateComponentView(convertedValue);
        } catch (ValidationException e) {
            if (notifyOfErrors) {
                firePropertyError(e);
            }
        } catch (Exception e) {
            if (notifyOfErrors) {
                ValidationException ve = new ValidationException(boundProperty, "Property name= '" + boundProperty + "'. Invalid value: " + componentValue, document);
                firePropertyError(ve);
            }
        } finally {
            binderIsStillChangingProperty = false;
        }
    }

    /**
     *
     * @param componentValue the new component specific value
     */
    protected void componentChanged(Object componentValue) {
        this.componentChanged(true, componentValue);
    }

    @Override
    public void react(DocumentChangeEvent event) {
        super.react(event);
        if (isSuspended()) {
            return;
        }
        if (event.getAction() == DocumentChangeEvent.Action.completeChanges) {
            componentChanged(false, getComponentValue());
        }
    }

    private void fireComponentValueChange(Object dataValue, Object componentValue) {
        BinderEvent.Action action =
                BinderEvent.Action.componentChange;
        BinderEvent event = new BinderEvent(this, action, dataValue, componentValue);
        notifyListeners(event);
    }

    private void fireClearPropertyError() {
        BinderEvent.Action action = BinderEvent.Action.clearError;
        BinderEvent event = new BinderEvent(this, action, null);
        notifyListeners(event);
    }

    protected void firePropertyError(ValidationException e) {
        BinderEvent.Action action = BinderEvent.Action.componentChangeError;
        BinderEvent event = new BinderEvent(this, action, e);
        notifyListeners(event);
    }

    private void notifyListeners(BinderEvent event) {
        if (binderListeners == null) {
            return;
        }
        for (BinderListener l : binderListeners) {
            l.react(event);
        }
    }

    /**
     * It is assumed that this method should be called when you want to set the
     * value of the component and do not want that in response a component
     * generated an event. In this implementation the method does nothing.
     *
     * @param dataValue a data value. Before assign it to a component it should
     * be converted to a component value.
     */
    @Override
    protected void propertyChanged(Object propertyValue) {
        if (binderIsStillChangingProperty) {
            return;
        }
        if (isSuspended()) {
            return;
        }
        Object convertedValue = this.componentValueOf(propertyValue);
        if (!needChangeComponent(convertedValue)) {
            return;
        }
        removeComponentListeners();
        setComponentValue(convertedValue);
        addComponentListeners();
    }

    protected abstract void addComponentListeners();

    protected abstract void removeComponentListeners();
}
