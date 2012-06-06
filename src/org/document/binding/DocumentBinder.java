package org.document.binding;

import org.document.Document;

/**
 *
 * @author V. Shyskin
 */
public class DocumentBinder<E extends Document> extends AbstractDocumentBinder {
    
    protected DocumentBinder() {
        super();
    }
    
    @Override
    public E getDocument() {
        return (E)super.getDocument();
    }
    
    @Override
    public void setDocument(Document document) {
        super.setDocument(document);
    }
    
    
}
