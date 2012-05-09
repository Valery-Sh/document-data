
package org.document;

import java.util.List;


/**
 *
 * @author V. Shyshkin
 */
public class DocumentList<E extends Document> extends ObservableList<E> implements ListChangeListener {
    
            
    public DocumentList(List baseList) {
        super(baseList);
    }      
    
    public DocumentList() {
        this(0);
    }      
    public DocumentList(int capacity) {
        super(capacity);
    }
    private E newDocument;
    
    public boolean newDocument(E e) {
        // invoked to throw NullPointerException if e == null
        e.getPropertyStore();
        
        this.setObservable(false);
        boolean b = this.add(e);
        this.setObservable(true);
        if ( b ) {
            newDocument = e;
            updateState(e);
            fireNewDocument(e,b);
        }
        return b;
    }
    
    protected void updateState(E e) {
        DocumentState state;
        if ( ! (e instanceof HasDocumentState) ) {
            return;
        }
        state = ((HasDocumentState)e).getDocumentState();
        state.setEditing(true);
        state.addListChangeListener(this); // Must be before setAttached
        state.setAttached(false);
        
    }
    protected void fireNewDocument(E e, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.appendNew);
        event.setElement(e);
        event.setResult(result);
        fireEvent(event);
    }
    @Override
    protected void fireEvent(ListChangeEvent event) {
        if ( ! (event.getAction() == ListChangeEvent.Action.appendNew) ) {
            listChanged(event); // first handle internally    
        }
        
        if ( ! isObservable() ) {
            return;
        }
        super.fireEvent(event);
    }

    @Override
    public void listChanged(ListChangeEvent event) {
        if ( newDocument == null ) {
            return;
        }
        if ( event.getAction() == ListChangeEvent.Action.newElementState ) {
            DocumentState state = (DocumentState)event.getSource();
            Document doc = (Document)event.getElement();
            if ( doc != newDocument) {
                return;
            }
            state.removeListChangeListener(this);
            newDocument = null;
        }
        
    }
}
