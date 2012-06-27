package org.document.binding;

import org.document.Document;

/**
 *
 * @author V. Shyskin
 */
public class DocumentBinder<E extends Document> extends AbstractDocumentBinder<Document> {

    public DocumentBinder() {
        super();
    }
    public DocumentBinder(Class supportedClass) {
        super(supportedClass);
    }
    
    @Override
    public E getDocument() {
        return (E)super.getDocument();
    }
    @Override
    public boolean add(Binder binder) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void add(String propertyName, EmbeddedDocumentBinder binder) {
        PropertyBinder b = new PropertyBinderEmbedded(propertyName, binder);
        binder.setEmbedded(true);
        add(b);
    }
    
}
