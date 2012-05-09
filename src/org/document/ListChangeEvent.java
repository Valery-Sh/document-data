package org.document;

import java.util.Collection;
import java.util.EventObject;

/**
 *
 * @author V. Shyshkin
 */
public class ListChangeEvent<E> extends EventObject{
    
    private Action action;
    private int index;
    private E element;
    private Object object;
    private E oldElement;
    private Object result;
    private Collection<? extends E> collection;
    
    public ListChangeEvent(Object source) {
        super(source);
    }
    public ListChangeEvent(Object source,Action action) {
        this(source);
        this.action = action;
    }
    public Action getAction() {
        return action;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Collection<? extends E> getCollection() {
        return collection;
    }

    public void setCollection(Collection<? extends E> collection) {
        this.collection = collection;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public E getElement() {
        return element;
    }

    public void setElement(E newElement) {
        this.element = newElement;
    }

    public E getOldElement() {
        return oldElement;
    }

    public void setOldElement(E oldElement) {
        this.oldElement = oldElement;
    }


    
    public enum Action {
        append,
        appendAll,
        add,
        set,
        addAll,
        removeObject, // notifies ErrorBinders
        remove, 
        removeAll, // notifies ErrorBinders
        retainAll,
        clear,
        appendNew,
        newElementState
        
    }
}