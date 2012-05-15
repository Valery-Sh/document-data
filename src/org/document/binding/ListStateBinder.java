/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import org.document.ListChangeEvent;

/**
 *
 * @author V. Shyshkin
 */
public class ListStateBinder<T extends PropertyBinder>  extends DocumentBinder {
    
    public ListStateBinder(Object alias) {
        super(alias);
    }

    @Override
    protected DocumentBinder create() {
        return new DocumentBinder();
    }

    @Override
    public void listChanged(ListChangeEvent event) {
    }
    
}
