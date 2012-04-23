/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Valery
 */
public class DefaultBinderRegistry implements BinderRegistry{

    protected String childName;
    protected List<BinderRegistry> childs;
    protected Map<String,List<Binder>> binders;
    protected Document document;
    protected ValidatorCollection validators;
    
    public DefaultBinderRegistry() {
        binders = new HashMap<String,List<Binder>>();
        validators = new ValidatorCollection();
        childs = new ArrayList<BinderRegistry>();
    }
    protected DefaultBinderRegistry(String childName) {
        this();
        this.childName = childName;
    }
    
    @Override
    public void add(Binder binder) {
        String propPath = binder.getPath();
        List<Binder> blist = binders.get(propPath);
        if ( blist == null ) {
            blist = new ArrayList<Binder>();
        }
        blist.add(binder);
        this.binders.put(propPath,blist);
        binder.setRegistry(this);
    }

    @Override
    public void remove(Binder binder) {
        String propPath = binder.getPath();
        List<Binder> blist = binders.get(propPath);
        if ( blist == null || blist.isEmpty()) {
            return;
        }
        blist.remove(binder);
        if ( blist.isEmpty() ) {
            binders.remove(binder.getPath());
        }
        
    }

    @Override
    public void firePropertyChange(String propPath, Object oldValue, Object newValue) {
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
        Document old = this.document;
        if ( old != null ) {
            this.resolvePendingChanges();
        }
        
        this.document = document;
        
        if ( this.document != null ) {
            this.document.setPropertyChangeHandler(this);
        } else if ( old != null ) {
            old.setPropertyChangeHandler(null);
        }
        
        refresh();
        
        for ( BinderRegistry br : childs ) {
            Object d = this.document.get(br.getChildName());
            if ( d == null ) {
                br.setDocument(null);
            } else if ( d instanceof Document ) {
                br.setDocument((Document)d);
            }
        }
    }
    protected void refresh() {
        for ( Map.Entry<String,List<Binder>> ent : this.binders.entrySet() ) {
            for ( Binder b : ent.getValue()) {
                b.dataChanged(null, b.getDataValue());
            }
        }
    }
    protected void resolvePendingChanges() {
        for ( Map.Entry<String,List<Binder>> ent : this.binders.entrySet() ) {
            for ( Binder b : ent.getValue()) {
                b.componentChanged(null, b.getComponentValue());
            }
        }
    }
    
    /**
     * For test purpose
     * @param path
     * @return 
     */
    protected List<Binder> getBinders(String path) {
        return this.binders.get(path);
    }

    @Override
    public void notifyError(Binder source, Exception e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public ValidatorCollection getValidators() {
        return validators;
    }

    @Override
    public void validate(String propPath, Object value) throws ValidationException {
        getValidators().validate(propPath, document, value);
    }

    @Override
    public void validate() throws ValidationException {
        getValidators().validate(document);
    }

    @Override
    public BinderRegistry createChild(String childName) {
        BinderRegistry br = new DefaultBinderRegistry(childName);
        childs.add(br);
        return br;
    }

/*    @Override
    public BinderRegistry getChild(String propName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
*/

    @Override
    public String getChildName() {
        return this.childName;
    }
}
