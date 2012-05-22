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
    protected boolean componentBusy;    
    /**
     * 
     * @param componentValue the new component specific value
     */
    protected void componentChanged(Object componentValue) {
        if (document == null) {
            return;
        }
        if (componentBusy ) {
            return;
        }
        if (document.propertyStore() instanceof HasDocumentState) {
            DocumentState state = ((HasDocumentState) document.propertyStore()).getDocumentState();
            state.getDirtyValues().put(propertyName, componentValue);
        }

        fireClearPropertyError();

        Object convertedValue;
        Object oldDataValue = document.propertyStore().get(propertyName);
        try {
            convertedValue = this.propertyValueOf(componentValue);
            if (DataUtils.equals(convertedValue, oldDataValue)) {
                return;
            }
            if (document instanceof HasValidator) {
                Validator v = ((HasValidator) document).getValidator();
                if (v != null) {
                    v.validate(propertyName, convertedValue);
                }
            }
            componentBusy = true;
            document.propertyStore().put(propertyName, convertedValue);
            fireComponentValueChange(convertedValue, componentValue);
        } catch (ValidationException e) {
            firePropertyError(e);
        } catch (Exception e) {
            ValidationException ve = new ValidationException(propertyName, "Property name= '" + propertyName +"'. Invalid value: " + componentValue );
            firePropertyError(ve);
        } finally {
            componentBusy = false;
        }
    }

    @Override
    public void react(DocumentChangeEvent event) {
        super.react(event);
        if (event.getAction() == DocumentChangeEvent.Action.completeChanges) {
            componentChanged(getComponentValue());
        }
    }

    private void firePropertyChanging(Object dataValue, Object componentValue) {
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

    /**
     * It is assumed that this method should be called when you want to set the
     * value of the component and do not want that in response a component
     * generated an event. In this implementation the method does nothing.
     *
     * @param dataValue a data value. Before assign it to a component it should
     * be converted to a component value.
     */
    @Override
    public void propertyChanged(Object propertyValue) {
        if ( componentBusy ) {
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
