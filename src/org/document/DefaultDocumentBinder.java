/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public class DefaultDocumentBinder<T extends PropertyBinder>  extends AbstractDocumentBinder {

    public DefaultDocumentBinder(Object alias) {
        super(alias);
    }
    protected DefaultDocumentBinder() {
        super();
    }

    @Override
    protected DocumentBinder create() {
        return new DefaultDocumentBinder();
    }

}
