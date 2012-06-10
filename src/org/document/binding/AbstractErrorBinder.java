package org.document.binding;

import org.document.Document;
import org.document.ValidationException;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractErrorBinder implements ErrorBinder{
    
    private Object boundObject;
    
    public AbstractErrorBinder(Object boundObject) {
        this.boundObject = boundObject;
    }
    @Override
    public Object getBoundObject() {
        return this.boundObject;
    }

    @Override
    public void setBoundObject(Object boundObject) {
        this.boundObject = boundObject;
    }

}
