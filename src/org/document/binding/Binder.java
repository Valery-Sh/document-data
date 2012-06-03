package org.document.binding;

/**
 * The root interface for all binder classes.
 * <p>The binder notifies of events of type {@link BinderEvent} all
 * registers listeners that implement interface {@link BinderListener}.
 * 
 * @author V. Shyshkin
 */
public interface Binder { //extends DocumentChangeListener {

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
