package org.document.binding;

import java.util.EventObject;
import org.document.ValidationException;

/**
 * 
 * @author V. Shyshkin
 */
public class BinderEvent extends EventObject {
    
    private Action action;
    private Object componentValue;
    private Object dataValue;    
    private ValidationException exception;
    private Binder binder;
    private Object oldValue;
    private Object newValue;
    private BindingContext context;
    
    public BinderEvent(Binder source) {
        super(source);
    }
    
    public BinderEvent(Binder source,Action action) {
        this(source);
        this.action = action;
    }
    public BinderEvent(Binder source, Action action, ValidationException e ) {
        this(source, action);
        this.exception = e;
    }
    public BinderEvent(Binder source,Action action,Object dataValue,Object componentValue ) {
        this(source,action);
        this.componentValue = componentValue;
        this.dataValue = dataValue;
    }
    public BinderEvent(BindingStateBinder source,Action action,Object dataValue,Object componentValue ) {
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

    public BindingContext getContext() {
        return context;
    }

    public void setContext(BindingContext context) {
        this.context = context;
    }

    public Object getComponentValue() {
        return componentValue;
    }

    public void setComponentValue(Object componentValue) {
        this.componentValue = componentValue;
    }

    public Object getDataValue() {
        return dataValue;
    }

    public void setDataValue(Object dataValue) {
        this.dataValue = dataValue;
    }

    public String getPropertyName() {
        return ((PropertyBinder)getSource()).getBoundProperty();
    }

    public ValidationException getException() {
        return exception;
    }

    public void setException(ValidationException exception) {
        this.exception = exception;
    }

/*    public Binder getBinder() {
        return binder;
    }

    public void setBinder(Binder binder) {
        this.binder = binder;
    }
*/
    public Object getOldValue() {
        return oldValue;
    }

    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    
    public enum Action {
        propertyChangeRequest,
        boundObjectError,
        boundObjectChange,
        aliasReplace,
        clearError,
        fixError,
        boundObjectSelectChange,
        binderRemoved,
        // concern documents
        
        suspendBinding,
        resumeBinding,
        propertyChange,
        propertyChangeNotSeected,
        completeChanges,
        
        boundObjectReplace,
        boundPropertyReplace,
        refresh,
        contextRequest
        
        
    }
}//class DocumentEvent

