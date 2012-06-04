package org.document.binding;

import java.util.List;
import org.document.Document;
import org.document.DocumentChangeEvent;
import org.document.DocumentChangeListener;
import org.document.DocumentList;
import org.document.DocumentPropertyStore;
import org.document.PropertyStore;

/**
 *
 * @author V. Shyshkin
 */
public class ListState<T extends Document> implements Document {

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
    private DocumentList<T> documentList;
    private DocumentChangeEvent documentChangeEvent;
    
    public DocumentList<T> getDocumentList() {
        return documentList;
    }
    /**
     * Sets the  specified list 
     * @param list 
     */
    public void setDocumentList(DocumentList<T> list) {
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
        /**
         * Handles events of type <code>DocumentChangeEvent</code>.
         * When any property value of a document changes the method is called
         * as an implementation of the {@link org.document.DocumentChangeListener }. listener
         * The special case is when an editing state changes. The event has it's
         * property {@link org.document.DocumentChangeEvent#propertyName} equals
         * to <code>"document.state.editing"</code>. In this case the method
         * cancels <i>newMark<i> of a selected document if the last is 
         * marked as <i>new</i>.. 
         * @param event the event to be handled
         * @see org.document.DocumentChangeEvent
         */
        @Override
        public void react(DocumentChangeEvent event) {
/*            if ( "document.state.editing".equals(event.getPropertyName()) &&
                 (Boolean)event.getNewValue() ) {
                if ( getDocumentList() instanceof DocumentList) {
                    DocumentList dl = (DocumentList)getDocumentList();
                    if ( dl.isNew(selected) && event.getSource() == selected ) {
                        dl.cancelNew();
                    }
                }
                
            } else { 
*/ 
            setDocumentChangeEvent(event);
//            }
        }
    } 
}
