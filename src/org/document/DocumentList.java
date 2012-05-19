package org.document;

import java.util.Collection;
import java.util.List;
import org.document.binding.BindingManager;

/**
 *
 *
 * @author V. Shyshkin
 */
public class DocumentList<E extends Document> extends ObservableList<E> {

    private E newDocument;
    private BindingManager bindingManager;
    
    public DocumentList(List baseList) {
        super(baseList);
    }

    public DocumentList(List baseList, BindingManager bindingManager) {
        super(baseList);
        this.bindingManager = bindingManager;
    }
    
    public DocumentList() {
        this(0);
    }

    public DocumentList(int capacity) {
        super(capacity);
    }

    public BindingManager getBindingManager() {
        return bindingManager;
    }
    
    public E newDocument(E e) {

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
    /**
     * The method is used when one of the remove methods is called.
     * When the given event objects defines one of the actions:
     * <ul>
     *  <li>remove</li>
     *  <li>removeAll</li>
     *  <li>removeObject</li>
     *  <li>retainAll</li>
     *  <li>set</li>
     * </ul>
     * and the modified list doesn't contain a document which is marked as "new"
     * then the property <code>newDocument</code> set to null. 
     * @param event the object of type {@link org.document.ListChangeEvent } 
     */
    @Override
    protected void beforeFireEvent(ListChangeEvent event) {
        switch (event.getAction()) {
            case remove:
            case removeAll:
            case removeObject:
            case retainAll:
            case set:    
                if ( containsNew() && ! contains(newDocument) ) {
                    newDocument = null;
                }
                break;
        }
    }

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
                case set:
                    e = event.getElement();
                    if (list.containsNew() && list.isNew((Document) e)) {
                        b = false;
                    }
                    break;

            }
            return b;
        }
    }
}//class DocumentList
