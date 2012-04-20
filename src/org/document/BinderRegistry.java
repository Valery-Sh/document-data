package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface BinderRegistry extends PropertyChangeHandler{
    void add(Binder binder);
    void remove(Binder binder);
    Document getDocument();
    void setDocument(Document document);
    void notifyError(Binder source,Exception e);
}
