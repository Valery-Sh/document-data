package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface DocumentBinder<T extends PropertyBinder> extends Binder,BinderListener, BinderCollection<T>, HasDocumentAlias{
    
    ValidatorCollection getValidators();    
    /**
     * 
     * @param propertyName the name of a property whose type is 
     * <code>org.document.Document</code>.
     * @return a new instance of the class 
     * <code>org.document.DocumentBinding</code>
     */
    DocumentBinder createChild(String propertyName);
    String getChildName();
    void setChildName(String childName);
        

    //Document getDocument();    
    void validate(String propPath,Object value) throws ValidationException;
    //void completeChanges();    
    
}
