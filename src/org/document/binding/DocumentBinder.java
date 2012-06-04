package org.document.binding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.document.Document;
import org.document.DocumentChangeEvent;
import org.document.DocumentChangeListener;
import org.document.DocumentState;
import org.document.HasDocumentState;
import org.document.HasValidator;
import org.document.PropertyStore;
import org.document.ValidationException;
import org.document.Validator;

/**
 *
 * @author V. Shyskin
 */
public class DocumentBinder<T extends PropertyBinder> implements Binder, BinderListener, BinderContainer<T> {//extends AbstractDocumentBinder {
    private boolean suspended;

    protected List<DocumentChangeListener> documentListeners;
    protected List<BinderListener> binderListeners;
    protected String childName;
    protected List<DocumentBinder> childs;
    protected Map<String,List<T>> binders;
    protected List<T> documentErrorBinders;
    protected Document document;
    protected DocumentErrorBinder documentErrorBinder;

    protected DocumentBinder() {
        binderListeners = new ArrayList<BinderListener>();
        documentErrorBinders = new ArrayList<T>();
        childs = new ArrayList<DocumentBinder>();
        documentErrorBinder = new DocumentErrorBinder();
        binders = new HashMap<String,List<T>>();
    }

    
    protected DocumentBinder create() {
        return new DocumentBinder();
    }
    public PropertyStore getDocumentStore() {
        return document.propertyStore();
    }
    
    public boolean isSuspended() {
        return suspended;
    }
    public void suspendAll() {
        this.suspended = true;
        suspend(null);
    }

    public void resumeAll() {
        this.suspended = false;
        resume(null);
        
    }
    
    public void suspend(String propertyName) {

        if (documentListeners == null || documentListeners.isEmpty()) {
            return;
        }

        DocumentChangeEvent event = new DocumentChangeEvent(this, DocumentChangeEvent.Action.suspendBinding);
        event.setPropertyName(propertyName);

        for (DocumentChangeListener l : documentListeners) {
            l.react(event);
        }
        
    }

    public void resume(String propertyName) {
        if (this.documentListeners == null || documentListeners.isEmpty()) {
            return;
        }

        DocumentChangeEvent event = new DocumentChangeEvent(this, DocumentChangeEvent.Action.resumeBinding);
        event.setPropertyName(propertyName);
        event.setNewValue(document);
        
        for (DocumentChangeListener l : documentListeners) {
            l.react(event);
        }
    }
    
    @Override
    public Document getDocument() {
        return document;
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
                if ( state.isEditing() ) {
                    //
                    // The old  document has an error. Allows accumulate errors
                    // for those error binders wich support accumulation
                    //
                    ValidationException e = new ValidationException("Error when trying to execute setEdiiting(false)", this.document);
                    //fireDocumentError(e);
                    documentErrorBinder.notifyError(e);
                    
                }
            }
        }
        Document oldDocument = this.document;
        if (object == null) {
            this.document = null;
        }

        this.document = object;
        documentErrorBinder.setDocument(document);
        
        if (this.document != null) {
            getDocumentStore().addDocumentChangeListener(this);
        }
        if (oldDocument != null && oldDocument != document) {
            oldDocumentStore.removeDocumentChangeListener(this);
        }
        if ( ! isSuspended() ) {
            fireDocumentChanging(oldDocument, document);
            fireDocumentChanged(oldDocument, document);
        }

        if (document == null) {
            return;
        }

        PropertyStore documentStore = getDocumentStore();

        if (documentStore instanceof HasDocumentState) {
            DocumentState state = ((HasDocumentState) documentStore).getDocumentState();
            if (state.isEditing()) {
/*                for (Map.Entry<String, List<T>> e : binders.entrySet()) {
                    List<T> l = e.getValue();
                    if (l == null) {
                        continue;
                    }
                    for (T b : l) {
                        //b.propertyChanged(state.getDirtyValues().get(b.getBoundProperty()));
                        b.propertyChanged(documentStore.get(b.getBoundProperty()));
                    }
                }
*/ 
                documentErrorBinder.clear();
                try {
                    if (document instanceof HasValidator) {
                        Validator v = ((HasValidator) document).getValidator();
                        if (v != null) {
                            v.validate(document);
                        }
                    }
                } catch (ValidationException e) {
                    documentErrorBinder.notifyError(e);
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
            case clearError:
                documentErrorBinder.clear(event.getPropertyName());
                break;
            case componentChangeError:
                documentErrorBinder.notifyError(event.getPropertyName(), event.getException());
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
    
    @Override
    public void addBinderListener(BinderListener l) {
    }

    @Override
    public void removeBinderListener(BinderListener l) {
    }
    protected void remove(T binder, Map<String, List<T>> binderMap) {
        String propPath = ((PropertyBinder) binder).getBoundProperty();
        List<T> blist = binderMap.get(propPath);
        if (blist == null || blist.isEmpty()) {
            return;
        }
        binder.removeBinderListener(this);
        if ( binder instanceof DocumentChangeListener ) {
            removeDocumentChangeListener((DocumentChangeListener)binder);
        }
        
        //removeDocumentChangeListener(binder);
        blist.remove(binder);
        if (blist.isEmpty()) {
            binderMap.remove(((PropertyBinder) binder).getBoundProperty());
        }

    }

    protected void remove(T binder, List<T> binderList) {
        binderList.remove(binder);
        binder.removeBinderListener(this);
        if ( binder instanceof DocumentChangeListener ) {
            removeDocumentChangeListener((DocumentChangeListener)binder);
        }
    }

    @Override
    public void remove(T binder) {
        if ( binder == null ) {
            return;
        }
        if (binder instanceof PropertyBinder) {
            remove(binder, binders);
        } else  if (binder instanceof ErrorBinder) {
            documentErrorBinder.remove("*document", (ErrorBinder)binder);
        }
    }
    
    @Override
    public void add(T binder) {
        if ( binder == null ) {
            return;
        }
        String propertyName = ((PropertyBinder) binder).getBoundProperty();
        List<T> blist = binders.get(propertyName);
        if (blist == null) {
            blist = new ArrayList<T>();
        }
        binder.addBinderListener(this);
        if ( binder instanceof DocumentChangeListener ) {
            addDocumentChangeListener((DocumentChangeListener)binder);
        }
        blist.add(binder);
        binders.put(propertyName, blist);
        if ( isSuspended() ) {
            suspend(propertyName);
        } else {
            resume(propertyName);
        }
    }
    
    protected void add(T binder, Map<String, List<T>> binderMap) {
        String propertyName = ((PropertyBinder) binder).getBoundProperty();

        List<T> blist = binderMap.get(propertyName);
        if (blist == null) {
            blist = new ArrayList<T>();
        }
        binder.addBinderListener(this);
        if ( binder instanceof DocumentChangeListener ) {
            addDocumentChangeListener((DocumentChangeListener)binder);
        }
        blist.add(binder);
        binderMap.put(propertyName, blist);
    }

    protected void add(T binder, List<T> binderList) {
        binderList.add(binder);
        binder.addBinderListener(this);
        if ( binder instanceof DocumentChangeListener ) {
            addDocumentChangeListener((DocumentChangeListener)binder);
        }
    }
    /**
     * Tries to resolve all pending component changes.
     * Is invoked for the old document and only from the 
     * setDocument's method body just before a new document is set.
     * Notifies all registered binders of the event of type 
     * {@link org.document.DocumentChangeEvent} with an action set to
     * {@link org.document.DocumentChangeEvent.Action#completeChanges}.
     * @see #setDocument(org.document.Document) 
     */
    protected void completeChanges() {
        if (document.propertyStore() == null) {
            return;
        }
        DocumentChangeEvent event = new DocumentChangeEvent(this, DocumentChangeEvent.Action.completeChanges);
        for (Map.Entry<String, List<T>> ent : this.binders.entrySet()) {
            for (Binder b : ent.getValue()) {
                if ( b instanceof DocumentChangeListener) {
                    ((DocumentChangeListener)b).react(event);
                }
                
            }
        }
    }
    private void fireDocumentChanging(Document oldDoc, Document newDoc) {
        if (this.documentListeners == null || documentListeners.isEmpty()) {
            return;
        }

        DocumentChangeEvent event = new DocumentChangeEvent(this, DocumentChangeEvent.Action.documentChanging);
        event.setOldValue(oldDoc);
        event.setNewValue(newDoc);

        for (DocumentChangeListener l : documentListeners) {
            l.react(event);
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
    public String getChildName() {
        return this.childName;
    }

    protected void setChildName(String childName) {
        this.childName = childName;
    }
    protected void firePropertyChange(DocumentChangeEvent event) {
        String propName = event.getPropertyName();

        List<T> blist = binders.get(propName);
        if (blist != null) {
            for (Binder b : blist ) {
                if ( b instanceof DocumentChangeListener) {
                    ((DocumentChangeListener)b).react(event);
                }
            }
        }
    }
    public void bind(HasBinder component) {
        Binder b = component.getBinder();
        if ( b instanceof PropertyBinder) {
            PropertyBinder pb = (PropertyBinder)b;
            String nm = pb.getBoundProperty();
            if ( nm != null ) {
                add((T)pb);
            }
        }
    }
    
    public void bind(String propertyName,ErrorBinder binder) {
        documentErrorBinder.bind(propertyName, binder);
    }
    public void bindDocument(ErrorBinder binder) {
        documentErrorBinder.bindDocument(binder);
    }
    public void bind(HasErrorBinder component) {
        documentErrorBinder.bind(component);
    }
    public DocumentErrorBinder getDocumentErrorBinder() {
        return documentErrorBinder;
    }
    protected List<BinderListener> getBinderListeners() {
        return binderListeners;
    }

}
