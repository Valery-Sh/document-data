package org.document.binding;

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

    @Override
    public void initBoundObjectDefaults() {
    }

    @Override
    public void addBoundObjectListeners() {
    }

    @Override
    public void removeBoundObjectListeners() {
    }


    @Override
    public void addBinderListener(BinderListener l) {
    }

    @Override
    public void removeBinderListener(BinderListener l) {
    }

}
