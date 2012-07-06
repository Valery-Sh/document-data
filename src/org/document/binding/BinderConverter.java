package org.document.binding;



/**
 *  
 * @author V. Shyshkin
 */
public interface BinderConverter<P,C> {
    P propertyValueOf(C boundObjectValue );
    C componentValueOf(P propertyValue);
    
}
