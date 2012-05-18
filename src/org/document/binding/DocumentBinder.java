/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

/**
 *
 * @author Valery
 */
public class DocumentBinder<T extends PropertyBinder>  extends AbstractDocumentBinder {

    public DocumentBinder(Object alias) {
        super(alias);
    }
    protected DocumentBinder() {
        super();
    }

    @Override
    protected DocumentBinder create() {
        return new DocumentBinder();
    }


}
