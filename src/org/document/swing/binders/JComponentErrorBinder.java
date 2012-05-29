/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.swing.binders;

import javax.swing.JComponent;
import org.document.ValidationException;
import org.document.binding.ErrorBinder;

/**
 *
 * @author V. Shyshkin
 */
public abstract class JComponentErrorBinder implements ErrorBinder {
    
    private JComponent component;
    
    public JComponentErrorBinder(JComponent component)  {
        this.component = component;
    }
    @Override
    public void notifyError(ValidationException e) {
        notifyError("*document", e);
    }
    protected String getMessage(String propertyName,ValidationException e) {
        String msg = "";
        if ( e.getMessage() == null ||  e.getMessage().trim().isEmpty()) {
            if ( "*document".equals(propertyName) ) {
                msg = "Document level error";
            } else {
                msg = "Property='" + propertyName + "'";
            }
        }
        msg += e.getMessage();
        
        return msg;
    }

    public JComponent getComponent() {
        return component;
    }

    public void setComponent(JComponent component) {
        this.component = component;
    }
    
    protected abstract void setMessage(String message);
    
    @Override
    public void notifyError(String propertyName,ValidationException e) {
        String pName = propertyName;
        if ( propertyName == null ) {
            pName = "*document";
        }
        setMessage(getMessage(pName,e));
        getComponent().setEnabled(true);
        getComponent().setVisible(true);
    }

    public void notifyFixed() {
        notifyFixed("*document");
    }
    @Override
    public void notifyFixed(String propertyName) {
        clear(propertyName);
    }
    

    public void clear() {
        clear("*document");
    }    
    
    @Override
    public void clear(String propertyName) {
        
        setMessage("");
        getComponent().setEnabled(false);
        getComponent().setVisible(false);
        
    }    
    
}
