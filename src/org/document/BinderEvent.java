package org.document;

import java.util.EventObject;

/**
 *
 * @author V. Shyshkin
 */
public class BinderEvent extends EventObject {
    
    private Action action;
    private Object componentValue;
    private Object dataValue;    
    private Exception exception;
    private Binder binder;
    
    public BinderEvent(Binder source) {
        super(source);
    }
    public BinderEvent(Binder source,Action action) {
        this(source);
        this.action = action;
    }
    public BinderEvent(Binder source, Action action, Exception e ) {
        this(source, action);
        this.exception = e;
    }
    public BinderEvent(Binder source,Action action,Object dataValue,Object componentValue ) {
        this(source,action);
        this.componentValue = componentValue;
        this.dataValue = dataValue;
    }
    public BinderEvent(ListBinder source,Action action,Object dataValue,Object componentValue ) {
        this(source);
        this.action = action;
        this.componentValue = componentValue;
        this.dataValue = dataValue;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Object getComponentValue() {
        return componentValue;
    }

    public void setComponentValueValue(Object componentValue) {
        this.componentValue = componentValue;
    }

    public Object getDataValue() {
        return dataValue;
    }

    public void setDataValue(Object dataValue) {
        this.dataValue = dataValue;
    }

    public String getPropertyName() {
        return ((PropertyBinder)getSource()).getPropertyName();
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Binder getBinder() {
        return binder;
    }

    public void setBinder(Binder binder) {
        this.binder = binder;
    }

    
    public enum Action {
        componentChangeValueError,
        componentValueChange,
        clearComponentChangeError,
        componentSelectChange
        
    }
}//class DocumentEvent

