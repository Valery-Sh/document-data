package org.document.binding;

import org.document.Document;
import org.document.ValidationException;

/**
 * The root interface for all  error binders.
 * This is a special type of <code><i>binder</i></code> concept.
 * The class does not extend the Binder interface, because it has 
 * a particular behavior.
 * 
 * @author V. Shyshkin
 */
public interface ErrorBinder {
     /**
      * The method should be called when a document level error
      * found.
      * @param e the object of type {@link ValidationException}
      */
     void errorFound(ValidationException e); // TO BE REMOVED
     /**
      * The method should be called when an error
      * detected for the property with a specified name.
      * @param propertyName the property name for which an error detected.
      * @param e the object of type {@link ValidationException}
      */
     void errorFound(String propertyName,ValidationException e);
     /**
      * The method instructs the binder that it should change it's  state
      * since the error has been fixed.
      * @param propertyName the property name 
      * @param document the object of type {@link org.document.Document}
      */
     void errorFixed(String propertyName,Document document);
}
