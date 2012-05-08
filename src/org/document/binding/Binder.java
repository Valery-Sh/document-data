package org.document.binding;

import org.document.DocumentChangeListener;

/**
 *
 * @author V. Shyshkin
 */
public interface Binder  extends DocumentChangeListener{
    //void dataChanged(Object newValue);
    //void componentChanged(Object newValue);
    Object getComponentValue();
    void addBinderListener(BinderListener l);
    void removeBinderListener(BinderListener l);
}
