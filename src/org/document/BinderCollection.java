package org.document;

/**
 *
 * @author V. Shyskin
 */
public interface BinderCollection<T extends Binder> extends DocumentChangeListener {
    void add(T binder);
    void remove(T binder);
    
    BinderCollection getSubset(Object... subsetId);
    Document getDocument();
    void setDocument(Document object);
    void addDocumentChangeListener(DocumentChangeListener l);
    void removeDocumentChangeListener(DocumentChangeListener l);

}
