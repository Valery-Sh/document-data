/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import org.document.DocumentChangeEvent;

/**
 *
 * @author Valery
 */
public class AbstractListSelectionBinder implements PropertyBinder {

    @Override
    public String getPropertyName() {
        return "selected";
    }

    @Override
    public void initComponent(Object dataValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void initComponentDefault() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getComponentValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addBinderListener(BinderListener l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeBinderListener(BinderListener l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void react(DocumentChangeEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getAlias() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
