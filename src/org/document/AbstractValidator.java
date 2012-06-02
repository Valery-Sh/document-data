/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.util.Map;

/**
 *
 * @author Valery
 */
public class AbstractValidator implements Validator{
    
    //private Object alias;
    private Map parameters;
    
/*    public AbstractValidator(Object alias) {
        this.alias = alias;
    }
*/
    public AbstractValidator() {
    }
    
    public AbstractValidator(Map params) {
        //this(alias);
        this.parameters = params;
    }

    @Override
    public void validate(Document document) {
    }

    @Override
    public void validate(Object key, Object value, Document document) {
    }
    
}
