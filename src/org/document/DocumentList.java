package org.document;

import java.util.Collection;
import java.util.List;
import org.document.binding.BindingManager;

/**
 * The class gets any collection of objects of type
 * <code>Document</code> that implements {@link java.util.List} and uses it as a
 * document store. There are two main differences between this class and it's
 * superclass. First, is that the methods that add a new element are not allowed
 * to complete there operation if an element is already in the list. Second is
 * that that for each document to be added, the list registers itself as an
 * event handler of
 * <code>DocumentChangeEvent</code>. Accordingly for each element to be removed
 * the list unregisters itself. In addition, each new document is marked as
 * <code><i>attached</i></code>. And when a document is removed it is marked as
 * <code><i>detached</i></code>.
 *
 * @author V. Shyshkin
 */
public class DocumentList<E extends Document> extends ObservableList<E> {

    private BindingManager bindingManager;
    ValidateHandler validateHandler;

    public DocumentList(List<E> baseList) {
        super(baseList);
        validateHandler = new ValidateHandlerImpl();
        this.addValidateHandler(validateHandler);

    }

    /*    public DocumentList(List<E> baseList, BindingManager bindingManager) {
     this(baseList);
     this.bindingManager = bindingManager;
     }
     */
    public DocumentList() {
        super();
        validateHandler = new ValidateHandlerImpl();
        this.addValidateHandler(validateHandler);

    }


    /*    public BindingManager getBindingManager() {
     return bindingManager;
     }
     */
    /**
     * Appends a given element to the end of the list. Fires an event of type {@link ListChangeEvent
     * } to notify listeners of a new element added. The event
     * <code>action<</code> property gets a value that equals to
     * {@link ListChangeEvent.Action#addAndSelect}. <p> In response the binding
     * manager will set a new document as
     * <code><i>selected</i></code>.
     *
     * @param e the element to be added
     * @return the added document or null if something wrong
     */
    public E addSelect(E e) {

        //e.propertyStore();

        ListChangeEvent event = this.createAddAndSelect(e, false);
        if (!validate(event)) {
            return null;
        }

        this.setObservable(false);
        boolean b = this.add(e);
        this.setObservable(true);
        if (b) {
            fireEvent(event, b);
        }
        return e;
    }

    public E getLast() {
        if (isEmpty()) {
            return null;
        }
        return get(size() - 1);
    }

    /**
     * Creates a new object of type
     * <code>ListChangeEvent</code>. Sets the event properties values: <ul>
     * <li>action to {@link ListChangeEvent.Action#addSelect}</li> <li>element
     * to the value of the parameter
     * <code>e</code></li> <li>result to the same value as
     * <code>e</code> or null if element cannot be added. </ul>
     *
     * @param e the element to be added
     * @param result <code>true</code> if the element added
     * otherwise <code>false</code>
     * @return a new instance of {@link ListChangeEvent }
     */
    protected ListChangeEvent createAddAndSelect(E e, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this, ListChangeEvent.Action.addAndSelect);
        event.setElement(e);
        event.setResult(result);
        if (result) {
            event.setIndex(this.size() - 1);
        } else {
            event.setIndex(-1);
        }
        return event;
    }


    /*    @Override
     protected void fireEvent(ListChangeEvent event) {
     if (!isObservable()) {
     return;
     }
     super.fireEvent(event);
     }
     */
    /**
     * The method is used when one of the remove or set methods is called. When
     * the given event objects defines one of the actions: <ul> <li>remove</li>
     * <li>removeAll</li> <li>removeObject</li> <li>retainAll</li> <li>set</li>
     * </ul> and the modified list doesn't contain a document which is marked as
     * "new" then the property
     * <code>addSelect</code> set to null.
     *
     * @param event the object of type {@link org.document.ListChangeEvent }
     */
    /*    @Override
     protected void beforeFireEvent(ListChangeEvent event) {
     switch (event.getAction()) {
     case remove:
     case removeAll:
     case removeObject:
     case retainAll:
     case set:    
     break;
     }
     }
     */
    protected class ValidateHandlerImpl implements ValidateHandler {

//        protected DocumentList list;
        public ValidateHandlerImpl() {//DocumentList list) {
            //          this.list = list;
        }

        protected boolean containsNull(Collection c) {
            boolean b = false;
            for (Object e : c) {
                if (e == null ) {
                    b = true;
                    break;
                }
            }
            return b;
        }

        @Override
        public boolean validate(ListChangeEvent event) {
            boolean b = true;
            switch (event.getAction()) {
                case add:
                case append:
                case addAndSelect:
                    Object e = event.getElement();
                    if (e == null || contains(e)) {
                        b = false;
                    }
                    break;
                case addAll:
                case appendAll:
                    Collection c = event.getCollection();
                    if (containsAny(c) || containsNull(c)) {
                        b = false;
                    }
                case set:
                    e = event.getElement(); //new element in set method
                    //Document old = (Document)event.getResult();
                    if (e == null || contains(e)) {
                        b = false;
                    }
                    break;
            }
            return b;
        }
    }
}//class DocumentList
