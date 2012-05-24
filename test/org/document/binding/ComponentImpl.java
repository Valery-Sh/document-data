package org.document.binding;

import java.beans.*;
import java.io.Serializable;

/**
 *
 * @author V. Shyshkin
 */
public class ComponentImpl implements Serializable {
    
    private String text;
    private PropertyChangeSupport propertySupport;
    
    public ComponentImpl() {
        propertySupport = new PropertyChangeSupport(this);
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String value) {
        String oldValue = text;
        text = value;
        propertySupport.firePropertyChange("text", oldValue, text);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
}
