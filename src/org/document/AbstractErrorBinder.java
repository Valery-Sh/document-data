package org.document;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractErrorBinder extends AbstractBinder implements ErrorBinder {
    
    //protected Binder source;
    protected Exception exception;
    protected boolean errorFound;
    
    @Override
    public void notifyPropertyError(Exception e) {
        //this.source = source;
        this.exception = e;
        if ( e == null ) {
            errorFound = false;
        } else {
            errorFound = true;
        }
    }
    @Override
    public void notifyDocumentError(Exception e) {
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

}
