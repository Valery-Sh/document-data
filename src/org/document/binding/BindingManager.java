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
    
    @Override
    public void setSourceList(List sourceList) {
        super.setSourceList(sourceList);
        init(sourceList);
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
    /**
     * Handles events of type <code>ListChangeEvent</code>.
     * The method is called in response of applying of one of the following 
     * methods: 
     * <ul>
     *   <li>{@link DocumentList#add(java.lang.Object) }</li> 
     *   <li>{@link DocumentList#add(int, java.lang.Object) } </li> 
     *   <li>{@link DocumentList#addAll(java.util.Collection) }</li> 
     *   <li>{@link DocumentList#addAll(int, java.util.Collection) }</li> 
     *   <li>{@link DocumentList#remove(java.lang.Object) }</li> 
     *   <li>{@link DocumentList#remove(int) }</li> 
     *   <li>{@link DocumentList#removeAll(java.util.Collection) }</li> 
     *   <li>{@link DocumentList#retainAll(java.util.Collection) }</li> 
     *   <li>{@link DocumentList#newDocument)}</li> 
     *   <li>{@link DocumentList#newDocument(org.document.Document) }</li> 
     *   <li>{@link DocumentList#clear()  }</li> 
     * 
     * </ul>
     * @param event the event to be handled
     * @see ListChangeEvent
     * @see ListChangeListener
     */
    @Override
    public void listChanged(ListChangeEvent event) {
        T newSel = selected;
        DocumentList<T> list = (DocumentList)event.getSource();
        DocumentList<Document> forListModel = new DocumentList<Document>(list);
        if ( event.getAction() == ListChangeEvent.Action.appendNew && (Boolean)event.getResult() ) {
            newSel = list.getNewDocument();
        } else if ( ! list.contains(selected) ) {
            T oldSel = null;
            if ( event.getAction() == ListChangeEvent.Action.set ) {
                 oldSel = (T)event.getResult();
            }
            if (oldSel == selected ) {
                newSel = list.get(event.getIndex());
            } else {
                newSel = list.isEmpty() ? null : list.get(0);
            }
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