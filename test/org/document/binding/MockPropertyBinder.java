/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import org.document.Document;
import org.document.DocumentChangeEvent;

/**
 *
 * @author Valery
 */
public class MockPropertyBinder implements PropertyBinder{
    
    protected Document document;
    protected String propertyName;
    protected DocumentChangeEvent.Action reactAction;
    public MockPropertyBinder(String propertyName) {
        this.propertyName = propertyName;
    }
    @Override
    public Document getDocument() {
        return this.document;
    }

    //@Override
    public BinderConverter getConverter() {
        return null;
    }

    //@Override
    public void setConverter(BinderConverter converter) {
        
    }

    @Override
    public String getPropertyName() {
        return this.propertyName;
    }

    @Override
    public void propertyChanged(Object propertyValue) {
    }

    @Override
    public void initComponentDefault() {
    }

    @Override
    public Object getComponentValue() {
        return null;
    }

    @Override
    public void addBinderListener(BinderListener l) {
    }

    @Override
    public void removeBinderListener(BinderListener l) {
    }

//    @Override
    public void react(DocumentChangeEvent event) {
        this.reactAction = event.getAction();
    }

}
