package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface DocumentBinding extends Binding,DocumentListener{
    Object getId();
    /**
     * 
     * @param propertyName the name of a property whose type is 
     * <code>org.document.Document</code>.
     * @return a new instance of the class 
     * <code>org.document.DocumentBinding</code>
     */
    DocumentBinding createChild(String propertyName);
    //BinderRegistry getChild(String propName);
    String getChildName();
//    void setDocument(Document document,boolean completeChanges);
//    void setDocument(HasDocument hasDocument,boolean completeChanges);
    void setDocument(HasDocument hasDocument);    
    void validate(String propPath,Object value) throws ValidationException;
    void notifyError(Binder source,Exception e);
    void notifyError(String propertyName,Exception e);
}
