
package org.document;

import java.util.*;


/**
 *
 * @author V. Shyshkin
 */
public class DocumentList<E extends Document> implements java.util.List<E> {
    
    private List<E> baseList;
            
    public DocumentList(List baseList) {
        this.baseList = baseList;
        this.listeners = new ArrayList<ListChangeListener>();
    }      
    
    public DocumentList() {
        this(0);
    }      
    public DocumentList(int capacity) {
        this.baseList = new ArrayList(capacity);
        this.listeners = new ArrayList<ListChangeListener>();
    }

    protected List<E> getBaseList() {
        return baseList;
    }

    protected void setBaseList(List<E> baseList) {
        this.baseList = baseList;
    }

    public List<ListChangeListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<ListChangeListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public int size() {
        return this.baseList.size();
    }

    @Override
    public boolean isEmpty() {
        return this.baseList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.baseList.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return this.baseList.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.baseList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size())
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(baseList.toArray(), size(), a.getClass());
	System.arraycopy(baseList.toArray(), 0, a, 0, size());
        if (a.length > size())
            a[size()] = null;
        return a;
    }

    @Override
    public boolean add(E e) {
        boolean b = this.baseList.add(e);
        if ( b ) {
            fireAppend(e,b);
        }
        return b;
    }
    protected void fireAppend(E e, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.append);
        event.setElement(e);
        event.setResult(result);
        fireEvent(event);
    }
    @Override
    public boolean remove(Object o) {
        boolean b = this.baseList.remove(o);
        if ( b ) {
            fireRemove(o,b);
        }
        return b;

    }
    protected void fireRemove(Object o, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.removeObject);
        event.setObject(o);
        event.setResult(result);
        fireEvent(event);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.baseList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean b = this.baseList.addAll(c);
        if ( b ) {
            fireAppendAll(c,b);
        }
        return b;
    }
    protected void fireAppendAll(Collection<? extends E> c, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.appendAll);
        event.setCollection(c);
        event.setResult(result);
        fireEvent(event);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean b = this.baseList.addAll(index,c);
        if ( b ) {
            fireAddAll(index,c,b);
        }
        return b;
    }
    protected void fireAddAll(int index,Collection<? extends E> c, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.addAll);
        event.setIndex(index);
        event.setCollection(c);
        event.setResult(result);
        fireEvent(event);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean b = this.baseList.removeAll(c);
        if ( b ) {
            fireRemoveAll(c,b);
        }
        return b;
    }
    protected void fireRemoveAll(Collection<?> c, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.removeAll);
        event.setCollection(c);
        event.setResult(result);
        fireEvent(event);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean b = this.baseList.retainAll(c);
        if ( b ) {
            fireRetainAll(c,b);
        }
        return b;
    }
    protected void fireRetainAll(Collection<?> c, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.retainAll);
        event.setCollection(c);
        event.setResult(result);
        fireEvent(event);
    }

    @Override
    public void clear() {
        this.baseList.clear();
        fireClear();
    }
    protected void fireClear() {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.clear);
        fireEvent(event);
    }

    @Override
    public E get(int index) {
        return this.baseList.get(index);
    }

    @Override
    public E set(int index, E element) {
        E e = this.baseList.set(index,element);
        fireSet(index,element, e);
        return e;
    }
    protected void fireSet(int index,E element,E result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.set);
        event.setIndex(index);
        event.setElement(element);
        event.setResult(result); // old element
        fireEvent(event);
    }

    @Override
    public void add(int index, E element) {
        this.baseList.add(index,element);
        fireAdd(index,element);
    }
    protected void fireAdd(int index,E element) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.add);
        event.setIndex(index);
        event.setElement(element);
        fireEvent(event);
    }

    @Override
    public E remove(int index) {
        E e = this.baseList.remove(index);
        fireRemove(index,e);
        return e;
    }
    protected void fireRemove(int index,E result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.remove);
        event.setIndex(index);
        event.setElement(result);
        event.setResult(result);
        fireEvent(event);
    }

    @Override
    public int indexOf(Object o) {
        return this.baseList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.baseList.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return this.baseList.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return this.baseList.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return this.baseList.subList(fromIndex, toIndex);
    }

    //
    // NEW FIELDS && METHODS
    //
    
    private List<ListChangeListener> listeners;
    
    protected void fireEvent(ListChangeEvent event) {
        for ( ListChangeListener l : listeners) {
            l.listChanged(event);
        }
    }
    public void addListChangeListener(ListChangeListener l) {
        this.listeners.add(l);
    }
    public void removeListChangeListener(ListChangeListener l) {
        this.listeners.remove(l);
    }
    public List<ListChangeListener> getListChangeListener() {
        return this.listeners;
    }


}
