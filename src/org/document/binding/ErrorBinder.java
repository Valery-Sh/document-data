package org.document.binding;

import org.document.Document;
import org.document.ValidationException;

/**
 *
 * @author V. Shyshkin
 */
public interface ErrorBinder {
     void notifyError(ValidationException e); // TO BE REMOVED
     void notifyError(String propertyName,ValidationException e);
     void notifyFixed(String propertyName,Document document);
}
