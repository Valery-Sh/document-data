package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface ErrorBinder extends Binder {
    //void clear();
    void notifyError(Binder source, Exception e);
}
