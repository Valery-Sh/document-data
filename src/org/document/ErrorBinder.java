package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface ErrorBinder extends Binder {
    //void clear();
    //void notifyPropertyError(Binder source, Exception e);
    void notifyPropertyError(Exception e);
    void notifyDocumentError(Exception e);
}
