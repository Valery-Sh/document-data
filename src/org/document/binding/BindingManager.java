package org.document.binding;

import java.util.Collection;
import java.util.List;
import org.document.*;

/**
 *
 * @author V. Shishkin
 */
public class BindingManager<T extends Document> extends AbstractBindingManager<T> implements ListChangeListener {

    protected DocumentList<Document> documents;
    //protected ListChangeHandler listChangeHandler;
    
    public BindingManager(List<T> sourceList) {
        super(sourceList);
        init(sourceList);
    }
    
    private void init(List<T> sourceList) {
        documents = new DocumentList(sourceList);
        documents.addListChangeListener(this);
    }
    public DocumentList getDocuments() {
        return documents;
    }

    @Override
    protected void afterSetSelected(T oldSelected) {
        if (documents.isNew(oldSelected) && !isEditing(oldSelected)) {
            documents.cancelNew();
        }
    }

    protected boolean isEditing(T doc) {
        boolean b = false;
        if (doc instanceof HasDocumentState) {
            b = ((HasDocumentState) doc).getDocumentState().isEditing();
        }
        return b;
    }

    @Override
    public void listChanged(ListChangeEvent event) {
        Document newSel = selected;
        DocumentList<Document> list = (DocumentList)event.getSource();
        if ( ! list.contains(selected) ) {
            newSel = list.isEmpty() ? null : list.get(0);
        }
        
        event.setSelectedObject(newSel);
        this.getBinders().listChanged(event);
//        event.setNewList(this.documents);
            
    }
    
/*        @Override
        public void listChanged(ListChangeEvent event) {
                DocumentList list = manager.getDocuments();
                
                switch (event.getAction()) {
                
                case add:
                case append:
                    break;
                case appendNew:
                    Document d = (Document)event.getElement();
                    if ( (d instanceof HasDocumentState) ) {
                        ((HasDocumentState)d).getDocumentState().setEditing(true);
                    }
                    //manager.getBinders().
                    break;
                case addAll:
                case appendAll:
                    Collection c = event.getCollection();
                    if (list.containsAny(c)) {
                    }
                case retainAll:
                    break;
                case set :    
                    Object e = event.getElement();
                    if (list.containsNew() && list.isNew((Document)e)) {
//                        b = false;
                    }
                    break;
                    
            }

        }
  */

}