/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public class MockBinderRegistry implements BinderRegistry{
    protected Document document;
    public MockBinderRegistry() {
        document = new MockDocument();
    }
    
    @Override
    public void add(Binder binder) {
        
    }

    @Override
    public void remove(Binder binder) {
        
    }

    @Override
    public Document getDocument() {
        return document;
    }

    @Override
    public void setDocument(Document document) {
        this.document = document;
    }

    @Override
    public ValidatorCollection getValidators() {
        return new ValidatorCollection();
    }

    @Override
    public void validate(String propPath, Object value) throws ValidationException {
        
    }

    @Override
    public void validate() throws ValidationException {
        
    }

    @Override
    public void notifyError(Binder source, Exception e) {
        
    }

    @Override
    public void firePropertyChange(String propPath, Object oldValue, Object newValue) {
        
    }

    @Override
    public BinderRegistry createChild(String propertyName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getChildName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
