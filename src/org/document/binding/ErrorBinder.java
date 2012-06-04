package org.document.binding;

import org.document.Document;
import org.document.ValidationException;

/**
 *
 * @author V. Shyshkin
 */
public interface ErrorBinder {
     void setError(ValidationException e); // TO BE REMOVED
     void setError(String propertyName,ValidationException e);
     void setFixed(String propertyName,Document document);
}
