package org.document.binding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.document.DataUtils;
import org.document.Document;
import org.document.DocumentChangeEvent;
import org.document.DocumentChangeListener;

/**
 * The class provides a skeletal implementation of the
 * <code>PropertyBinder</code> interface. The class still does not know anything
 * about the concrete component. But it defines some abstract methods that must
 * be implemented in subclasses. The class is useful in cases where there is no
 * need to handle events that are fired by the component. An example is a
 * <code>javax.swing.Label</code>. But what about
 * <code>javax.swing.TextField</code> ? We want not only change it's text
 * property but also handle changes in the component itself. It is more
 * convenient to use the class {@link AbstractEditablePropertyBinder }. The last
 * provides functionality that allows to avoid some pitfalls related to the
 * component's events. <p> Below is an example of a class that extends the
 * specified one. The example uses a swing component {@link javax.swing.JLabel}.
 *
 *
 * <code>
 * <pre>
 * public class LabelStringBinder extends AbstractPropertyBinder {
 *   protected JLabel boundComponent;
 *
 *   public BdLabelBinder(String propertyName, JLabel textField) {
 *       this.boundComponent = textField;
 *       this.boundProperty = propertyName;
 *   }
 *   // ==============================================================
 *   //      AbstractPropertyBinder's Abstract methods Implementation
 *   // ==============================================================
 *
 * @Override protected void setComponentValue(Object componentValue) {
 * boundComponent.setText(componentValue); }
 * @Override protected Object componentValueOf(Object propertyValue) { return
 * propertyValue == null ? "" : propertyValue.toString(); }
 * @Override protected Object propertyValueOf(Object componentValue) { return
 * boundComponent.getText(); }
 * @Override public Object getComponentValue() { return
 * boundComponent.getText(); }
 * @Override public void initBoundObjectDefaults() {
 * this.boundComponent.setText(""); } }
 * </pre>
 * </code>
 *
 * The class <code>AbstractPropertyBinder</code> provides methods that make it
 * possible temporary prohibit the instances both fire evens and handle incoming
 * events. When {@link #suspend()} method is called the component doesn't accept
 * and fire any events. The method {@link #resume() } takes an instance in it's
 * normal state.
 *
 * @see PropertyBinder
 * @see AbstractEditablePropertyBinder
 *
 * @author V. Shyshkin
 */
public abstract class AbstractPropertyBinder extends AbstractBinder implements Serializable, PropertyBinder, DocumentChangeListener {
    private BindingContext context;
    private String alias;
   // protected Object boundObject;
    protected String boundProperty;
    protected Document document;
    protected List<BinderListener> binderListeners;
    protected BinderConverter converter;
    protected boolean suspended;
//    protected boolean bound;
    
    public AbstractPropertyBinder(Object boundObject) {
        super(boundObject);
    }
    @Override
    public String getAlias() {
        if (this.alias == null) {
            this.alias = "default";
        }
        return alias;
    }

    @Override
    public BindingContext getContext() {
        return context;
    }

    @Override
    public void setContext(BindingContext context) {
        this.context = context;
    }

    @Override
    public void setAlias(String alias) {
        if (alias == null) {
            this.alias = "default";
        } else {
            this.alias = alias;
        }
    }
    @Override
    public void unbind() {
        removeBoundObjectListeners();
        if ( binderListeners != null ) {
            binderListeners.clear();
        }
        document = null;
    }
    /**
     * Returns an instance of class
     * <code>BinderConverter</code> that should be used to convert the value of
     * the property to the value of the component, and vice versa.
     *
     * @return the instance of converter or <code>null</code> if is not
     * assigned.
     */
    public BinderConverter getConverter() {
        return converter;
    }

    /**
     * Sets the specified
     * <code>BinderConverter</code> object. The converted may be used to convert
     * the value of the property to the value of the component, and vice versa.
     */
    public void setConverter(BinderConverter converter) {
        this.converter = converter;
    }

    /**
     * Returns a currently selected
     * <code>Document</code> instance.
     *
     * @return an object of class {@link org.document.Document).
     */
    @Override
    public Document getDocument() {
        return document;
    }


    /*    protected void removeBoundObject() {

     removeBoundObjectListeners();
     boundObject = null;
     document = null;
     }
     */
    /**
     * Indicates whether the binder is in suspend state.
     *
     * @return <code>true</code> if a binder is in suspend state.
     * <code>false</code> otherwise.
     * @see #suspend()
     * @see #resume()
     */
    public boolean isSuspended() {
        return suspended;
    }

    /**
     * Takes the binder in the suspend state. When the binder in this state then
     * it doesn't fire events an doesn't accept incoming events.
     */
    public void suspend() {
        this.suspended = true;
    }

    /**
     * Takes the binder in its normal (not suspend) state.
     */
    public void resume() {
        this.suspended = false;
    }

    /**
     * Appends the specified listener to the list of binder listeners.
     *
     * @param l the binder listener to be added
     * @see BinderListener
     * @see BinderEvent
     */
    @Override
    public void addBinderListener(BinderListener l) {
        if (this.binderListeners == null) {
            this.binderListeners = new ArrayList<BinderListener>(1);
        }
        binderListeners.add(l);
        if (binderListeners.size() > 1) {
            throw new IndexOutOfBoundsException("Only one BinderListener can be registered");
        }

    }

    /**
     * Removes the specified listener from the list of binder listeners.
     *
     * @param l the binder listener to be removed
     * @see BinderListener
     * @see BinderEvent
     */
    @Override
    public void removeBinderListener(BinderListener l) {
        if (binderListeners == null || binderListeners.isEmpty()) {
            return;
        }
        this.binderListeners.remove(l);
    }

    /**
     * Handles the specified event of type
     * <code>DocumentChangeEvent</code>. This is the
     * <code>DocumentChangeListener</code> implementation method. It accepts
     * some actions that the event can provide: <ul>
     * <li>{@link org.document.DocumentChangeEvent.Action#documentChange}. This
     * action notifies the binder of the current document change. Since the
     * event contains the reference to a new document the binder retains this
     * reference. </li> <li>
     * {@link org.document.DocumentChangeEvent.Action#propertyChange}
     *
     * </li> <li> {@link org.document.DocumentChangeEvent.Action#suspendBinding}
     * </li> <li> {@link org.document.DocumentChangeEvent.Action#resumeBinding}
     * </li>
     *
     * </ul>
     *
     * @param event the event to be handled
     */
    @Override
    public void react(DocumentChangeEvent event) {
        switch (event.getAction()) {
            case documentChange:
                if (isSuspended()) {
                    return;
                }
                this.document = (Document) event.getNewValue();
                if (document != null && getBoundProperty() != null) {

                    propertyChanged(getBoundProperty(), document.propertyStore().get(getBoundProperty()));
                } else if (document == null) {
                    initBoundObjectDefaults();
                }
                return;
            case resumeBinding:
                if (!isSuspended()) {
                    return;
                }
                if (event.getPropertyName() != null && !event.getPropertyName().equals(boundProperty)) {
                    return;
                }
                this.suspended = false;
                this.document = (Document) event.getNewValue();
                if (document != null && getBoundProperty() != null) {
                    propertyChanged(event.getPropertyName(), document.propertyStore().get(getBoundProperty()));
                } else if (document == null) {
                    initBoundObjectDefaults();
                }
                return;
            case suspendBinding:
                if (isSuspended()) {
                    return;
                }
                if (event.getPropertyName() != null && !event.getPropertyName().equals(boundProperty)) {
                    return;
                }
                this.suspended = true;
                return;
            case propertyChange:
                if (!isSuspended()) {
                    propertyChanged(event.getPropertyName(), event.getNewValue());
                }
                return;
        }//switch
    }

    /*    protected void update(BinderEvent.Action action) {
     switch (action) {
     case boundPropertyReplace:
     if (document != null && getBoundProperty() != null) {

     propertyChanged(getBoundProperty(), document.propertyStore().get(getBoundProperty()));
     } else if (document == null) {
     initBoundObjectDefaults();
     }
     break;
     case boundObjectReplace:

     if (document != null && getBoundProperty() != null) {

     propertyChanged(getBoundProperty(), document.propertyStore().get(getBoundProperty()));
     } else if (document == null) {
     initBoundObjectDefaults();
     }
     break;
     }
     }
     */
    @Override
    public Object getBoundObject() {
        return boundObject;
    }

    /**
     *
     * @param boundObject the bound object to be set
     */
    @Override
    public void setBoundObject(Object boundObject) {
        if (this.boundObject == boundObject) {
            return;
        }
        super.setBoundObject(boundObject);
/*        removeBoundObjectListeners();

        initBoundObjectDefaults();
        if (boundObject == null) {
            this.boundObject = null;
            return;
        }
        this.boundObject = boundObject;

        addBoundObjectListeners();
*/
        if (document != null && getBoundProperty() != null) {
            propertyChanged(getBoundProperty(), document.propertyStore().get(getBoundProperty()));
        } else if (document == null) {
            initBoundObjectDefaults();
        }
    }

    /**
     * @return the bound property name
     */
    @Override
    public String getBoundProperty() {
        return this.boundProperty;
    }

    /**
     * Sets the specified bound property name.
     *
     * @param propertyName the name of the bound property
     */
    @Override
    public void setBoundProperty(String propertyName) {

        if (this.boundProperty != null && this.boundProperty.equals(propertyName)) {
            return;
        }
        removeBoundObjectListeners();
        //document = null;
        initBoundObjectDefaults();
        if (boundProperty == null) {
            this.boundProperty = null;
            return;
        }
        this.boundProperty = propertyName;
        addBoundObjectListeners();
        //
        // refresh with the new property
        //
        if (document != null && getBoundProperty() != null) {
            propertyChanged(getBoundProperty(), document.propertyStore().get(getBoundProperty()));
        } else if (document == null) {
            initBoundObjectDefaults();
        }

        /*        BinderEvent e = new BinderEvent(this, BinderEvent.Action.boundPropertyReplace);
         e.setNewValue(propertyName);
         e.setOldValue(this.boundProperty);

         this.boundProperty = propertyName;

         //
         // Now we notify a DocumentBinder in order to rebind with the 
         // new boundObject
         //
         if (binderListeners == null) {
         return;
         }
         //
         // create a copy of binderListeners since some listeners may remove themself 
         //
         List<BinderListener> list = new ArrayList<BinderListener>();
         list.addAll(binderListeners);
         for (BinderListener l : list) {
         l.react(e);
         }
         update(BinderEvent.Action.boundPropertyReplace);
         */
    }

    /**
     * Prepends cyclic component modifications.
     *
     * @param value a new value to be checked
     * @return <code>true</code> if the component shouldn't be changed.      <code>false otherwise
     */
    protected boolean needChangeComponent(Object value) {
        boolean result = true;

        Object currentValue = getComponentValue();

        if (value == null && currentValue == null) {
            result = false;
        } else if (value == null || currentValue == null) {
            result = true;
        } else if (DataUtils.isValueType(value)) {
            if (value.equals(currentValue)) {
                result = false;
            } else {
                result = true;
            }
        } else if (value == currentValue) {
            result = false;
        }
        return result;
    }

    /**
     * The method is called when a a property value is changed, and in response,
     * it is necessary to change the value in the associated component. First
     * the method converts data to a component value by calling an abstract
     * method {@link #componentValueOf(java.lang.Object) }. The method checks
     * whether or not to actually change the value of the component (possibly a
     * component already has the same meaning) and, if so, then the new value
     * assigned to the component by calling a protected method {@link #setComponentValue(java.lang.Object).
     * Usually, the method is not overriden by subclasses. Instead, you
     * might to override the method
     * <code>setComponentValue</code>.
     *
     * @param propertyValue the ne value of the bound property
     */
    protected void propertyChanged(String property, Object propertyValue) {
        if (property == null) {
            return;
        }
        if ((!property.equals(getBoundProperty())) && !"*".equals(property)) {
            return;
        }

//        if (getBoundObject() == null) {
//            return;
//        }
        Object convertedValue = this.componentValueOf(propertyValue);
        if (!needChangeComponent(convertedValue)) {
            return;
        }
        setComponentValue(convertedValue);
    }

    /**
     * Returns a value of the object that the binder considers to be a
     * component. The method is declared as
     * <code>abstract</code> and should be overriden in subclasses.
     *
     * @return value of a component
     */
    protected abstract Object getComponentValue();

    /**
     * Sets the specified value to the object that the binder considers to be a
     * component. The method is declared as
     * <code>abstract</code> and should be overriden in subclasses.
     *
     * @return value of a component
     */
    protected abstract void setComponentValue(Object componentValue);

    /**
     * Converts the specified bound property value to the component value. The
     * method is declared as
     * <code>abstract</code> and should be overriden in subclasses.
     *
     * @param propertyValue the property value to be converted
     * @return
     */
    protected abstract Object componentValueOf(Object propertyValue);

    /**
     * Converts the specified component value to the bound property value. The
     * method is declared as
     * <code>abstract</code> and should be overriden in subclasses.
     *
     * @param componentValue the component value to be converted
     * @return
     */
    protected abstract Object propertyValueOf(Object componentValue);

}
