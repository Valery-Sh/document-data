package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface PropertyBinder extends Binder,DocumentChangeListener{
    String getPropertyName();
    void init(Object dataValue);
}
