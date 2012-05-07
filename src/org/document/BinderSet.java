package org.document;

/**
 *
 * @author V. Shyskin
 */
public interface BinderSet<T extends Binder> extends DocumentChangeListener {
    void add(T binder);
    void remove(T binder);
    
    BinderSet getSubset(Object... subsetId);
    
    void setDocument(Document object);
    void addDocumentChangeListener(DocumentChangeListener l);
    void removeDocumentChangeListener(DocumentChangeListener l);

}
