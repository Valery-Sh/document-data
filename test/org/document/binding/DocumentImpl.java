/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import org.document.Document;
import org.document.ObservableListTest;
import org.document.PropertyStore;

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
