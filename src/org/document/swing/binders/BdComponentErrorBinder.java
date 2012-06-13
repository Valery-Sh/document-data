package org.document.swing.binders;

import javax.swing.JComponent;
import org.document.Document;
import org.document.ValidationException;
import org.document.binding.AbstractErrorBinder;

/**
 *
 * @author V. Shyshkin
 */
public  abstract class BdComponentErrorBinder extends AbstractErrorBinder {
    
    
    public BdComponentErrorBinder(JComponent component)  {
        super(component);
    }
    @Override
    public void errorFound(ValidationException e) {
        errorFound("*document", e);
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
        return (JComponent) getBoundObject();
    }

    
    protected abstract void setMessage(String message);
    
    @Override
    public void errorFound(String propertyName,ValidationException e) {
        String pName = propertyName;
        if ( propertyName == null ) {
            pName = "*document";
        }
        setMessage(getMessage(pName,e));
        getComponent().setEnabled(true);
        getComponent().setVisible(true);
    }

    public void notifyFixed(Document document) {
        errorFixed("*document",document);
    }
    @Override
    public void errorFixed(String propertyName,Document document) {
        setMessage("");
        getComponent().setEnabled(false);
        getComponent().setVisible(false);
    }
}
