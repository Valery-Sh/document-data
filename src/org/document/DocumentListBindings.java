package org.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author V. Shyshkin
 */
public class DocumentListBindings implements ListBindings, BinderListener {

    protected List<DocumentChangeListener> documentListeners;
    protected Object id;
    protected Map<String, List<Binder>> binders;
    protected Map<String, List<Binder>> errorBinders;
    protected List<Binder> documentErrorBinders;
    protected List<Binder> listBinders;    
    //protected DocumentStore documentStore;
    protected Document document;
    protected ValidatorCollection validators;

    public DocumentListBindings(Object id) {
        this.id = id;
    }

    public DocumentListBindings() {
        binders = new HashMap<String, List<Binder>>();
        errorBinders = new HashMap<String, List<Binder>>();
        documentErrorBinders = new ArrayList<Binder>();
        listBinders = new ArrayList<Binder>();
    }

    /**
     * The
     * <code>id</code> is a user defined identifier.
     *
     * @return user defined identifier
     */
    public Object getId() {
        return id;
    }

    protected void add(Binder binder, Map<String, List<Binder>> binderMap) {
        String propPath = ((PropertyBinder) binder).getPropertyName();
        List<Binder> blist = binderMap.get(propPath);
        if (blist == null) {
            blist = new ArrayList<Binder>();
        }
        binder.addBinderListener(this);
        addDocumentChangeListener(binder);
        blist.add(binder);
        binderMap.put(propPath, blist);
        //binder.setDocumentBinding(this);

    }

    protected void add(Binder binder, List<Binder> binderList) {
        binderList.add(binder);
        binder.addBinderListener(this);
        addDocumentChangeListener(binder);
//        binder.setDocumentBinding(this);

    }

    @Override
    public void add(Binder binder) {
        if (binder instanceof ErrorBinder) {
            if (((ErrorBinder) binder).getPropertyName() == null) {
                // documentStore error binder
                add(binder, documentErrorBinders);
            } else {
                add(binder, errorBinders);
            }
        } else if (binder instanceof PropertyBinder) {
            add(binder, binders);
        } else if (binder instanceof ListBinder) {
            add(binder, listBinders);
        }
    }

    protected void remove(Binder binder, Map<String, List<Binder>> binderMap) {
        String propPath = ((PropertyBinder) binder).getPropertyName();
        List<Binder> blist = binderMap.get(propPath);
        if (blist == null || blist.isEmpty()) {
            return;
        }
        binder.removeBinderListener(this);
        removeDocumentChangeListener(binder);
        blist.remove(binder);
        if (blist.isEmpty()) {
            binderMap.remove(((PropertyBinder) binder).getPropertyName());
        }

    }

    protected void remove(Binder binder, List<Binder> binderList) {
        binderList.remove(binder);
        binder.removeBinderListener(this);
        removeDocumentChangeListener(binder);
//        binder.setDocumentBinding(this);

    }

    @Override
    public void remove(Binder binder) {
        if (binder instanceof ErrorBinder) {
            if (((ErrorBinder) binder).getPropertyName() == null) {
                // documentStore error binder
                remove(binder, documentErrorBinders);
            } else {
                remove(binder, errorBinders);
            }
        } else if (binder instanceof PropertyBinder) {
            remove(binder, binders);
        } else if (binder instanceof ListBinder) {
            remove(binder, listBinders);
        }
    }

    public Map<String, List<Binder>> getBinders() {
        return this.binders;
    }

    public List<Binder> getBinders(String propertyName) {
        return this.binders.get(propertyName);
    }

    public Map<String, List<Binder>> getErrorBinders() {
        return this.errorBinders;
    }

    public List<Binder> getErrorBinders(String propertyName) {
        return this.errorBinders.get(propertyName);
    }

    public List<Binder> getDocumentErrorBinders() {
        return this.documentErrorBinders;
    }

    private void fireDocumentChanged(Document oldDoc, Document newDoc) {
        if (this.documentListeners == null || documentListeners.isEmpty()) {
            return;
        }

        DocumentChangeEvent event = new DocumentChangeEvent(document, DocumentChangeEvent.Action.documentChange);
        event.setOldValue(oldDoc);
        event.setNewValue(newDoc);

        for (DocumentChangeListener l : documentListeners) {
            l.react(event);
        }
    }

    @Override
    public void react(DocumentChangeEvent event) {
    }

    @Override
    public void react(BinderEvent event) {
        switch (event.getAction()) {
            case selectChange:
                DocumentChangeEvent e = new DocumentChangeEvent(document,DocumentChangeEvent.Action.selectChange);
                e.setNewValue(event.getDataValue());
                e.setOldValue(event.getComponentValue());
                for (DocumentChangeListener l : documentListeners) {
                    l.react(e);
                }
                
                break;
        }        
    }

    @Override
    public void addDocumentChangeListener(DocumentChangeListener l) {
        if (documentListeners == null) {
            documentListeners = new ArrayList<DocumentChangeListener>();
        }
        documentListeners.add(l);
    }

    @Override
    public void removeDocumentChangeListener(DocumentChangeListener l) {
        if (documentListeners == null) {
            return;
        }
        documentListeners.remove(l);
    }

    @Override
    public void setDocument(Document document) {
        Document old = this.document;
        this.document = document;
        fireDocumentChanged(old, document);
    }
}
