package org.document.binding;

import org.document.PropertyBinder;

/**
 *
 * @author V. Shyshkin
 */
public interface ErrorBinder extends PropertyBinder {
    void notifyError(Exception e);
    
}
