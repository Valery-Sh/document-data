package org.document;

/**
 *
 * @author V. Shyshkin
 */
public interface PropertyBinder extends Binder,DocumentListener{
    String getPropertyName();
    void init(Object dataValue);
}
