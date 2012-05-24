/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import org.document.*;

/**
 *
 * @author Valery
 */
public class DocumentBinderImpl extends AbstractDocumentBinder {
    
    protected PropertyStore document;
    
    public DocumentBinderImpl() {
        document = new MockDocument();
    }

    @Override
    protected DocumentBinder create() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
