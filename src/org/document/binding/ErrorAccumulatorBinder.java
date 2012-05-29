/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.document.Document;
import org.document.ValidationException;

/**
 *
 * @author Valery
 */
public class ErrorAccumulatorBinder extends AbstractPropertyBinder{
    
    private Set<String> propertySet;
    private Map<Document,ValidationException> documentExceptions;
    private Map<String,Map<Document,ValidationException>> propertyExceptions;
    
    /**
     * Indicates that all properties will use the binder
     */
    protected boolean allProperties;
    

    
    public ErrorAccumulatorBinder() {
        propertyName = "*";
        this.allProperties = true;
        propertySet = new HashSet<String>();
        propertyExceptions = new HashMap<String,Map<Document,ValidationException>>();
    }

    public ErrorAccumulatorBinder(String... propertyNames) {
        this();
        propertySet.addAll(Arrays.asList(propertyNames));
    }   
    
    
    public final Set<String> getPropertySet() {
        return propertySet;
    }

    @Override
    public String getPropertyName() {
        return "*";
    }
    
    
    @Override
    public Object getComponentValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void setComponentValue(Object compValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Object componentValueOf(Object dataValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Object propertyValueOf(Object compValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void initComponentDefault() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
