package org.document.binding;

/**
 *
 * @author V. Shyshkin
 */
public interface Binder { //extends DocumentChangeListener {

    Object getComponentValue();

    void addBinderListener(BinderListener l);

    void removeBinderListener(BinderListener l);
}
