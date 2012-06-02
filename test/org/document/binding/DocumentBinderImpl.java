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

    @Override
    public void addBinderListener(BinderListener l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeBinderListener(BinderListener l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
