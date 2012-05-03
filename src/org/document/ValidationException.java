/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public class ValidationException extends RuntimeException{
    
    private String propertyName;
    
/*    public ValidationException(String message) {
        super(message);
    }
*/
    public ValidationException(String propertyName,String message) {
        super(message);
        this.propertyName = propertyName;
    }
    public ValidationException(String message) {
        this(null,message);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
    
}
