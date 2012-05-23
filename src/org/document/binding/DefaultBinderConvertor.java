/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import org.document.DataUtils;
import org.document.Document;
import org.document.schema.DocumentSchema;
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
        
        Class propertyType = definePropertyType();
        
        boolean isPrimitive = propertyType.isPrimitive();
        Class wrapper = null; 
        if ( propertyType.isPrimitive() ) {
             wrapper = DataUtils.getWrapper(propertyType);
        }

        if ( propertyType.equals(Object.class) || propertyType.equals(String.class)) {
            result = s;
        } else if ( wrapper != null && Number.class.isAssignableFrom(wrapper)) {
            result = ConvertUtil.toNumber(s, propertyType);
        }  else if ( Number.class.isAssignableFrom(propertyType)) {
            result = ConvertUtil.toNumber(s, propertyType);
        } else if ( propertyType.equals(java.util.Date.class) ||
                    propertyType.equals(java.sql.Date.class) ||
                    propertyType.equals(java.util.Calendar.class) ) {
            result = ConvertUtil.toDate(s, propertyType);
        }
        return (P)result;
    }
    
    @Override
    public C componentValueOf(P propertyValue) {
        Object result = null;
        String s = propertyValue == null ? "" : propertyValue.toString();
        Class propertyType = definePropertyType();        
        
        if ( propertyType.isPrimitive() ) {
             propertyType = DataUtils.getWrapper(propertyType);
        }
        
        if ( propertyType.equals(Object.class) || propertyType.equals(String.class)) {
            result = s;
        } else if ( Number.class.isAssignableFrom(propertyType)) {
            result = ConvertUtil.stringOf((Number)propertyValue);
        } else if ( propertyType.equals(java.util.Date.class) ) {
            result = ConvertUtil.stringOf((java.util.Date)propertyValue);
        } else if ( propertyType.equals(java.sql.Date.class) ) {
            result = ConvertUtil.stringOf((java.sql.Date)propertyValue);
        } else if ( propertyType.equals(java.util.Calendar.class) ) {
            result = ConvertUtil.stringOf((java.util.Calendar)propertyValue);
        }
        return (C)result;
    }
    
    protected Class definePropertyType() {
        Class propertyType;
        if ( getDocument().propertyStore() instanceof HasSchema) {
            DocumentSchema sc = ((HasSchema)getDocument().propertyStore()).getSchema();
            propertyType = sc.getField(binder.getPropertyName()).getPropertyType();
        } else {
            Object o = getDocument().propertyStore().get(binder.getPropertyName());
            propertyType = o == null ? Object.class : o.getClass();
        }
        return propertyType;
    }
}
