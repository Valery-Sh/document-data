package org.document.binding;

import org.document.Document;

/**
 *
 * @author V. Shyshkin
 */
public interface BinderConverter<P,C> {
    P propertyValueOf(C componentValue );
    C componentValueOf(P propertyValue);
    
}