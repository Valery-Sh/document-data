package org.document.binding;

import java.util.List;
import org.document.*;

/**
 *
 * @author V. Shishkin
 */
public class BindingManager<T extends Document> extends AbstractBindingManager<T> implements ListChangeListener {

    protected DocumentList<Document> documents;
    
    public BindingManager(List<T> sourceList) {
        super(sourceList);
        init(sourceList);
    }

    public BindingManager() {
        super();
    }

    private void init(List<T> sourceList) {
        documents = new DocumentList(sourceList,this);
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
        T newSel = selected;
        DocumentList<T> list = (DocumentList)event.getSource();
        List<Document> forListModel = new DocumentList<Document>(list);
        
        if ( ! list.contains(selected) ) {
            newSel = list.isEmpty() ? null : list.get(0);
            if ( selected != null ) {
                Object o = documentBinderOf(selected);
                if ( o != null ) {
                    selected.propertyStore().removeDocumentChangeListener(documentBinderOf(selected));
                }
            }
        }
        getListState().setDocumentList(forListModel);
        setSelected(newSel);
    }


}