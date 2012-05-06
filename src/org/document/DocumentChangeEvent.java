
package org.document;

import java.util.EventObject;

/**
 *
 * @author V. Shyshkin
 */
public class DocumentChangeEvent extends EventObject {
    
    private Action action;
    private String propertyName;
    private Object oldValue;
    private Object newValue;    
    private Exception exception;
    private PropertyBinder binder;
    
    public DocumentChangeEvent(PropertyDataStore source) {
        super(source);
    }
    public DocumentChangeEvent(PropertyDataStore source,Action action) {
        this(source);
        this.action = action;
    }
    public DocumentChangeEvent(Document source,Action action) {
        super(source);
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public PropertyBinder getBinder() {
        return binder;
    }

    public void setBinder(PropertyBinder binder) {
        this.binder = binder;
    }

    
    public enum Action {
        documentChange,
        selectChange,
        propertyChangeNotify,
        validateErrorNotify,
        validateProperty,
        validateAllProperties,
        validateDocument,
        propertyErrorNotify, // notifies ErrorBinders
        documentErrorNotify, // notifies ErrorBinders
        
    }
}