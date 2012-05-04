package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface Binding extends DocumentListener{
    void add(Binder binder);
    void remove(Binder binder);
    ValidatorCollection getValidators();
    void setDocument(Document object);
    void addDocumentListener(DocumentListener l);
    void removeDocumentListener(DocumentListener l);
    
    
    
//    void setDocument(HasDocument hasDocument,boolean completeChanges);
    
    
}
