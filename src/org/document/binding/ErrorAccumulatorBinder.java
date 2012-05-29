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
 * @author V. Shyshkin
 */
public class ErrorAccumulatorBinder implements ErrorBinder{
    
    private Set<String> propertySet;
    private Map<Document,ValidationException> documentExceptions;
    private Map<String,Map<Document,ValidationException>> exceptions;
    
    /**
     * Indicates that all properties will use the binder
     */
    protected boolean allProperties;
    

    
    public ErrorAccumulatorBinder() {
        
        this.allProperties = true;
        propertySet = new HashSet<String>();
        exceptions = new HashMap<String,Map<Document,ValidationException>>();
    }

    public ErrorAccumulatorBinder(String... propertyNames) {
        this();
        propertySet.addAll(Arrays.asList(propertyNames));
    }   
    
    
    public final Set<String> getPropertySet() {
        return propertySet;
    }
    /**
     * Document error found.
     * @param e 
     */
    @Override
    public void notifyError(ValidationException e) {
        notifyError("*document", e);
    }
    @Override
    public void notifyError(String propertyName,ValidationException e) {
        String pName = propertyName;
        if ( propertyName == null ) {
            pName = "*document";
        }
        Map<Document,ValidationException> m = exceptions.get(pName); 
        if ( m == null) {
            m = new HashMap<Document,ValidationException>();
        }        
        exceptions.put(pName, m);
        m.put(e.getDocument(), e);
    }

    public void notifyFixed() {
        notifyFixed("*document", null);
    }
    @Override
    public void notifyFixed(String propertyName,ValidationException e) {
        String pName = propertyName;
        if ( propertyName == null ) {
            pName = "*document";
        }
        Map<Document,ValidationException> m = exceptions.get(pName); 
        if ( m == null) {
            m = new HashMap<Document,ValidationException>();
        }        
        exceptions.put(pName, m);
        m.put(e.getDocument(), e);
    }
    @Override
    public void clear(String propertyName) {
        String pName = propertyName;
        if ( propertyName == null ) {
            pName = "*document";
        }
        List<ErrorBinder> ebinders = binders.get(pName); 

        if ( ebinders == null || ebinders.isEmpty()) {
            return;
        }        
        for ( ErrorBinder b : ebinders) {
            b.clear(propertyName);
        }
    }    
    protected void add(String propertyName,ErrorBinder binder) {
        String pName = propertyName;
        if ( propertyName == null ) {
            pName = "*document";
        }
        List<ErrorBinder> ebinders = binders.get(pName); 
        if ( ebinders == null ) {
            ebinders = new ArrayList<ErrorBinder>();
            binders.put(propertyName, ebinders);
        }
        if ( ebinders.contains(binder)) {
            throw new IllegalArgumentException("The same ErrorBinder for propertyName='"+propertyName+"' already exists.");
        }
        ebinders.add(binder);
    }
    protected void add(ErrorBinder binder) {
        this.add("*document",binder);
    }

    public void clear() {
        clear("*document");
    }    
    
}
