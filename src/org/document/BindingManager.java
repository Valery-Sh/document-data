package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface BindingManager extends DocumentListener{
    void add(Binder binder);
    void remove(Binder binder);
    
    /**
     * 
     * @param propertyName the name of a property whose type is 
     * <code>org.document.Document</code>.
     * @return a new instance of the class 
     * <code>org.document.BindingManager</code>
     */
    BindingManager createChild(String propertyName);
    //BinderRegistry getChild(String propName);
    String getChildName();
    Document getDocument();
    void setDocument(Document document,boolean completeChanges);
    void setDocument(HasDocument hasDocument,boolean completeChanges);
    void completeChanges();
    ValidatorCollection getValidators();
    void validate(String propPath,Object value) throws ValidationException;
    void validate() throws ValidationException;    
    void notifyError(Binder source,Exception e);
    void notifyError(String propertyName,Exception e);
}
