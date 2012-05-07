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
public class CommonBinderSet<T extends Binder> implements BinderSet<T>{
    
    private AbstractBindingManagerNew bindingManager;
    
    private Document selected;
    
    private Set<T> binders;
    
    public CommonBinderSet(AbstractBindingManagerNew bindingManager) {
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
    public BinderSet getSubset(Object... subsetId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDocument(Document document) {
        
        Document oldSelected = this.selected;
        this.selected = document;
        
        for ( T b : binders) {
            DocumentChangeEvent e = new DocumentChangeEvent(this, DocumentChangeEvent.Action.documentChange);
            e.setNewValue(document);
            e.setOldValue(oldSelected);
            e.setOldAlias(bindingManager.getAlias(oldSelected));
            e.setNewAlias(bindingManager.getAlias(document));
            b.react(e);
        }
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
