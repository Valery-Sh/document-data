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

/*    protected DocumentBinder(Object alias) {
        super(alias);
    }
*/ 
    protected DocumentBinder() {
        super();
    }

    @Override
    protected DocumentBinder create() {
        return new DocumentBinder();
    }
    @Override
    public void addBinderListener(BinderListener l) {
    }

    @Override
    public void removeBinderListener(BinderListener l) {
    }

}
