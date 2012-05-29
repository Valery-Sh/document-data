
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
    private ValidationException exception;
    private Object oldAlias;
    private Object newAlias;    
    
/*    public DocumentChangeEvent(PropertyDataStore source) {
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
*/
    public DocumentChangeEvent(Object source) {
        super(source);
    }
    public DocumentChangeEvent(Object source,Action action) {
        this(source);
        this.action = action;
    }
    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Object getNewAlias() {
        return newAlias;
    }

    public void setNewAlias(Object newAlias) {
        this.newAlias = newAlias;
    }

    public Object getOldAlias() {
        return oldAlias;
    }

    public void setOldAlias(Object oldAlias) {
        this.oldAlias = oldAlias;
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

    public ValidationException getException() {
        return exception;
    }

    public void setException(ValidationException exception) {
        this.exception = exception;
    }

    
    public enum Action {
        documentChange,
        documentChanging,
        propertyChange,
        completeChanges,
        editingChange,
        propertyError, // notifies ErrorBinders
        documentError, // notifies ErrorBinders
        
    }
}