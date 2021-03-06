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
public class ErrorAccumulatorBinder extends AbstractErrorBinder{
    
    private Set<String> propertySet;
    private Map<Document,ValidationException> documentExceptions;
    private Map<String,Map<Document,ValidationException>> exceptions;
    
    /**
     * Indicates that all properties will use the binder
     */
    protected boolean allProperties;
    

    
    public ErrorAccumulatorBinder() {
        super(null);
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
    public void errorFound(ValidationException e) {
        errorFound("*document", e);
    }
    @Override
    public void errorFound(String propertyName,ValidationException e) {
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

    public void notifyFixed(Document document) {
        errorFixed("*document",document);
    }
    
    @Override
    public void errorFixed(String propertyName,Document document) {
        if ( document == null ) {
            return;
        }
        String pName = propertyName;
        if ( propertyName == null ) {
            pName = "*document";
        }
        Map<Document,ValidationException> m = exceptions.get(pName); 
        if ( m == null) {
            m = new HashMap<Document,ValidationException>();
        }        
        m.remove(document);
        
    }

    @Override
    public void initDefaults() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
