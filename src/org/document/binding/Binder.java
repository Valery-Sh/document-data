package org.document.binding;

/**
 * The root interface for all binder classes.
 * <p>The binder notifies of events of type {@link BinderEvent} all
 * registers listeners that implement interface {@link BinderListener}.
 * 
 * @author V. Shyshkin
 */
public interface Binder { 
    
    Object getBoundObject();
    void setBoundObject(Object boundObject);
    /**
     * May be useful when it is not possible to convert the bound property value
     * to a component value. For example, when (@link #getDocument() } returns
     * <code>null</code>.
     */
    void initBoundObjectDefaults();

    /**
     * Should be implemented if the binder is a listener of the bound component
     * events. The implementation is a component specific.
     */
    void addBoundObjectListeners();

    /**
     * Should be implemented if the binder is a listener of the bound component
     * events. The implementation is a component specific.
     */
    void removeBoundObjectListeners();
    
        /**
     * Adds an object that implements <code>BinderListener</code>.
     * @param l 
     */
    void addBinderListener(BinderListener l);
    /**
     * Removes an object that implements <code>BinderListener</code>
     * @param l 
     */
    void removeBinderListener(BinderListener l);


}
