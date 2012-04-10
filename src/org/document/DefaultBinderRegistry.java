/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Valery
 */
public class DefaultBinderRegistry implements BinderRegistry{

    protected Map<String,List<Binder>> binders;
    protected Document document;
            
    @Override
    public void add(Binder binder) {
        String propPath = binder.getPath();
        List<Binder> blist = binders.get(propPath);
        if ( blist == null ) {
            blist = new ArrayList<Binder>();
        }
        blist.add(binder);
        this.binders.put(propPath,blist);
    }

    @Override
    public void remove(Binder binder) {
        String propPath = binder.getPath();
        List<Binder> blist = binders.get(propPath);
        if ( blist == null ) {
            return;
        }
        blist.remove(binder);
    }

    @Override
    public void firePropertyChange(Document doc, String propPath, Object oldValue, Object newValue) {
        List<Binder> blist = binders.get(propPath);
        if ( blist == null ) {
            return;
        }
        for ( Binder b : blist) {
            b.dataChanged(oldValue, newValue);
        }
    }

    @Override
    public Document getDocument() {
        return this.document;
    }

    @Override
    public void setDocument(Document document) {
        this.document = document;
        this.document.setPropertyChangeHandler(this);
    }
    
}
