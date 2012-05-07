package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface Bindings extends DocumentChangeListener{
    void add(Binder binder);
    void remove(Binder binder);
    
    void setDocument(Document object);
    void addDocumentChangeListener(DocumentChangeListener l);
    void removeDocumentChangeListener(DocumentChangeListener l);
    
}
