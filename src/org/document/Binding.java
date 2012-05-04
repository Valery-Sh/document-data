package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface Binding extends DocumentListener{
    void add(Binder binder);
    void remove(Binder binder);
    ValidatorCollection getValidators();
    void setObjectDocument(Document object);
    void addDocumentListener(DocumentListener l);
    void removeDocumentListener(DocumentListener l);
    
    
    
//    void setObjectDocument(HasDocument hasDocument,boolean completeChanges);
    
    
}
