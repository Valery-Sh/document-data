package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface Binding {
    void add(Binder binder);
    void remove(Binder binder);
    void completeChanges();    
    ValidatorCollection getValidators();
    void validate() throws ValidationException;    
    Document getDocument();
    void setDocument(Document document);
//    void setDocument(HasDocument hasDocument,boolean completeChanges);
    
    
}
