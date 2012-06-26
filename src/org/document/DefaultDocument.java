/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import org.document.schema.PropertyChangeAccessors;

/**
 *
 * @author Valery
 */
public class DefaultDocument implements Document {

    protected transient DocumentPropertyStore propertyStore;

    public static Document create(Object o) {
        if (o == null || (o instanceof Document)) {
            return (Document) o;
        }
        //Document d = null;
        return new DefaultDocument(o);
    }

    public DefaultDocument(Object o) {
        //
        // DocumentPropertyStore is a default PropertyStore
        //

        initDocument(o);
    }

    protected final void initDocument(Object o) {
        try {
            this.propertyStore = new DocumentPropertyStore(o);
            PropertyChangeAccessors a = Registry.getSchema(o.getClass()).getPropertyChangeAccessors();
            if (a.hasAddBoundPropertyChangeListener()) {
                a.addBoundPropertyChangeListener(o, propertyStore);
            } else if (a.hasAddPropertyChangeListener()) {
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannnot create a document for the object " + o);
        }

    }
    //
    // Document interface implementation
    //

    @Override
    public PropertyStore propertyStore() {
        return this.propertyStore;
    }
}
