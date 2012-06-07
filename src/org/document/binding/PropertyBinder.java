package org.document.binding;

import org.document.Document;

/**
 * Represents the objects that are used to bind a named property to 
 * another object, which is considered  a component.
 * The instance of the interface is aware of a currently selected
 * document. The instance also knows the property to which it is bound.
 * 
 * @author V. Shyshkin
 */
public interface PropertyBinder<E extends Document> extends Binder {//,DocumentChangeListener {//, HasDocumentAlias {
    /**
     * Returns a document whose property is used for binding.
     * @return returns an object of type {@link org.document.Document}
     */
    E getDocument();
    
    Object getBoundObject();
    void setBoundObject(Object boundObject);
    
    String getAlias();
    void setAlias(String alias);
    
    //void setDocument(E document);
    
    /**
     * Returns a bound  property name.
     *
     * @return a property name as a <code>String</code>.
     */
    String getBoundProperty();
    /**
     * Sets a specified property name as a bound property.
     * @param propertyName a property name to be set
     */
    void setBoundProperty(String propertyName);

}
