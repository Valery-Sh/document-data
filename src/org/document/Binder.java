package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface Binder  extends DocumentChangeListener{
    public static final String ALIAS = "alias_of_a_document";
    void dataChanged(Object newValue);
    void componentChanged(Object newValue);
    Object getComponentValue();
    void addBinderListener(BinderListener l);
    void removeBinderListener(BinderListener l);
}
