package org.document.binding;

/**
 * Represents a container for binders.
 * 
 * @author V. Shyshkin
 */
public interface ContainerBinder<E extends Binder,HasAlias> extends Binder, Iterable<E>  {
    String getAlias();
    void setAlias(String alias);
    
    String getClassName();
    void setClassName(String className);
    
    boolean add(E binder);
    boolean remove(E binder);
    boolean contains(E binder);
    
    
}
