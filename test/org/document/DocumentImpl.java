package org.document;

/**
 *
 * @author Valery
 */
public class DocumentImpl implements Document{
    protected PropertyStore propertyStore;
    public DocumentImpl() {
        this.propertyStore = new PropertyStoreImpl();
    }
    
    @Override
    public PropertyStore propertyStore() {
        return this.propertyStore;
    }
    
}
