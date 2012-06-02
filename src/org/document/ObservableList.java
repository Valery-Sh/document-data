
package org.document;

import java.util.*;


/**
 *
 * @author V. Shyshkin
 */
public class ObservableList<E> implements java.util.List<E> {
    
    private List<E> baseList;
    private List<ValidateHandler> validators;
    
    public ObservableList(List baseList) {
        this.baseList = baseList;
        this.listeners = new ArrayList<ListChangeListener>();
        this.validators = new ArrayList<ValidateHandler>();
        this.observable = true;
    }      
    
    public ObservableList() {
        this(10);
    }      
    public ObservableList(int capacity) {
        this.baseList = new ArrayList(capacity);
        this.listeners = new ArrayList<ListChangeListener>();
        this.validators = new ArrayList<ValidateHandler>();
        this.observable = true;
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
        boolean b = baseList.contains(o);
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
        ListChangeEvent event = this.createAppend(e, false);
        if ( ! validate(event) ) {
            return false;
        }
        boolean b = this.baseList.add(e);
        if ( b ) {
            fireEvent(event,b);
        }
        return b;
    }
    
    protected ListChangeEvent createAppend(E e, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.append);
        event.setElement(e);
        event.setResult(result);
        return event;
    }
    
    @Override
    public boolean remove(Object o) {
        
        ListChangeEvent event = this.createRemove(o, false);
        if ( ! validate(event) ) {
            return false;
        }
        
        boolean b = this.baseList.remove(o);
        if ( b ) {
            fireEvent(event,b);
        }
        return b;

    }
    protected ListChangeEvent createRemove(Object o, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.removeObject);
        event.setObject(o);
        event.setResult(result);
        return event;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.baseList.containsAll(c);
    }

    public boolean containsAny(Collection<?> c) {
        boolean b = false;
        for ( Object e : c) {
            if ( baseList.contains(e) ){
                b = true;
                break;
            }
        }
        return b;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        ListChangeEvent event = this.createAppendAll(c, false);
        if ( ! validate(event) ) {
            return false;
        }
        
        boolean b = this.baseList.addAll(c);
        if ( b ) {
            fireEvent(event,b);
        }
        return b;
    }
    protected ListChangeEvent createAppendAll(Collection<? extends E> c, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.appendAll);
        event.setCollection(c);
        event.setResult(result);
        return event;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        ListChangeEvent event = this.createAddAll(index,c, false);
        if ( ! validate(event) ) {
            return false;
        }

        boolean b = this.baseList.addAll(index,c);
        if ( b ) {
            fireEvent(event,b);
        }
        return b;
    }
    protected ListChangeEvent createAddAll(int index,Collection<? extends E> c, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.addAll);
        event.setIndex(index);
        event.setCollection(c);
        event.setResult(result);
        return event;
    }
    /**
     * {@inheritDoc}
     * @param c
     * @return 
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        
        ListChangeEvent event = this.createBeforeRemoveAll(c, false);
        if ( ! validate(event) ) {
            return false;
        }
        fireEvent(event, true);
        boolean b = this.baseList.removeAll(c);
        event = this.createRemoveAll(c, false);
        fireEvent(event, b);
        return b;
    }
    protected ListChangeEvent createBeforeRemoveAll(Collection<?> c, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.beforeRemoveAll);
        event.setCollection(c);
        event.setResult(result);
        return event;
    }
    
    protected ListChangeEvent createRemoveAll(Collection<?> c, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.removeAll);
        event.setCollection(c);
        event.setResult(result);
        return event;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        ListChangeEvent event = this.createBeforeRetainAll(c, false);
        if ( ! validate(event) ) {
            return false;
        }

        fireEvent(event,false);
        boolean b = this.baseList.retainAll(c);
        if ( b ) {
            fireEvent(event,b);
        }
        
        return b;
    }
    protected ListChangeEvent createBeforeRetainAll(Collection<?> c, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.beforeRetainAll);
        event.setCollection(c);
        event.setResult(result);
        return event;
    }
    
    protected ListChangeEvent createRetainAll(Collection<?> c, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.retainAll);
        event.setCollection(c);
        event.setResult(result);
        return event;
    }

    @Override
    public void clear() {
        ListChangeEvent event = this.createBeforeClear();
        if ( ! validate(event) ) {
            return;
        }
        fireEvent(event,null);
        this.baseList.clear();
        event = this.createClear();
        fireEvent(event,null);
    }
    protected ListChangeEvent createBeforeClear() {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.beforeClear);
        event.setCollection(this);
        return event;
    }
    
    protected ListChangeEvent createClear() {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.clear);
        return event;
    }

    @Override
    public E get(int index) {
        return this.baseList.get(index);
    }

    @Override
    public E set(int index, E element) {
        
        ListChangeEvent event = this.createSet(index, element,get(index));
        if ( ! validate(event) ) {
            return null;
        }
        
        E e = this.baseList.set(index,element);
        fireEvent(event, e);
        return e;
    }
    /**
     * Create a new object of type <code>ListChangeEvent</code> for a given
     *  index, element and result. 
     * The method is called from the method {@link #set(int, java.lang.Object) } 
     *  in order to notify of element replace.
     * @param index the index of the element which is to be replaced by <code>set</code>
     * method.
     * @param element a new element to be set
     * @param result an old element 
     * @return a new object of type <code>ListChangeEvent</code>
     */
    protected ListChangeEvent createSet(int index,E element,E result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.set);
        event.setIndex(index);
        event.setElement(element);
        event.setResult(result); // old element
        return event;
    }

    @Override
    public void add(int index, E element) {
        ListChangeEvent event = this.createAdd(index, element);
        if ( ! validate(event) ) {
            return;
        }
        this.baseList.add(index,element);
        fireEvent(event,element);
    }
    protected ListChangeEvent createAdd(int index,E element) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.add);
        event.setIndex(index);
        event.setElement(element);
        event.setResult(true);
        return event;
    }

    @Override
    public E remove(int index) {
        ListChangeEvent event = this.createRemove(index,null);
        if ( ! validate(event) ) {
            return null;
        }
        
        E e = this.baseList.remove(index);
        event.setElement(e);
        fireEvent(event,e);
        return e;
    }
    protected ListChangeEvent createRemove(int index,E result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.remove);
        event.setIndex(index);
        event.setElement(result);
        event.setResult(result);
        return event;
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
    private boolean observable;

    protected void beforeFireEvent(ListChangeEvent event) {
    }    

    protected void fireEvent(ListChangeEvent event, Object result) {
        
        event.setResult(result);
        beforeFireEvent(event);
        fireEvent(event);
    }    
    protected void fireEvent(ListChangeEvent event) {
        if ( ! observable ) {
            return;
        }
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

    public boolean isObservable() {
        return observable;
    }

    void setObservable(boolean observable) {
        this.observable = observable;
    }

    public void addValidateHandler(ValidateHandler v) {
        validators.add(v);
    }
    public boolean validate(ListChangeEvent event) {
        for ( ValidateHandler v : validators) {
            if ( ! v.validate(event) ) {
                return false;
            }
        }
        return true;
        
    }

    public static interface ValidateHandler {
        
        boolean validate(ListChangeEvent event);
        
    }
}
