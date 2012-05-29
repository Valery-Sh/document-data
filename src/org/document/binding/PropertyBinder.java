package org.document.binding;

import org.document.Document;
import org.document.DocumentChangeListener;
//import org.document.HasDocumentAlias;

/**
 *
 * @author V. Shyshkin
 */
public interface PropertyBinder extends Binder {//,DocumentChangeListener {//, HasDocumentAlias {

    Document getDocument();

    /**
     * Returns a property name whose value is bound to a component.
     *
     * @return a property name as a <code>String</code>.
     */
    String getPropertyName();

    /**
     * It is assumed that this method should be called when you want to set the
     * value of the component and do not want that in response a component
     * generated an event.
     *
     * @param propertyValue a data value. Before assign it to a component it
     * should be converted to a component value.
     */
    void propertyChanged(Object propertyValue);

    void initComponentDefault();
}
