package org.document.binding;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractBinder implements Binder {

    protected Object boundObject;
    private BindingContext context;

    public AbstractBinder(Object boundObject) {
        this.boundObject = boundObject;
    }

    @Override
    public void setBoundObject(Object boundObject) {
        if (this.boundObject == boundObject) {
            return;
        }
        removeBoundObjectListeners();

        initBoundObjectDefaults();
        if (boundObject == null) {
            this.boundObject = null;
            return;
        }
        this.boundObject = boundObject;

        addBoundObjectListeners();

    }


    @Override
    public Object getBoundObject() {
        return boundObject;
    }

    @Override
    public void initDefaults() {
    }
}
