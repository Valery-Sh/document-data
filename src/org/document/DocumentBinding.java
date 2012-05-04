package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface DocumentBinding extends Binding,DocumentListener, BinderListener{
    Object getId();
    
    /**
     * 
     * @param propertyName the name of a property whose type is 
     * <code>org.document.Document</code>.
     * @return a new instance of the class 
     * <code>org.document.DocumentBinding</code>
     */
    DocumentBinding createChild(String propertyName);
    String getChildName();
    
    Document getBoundDocument();    
    void validate(String propPath,Object value) throws ValidationException;
    void completeChanges();    
}
