package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface Binder  extends DocumentListener{
    void dataChanged(Object newValue);
    void componentChanged(Object newValue);
    Object getComponentValue();
    void addBinderListener(BinderListener l);
    void removeBinderListener(BinderListener l);
}
