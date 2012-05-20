package org.document.binding;

import org.document.DocumentChangeEvent;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractPropertyErrorBinder extends AbstractPropertyBinder implements PropertyErrorBinder {
    
    protected Exception exception;
    protected boolean errorFound;
    
    @Override
    public void notifyError(Exception e) {
        //this.source = source;
        this.exception = e;
        if ( e == null ) {
            errorFound = false;
        } else {
            errorFound = true;
        }
    }
    

    public boolean isErrorFound() {
        return errorFound;
    }

    /**
     * return current component value
     */
    @Override
    public abstract Object getComponentValue();
    @Override
    public abstract void initComponentDefault();

    protected void setErrorFound(boolean value) {
        this.errorFound = value;
    }
    @Override
    public void react(DocumentChangeEvent event) {
        switch(event.getAction()) {
            case documentChange :
                this.notifyError(null);
                break;
            case propertyError :
                if ( event.getPropertyName() == null || ! event.getPropertyName().equals(getPropertyName())) {
                    break;
                }
                notifyError(event.getException());
                break;
        }//switch
    }

}
