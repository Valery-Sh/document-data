package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface ErrorBinder extends PropertyBinder {
    void notifyError(Exception e);
    
}
