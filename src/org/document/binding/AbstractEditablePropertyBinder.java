package org.document.binding;

import org.document.*;

/**
 * The class extends the functionality of the <code>AbstractPropertyBinder</code>
 * in order  to ensure processing of events received from the component.
 * In order to subclass you should implement the following abstract methods:
 * <ol>
 *   <li>{@link #addBoundObjectListeners()}</li>
 *   <li>{@link #removeBoundObjectListeners()}</li>
 *   <li>{@link #getBoundObjectValue()}</li>
 *   <li>{@link #setBoundObjectValue(java.lang.Object) }</li>
 *   <li>{@link #propertyValueOf(java.lang.Object)  }</li>
 *   <li>{@link #componentValueOf(java.lang.Object) }</li>
 *   <li>{@link #initBoundObjectDefaults() </li>
 * </ol>
 * 
 * Consider for example the component <code>javax.swing.JTextField</code>.
 * To track changes in the text of the component the binder must register 
 * itself as a listener of the {@link javafx.swing.event.DocumentEvent}. 
 * Suppose the value of  the bound property changed. The binder must set 
 * a new value to the <code>JTextField</code>. This time we don't want the 
 * component to fire event as we don't like cyclic dependencies. The obvious
 * decision the binder takes is to remove itself from the listener list 
 * of the component. After the component changed add the binder  registers
 * itself again. Things become more complicated when a user changes 
 * the component by typing the text. Every type he presses a key the component
 * fires <code>DocumentEvent</code> and the binder must change the bound 
 * property. We often see a code to check "oldValue" and "newValue". Something
 * like:
 * <code>
 * <pre>
 *   public void setFirstName(String name) {
 *      String oldValue = this.firstName;
 *      firstName = name;
 *      if ( ! firstName.equals(name) ) {
 *          firePropertyChangeEvent("firstName",oldValue,firstName);
 *      }
 * 
 *   }
 * </pre>
 * </code>
 * It may seem that this solves the problem. But the <code>DocumentListener</code> 
 * of the component  <code>JTextField</code>  has three methods and sometime 
 * only one is invoked and sometime two. For example when the user selects text ad the click
 * <code>DELETE</code> key then only <code>removeUpdate</code> is called. But if
 * it presses a letter then first the method <code>removeUpdate</code> is called.
 * If <code>firstName</code> property from the 
 * example above contains the value "Bill" then the method setFirstName gets
 * empty string as a result of <code>removeUpdate</code> call. And the method
 * fires <code>PropertyChangeEvent</code>. As a result the binder is
 * notified of the property changes again and tries to set the new value
 * to the component. But when all this invocation chain finished the value of the
 * <code>firstName</code> becomes equal to the empty string. But then the component
 * calls the method <code>insertUpdate</code> in order to insert a letter.
 * (The component has been waiting until the <code>removeMethod</code> 
 * finished).
 * And the process is repeated with a new property value that equals to 
 * a string representation of the letter typed. In addition when the
 * binder tries to execute <code>jTextField.setText("")</code>  inside 
 * <code>removeUpdate</code> then the component will throw an exception just 
 * before <code>insertUpdate</code> method call.<p>
 * <p>
 * 
 * 
 * 
 * @author V. Shyshkin
 */
public abstract class AbstractEditablePropertyBinder extends AbstractPropertyBinder {
    /**
     * Indicates whether the binder accepts events.
     * If the value is <code>true</code> the methods {@link #propertyChanged(java.lang.Object)}  
     * and {@link #boundObjectChanged(java.lang.Object) do nothing.
     * The default value is <code>false</code>. The field value is set
     * by the method <code>boundObjectChanged</code> just before changing the
     * property value.
     * 
     */
    protected boolean binderIsStillChangingProperty;  //propertyValueChangingInProgressByTheBinder
    
    public AbstractEditablePropertyBinder(Object boundObject) {
        super(boundObject);
    }
    /**
     * Converts the specified component value and assigns the converted value
     * to the bound property.
     * The method is called in response to the component value change.
     * The method does not perform any action if at least one 
     * of the following conditions occurs:
     * <ul>
     *   <li>The binder is in <i><code>suspend</code></i> state;</li>
     *   <li>The method {@link #getDocument() } returns <code>null</code></li>
     *   <li>The flag {@link #binderIsStillChangingProperty} is set to <code>true</code></li>
     * </ul>
     * If none of the above conditions is met the method tries to convert
     * the specified component value to the bound property value and set the 
     * converted value to the property. The conversion is done by the 
     * abstract method {@link #propertyChanged(java.lang.Object) }.
     * <p>
     * An error may occur during the execution of the method. This can happen 
     * in two cases:
     * <ol>
     *  <li> 
     *      The method <code>propertyValueOf</code> cannot convert the specified 
     *      value.
     *  </li>
     *  <li>
     *      There is an object of type {@link Validator } assigned
     *      to the selected document and the validation was unsuccessful.
     *  </li>
     * </ol>
     * The method doesn't throw an exception in case of error. Instead it notifies
     * all registered listeners of the {@link BinderEvent} with an action property
     * set to {@link BinderEvent.Action#boundObjectError }.
     * 
     * @param componentValue the component specific new value
     */
    protected void boundObjectChanged(boolean notifyOfErrors, Object componentValue) {
        if ( getBoundProperty() == null) {
            return;
        }
        if (isSuspended()) {
            return;
        }
        if (getDocument() == null) {
            return;
        }
        if (binderIsStillChangingProperty) {
            return;
        }
        if (getDocument().propertyStore() instanceof HasDocumentState) {
            DocumentState state = ((HasDocumentState) getDocument().propertyStore()).getDocumentState();
            state.getDirtyValues().put(boundProperty, componentValue);
        }

        fireClearPropertyError();

        Object convertedValue;
        Object oldDataValue = getDocument().propertyStore().get(boundProperty);
        try {
            convertedValue = this.propertyValueOf(componentValue);
            if (DataUtils.equals(convertedValue, oldDataValue)) {
                return;
            }
            if (getDocument() instanceof HasValidator) {
                Validator v = ((HasValidator) getDocument()).getValidator();
                if (v != null) {
                    v.validate(boundProperty, convertedValue, getDocument());
                }
            }
            binderIsStillChangingProperty = true;
            //getDocument().propertyStore().put(boundProperty, convertedValue);
            firePropertyChangeRequest(oldDataValue, convertedValue);
            
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
                ValidationException ve = new ValidationException(boundProperty, "Property name= '" + boundProperty + "'. Invalid value: " + componentValue, getDocument());
                firePropertyError(ve);
            }
        } finally {
            binderIsStillChangingProperty = false;
        }
    }

    /**
     * Just calls the method <code>boundObjectChanged(boolean,Object)</code>
     * with the first parameter set to <code>true</code>.
     * 
     * @param componentValue the new component value
     * @see #boundObjectChanged(boolean, java.lang.Object) 
     */
    protected void boundObjectChanged(Object componentValue) {
        this.boundObjectChanged(true, componentValue);
    }
    /**
     * Handles the specified event of type <code>DocumentChangeEvent</code>.
     * The method overrides the method in the superclass. It extends
     * the set of actions to be handled with {@link org.document.DocumentChangeEvent.Action#completeChanges}.
     * There is a list of all handled actions:
     * <ul>
     *   <li>{@link org.document.DocumentChangeEvent.Action#documentChange}. 
     *      This action notifies the binder of the current document change. Since the event
     *      contains the reference to a new document  the binder retains 
     *      this reference.
     *    </li>
     *    <li>
     *      {@link org.document.DocumentChangeEvent.Action#propertyChange}
     *    </li>
     *    <li>
     *      {@link org.document.DocumentChangeEvent.Action#suspendBinding}
     *    </li>
     *    <li>
     *      {@link org.document.DocumentChangeEvent.Action#resumeBinding}
     *    </li>
     *    <li>
     *      {@link org.document.DocumentChangeEvent.Action#completeChanges}
     *    </li>
* 
     * </ul>
     * 
     * @param event the event to be handled 
     */
    @Override
    public void react(BinderEvent event) {
        super.react(event);
        if (isSuspended()) {
            return;
        }
        switch(event.getAction()) {
            case completeChanges :
                boundObjectChanged(false, getBoundObjectValue());
                break;
        }
    }
    
/*    @Override
    public void react(DocumentChangeEvent event) {
        super.react(event);
        if (isSuspended()) {
            return;
        }
        switch(event.getAction()) {
            case completeChanges :
                boundObjectChanged(false, getBoundObjectValue());
                break;
        }
    }
*/    
    
    private void firePropertyChangeRequest(Object oldValue, Object newValue) {
        BinderEvent.Action action =
                BinderEvent.Action.propertyChangeRequest;
        BinderEvent event = new BinderEvent(this, action);
        event.setOldValue(oldValue);
        event.setNewValue(newValue);
        notifyListeners(event);
    }

    private void fireComponentValueChange(Object dataValue, Object componentValue) {
        BinderEvent.Action action =
                BinderEvent.Action.boundObjectChange;
        BinderEvent event = new BinderEvent(this, action, dataValue, componentValue);
        notifyListeners(event);
    }

    private void fireClearPropertyError() {
        BinderEvent.Action action = BinderEvent.Action.clearError;
        BinderEvent event = new BinderEvent(this, action, null);
        notifyListeners(event);
    }
    /**
     * Creates an instance of <code>BinderEvent</code> for the specified 
     * validation exception and notifies all registers listeners of type 
     * <code>BinderListener</code>.
     * The created event has an <code>action</code> property than is set to
     * {@link BinderEvent.Action#boundObjectError )
     * @param e the exception for which the event object to be created
     * @see BinderEvent
     * @see BinderListener
     */
    protected void firePropertyError(ValidationException e) {
        BinderEvent.Action action = BinderEvent.Action.boundObjectError;
        BinderEvent event = new BinderEvent(this, action, e);
        notifyListeners(event);
    }

    private void notifyListeners(BinderEvent event) {
        if (binderListener == null) {
            return;
        }
        binderListener.react(event);
//        for (BinderListener l : binderListener) {
//            l.react(event);
//        }
    }

    /**
     * The method is called when a a property value is
     * changed, and in response, it is necessary to change the value in the
     * associated component. First the method converts data to a component
     * value by calling an abstract method
     * {@link #componentValueOf(java.lang.Object) }. The method checks whether
     * or not to actually change the value of the component (possibly a
     * component already has the same meaning) and, if so, then the new value
     * assigned to the component by calling a protected method {@link #setBoundObjectValue(java.lang.Object).
     * Usually, the method is not overriden by subclasses. Instead, you
     * might to override the method
     * <code>setBoundObjectValue</code>.
     *
     * @param propertyValue the ne value of the bound property
     */
    @Override
    protected void propertyChanged(String property,Object propertyValue, boolean forceRefresh) {
        if ( property == null ) {
            return;
        }
        if ( ! property.equals(getBoundProperty()) ) {
            return;
        }
        if ( getBoundObject() == null ) {
            return;
        }
        
        if (binderIsStillChangingProperty) {
            return;
        }
        if (isSuspended()) {
            return;
        }

        Object convertedValue = this.componentValueOf(propertyValue);
        if ( ! forceRefresh && !needChangeComponent(convertedValue)) {
            return;
        }
        removeBoundObjectListeners();
        setBoundObjectValue(convertedValue);
        addBoundObjectListeners();
    }
}
