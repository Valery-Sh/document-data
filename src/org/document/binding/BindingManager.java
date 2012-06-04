package org.document.binding;

import java.util.List;
import org.document.*;

/**
 *
 * @author V. Shishkin
 */
public class BindingManager<T extends Document> extends AbstractBindingManager<T> implements ListChangeListener {

    //protected DocumentList<Document> documents;
    
    public BindingManager(List<T> sourceList) {
        super(sourceList);
        init(sourceList);
    }

    public BindingManager() {
        super();
    }

    private void init(List<T> sourceList) {
        //getDocuments() = new DocumentList(sourceList,this);
        getDocuments().addListChangeListener(this);
    }
    
    @Override
    public void setSourceList(List sourceList) {
        super.setSourceList(sourceList);
        init(sourceList);
    }
    public DocumentList getDocuments() {
        return (DocumentList)getListState().getDocumentList();
    }

    @Override
    protected void afterSetSelected(T oldSelected) {
//        if (getDocuments().isNew(oldSelected) && !isEditing(oldSelected)) {
//            getDocuments().cancelNew();
//        }
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
     *   <li>{@link DocumentList#addSelect)}</li> 
     *   <li>{@link DocumentList#addSelect(org.document.Document) }</li> 
     *   <li>{@link DocumentList#clear()  }</li> 
     * 
     * </ul>
     * @param event the event to be handled
     * @see ListChangeEvent
     * @see ListChangeListener
     */
    @Override
    public void listChanged(ListChangeEvent event) {
        if ( event.getAction() == ListChangeEvent.Action.beforeClear ||
             event.getAction() == ListChangeEvent.Action.beforeRemoveAll ||
             event.getAction() == ListChangeEvent.Action.beforeRetainAll   ) {        
            return;
        }
        updateDocumentState(event);
        T newSel = selected;
        DocumentList<T> list = (DocumentList)event.getSource();
        //DocumentList<T> forListModel = new DocumentList<T>(list);
        if ( event.getAction() == ListChangeEvent.Action.addAndSelect && (Boolean)event.getResult() ) {
            newSel = list.getLast();
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
        //getListState().setDocumentList(forListModel);
        // We assign the same list
        getListState().setDocumentList(list);
        
        setSelected(newSel);
    }
    protected void updateDocumentState(ListChangeEvent e) {
        switch(e.getAction()) {
            case add : 
            case append :
                if ( (Boolean)e.getResult() ) {
                    this.updateAttachState((T)e.getElement(), true);
                }
                break;
            case addAndSelect : 
                break;
            case removeObject :
                if ( (Boolean)e.getResult() ) {
                    this.updateAttachState((T)e.getElement(), false);
                }
                break;
            case remove :
                this.updateAttachState((T)e.getElement(), false);
                break;
            case set : 
                if (e.getResult() != null) {
                    //old document
                    this.updateAttachState((T)e.getResult(), false);
                } 
                if (e.getElement() != null) {
                    //new document
                    this.updateAttachState((T)e.getResult(), true);
                } 
                break;
            case beforeClear : 
                DocumentList<T> l = (DocumentList)e.getCollection();
                for ( T d : l ) {
                    updateAttachState(d, false);
                }
                break;
            case appendAll :
            case addAll :    
                l = (DocumentList)e.getCollection();
                if ( (Boolean)e.getResult() ) {
                    for ( T d : l ) {
                        updateAttachState(d, true);
                    }
                }
                break;
            case beforeRemoveAll : 
                if ( getDocuments() == null ) {
                    break;
                }
                l = (DocumentList)e.getCollection();
                DocumentList dl = getDocuments();
                if ( (Boolean)e.getResult() ) {
                    for ( T d : l ) {
                        if ( dl.contains(d) ) {
                            updateAttachState(d, false);
                        }    
                    }
                }
                break;
            case beforeRetainAll : 
                if ( getDocuments() == null ) {
                    break;
                }
                l = (DocumentList)e.getCollection();
                dl = getDocuments();
                if ( (Boolean)e.getResult() ) {
                    for ( T d : l ) {
                        if ( ! dl.contains(d) ) {
                            updateAttachState(d, false);
                        }    
                    }
                }
                break;
                
        }//switch
    }
    

}