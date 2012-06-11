package org.document.binding;

/**
 * Represents a container for binders.
 * 
 * @author V. Shyshkin
 */
public interface ContainerBinder extends Binder {
    String getAlias();
    void setAlias(String alias);
    
    String getClassName();
    void setClassName(String className);
    
    boolean add(Binder binder);
    boolean remove(Binder binder);
    boolean contains(Binder binder);
    
    
}
