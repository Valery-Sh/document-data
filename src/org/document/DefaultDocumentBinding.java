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
public class DefaultDocumentBinding implements DocumentBinding {
    protected Object id;
    protected String childName;
    protected List<DocumentBinding> childs;
    protected Map<String, List<Binder>> binders;
    protected Map<String, List<Binder>> errorBinders;
    protected Document document;
    protected ValidatorCollection validators;

    public DefaultDocumentBinding(Object id) {
        this();
        this.id = id;
    }
    
    public DefaultDocumentBinding() {
        binders = new HashMap<String, List<Binder>>();
        errorBinders = new HashMap<String, List<Binder>>();
        validators = new ValidatorCollection();
        childs = new ArrayList<DocumentBinding>();
    }

    protected DefaultDocumentBinding(String childName) {
        this();
        this.childName = childName;
    }
    /**
     * The <code>id</code> is a user defined identifier.
     * @return user defined identifier
     */
    @Override
    public Object getId() {
        return id;
    }

    protected void add(Binder binder, Map<String, List<Binder>> binderMap) {
        String propPath = binder.getPropertyName();
        List<Binder> blist = binderMap.get(propPath);
        if (blist == null) {
            blist = new ArrayList<Binder>();
        }
        blist.add(binder);
        binderMap.put(propPath, blist);
        binder.setDocumentBinding(this);

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
        String propPath = binder.getPropertyName();
        List<Binder> blist = binderMap.get(propPath);
        if (blist == null || blist.isEmpty()) {
            return;
        }
        blist.remove(binder);
        if (blist.isEmpty()) {
            binderMap.remove(binder.getPropertyName());
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

    protected void firePropertyChange(String propName,Object oldValue, Object newValue) {

        List<Binder> blist = binders.get(propName);
        if (blist == null) {
            return;
        }
        for (Binder b : blist) {
            b.dataChanged(oldValue, newValue);
        }
    }

    protected void firePropertyChange(Binder binder, Object oldValue, Object newValue) {
        List<Binder> blist = binders.get(binder.getPropertyName());
        if (blist == null) {
            return;
        }
        for (Binder b : blist) {
            if ( b == binder )
                continue;
            b.dataChanged(oldValue, newValue);
        }
    }

    @Override
    public Document getDocument() {
        return this.document;
    }
    
/*    @Override
    public void setDocument(HasDocument hasDocument, boolean completeChanges) {
        this.setDocument(hasDocument.getDocument(), completeChanges);
    }
*/
    @Override
    public void setDocument(HasDocument hasDocument) {
        this.setDocument(hasDocument.getDocument());
    }

    @Override
    public void setDocument(Document document) {

        Document old = this.document;
        if (old != null) {
            completeChanges();
        }

        this.document = document;

        if (this.document != null) {
            //this.document.setPropertyChangeHandler(this);
            this.document.addDocumentListener(this);

        } else if (old != null) {
            //old.setPropertyChangeHandler(null);
            old.removeDocumentListener(this);
        }

        refresh();
        for (DocumentBinding child : childs) {
            Object d = this.document.get(child.getChildName());
            if (d == null) {
                child.setDocument((Document)null);
            } else if (d instanceof Document) {
                child.setDocument((Document) d);
            }
        }
/*        for (DocumentBinding child : childs) {
            Object d = this.document.get(child.getChildName());
            if (d == null) {
                child.setDocument((Document)null, false);
            } else if (d instanceof Document) {
                child.setDocument((Document) d, false);
            }
        }
*/
    }
    
/*    @Override
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
            //this.document.setPropertyChangeHandler(this);
            this.document.addDocumentListener(this);

        } else if (old != null) {
            //old.setPropertyChangeHandler(null);
            old.removeDocumentListener(this);
        }

        refresh();

        for (DocumentBinding child : childs) {
            Object d = this.document.get(child.getChildName());
            if (d == null) {
                child.setDocument((Document)null, false);
            } else if (d instanceof Document) {
                child.setDocument((Document) d, false);
            }
        }
    }
*/
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

/*        for (DocumentBinding child : childs) {
            Object d = this.document.get(child.getChildName());
            if (d != null && (d instanceof Document)) {
                child.completeChanges();
            }
        }

*/
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
        List<Binder> blist = this.errorBinders.get(source.getPropertyName());
        if (blist != null) {
            for (Binder b : blist) {
                ((ErrorBinder) b).notifyError(source, e);
            }
        }
        //if ((e instanceof ValidationException) && this.getValidators().getProperyValidators().isEmpty() ) {
        //    throw (ValidationException)e;
        //}
        /*
         * for (Map.Entry<String, List<Binder>> ent :
         * this.errorBinders.entrySet()) { for (Binder b : ent.getValue()) {
         * ((ErrorBinder) b).notifyError(source, e); } }
         */
    }
    @Override
    public void notifyError(String propertyName, Exception e) {
        List<Binder> blist = this.errorBinders.get(propertyName);
        if (blist != null) {
            for (Binder b : blist) {
                ((ErrorBinder) b).notifyError(null, e);
            }
        }
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
    public DocumentBinding createChild(String childName) {
        DocumentBinding binding = new DefaultDocumentBinding(childName);
        childs.add(binding);
        if (this.document != null) {
            binding.setDocument((Document) document.get(childName));
        }
        return binding;
    }

    
    @Override
    public String getChildName() {
        return this.childName;
    }

    @Override
    public void react(DocumentEvent event) {
        if (event.getAction().equals(DocumentEvent.Action.propertyChangeNotify)) {
            if ( event.getBinder() == null ) {
                firePropertyChange(event.getPropertyName(), event.getOldValue(), event.getNewValue());
            } else {
                firePropertyChange(event.getBinder(), event.getOldValue(), event.getNewValue());            
            }
        } else if (event.getAction().equals(DocumentEvent.Action.validateProperty)) {
            this.validate(event.getPropertyName(), event.getNewValue());
        } else if (event.getAction().equals(DocumentEvent.Action.validateErrorNotify)) {
            notifyError(event.getPropertyName(), event.getException());
        } else if (event.getAction().equals(DocumentEvent.Action.validateAllProperties)) {
            validators.validateProperties((Document)event.getSource());
        }
    }
}
