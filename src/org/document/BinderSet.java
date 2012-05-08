/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Valery
 */
public class BinderSet<T extends Binder> implements BinderCollection<T>{
    
    private AbstractBindingManager bindingManager;
    
    private Document selected;
    
    private Set<T> binders;
    
    public BinderSet(AbstractBindingManager bindingManager) {
        this.binders = new HashSet<T>();
        this.bindingManager = bindingManager;
    }
    
    @Override
    public void add(T binder) {
        binders.add(binder);
    }

    @Override
    public void remove(T binder) {
        binders.remove(binder);
    }

    @Override
    public BinderCollection getSubset(Object... subsetId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDocument(Document newDocument) {
        
        Document oldSelected = this.selected;
        this.selected = newDocument;
        
        for ( T b : binders) {
            DocumentChangeEvent e = new DocumentChangeEvent(this, DocumentChangeEvent.Action.documentChange);
            e.setOldValue(bindingManager.getBinding(oldSelected));
            e.setNewValue(bindingManager.getBinding(newDocument));
            b.react(e);
        }
    }
    
    @Override
    public Document getDocument() {
        return this.selected;
    }
    
    @Override
    public void addDocumentChangeListener(DocumentChangeListener l) {
    }

    @Override
    public void removeDocumentChangeListener(DocumentChangeListener l) {
    }

    @Override
    public void react(DocumentChangeEvent event) {
    }
    
}
