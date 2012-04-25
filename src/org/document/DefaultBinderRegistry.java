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
public class DefaultBinderRegistry implements BinderRegistry {
    
    protected String childName;
    protected List<BinderRegistry> childs;
    protected Map<String, List<Binder>> binders;
    protected Map<String, List<Binder>> errorBinders;
    protected Document document;
    protected ValidatorCollection validators;
    
    public DefaultBinderRegistry() {
        binders = new HashMap<String, List<Binder>>();
        errorBinders = new HashMap<String, List<Binder>>();
        validators = new ValidatorCollection();
        childs = new ArrayList<BinderRegistry>();
    }

    protected DefaultBinderRegistry(String childName) {
        this();
        this.childName = childName;
    }

    protected void add(Binder binder, Map<String, List<Binder>> binderMap) {
        String propPath = binder.getDataEntityName();
        List<Binder> blist = binderMap.get(propPath);
        if (blist == null) {
            blist = new ArrayList<Binder>();
        }
        blist.add(binder);
        binderMap.put(propPath, blist);
        binder.setRegistry(this);
        
    }

    @Override
    public void add(Binder binder) {
        if (binder instanceof ErrorBinder) {
            add(binder, errorBinders);
        } else {
            add(binder, binders);
        }
    }

    protected void remove(Binder binder, Map<String, List<Binder>> binderMap) {
        String propPath = binder.getDataEntityName();
        List<Binder> blist = binderMap.get(propPath);
        if (blist == null || blist.isEmpty()) {
            return;
        }
        blist.remove(binder);
        if (blist.isEmpty()) {
            binderMap.remove(binder.getDataEntityName());
        }
        
    }
    
    @Override
    public void remove(Binder binder) {
        if (binder instanceof ErrorBinder) {
            remove(binder, errorBinders);
        } else {
            remove(binder, binders);
        }
    }
    
    @Override
    public void firePropertyChange(String propPath, Object oldValue, Object newValue) {
        List<Binder> blist = binders.get(propPath);
        if (blist == null) {
            return;
        }
        for (Binder b : blist) {
            b.dataChanged(oldValue, newValue);
        }
    }
    
    @Override
    public Document getDocument() {
        return this.document;
    }
    
    @Override
    public void setDocument(Document document, boolean completeChanges) {
        
        Document old = this.document;
        if (old != null) {
            if (completeChanges) {
                completeChanges();
            } else {
                this.resolvePendingChanges();
            }
        }
        
        this.document = document;
        
        if (this.document != null) {
            this.document.setPropertyChangeHandler(this);
        } else if (old != null) {
            old.setPropertyChangeHandler(null);
        }
        
        refresh();
        
        for (BinderRegistry br : childs) {
            Object d = this.document.get(br.getChildName());
            if (d == null) {
                br.setDocument(null, false);
            } else if (d instanceof Document) {
                br.setDocument((Document) d, false);
            }
        }
    }
    
    protected void refresh() {
        for (Map.Entry<String, List<Binder>> ent : this.binders.entrySet()) {
            for (Binder b : ent.getValue()) {
                b.dataChanged(null, b.getDataValue());
                notifyError(b, null);                
            }
        }
    }

    protected void resolvePendingChanges() {
        for (Map.Entry<String, List<Binder>> ent : this.binders.entrySet()) {
            for (Binder b : ent.getValue()) {
                b.componentChanged(null, b.getComponentValue());
            }
        }
    }

    @Override
    public void completeChanges() {
        if (document == null) {
            return;
        }
        
        this.resolvePendingChanges();
        
        for (BinderRegistry br : childs) {
            Object d = this.document.get(br.getChildName());
            if (d != null && (d instanceof Document)) {
                br.completeChanges();
            }
        }
    }

    /**
     * For test purpose
     *
     * @param path
     * @return
     */
    protected List<Binder> getBinders(String path) {
        return this.binders.get(path);
    }
    
    @Override
    public void notifyError(Binder source, Exception e) {
        List<Binder> blist = this.errorBinders.get(source.getDataEntityName());
        if ( blist != null ) {
            for (Binder b : blist) {
                ((ErrorBinder) b).notifyError(source, e);
            }
        }
/*        for (Map.Entry<String, List<Binder>> ent : this.errorBinders.entrySet()) {
            for (Binder b : ent.getValue()) {
                ((ErrorBinder) b).notifyError(source, e);
            }
        }
*/
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
        if (this.document != null) {
            br.setDocument((Document) document.get(childName), true);
        }
        return br;
    }

    /*
     * @Override public BinderRegistry getChild(String propName) { throw new
     * UnsupportedOperationException("Not supported yet."); }
     */
    @Override
    public String getChildName() {
        return this.childName;
    }
}
