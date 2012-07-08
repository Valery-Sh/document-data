/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.samples;

import java.io.Serializable;
import org.document.BoundPropertyListener;

/**
 *
 * @author Valery
 */
public class Address {
    private BoundPropertyListener boundPropertyListener;    
    private String country;
    private String state;
    
    public Address() {
        super();
    }
    
    
    public void addBoundPropertyChangeListener(BoundPropertyListener l) {
        boundPropertyListener = l;
    }
    private void bind(String name, Object value) {
        if ( boundPropertyListener != null ) {
            boundPropertyListener.bind(name, value);
        }
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        bind("country", country);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
        bind("state", state);
    }


}
