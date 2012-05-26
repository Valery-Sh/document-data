package org.document.binding;

import java.util.List;
import org.document.Document;
import org.document.DocumentChangeEvent;
import org.document.DocumentChangeListener;
import org.document.DocumentPropertyStore;
import org.document.PropertyStore;

/**
 *
 * @author V. Shyshkin
 */
public class ListState implements Document {

    protected DocumentPropertyStore listStatePropertyStore;
    private DocumentChangeHandler documentChangeHandler;

    public ListState() {
        //
        // DocumentPropertyStore is a default PropertyStore
        //
        this.listStatePropertyStore = new DocumentPropertyStore(this);
        this.documentChangeHandler = new DocumentChangeHandler();
    }

    
    //
    // Document interface implementation
    //
    @Override
    public PropertyStore propertyStore() {
        return this.listStatePropertyStore;
    }
    
   public DocumentChangeHandler documentChangeHandler() {
        return this.documentChangeHandler;
    }
    //
    // ===================================================
    //
    private Document selected;
    private List<Document> documentList;
    private DocumentChangeEvent documentChangeEvent;
    
    public List<Document> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<Document> list) {
        this.documentList = list;
        listStatePropertyStore.bind("documentList", list);
    }

    public Document getSelected() {
        return selected;
    }

    public void setSelected(Document selected) {
        this.selected = selected;
        listStatePropertyStore.bind("selected", selected);
    }

    public DocumentChangeEvent getDocumentChangeEvent() {
        return documentChangeEvent;
    }

    public void setDocumentChangeEvent(DocumentChangeEvent documentChangeEvent) {
        this.documentChangeEvent = documentChangeEvent;
        listStatePropertyStore.bind("documentChangeEvent", documentChangeEvent);
    }

    public class DocumentChangeHandler implements DocumentChangeListener {
        
        DocumentChangeEvent.Action action; // for test only purpose
        
        public DocumentChangeHandler() {
            
        }
        @Override
        public void react(DocumentChangeEvent event) {
            setDocumentChangeEvent(event);
        }
    } 
}
