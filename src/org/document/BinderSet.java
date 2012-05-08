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

    public Set<T> getSubset(Object alias) {
        Set<T> subset = new HashSet<T>();
        for ( T b : binders) {
            if ( b instanceof HasDocumentAlias ) {
                Object a = ((HasDocumentAlias)b).getAlias();
                if ( a == alias ) {
                    subset.add(b);
                } else if ( a != null && a.equals(alias)) {
                    subset.add(b);
                } else if ( alias != null && alias.equals(a)) {
                    subset.add(b);
                }
            }
        }
        return subset;
    }

    @Override
    public void setDocument(Document newDocument) {
        
        Document oldSelected = this.selected;
        this.selected = newDocument;
        
        for ( T b : binders) {
            DocumentChangeEvent e = new DocumentChangeEvent(this, DocumentChangeEvent.Action.documentChange);
            //Object alias = bindingManager.getAlias(oldSelected);
            e.setOldValue(oldSelected);
            //alias = bindingManager.getAlias(selected);
            //e.setNewValue(bindingManager.getDocumentBinder(alias));
            e.setNewValue(newDocument);
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
