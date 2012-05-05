package org.document;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractPropertyErrorBinder extends AbstractBinder implements PropertyErrorBinder {
    
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
    public void react(DocumentEvent event) {
        switch(event.getAction()) {
            case documentChange :
                this.notifyError(null);
                break;
            case propertyErrorNotify :
                if ( event.getPropertyName() == null || ! event.getPropertyName().equals(getPropertyName())) {
                    break;
                }
                notifyError(event.getException());
                break;
        }//switch
    }

}
