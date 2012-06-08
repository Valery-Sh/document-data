package org.document.binding;

import java.util.List;
import org.document.BindingState;
import org.document.Document;
import org.document.DocumentList;

/**
 *
 * @author V. Shyshkin
 */
public abstract class DocumentListBinder<E extends Document> extends BindingStateBinder {
    //protected BindingState bindi
    //private List<? extends Document> list;
    private List list;
    public DocumentListBinder() {
        super();
    }

    public DocumentListBinder(Object component) {
        super(component);
    }
    /**
     * Overriden in order to assign the <code>null</code> value to 
     * the <code>list</code> property.
     * @param state the object of type {@link BindingState}
     */
    @Override
    public void setBindingState(BindingState state) {
        super.setBindingState(state);
        this.list = null;
    }


    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        BindingState bs = new BindingState();
        this.setBindingState(bs);
        DocumentList dl = new DocumentList(list);
        bs.setDocumentList(dl);
        //setDocument(bs);
        this.list = list;
    }
    
    public void setSelected(int index) {
        if ( index < 0 || getList() == null || index > getList().size()-1 ) {
            return;
        }
        getBindingState().setSelected((E)list.get(index));
    }
    public E getSelected() {
        return (E)getBindingState().getSelected();
    }
    public int getSelectedIndex() {
        if ( getBindingState().getDocumentList() == null || getBindingState().getDocumentList().isEmpty() ) {
            return -1;
        }
        if ( getBindingState().getSelected() == null ) {
            return -1;
        }
        return getBindingState().getDocumentList().indexOf(getBindingState().getSelected());
    }
    
    public void setSelected(E document) {
        if ( getList() == null ) {
            return;
        }
        getBindingState().setSelected(document);
    }
    
}
