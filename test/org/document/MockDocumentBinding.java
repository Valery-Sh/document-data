/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public class MockDocumentBinding implements DocumentBindings{
    protected PropertyStore document;
    public MockDocumentBinding() {
        document = new MockDocument();
    }
    
    @Override
    public void add(PropertyBinder binder) {
        
    }

    @Override
    public void remove(PropertyBinder binder) {
        
    }

    @Override
    public PropertyStore getDocument() {
        return document;
    }

    @Override
    public void setDocument(PropertyStore document, boolean completeChanges) {
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
    public void notifyError(PropertyBinder source, Exception e) {
        
    }
    @Override
    public void notifyPropertyError(String propertyName, Exception e) {
        
    }


    @Override
    public DocumentBindings createChild(String propertyName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getChildName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void completeChanges() {
        
    }

    @Override
    public void react(DocumentChangeEvent event) {
    }

    @Override
    public void setDocument(Document hasDocument, boolean completeChanges) {
        this.document = hasDocument.getPropertyStore();
    }
    
}
