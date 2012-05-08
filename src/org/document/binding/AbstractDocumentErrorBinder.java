package org.document.binding;

import org.document.DocumentChangeEvent;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractDocumentErrorBinder extends AbstractBinder implements DocumentErrorBinder {
    
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
    
    @Override
    public void componentChanged(Object newValue) {
    }

    public boolean isErrorFound() {
        return errorFound;
    }

    /**
     * return current component value
     */
    @Override
    public abstract Object getComponentValue();

    protected void setErrorFound(boolean value) {
        this.errorFound = value;
    }
    @Override
    public void react(DocumentChangeEvent event) {
        switch(event.getAction()) {
            case documentChange :
                this.notifyError(null);
                break;
            case documentError :
                if ( event.getPropertyName() != null ) {
                    break;
                }
                notifyError(event.getException());
                break;
        }//switch
    }

}