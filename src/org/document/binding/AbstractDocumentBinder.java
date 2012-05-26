package org.document.binding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.document.*;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractDocumentBinder<T extends PropertyBinder> implements Binder, BinderListener, BinderContainer<T> {//, HasDocumentAlias {

    //private Object alias;
    protected List<DocumentChangeListener> documentListeners;
    protected List<BinderListener> binderListeners;
    protected String childName;
    protected List<DocumentBinder> childs;
    protected Map<String, List<T>> binders;
    protected Map<String, List<T>> errorBinders;
    protected List<T> documentErrorBinders;
    protected Document document;

    protected AbstractDocumentBinder() {
        binders = new HashMap<String, List<T>>();
        binderListeners = new ArrayList<BinderListener>();

        errorBinders = new HashMap<String, List<T>>();
        documentErrorBinders = new ArrayList<T>();
        childs = new ArrayList<DocumentBinder>();
    }

    /*    protected AbstractDocumentBinder(Object alias) {
     this();
     //        this.alias = alias;
     }
     */
    /*    @Override
     public Object getAlias() {
     return alias;
     }
     */
    @Override
    public Object getComponentValue() {
        return null;
    }

    /*    @Override
     public void addBinderListener(BinderListener l) {
     binderListeners.add(l);
     if (binderListeners.size() > 1 ) {
     throw new IndexOutOfBoundsException("AbstractDocumentBinder. Only one BinderListener can be registered");
     }

     }

     @Override
     public void removeBinderListener(BinderListener l) {
     binderListeners.remove(l);
     }
     */
    @Override
    public abstract void addBinderListener(BinderListener l);

    @Override
    public abstract void removeBinderListener(BinderListener l);

    protected List<BinderListener> getBinderListeners() {
        return binderListeners;
    }

    protected void add(T binder, Map<String, List<T>> binderMap) {
        String propertyName = ((PropertyBinder) binder).getPropertyName();

        List<T> blist = binderMap.get(propertyName);
        if (blist == null) {
            blist = new ArrayList<T>();
        }
        binder.addBinderListener(this);
        addDocumentChangeListener(binder);
        blist.add(binder);
        binderMap.put(propertyName, blist);
    }

    protected void add(T binder, List<T> binderList) {
        binderList.add(binder);
        binder.addBinderListener(this);
        addDocumentChangeListener(binder);
    }

    @Override
    public void add(T binder) {
        if ( binder == null ) {
            return;
        }
        if (binder instanceof ErrorBinder) {
            if (((PropertyBinder) binder).getPropertyName() == null) {
                // documentStore error binder
                add(binder, documentErrorBinders);
            } else {
                add(binder, errorBinders);
            }
        } else if (binder instanceof PropertyBinder) {
            add(binder, binders);
        }
    }

    protected void remove(T binder, Map<String, List<T>> binderMap) {
        String propPath = ((PropertyBinder) binder).getPropertyName();
        List<T> blist = binderMap.get(propPath);
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

    protected void remove(T binder, List<T> binderList) {
        binderList.remove(binder);
        binder.removeBinderListener(this);
        removeDocumentChangeListener(binder);
//        binder.setDocumentBinding(this);

    }

    @Override
    public void remove(T binder) {
        if ( binder == null ) {
            return;
        }
        if (binder instanceof ErrorBinder) {
            if (((PropertyBinder) binder).getPropertyName() == null) {
                // documentStore error binder
                remove(binder, documentErrorBinders);
            } else {
                remove(binder, errorBinders);
            }
        } else if (binder instanceof PropertyBinder) {
            remove(binder, binders);
        }
    }

    public Map<String, List<T>> getBinders() {
        return this.binders;
    }

    public List<T> getBinders(String propertyName) {
        return this.binders.get(propertyName);
    }

    public Map<String, List<T>> getErrorBinders() {
        return this.errorBinders;
    }

    public List<T> getErrorBinders(String propertyName) {
        return this.errorBinders.get(propertyName);
    }

    public List<T> getDocumentErrorBinders() {
        return this.documentErrorBinders;
    }

    protected void firePropertyChange(DocumentChangeEvent event) {
        String propName = event.getPropertyName();
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        List<T> blist = binders.get(propName);
        if (blist != null) {
            for (Binder b : blist) {
                //b.dataChanged(oldValue, newValue);
                //b.dataChanged(newValue);
                b.react(event);
            }
        }
        blist = errorBinders.get(propName);
        if (blist != null) {
            for (Binder b : blist) {
                //b.dataChanged(oldValue, newValue);
                //b.dataChanged(newValue);
                b.react(event);
            }
        }
        blist = errorBinders.get("*");
        if (blist != null) {
            for (Binder b : blist) {
                b.react(event);
            }
        }

    }

    @Override
    public Document getDocument() {
        return document;
    }

    public PropertyStore getDocumentStore() {
        //return this.documentStore;
        return document.propertyStore();
    }

    @Override
    public void setDocument(Document object) {
        PropertyStore oldDocumentStore = null;
        if (document != null) {
            oldDocumentStore = getDocumentStore();
        }

        if (oldDocumentStore != null) {
            completeChanges();
            if (oldDocumentStore instanceof HasDocumentState) {
                DocumentState state = ((HasDocumentState) oldDocumentStore).getDocumentState();
                state.setEditing(false);
            }
        }
        Document oldDocument = this.document;
        if (object == null) {
            this.document = null;
        }

        this.document = object;

        if (this.document != null) {
            getDocumentStore().addDocumentChangeListener(this);
        }
        if (oldDocument != null && oldDocument != document) {
            oldDocumentStore.removeDocumentChangeListener(this);
        }
        // ??

        fireDocumentChanged(oldDocument, document);
        //refresh();
        if (document == null) {
            return;
        }

        PropertyStore documentStore = getDocumentStore();

        if (documentStore instanceof HasDocumentState) {
            DocumentState state = ((HasDocumentState) documentStore).getDocumentState();
            if (state.isEditing()) {
                for (Map.Entry<String, List<T>> e : binders.entrySet()) {
                    List<T> l = e.getValue();
                    if (l == null) {
                        continue;
                    }
                    for (T b : l) {
                        //b.propertyChanged(state.getDirtyValues().get(b.getPropertyName()));
                        b.propertyChanged(documentStore.get(b.getPropertyName()));
                    }
                }
                completeChanges();
                fireDocumentError(null);
                try {
                    if (document instanceof HasValidator) {
                        Validator v = ((HasValidator) document).getValidator();
                        if (v != null) {
                            v.validate(document);
                        }
                    }

                } catch (ValidationException e) {
                    //this.notifyDocumentError(e);
                    fireDocumentError(e);
                }

            }
        }
        for (DocumentBinder child : childs) {
            Object d = documentStore.get(child.getChildName());
            if (d == null) {
                child.setDocument((Document) null);
            } else if (d instanceof Document) {
                child.setDocument((Document) d);
            }
        }
    }

    private void fireDocumentChanged(Document oldDoc, Document newDoc) {
        if (this.documentListeners == null || documentListeners.isEmpty()) {
            return;
        }

        DocumentChangeEvent event = new DocumentChangeEvent(this, DocumentChangeEvent.Action.documentChange);
        event.setOldValue(oldDoc);
        event.setNewValue(newDoc);

        for (DocumentChangeListener l : documentListeners) {
            l.react(event);
        }
    }

    protected void completeChanges() {
        if (document.propertyStore() == null) {
            return;
        }
        DocumentChangeEvent event = new DocumentChangeEvent(this, DocumentChangeEvent.Action.completeChanges);
        for (Map.Entry<String, List<T>> ent : this.binders.entrySet()) {
            for (Binder b : ent.getValue()) {
                b.react(event);
            }
        }
    }

    protected void firePropertyError(String propertyName, Exception e) {
        if (this.documentListeners == null || documentListeners.isEmpty()) {
            return;
        }

        DocumentChangeEvent event = new DocumentChangeEvent(this, DocumentChangeEvent.Action.propertyError);
        event.setPropertyName(propertyName);
        event.setException(e);

        for (DocumentChangeListener l : documentListeners) {
            String nm = ((PropertyBinder) l).getPropertyName();
            if ((l instanceof ErrorBinder)
                    && propertyName.equals(nm) || "*".equals(nm)) {
                l.react(event);
            }
        }

    }

    protected void fireDocumentError(Exception e) {
        if (this.documentListeners == null || documentListeners.isEmpty()) {
            return;
        }

        DocumentChangeEvent event = new DocumentChangeEvent(document, DocumentChangeEvent.Action.documentError);
        event.setException(e);

        for (DocumentChangeListener l : documentListeners) {
            if ((l instanceof ErrorBinder) && !((ErrorBinder) l).isPropertyError()) {
                l.react(event);
            }
        }

    }

    protected abstract DocumentBinder create();

    public DocumentBinder createChild(String childName) {
        DocumentBinder binder = create();
        binder.setChildName(childName);
        childs.add(binder);
        if (document != null && document.propertyStore() != null) {
            binder.setDocument((Document) getDocumentStore().get(childName));
        }
        return binder;
    }

    public String getChildName() {
        return this.childName;
    }

    protected void setChildName(String childName) {
        this.childName = childName;
    }

    @Override
    public void react(DocumentChangeEvent event) {
        if (event.getAction().equals(DocumentChangeEvent.Action.documentChange)) {
            setDocument((Document) event.getNewValue());
        } else if (event.getAction().equals(DocumentChangeEvent.Action.propertyChange)) {
            firePropertyChange(event);
        }
    }

    @Override
    public void react(BinderEvent event) {
        switch (event.getAction()) {
            case clearComponentChangeError:
                firePropertyError(event.getPropertyName(), event.getException());
                break;
            case componentChangeValueError:
                firePropertyError(event.getPropertyName(), event.getException());
                break;

        }
    }

    //@Override
    public void addDocumentChangeListener(DocumentChangeListener l) {
        if (documentListeners == null) {
            documentListeners = new ArrayList<DocumentChangeListener>();
        }
        documentListeners.add(l);
    }

    //@Override
    public void removeDocumentChangeListener(DocumentChangeListener l) {
        if (documentListeners == null || documentListeners.isEmpty()) {
            return;
        }
        documentListeners.remove(l);
    }
}
