package org.document.binding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.document.Document;
import org.document.ValidationException;

/**
 *
 * @author V. Valery
 */
public class DocumentErrorBinder implements ErrorBinder{
    
    private Document document;
    private Map<String,List<ErrorBinder>> binders;
    
    public DocumentErrorBinder() {
        binders = new HashMap<String,List<ErrorBinder>>();
    }

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
        List<ErrorBinder> ebinders = binders.get(pName); 
        if ( ebinders == null || ebinders.isEmpty()) {
            return;
        }        
        for ( ErrorBinder b : ebinders) {
            b.notifyError(propertyName, e);
        }
    }

    public void notifyFixed() {
        notifyFixed("*document");
    }
    @Override
    public void notifyFixed(String propertyName) {
        String pName = propertyName;
        if ( propertyName == null ) {
            pName = "*document";
        }
        List<ErrorBinder> ebinders = binders.get(pName); 

        if ( ebinders == null || ebinders.isEmpty()) {
            return;
        }        
        for ( ErrorBinder b : ebinders) {
            b.notifyFixed(propertyName);
        }
        
    }
    
    public void add(String propertyName,ErrorBinder binder) {
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
    public void add(ErrorBinder binder) {
        this.add("*document",binder);
    }

    public void clear() {
        clear("*document");
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
    
    public void remove(String propertyName) {
        binders.remove(propertyName);
    }    

    public void remove(String propertyName,ErrorBinder binder) {
        String pName = propertyName;
        if ( propertyName == null ) {
            pName = "*document";
        }
        List<ErrorBinder> ebinders = binders.get(pName); 

        if ( ebinders != null ) {
            ebinders.remove(binder);
        }
        
    }    
    /**
     * Returns a current document object.
     * @return an object of type {@link org.document.Document}
     */
    public Document getDocument() {
        return document;
    }
    /**
     * Sets a given document.
     * @param document a document to be set
     */
    public void setDocument(Document document) {
        this.document = document;
    }
    
}
