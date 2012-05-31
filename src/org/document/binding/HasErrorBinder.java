package org.document.binding;

/**
 *
 * @author V. Shyshkin
 */
public interface HasErrorBinder {
    String getPropertyName();
    ErrorBinder getErrorBinder();
}
