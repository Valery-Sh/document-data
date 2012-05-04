/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Valery
 */
public class ValidatorCollection {
    private Map<String,PropertyValidator> pvalidators;
    private List<DocumentValidator> lvalidators;
    public ValidatorCollection() {
      pvalidators = new HashMap<String,PropertyValidator>();  
      lvalidators = new ArrayList<DocumentValidator>();  
    }
    public int add(DocumentValidator validator) {
        this.lvalidators.add(validator);
        return lvalidators.size()-1;
    } 
    public  Map<String,PropertyValidator> getProperyValidators() {
        return pvalidators;
    }
    public  List<DocumentValidator> getDocumentValidators() {
        return lvalidators;
    }
    
    public void remove(DocumentValidator validator) {
        this.lvalidators.remove(validator);
    } 
    public void remove(int vindex) {
        this.lvalidators.remove(vindex);
    } 
    public void put(String propPath,PropertyValidator validator) {
        this.pvalidators.put(propPath,validator);
    } 
    public void remove(String propPath) {
        this.pvalidators.remove(propPath);
    } 
    
    public void validate(String propPath, DocumentStore doc, Object value) throws ValidationException {
        PropertyValidator v = pvalidators.get(propPath);
        if ( v == null ) {
            return;
        }
        v.validate(propPath,doc,value);
    }
    public void validateProperties(DocumentStore doc) throws ValidationException {
        String propPath;
        for ( Map.Entry<String,PropertyValidator> es  : pvalidators.entrySet()) {
            propPath = es.getKey();
        
            PropertyValidator v = pvalidators.get(propPath);
            if ( v == null ) {
                continue;
            }
            v.validate(propPath,doc,doc.get(propPath));
        }
    }
    
    public void validate(DocumentStore store) throws ValidationException {
        for ( DocumentValidator v : lvalidators) {
            v.validate(store);            
        }
    }
    public void validate(Document document) throws ValidationException {
        for ( DocumentValidator v : lvalidators) {
            v.validate(document);            
        }
    }
    
    
}
