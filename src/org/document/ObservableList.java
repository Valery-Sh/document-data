
package org.document;

import java.util.*;


/**
 * Observable implementation of the <code>java.util.List</code>.
 * Allows register and unregister event listeners in order to notify them of
 * the list change.
 * Every method that can change the content of the list fires an event of typr
 * {@link ListChangeEvent } in order to notify all registers listeners of type
 * {@link ListChangeListener}} of that list changed.
 * <p>
 * The class does not provide its own mechanism to perform operations
 * or to allocate memory. Instead, it relies on the supplied object of 
 * a class that implements <code>java.util.List</code> and use it's methods
 * to access data.
 * 
 * @author V. Shyshkin
 */
public class ObservableList<E> implements java.util.List<E> {
    
    private List<E> baseList;
    private List<ValidateHandler> validators;
    /**
     * Creates a new instance of the class for the specified list that 
     * is used to access elements.
     * 
     * @param baseList any object of the class that implements {@link java.util.List}.
     */
    public ObservableList(List baseList) {
        this();
        this.baseList = baseList;
        
    }      
    /**
     * Creates a new instance of the class.
     * To be able to access elements creates internally a new instance of
     * {@link java.util.ArrayList}.
     */
    public ObservableList() {
        
        this.baseList = new ArrayList(10);
        this.listeners = new ArrayList<ListChangeListener>();
        this.validators = new ArrayList<ValidateHandler>();
        this.observable = true;
    }      
    /**
     * @return internal list that is used to access elements of the 
     *  specified list.
     */
    protected List<E> getBaseList() {
        return baseList;
    }
    /**
    /**
     * Sets the specified list that is to be used internally to access 
     * elements of this list.
     *
     * @param baseList the list to be used to access elements of this list
     */
    protected void setBaseList(List<E> baseList) {
        this.baseList = baseList;
    }
    /**
     * @return a collection of all registered listeners of the
     * event of type {@link ListChangeEvent}.
     */
    public List<ListChangeListener> getListeners() {
        return listeners;
    }

/*    public void setListeners(List<ListChangeListener> listeners) {
        this.listeners = listeners;
    }
*/
    /**
     * @return the number of elements in the list.
     * @see java.util.List#size() 
     */
    @Override
    public int size() {
        return this.baseList.size();
    }
    /**
     * @return <code>false</code> if the list contains at least one element.
     *  <code>true</code> otherwise
     * @see java.util.List#isEmpty() 
     */

    @Override
    public boolean isEmpty() {
        return this.baseList.isEmpty();
    }
    /**
     * Returns <code>true</code> if this list contains the specified element. 
     * More formally, returns true if and only if this list contains 
     * at least one element <code>e</code> such that 
     * <code>(o==null ? e==null : o.equals(e))</code>.
     * @see java.util.List#contains(java.lang.Object) 
     */ 
    @Override
    public boolean contains(Object o) {
        boolean b = baseList.contains(o);
        return this.baseList.contains(o);
    }
    /**
     * @return an iterator over the elements in the list
     * @see java.util.List#iterator() 
     */ 
    @Override
    public Iterator<E> iterator() {
        return this.baseList.iterator();
    }
    /**
     * @return Returns an array containing all of the elements
     * in this list in proper sequence (from first to last element).
     * @see java.util.List#toArray() () 
     */
    @Override
    public Object[] toArray() {
        return this.baseList.toArray();
    }
    /**
     * @param a  the array into which the elements of this list 
     * are to be stored
     * @return an array containing all of the elements in this list in 
     * proper sequence (from first to last element)
     * @see java.util.List#toArray(T[]) 
     */
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
    /**
     * Appends a given element to the end of the list.
     * Fires an event of type {@link ListChangeEvent } to notify
     * listeners of a new element added. The <code>action<</code>
     * property of the event gets a value that equals to 
     * {@link ListChangeEvent.Action#append}.
     * <p>
     * @param e the  element to be added
     * @return <code>true</code> if an element is appended to the list.
     * <code>false</code> otherwise. If the list already contains 
     *  an element to be added than the method does nothing and returns 
     * <code>false</code>.
     * @see java.util.List#add(java.lang.Object) 
     */
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
    /**
     * Creates and returns an object of type <code>ListChangeEvent</code>.
     * The created object is used by the method {@link #add(java.lang.Object) }
     * to fire the event of type {@link ListChangeEvent }.
     * The following properties of the event are set:
     * <ul>
     *   <li><code>action</code> - {@link ListChangeEvent.Action#append}.</li>
     *   <li><i><code>element</code></i> - a value of the parameter <b>e</b></li>
     *   <li><i><code>result</code></i> - a value of the parameter <code>result</code></li>
     * </ul>
     * @param e the value that is passed by the add() method.
     * @param result the value that is passed by the add() method.
     * @return the object of type <code>ListChangeEvent</code>
     */
    protected ListChangeEvent createAppend(E e, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.append);
        event.setElement(e);
        event.setResult(result);
        return event;
    }

    /**
     * Removes the specified element from the list.
     * 
     * Fires an event of type {@link ListChangeEvent } to notify
     * listeners of an element is removed. The <code>action<</code>
     * property of the event gets a value that equals to 
     * {@link ListChangeEvent.Action#removeObject}.
     * <p>
     * @param o the object to be removed
     * @return  <code>true</code> if the object is actually removed
     * {@inheritDoc }
     * @see java.util.List#remove(java.lang.Object) 
     */
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
    /**
     * Creates and returns an object of type <code>ListChangeEvent</code>.
     * The created object is used by the method {@link #remove(java.lang.Object) }
     * to fire the event of type {@link ListChangeEvent }.
     * The following properties of the event are set:
     * <ul>
     *   <li><code>action</code> - {@link ListChangeEvent.Action#removeObject}. }</li>
     *   <li><i><code>object</code></i> - a value of the parameter <b>o</b></li>
     *   <li><i><code>result</code></i> - a value of the parameter <code>result</code></li>
     * </ul>
     * @param e the value that is passed by the add() method.
     * @param result the value that is passed by the add() method.
     * @return the object of type <code>ListChangeEvent</code>
     */
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

    /**
     * Appends all or none elements of the given collection the end of the list.
     * 
     * Fires an event of type {@link ListChangeEvent } to notify
     * listeners of a collection of elements is added. The <code>action<</code>
     * property of the event gets a value that equals to 
     * {@link ListChangeEvent.Action#appendAll}.
     * <p>
     * @param c the  collection of elements to be added
     * @return <code>true</code> if an element is inserted to the list.
     * <code>false</code> otherwise. If at least a single element from the
     * specified collections already exists in the list  than the method does
     * nothing and returns false.
     * {@inheritDoc }
     * @see java.util.List#addAll(java.util.Collection) } 
     */
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
    /**
     * Creates and returns an object of type <code>ListChangeEvent</code>.
     * The created object is used by the method {@link #addAll(java.util.Collection) }
     * to fire the event of type {@link ListChangeEvent }.
     * The following properties of the event are set:
     * <ul>
     *   <li><code>action</code> - {@link ListChangeEvent.Action#appendAll }. </li>
     *   <li><code>collection</code> - a value of the parameter <code><b>c</b></code>. </li>
     *   <li><i><code>result</code></i> - a value of the parameter <code>result</code></li>
     * </ul>
     * @param c the value of type <code>java.util.Collection</code> 
     *  that is passed by the <code>addAll(Collection)</code> method.
     * @param result the value that is passed by the add() method.
     * @return the object of type <code>ListChangeEvent</code>
     */
    protected ListChangeEvent createAppendAll(Collection<? extends E> c, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.appendAll);
        event.setCollection(c);
        event.setResult(result);
        return event;
    }
    /**
     * Inserts all or none elements of the given collection into
     * the the list at the specified position.
     * 
     * Fires an event of type {@link ListChangeEvent } to notify
     * listeners of a collection of elements is inserted. The <code>action<</code>
     * property of the event gets a value that equals to 
     * {@link ListChangeEvent.Action#addAll}.
     * <p>
     * @param c the  collection of elements to be inserted
     * @param index the index  at wich a first element of the specified 
     * collection is to be inserted
     * @return <code>true</code> if all element are inserted into the list.
     * <code>false</code> otherwise. If at least a single element from the
     * specified collections already exists in the list  than the method does
     * nothing and returns false.
     * {@inheritDoc }
     * @see java.util.List#addAll(java.util.Collection) } 
     */
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
    /**
     * Creates and returns an object of type <code>ListChangeEvent</code>.
     * The created object is used by the method {@link #addAll(int, java.util.Collection) }
     * to fire the event of type {@link ListChangeEvent }.
     * The following properties of the event are set:
     * <ul>
     *   <li><code>action</code> - {@link ListChangeEvent.Action#addAll}. }</li>
     *   <li><i><code>Collection</code></i> - a value of the parameter <b>c</b></li>
     *   <li><i><code>index</code></i> - a value of the parameter <b>index</b></li>
     *   <li><i><code>result</code></i> - a value of the parameter <code>result</code></li>
     * </ul>
     * @param e the value that is passed by the add() method.
     * @param result the value that is passed by the add() method.
     * @return the object of type <code>ListChangeEvent</code>
     */
    protected ListChangeEvent createAddAll(int index,Collection<? extends E> c, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.addAll);
        event.setIndex(index);
        event.setCollection(c);
        event.setResult(result);
        return event;
    }
    /**
     * Removes all of the elements of the list that are also
     * contained in the specified collection.
     * 
     * Fires an event of type {@link ListChangeEvent } to notify
     * listeners of a collection of elements is added. The <code>action<</code>
     * property of the event gets a value that equals to 
     * {@link ListChangeEvent.Action#removeAll}.
     * <p>
     * @param c the  collection of elements to be removed
     * @return <code>true</code> if the elements of the collection are removed
     * <code>false</code> otherwise.
     * {@inheritDoc }
     * @see java.util.List#removeAll(java.util.Collection) } 
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
    /**
     * Creates and returns an object of type <code>ListChangeEvent</code>.
     * 
     * The created object is used by the method {@link #removeAll(java.util.Collection) }
     * to fire the event of type {@link ListChangeEvent }.
     * The following properties of the event are set:
     * <ul>
     *   <li><code>action</code> - {@link ListChangeEvent.Action#beforeRemoveAll }. </li>
     *   <li><code>collection</code> - a value of the parameter <code><b>c</b></code>. </li>
     *   <li><i><code>result</code></i> - a value of the parameter <code>result</code></li>
     * </ul>
     * @param c the value of type <code>java.util.Collection</code> 
     *  that is passed by the <code>addAll(Collection)</code> method.
     * @param result the value that is passed by the add() method.
     * @return the object of type <code>ListChangeEvent</code>
     */
    protected ListChangeEvent createBeforeRemoveAll(Collection<?> c, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.beforeRemoveAll);
        event.setCollection(c);
        event.setResult(result);
        return event;
    }
    /**
     * Creates and returns an object of type <code>ListChangeEvent</code>.
     * 
     * The created object is used by the method {@link #removeAll(java.util.Collection) }
     * to fire the event of type {@link ListChangeEvent }.
     * The following properties of the event are set:
     * <ul>
     *   <li><code>action</code> - {@link ListChangeEvent.Action#removeAll }. </li>
     *   <li><code>collection</code> - a value of the parameter <code><b>c</b></code>. </li>
     *   <li><i><code>result</code></i> - a value of the parameter <code>result</code></li>
     * </ul>
     * @param c the value of type <code>java.util.Collection</code> 
     *  that is passed by the <code>addAll(Collection)</code> method.
     * @param result the value that is passed by the add() method.
     * @return the object of type <code>ListChangeEvent</code>
     */
    protected ListChangeEvent createRemoveAll(Collection<?> c, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.removeAll);
        event.setCollection(c);
        event.setResult(result);
        return event;
    }
    /**
     * Removes from this collection all of its elements that are not contained
     * in the specified collection.
     * 
     * Fires an event of type {@link ListChangeEvent } to notify
     * listeners of a collection of elements is removed. The <code>action<</code>
     * property of the event gets a value that equals to 
     * {@link ListChangeEvent.Action#retainAll}.
     * <p>
     * @param c the  collection of elements that is to be retained
     * @return <code>true</code> the list changed
     * <code>false</code> otherwise.
     * {@inheritDoc }
     * @see java.util.List#retainAll(java.util.Collection) } 
     */
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
    /**
     * Creates and returns an object of type <code>ListChangeEvent</code>.
     * 
     * The created object is used by the method {@link #retainAll(java.util.Collection) }
     * to fire the event of type {@link ListChangeEvent }.
     * The following properties of the event are set:
     * <ul>
     *   <li><code>action</code> - {@link ListChangeEvent.Action#beforeRetainAll }. </li>
     *   <li><code>collection</code> - a value of the parameter <code><b>c</b></code>. </li>
     *   <li><i><code>result</code></i> - a value of the parameter <code>result</code></li>
     * </ul>
     * @param c the value of type <code>java.util.Collection</code> 
     *  that is passed by the <code>addAll(Collection)</code> method.
     * @param result the value that is passed by the add() method.
     * @return the object of type <code>ListChangeEvent</code>
     */
    protected ListChangeEvent createBeforeRetainAll(Collection<?> c, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.beforeRetainAll);
        event.setCollection(c);
        event.setResult(result);
        return event;
    }
    /**
     * Creates and returns an object of type <code>ListChangeEvent</code>.
     * 
     * The created object is used by the method {@link #retainAll(java.util.Collection) }
     * to fire the event of type {@link ListChangeEvent }.
     * The following properties of the event are set:
     * <ul>
     *   <li><code>action</code> - {@link ListChangeEvent.Action#retainAll }. </li>
     *   <li><code>collection</code> - a value of the parameter <code><b>c</b></code>. </li>
     *   <li><i><code>result</code></i> - a value of the parameter <code>result</code></li>
     * </ul>
     * @param c the value of type <code>java.util.Collection</code> 
     *  that is passed by the <code>addAll(Collection)</code> method.
     * @param result the value that is passed by the add() method.
     * @return the object of type <code>ListChangeEvent</code>
     */
    protected ListChangeEvent createRetainAll(Collection<?> c, boolean result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.retainAll);
        event.setCollection(c);
        event.setResult(result);
        return event;
    }

    /** 
     * Removes all of the elements from the list.
     * Fires an event of type {@link ListChangeEvent } twice. First to
     * to notify of the event with action 
     * {@link ListChangeEvent.Action#beforeClear}. Second after the operation
     * completes with the <code>action<</code> that equals
     * {@link ListChangeEvent.Action#clear}.
     * @see java.util.List
     */
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
    /**
     * Creates and returns an object of type <code>ListChangeEvent</code>.
     * 
     * The created object is used by the method {@link #clear() }
     * to fire the event of type {@link ListChangeEvent }.
     * The following properties of the event are set:
     * <ul>
     *   <li><code>action</code> - {@link ListChangeEvent.Action#beforeRemoveAll }. </li>
     *   <li><code>collection</code> - this list. </li>
     * </ul>
     * @return the object of type <code>ListChangeEvent</code>
     */
    protected ListChangeEvent createBeforeClear() {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.beforeClear);
        event.setCollection(this);
        return event;
    }
    /**
     * Creates and returns an object of type <code>ListChangeEvent</code>.
     * 
     * The created object is used by the method {@link #clear() }
     * to fire the event of type {@link ListChangeEvent }.
     * Doesn't change event property.
     * @return the object of type <code>ListChangeEvent</code>
     */
    protected ListChangeEvent createClear() {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.clear);
        return event;
    }

    /**
     * Return the element at the specified position in the list.
     * @param index the position of the element to be returned
     * @return the element at the specified position 
     * @throws IndexOutOfBoundsException
     * @see java.util.List
     */
    @Override
    public E get(int index) {
        return this.baseList.get(index);
    }
    /**
     * Replaces the element at the specified position in the list
     *  with a new specified element.
     * 
     * Fires an event of type {@link ListChangeEvent } to notify
     * listeners of an element replaced. The <code>action<</code>
     * property of the event gets a value that equals to 
     * {@link ListChangeEvent.Action#set}.
     * <p>
     * @param index position of the element in the list
     * @param element the  element to be replaced
     * @return the element that resided at the specified position
     * prior to replacing
     * @see java.util.List#add(java.lang.Object) 
     */
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
     * Creates and returns an object of type <code>ListChangeEvent</code>.
     * The created object is used by the method {@link #set(int, java.lang.Object)  }
     * to fire the event of type {@link ListChangeEvent }.
     * The following properties of the event are set:
     * <ul>
     *   <li><code>action</code> - {@link ListChangeEvent.Action#set. }</li>
     *   <li><i><code>index</code></i> - a value of the parameter <b>index</b></li>
     *   <li><i><code>element</code></i> - a value of the parameter <b>element</b></li>
     *   <li><i><code>result</code></i> - a value of the parameter <code>result</code></li>
     * </ul>
     * @param index the value that is passed by the set() method.     
     * @param element the value that is passed by the set() method.
     * @param result the value that is passed by the add() method.
     * @return the object of type <code>ListChangeEvent</code>
     */
    protected ListChangeEvent createSet(int index,E element,E result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.set);
        event.setIndex(index);
        event.setElement(element);
        event.setResult(result); // old element
        return event;
    }

    /**
     * Inserts a given element in the specified position of the list.
     * 
     * Fires an event of type {@link ListChangeEvent } to notify
     * listeners of a new element added. The <code>action<</code>
     * property of the event gets a value that equals to 
     * {@link ListChangeEvent.Action#add}.
     * <p>
     * @param index the index  at wich a specified element to be inserted
     * @param e the  element to be added
     * @return <code>true</code> if an element is inserted to the list.
     * <code>false</code> otherwise. If the list already contains 
     *  an element to be inserted than the method does nothing.
     * {@inheritDoc }
     * @see java.util.List#add(int, java.lang.Object) 
     */
    @Override
    public void add(int index, E element) {
        
        ListChangeEvent event = this.createAdd(index, element);
        if ( ! validate(event) ) {
            return;
        }
        this.baseList.add(index,element);
        fireEvent(event,element);
    }
    /**
     * Creates and returns an object of type <code>ListChangeEvent</code>.
     * The created object is used by the method {@link #add(int, java.lang.Object)  }
     * to fire the event of type {@link ListChangeEvent }.
     * The following properties of the event are set:
     * <ul>
     *   <li><code>action</code> - {@link ListChangeEvent.Action#add. }</li>
     *   <li><i><code>index</code></i> - a value of the parameter <b>index</b></li>
     *   <li><i><code>element</code></i> - a value of the parameter <b>element</b></li>
     *   <li><i><code>result</code></i> - a value of the parameter <code>result</code></li>
     * </ul>
     * @param e the value that is passed by the add() method.
     * @param result the value that is passed by the add() method.
     * @return the object of type <code>ListChangeEvent</code>
     */
    protected ListChangeEvent createAdd(int index,E element) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.add);
        event.setIndex(index);
        event.setElement(element);
        event.setResult(true);
        return event;
    }

    /**
     * Removes the element at the specified position int the list.
     * 
     * Fires an event of type {@link ListChangeEvent } to notify
     * listeners of an element is removed. The <code>action<</code>
     * property of the event gets a value that equals to 
     * {@link ListChangeEvent.Action#remove}.
     * <p>
     * @param index the index  at wich a specified element to be removed
     * @return the removed element
     * {@inheritDoc }
     * @see java.util.List#remove(int) 
     */
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
    
    /**
     * Creates and returns an object of type <code>ListChangeEvent</code>.
     * The created object is used by the method {@link #remove(int)  }
     * to fire the event of type {@link ListChangeEvent }.
     * The following properties of the event are set:
     * <ul>
     *   <li><code>action</code> - {@link ListChangeEvent.Action#remove. }</li>
     *   <li><i><code>index</code></i> - a value of the parameter <b>index</b></li>
     *   <li><i><code>element</code></i> - a value of the parameter <b>result</b></li>
     *   <li><i><code>result</code></i> - a value of the parameter <code>result</code></li>
     * </ul>
     * @param index the value that is passed by the remove() method.
     * @param result the value that is passed by the remove() method.
     * @return the object of type <code>ListChangeEvent</code>
     */
    protected ListChangeEvent createRemove(int index,E result) {
        ListChangeEvent event = new ListChangeEvent(this,ListChangeEvent.Action.remove);
        event.setIndex(index);
        event.setElement(result);
        event.setResult(result);
        return event;
    }
    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * 
     * @param o an element to search for
     * @return the position of the specified object in the list or -1 if the 
     *      specified object doesn't exists 
     * @see java.util.List#indexOf(java.lang.Object) 
     */
    @Override
    public int indexOf(Object o) {
        return this.baseList.indexOf(o);
    }
    /**
     * Returns the index of the last occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * 
     * @param o an element to search for
     * @return the position of the specified object in the list or -1 if the 
     *      specified object doesn't exists 
     * @see java.util.List#lastIndexOf(java.lang.Object) 
     */
    @Override
    public int lastIndexOf(Object o) {
        return this.baseList.lastIndexOf(o);
    }
    /**
     * @see java.util.List#listIterator()
     */
    @Override
    public ListIterator<E> listIterator() {
        return this.baseList.listIterator();
    }
    /**
     * @see java.util.List#listIterator(int) 
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        return this.baseList.listIterator(index);
    }
    /**
     * @see java.util.List#subList(int, int) 
     */
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
    /**
     * 
     * @return <code>true</code> if the list fires events 
     *  when the list changes. <code>false</code>otherwise
     * @see #setObservable(boolean) 
     */
    public boolean isObservable() {
        return observable;
    }
    /**
     * Assigns a boolean value that indicates whether this list 
     * fires events when operations that change the list are called.
     * 
     * @param observable <code>true</code> the list fires events, 
     *    <code>false</code> otherwise
     * @see #isObservable() 
     */
    public void setObservable(boolean observable) {
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
