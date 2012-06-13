package org.document.binding;

import java.util.EventObject;
import org.document.ValidationException;

/**
 * 
 * @author V. Shyshkin
 */
public class BinderEvent extends EventObject {
    
    private Action action;
    private Object boundObjectValue;
    private Object propertyValue;  
    private String propertyName;
    
    private ValidationException exception;
    private Object oldValue;
    private Object newValue;
    
    protected BinderEvent(Binder source) {
        super(source);
    }
    
    public BinderEvent(Binder source,Action action) {
        this(source);
        this.action = action;
    }
    public BinderEvent(Binder source,Action action,String propertyName) {
        this(source);
        this.action = action;
        this.propertyName = propertyName;
    }
    
    public BinderEvent(Binder source, Action action, ValidationException e ) {
        this(source, action);
        this.exception = e;
    }
    public BinderEvent(Binder source,Action action,Object dataValue,Object componentValue ) {
        this(source,action);
        this.boundObjectValue = componentValue;
        this.propertyValue = dataValue;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

/*    public BindingContext getContext() {
        return context;
    }

    public void setContext(BindingContext context) {
        this.context = context;
    }
*/
    public Object getComponentValue() {
        return boundObjectValue;
    }

    public void setComponentValue(Object componentValue) {
        this.boundObjectValue = componentValue;
    }

    public Object getDataValue() {
        return propertyValue;
    }

    public void setDataValue(Object dataValue) {
        this.propertyValue = dataValue;
    }

    public String getBoundProperty() {
        if ( getSource() instanceof PropertyBinder ) {
            return ((PropertyBinder)getSource()).getBoundProperty();
        } else {
            return null;
        }
    }
    public String getPropertyName() {
        return propertyName;
    }

    public ValidationException getException() {
        return exception;
    }

    public void setException(ValidationException exception) {
        this.exception = exception;
    }

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
        boundObjectChange,
        boundObjectReplace,
        boundPropertyReplace,
        
        aliasReplace,
        
        propertyError,
        clearError,
        fixError,

        suspendBinding,
        resumeBinding,
        completeChanges,
        
        refresh,
        binderAdded,
        binderRemoved
    }
}//class DocumentEvent

