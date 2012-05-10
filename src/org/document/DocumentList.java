package org.document;

import java.util.Collection;
import java.util.List;

/**
 * Cannot:
 * <ul>
 *  <li>add  document which state: <code>attached == true;</code></li>
 *  <li>add  document which state: <code>editing == true;</code></li>
 *  <li>add  duplicate document</li>
 * </ul>
 * document marked as new that is created by newDocument() always has state:
 * editing == true and attached == false.
 * When a document removes it 
 * 
 *   
 * 
 * @author V. Shyshkin
 */
public class DocumentList<E extends Document> extends ObservableList<E> {

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

    public E newDocument(E e) {
        // invoked to throw NullPointerException if e == null
        e.getPropertyStore();

        ListChangeEvent event = this.createNewElementState(e, false);
        if (!validate(event)) {
            return null;
        }

        this.setObservable(false);
        boolean b = this.add(e);
        this.setObservable(true);
        if (b) {
            newDocument = e;
            //updateState(e);
            fireEvent(event, b);
        }
        return newDocument;
    }
/*    protected void updateState(E e) {
        DocumentState state;
        if (!(e instanceof HasDocumentState)) {
            return;
        }
        state = ((HasDocumentState) e).getDocumentState();
        state.setEditing(true);
//        state.addListChangeListener(this); // Must be before setAttached
//        state.setAttached(false);
        //addListChangeListener(state);
    }
*/
    public E getNewDocument() {
        return newDocument;
    }

    public boolean containsNew() {
        return newDocument != null;
    }

    public boolean isNew(E e) {
        return newDocument == e;
    }
    public void cancelNew() {
        newDocument = null;
    }

    protected ListChangeEvent createNewElementState(E e, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this, ListChangeEvent.Action.append);
        event.setElement(e);
        event.setResult(result);
        return event;
    }


    protected void fireNewDocument(E e, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this, ListChangeEvent.Action.appendNew);
        event.setElement(e);
        event.setResult(result);
        fireEvent(event);
    }

    @Override
    protected void fireEvent(ListChangeEvent event) {
//        if (!(event.getAction() == ListChangeEvent.Action.appendNew )) {
//            listChanged(event); // first handle internally    
//        }

        if (!isObservable()) {
            return;
        }
        super.fireEvent(event);
    }
    protected void newRemoved(ListChangeEvent event) {
        if (newDocument == null) {
            return;
        }
        if ( contains(newDocument) ) {
            ListChangeEvent e = new ListChangeEvent(this,ListChangeEvent.Action.removeNew);
            e.setElement(newDocument);
//            ((HasDocumentState)newDocument).getDocumentState().listChanged(e);
        }
    }

/*    public void listChanged(ListChangeEvent event) {
        if (newDocument == null) {
            return;
        }
        if (event.getAction() == ListChangeEvent.Action.newElementState) {
            DocumentState state = (DocumentState) event.getSource();
            Document doc = (Document) event.getElement();
            if (doc != newDocument) {
                return;
            }
            state.removeListChangeListener(this);
            newDocument = null;
        }

    }
*/
    protected static class ModifyValidateHandler implements ValidateHandler {

        protected DocumentList list;

        public ModifyValidateHandler(DocumentList list) {
            this.list = list;
        }

        @Override
        public boolean validate(ListChangeEvent event) {
            boolean b = true;
            switch (event.getAction()) {
                case add:
                case append:
                    Object e = event.getElement();
                    if (list.contains(e)) {
                        b = false;
                    }
                    break;
                case appendNew:
                    if (list.containsNew()) {
                        b = false;
                    }
                    break;
                case addAll:
                case appendAll:
                    Collection c = event.getCollection();
                    if (list.containsAny(c)) {
                        b = false;
                    }
                case retainAll:
                    break;
                case set :    
                    e = event.getElement();
                    if (list.containsNew() && list.isNew((Document)e)) {
                        b = false;
                    }
                    break;
                    
            }
            return b;
        }
    }
}//class DocumentList
