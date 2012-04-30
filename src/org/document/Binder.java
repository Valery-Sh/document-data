package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface Binder {
    String getPropertyName();
    void dataChanged(Object oldValue, Object newValue);
    void componentChanged(Object oldValue, Object newValue);
    Object getDataValue();
    Object getComponentValue();
    void setDocumentBinding(DocumentBinding registry);
}
