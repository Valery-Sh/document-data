/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public abstract class AbstractErrorBinder extends AbstractBinder implements ErrorBinder {
    
    protected Binder source;
    protected Exception exception;
    protected boolean errorFound;
    
    @Override
    public void notifyError(Binder source, Exception e) {
        this.source = source;
        this.exception = e;
        if ( e == null ) {
            errorFound = false;
        } else {
            errorFound = true;
        }
    }
    
    @Override
    public void componentChanged(Object oldValue, Object newValue) {
    }

    @Override
    public Object getDataValue() {
        return errorFound;
/*        if ( exception == null ) {
            return false;
        }
        return true;
*/
    }

    /**
     * return current component value
     */
    @Override
    public abstract Object getComponentValue();

    @Override
    protected void setDataValue(Object dataValue) {
        this.errorFound = (Boolean)dataValue;
    }

}
