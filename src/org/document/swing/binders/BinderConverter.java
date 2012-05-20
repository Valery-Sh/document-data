package org.document.swing.binders;

/**
 *
 * @author V. Shyshkin
 */
public interface BinderConverter<P,C> {
    P propertyValueOf(C componentValue);
    C componentValueOf(P propertyValue);
}
