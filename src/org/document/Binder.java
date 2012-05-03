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
    //void setDataValue(Object value);
    Object getComponentValue();
    void setDirtyComponentValue(Object value);
    void setDocumentBinding(DocumentBinding binding);
    void init(Object dataValue);
}
