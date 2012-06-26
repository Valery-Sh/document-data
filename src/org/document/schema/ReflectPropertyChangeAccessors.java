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
public class ReflectPropertyChangeAccessors implements PropertyChangeAccessors{
    private Method addPropertyChangeListener;
    private Method addBoundPropertyChangeListener;

    @Override
    public boolean hasAddPropertyChangeListener() {
        return addPropertyChangeListener != null;
    }

    public Method getAddPropertyChangeListener() {
        return addPropertyChangeListener;
    }

    public void setAddPropertyChangeListener(Method addPropertyChangeListener) {
        this.addPropertyChangeListener = addPropertyChangeListener;
    }

    public Method getAddBoundPropertyChangeListener() {
        return addBoundPropertyChangeListener;
    }

    public void setAddBoundPropertyChangeListener(Method addBoundPropertyChangeListener) {
        this.addBoundPropertyChangeListener = addBoundPropertyChangeListener;
    }

    @Override
    public boolean hasAddBoundPropertyChangeListener() {
        return addBoundPropertyChangeListener != null;
    }


    @Override
    public void addPropertyChangeListener(Object target, Object value) throws Exception {
        addPropertyChangeListener.invoke(target, value);
    }

    @Override
    public void addBoundPropertyChangeListener(Object target, Object value) throws Exception {
         addBoundPropertyChangeListener.invoke(target, value);
    }
    
}
