package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface DocumentBindings extends Bindings, BinderListener{
    Object getId();
    ValidatorCollection getValidators();    
    /**
     * 
     * @param propertyName the name of a property whose type is 
     * <code>org.document.Document</code>.
     * @return a new instance of the class 
     * <code>org.document.DocumentBinding</code>
     */
    DocumentBindings createChild(String propertyName);
    String getChildName();
        

    //Document getDocument();    
    void validate(String propPath,Object value) throws ValidationException;
    void completeChanges();    
}
