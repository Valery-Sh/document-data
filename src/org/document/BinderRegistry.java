package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface BinderRegistry extends PropertyChangeHandler{
    void add(Binder binder);
    void remove(Binder binder);
    /**
     * 
     * @param propertyName the name of a property whose type is 
     * <code>org.document.Document</code>.
     * @return a new instance of the class 
     * <code>org.document.BinderRegistry</code>
     */
    BinderRegistry createChild(String propertyName);
    //BinderRegistry getChild(String propName);
    String getChildName();
    Document getDocument();
    void setDocument(Document document,boolean completeChanges);
    void completeChanges();
    ValidatorCollection getValidators();
    void validate(String propPath,Object value) throws ValidationException;
    void validate() throws ValidationException;    
    void notifyError(Binder source,Exception e);
}
