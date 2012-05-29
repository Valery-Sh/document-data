/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public class ValidationException extends RuntimeException {

    private String propertyName;
    private Object propertyValue;
    private Object componentValue;
    private Document document;

    /*    public ValidationException(String message) {
     super(message);
     }
     */
    public ValidationException(String propertyName, String message, Document document) {
        super(message);
        this.propertyName = propertyName;
    }

    public ValidationException(String message, Document document) {
        this(null, message, document);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Object propertyValue) {
        this.propertyValue = propertyValue;
    }

    public Object getComponentValue() {
        return componentValue;
    }

    public void setComponentValue(Object componentValue) {
        this.componentValue = componentValue;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
    
}
