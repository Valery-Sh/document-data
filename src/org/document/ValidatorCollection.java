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
    
    public void validate(String propPath, Document doc, Object value) throws ValidationException {
        PropertyValidator v = pvalidators.get(propPath);
        if ( v == null ) {
            return;
        }
        v.validate(propPath,doc,value);
    }
    public void validate(Document doc) throws ValidationException {
        for ( DocumentValidator v : lvalidators) {
            v.validate(doc);            
        }
    }
    
    
}
