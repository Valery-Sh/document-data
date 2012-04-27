package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface Binder {
    String getDataEntityName();
    void dataChanged(Object oldValue, Object newValue);
    void componentChanged(Object oldValue, Object newValue);
    Object getDataValue();
    Object getComponentValue();
    void setBindingManager(BindingManager registry);
}
