package org.document.binding;

import org.document.Document;
import org.document.DocumentChangeEvent;
import org.document.DocumentChangeListener;

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
    @Override
    public void unbind() {

        if (documentListeners == null || documentListeners.isEmpty()) {
            return;
        }

        DocumentChangeEvent event = new DocumentChangeEvent(this, DocumentChangeEvent.Action.unbind);
        event.setPropertyName(null);

        for ( Object l : documentListeners ) {
            ((DocumentChangeListener)l).react(event);
        }

    }
    
}
