package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface PropertyChangeHandler {
    void firePropertyChange(String propPath, Object oldValue,Object newValue);    
}
