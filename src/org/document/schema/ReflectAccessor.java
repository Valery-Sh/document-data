/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.schema;

import java.lang.reflect.Method;

/**
 *
 * @author Valery
 */
public class ReflectAccessor implements FieldAccessors{
    private Method getAccessor;
    private Method setAccessor;
    
    public ReflectAccessor(Method getMethod, Method setMethod) {
        this.getAccessor = getMethod;
        this.setAccessor = setMethod;
    }
    @Override
    public Object get(Object target) throws Exception {
       return  getAccessor.invoke(target, null);
    }

    @Override
    public void set(Object target,Object value) throws Exception  {
        setAccessor.invoke(target, value);
    }

    @Override
    public boolean hasGettter() {
        return getAccessor != null;
    }

    @Override
    public boolean hasSetter() {
        return setAccessor != null;
    }

    public Method getGetAccessor() {
        return getAccessor;
    }

    public void setGetAccessor(Method getAccessor) {
        this.getAccessor = getAccessor;
    }

    public Method getSetAccessor() {
        return setAccessor;
    }

    public void setSetAccessor(Method setAccessor) {
        this.setAccessor = setAccessor;
    }

    
}
