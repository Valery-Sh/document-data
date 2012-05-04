package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface Binding {
    void add(Binder binder);
    void remove(Binder binder);
//    void completeChanges();    
    ValidatorCollection getValidators();
    //void validate() throws ValidationException;    
    //Document getDocument();
    Object getBoundObject();
    void bindTo(Object object);
//    void bindTo(HasDocument hasDocument,boolean completeChanges);
    
    
}
