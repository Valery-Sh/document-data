/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import org.document.DataUtils;
import org.document.Document;
import org.document.schema.DocumentSchema;
import org.document.schema.Field;
import org.document.schema.HasSchema;

/**
 *
 * @author Valery
 */
public class DefaultBinderConvertor<P,C> implements BinderConverter<P,C>  {

    protected PropertyBinder binder;
    
    public DefaultBinderConvertor(PropertyBinder binder) {
        this.binder = binder;
        
    }
    protected Document getDocument() {
        return binder.getDocument();
    }

    @Override
    public P propertyValueOf(C componentValue) {
        Object result = null;
        String s = componentValue == null ? "" : componentValue.toString();
        Class propertyType;
        if ( getDocument().propertyStore() instanceof HasSchema) {
            DocumentSchema sc = ((HasSchema)getDocument().propertyStore()).getSchema();
            propertyType = sc.getField(binder.getPropertyName()).getPropertyType();
        } else {
            Object o = getDocument().propertyStore().get(binder.getPropertyName());
            propertyType = o == null ? Object.class : o.getClass();
        }
        if ( propertyType.isPrimitive() ) {
            propertyType = DataUtils.getWrapper(propertyType);
        }
        if ( propertyType.equals(Object.class) || propertyType.equals(String.class)) {
            result = s;
        } else if ( Number.class.isAssignableFrom(Integer.class)) {
            result = NumberUtil.toNumber(s, propertyType);
        }
        return (P)result;
    }
    
    @Override
    public C componentValueOf(P propertyValue) {
        return (C)propertyValue;
    }
    
}
