/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.util.HashMap;

/**
 *
 * @author Valery
 */
public class SimpleObjectDocument implements HasDocument {
    
    private MapBasedDocument document;
    
    @Override
    public Document getDocument() {
        if ( document== null ) {
            this.document = new MapBasedDocument(new HashMap());
        }
        return document;
    }
    //
    // --------------------------------------------
    //
    private String firstName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        getDocument().put("firstName", firstName);
        this.firstName = firstName;
    }
    
    
}
