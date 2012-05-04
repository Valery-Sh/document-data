package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface Binder extends DocumentListener{
    String getPropertyName();
    void dataChanged(Object newValue);
    void componentChanged(Object newValue);
    //Object getDataValue();
    //void setDataValue(Object value);
    Object getComponentValue();
    //void setDirtyComponentValue(Object value);
    //void setDocumentBinding(DocumentBinding binding);
    void init(Object dataValue);
    void addBinderListener(BinderListener l);
    void removeBinderListener(BinderListener l);
}
