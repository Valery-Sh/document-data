package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface Binding extends DocumentChangeListener{
    void add(Binder binder);
    void remove(Binder binder);
//    ValidatorCollection getValidators();
    
    void setDocument(Document object);
    void addDocumentListener(DocumentChangeListener l);
    void removeDocumentListener(DocumentChangeListener l);
}
