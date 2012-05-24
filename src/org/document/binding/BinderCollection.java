package org.document.binding;

import org.document.Document;
import org.document.DocumentChangeListener;

/**
 *
 * @author V. Shyskin
 */
public interface BinderCollection<T extends Binder> extends DocumentChangeListener {//, ListChangeListener {
    void add(T binder);
    void remove(T binder);
    
    //BinderCollection getSubset(Object subsetId);
    Document getDocument();
    void setDocument(Document object);
   // void addDocumentChangeListener(DocumentChangeListener l);
   // void removeDocumentChangeListener(DocumentChangeListener l);

}
