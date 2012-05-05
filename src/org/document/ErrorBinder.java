package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface ErrorBinder extends Binder {
    void notifyError(Exception e);
    
}
