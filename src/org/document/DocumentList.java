
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
    }      
    
    public DocumentList() {
        this.baseList = new ArrayList();
    }      
    public DocumentList(int capacity) {
        this.baseList = new ArrayList(capacity);
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
        return this.baseList.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return this.baseList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.baseList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return this.baseList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return this.baseList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.baseList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.baseList.retainAll(c);
    }

    @Override
    public void clear() {
        this.baseList.clear();
    }

    @Override
    public E get(int index) {
        return this.baseList.get(index);
    }

    @Override
    public E set(int index, E element) {
        return this.baseList.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        this.baseList.add(index, element);
    }

    @Override
    public E remove(int index) {
        return this.baseList.remove(index);
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




}
