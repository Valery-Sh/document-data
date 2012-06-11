package org.document.binding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
public abstract class AbstractDocumentBinder<E extends Document> extends AbstractBinder implements  BinderListener, DocumentChangeListener, ContainerBinder {//BinderContainer<T> {//extends AbstractDocumentBinder {
    private String className;
    private String alias;

    private boolean suspended;

    protected List<BinderListener> binderListeners;
    protected String childName;
    protected List<DocumentBinder> childs;
    protected List<PropertyBinder> binders;
    protected DocumentErrorBinder documentErrorBinder;

    protected AbstractDocumentBinder() {
        this(null);
    }
    protected AbstractDocumentBinder(Object boundObject) {
        super(boundObject);
        binderListeners = new ArrayList<BinderListener>();
        childs = new ArrayList<DocumentBinder>();
        documentErrorBinder = new DocumentErrorBinder();
        binders = new ArrayList<PropertyBinder>();
        
    }
    @Override
    public void initDefaults() {
        for ( PropertyBinder b : binders) {
            b.initDefaults();
        }
    }
    
    @Override
    public Iterator<PropertyBinder> iterator() {
        return binders.iterator();
    }
    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public void setClassName(String className) {
        this.className = className;
    }
    
    @Override
    public boolean contains(Binder binder) {
        if ( binder instanceof PropertyBinder) {
            return binders.contains((PropertyBinder)binder);
        }
        return false;
    }
    @Override
    public boolean remove(Binder binder) {
        if ( binder instanceof PropertyBinder) {
            return binders.remove((PropertyBinder)binder);
        }
        return false;
    }

//    public PropertyStore getDocumentStore() {
//        return getContext().getSelected().propertyStore();
//    }

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

        DocumentChangeEvent event = new DocumentChangeEvent(this, DocumentChangeEvent.Action.suspendBinding);
        event.setPropertyName(propertyName);
        for (Binder b : binders) {
            if (b instanceof DocumentChangeListener) {
                ((DocumentChangeListener) b).react(event);
            }
        }

    }

    public void resume(String propertyName) {
        DocumentChangeEvent event = new DocumentChangeEvent(this, DocumentChangeEvent.Action.resumeBinding);
        event.setPropertyName(propertyName);
        event.setNewValue(getContext().getSelected());
        for (Binder b : binders) {
            if (b instanceof DocumentChangeListener) {
                ((DocumentChangeListener) b).react(event);
            }
        }
    }

    protected E getDocument() {
        return (E)getContext().getSelected();
    }

    protected void documentChange(DocumentChangeEvent e) {
        E newDoc = (E)e.getNewValue();
        E oldDoc = (E)e.getOldValue();
        PropertyStore oldDocumentStore = null;
        if (oldDoc != null) {
            oldDocumentStore = oldDoc.propertyStore();
        }

        if (oldDocumentStore != null) {
            completeChanges();
            if (oldDocumentStore instanceof HasDocumentState) {
                DocumentState state = ((HasDocumentState) oldDocumentStore).getDocumentState();
                state.setEditing(false);
                if (state.isEditing()) {
                    //
                    // The old  document has an error. Allows accumulate errors
                    // for those error binders wich support accumulation
                    //
                    ValidationException ex = new ValidationException("Error when trying to execute setEdiiting(false)", oldDoc);
                    documentErrorBinder.notifyError(ex);

                }
            }
        }
//        E oldDocument = getDocument();

        documentErrorBinder.setDocument(oldDoc);

        if (!isSuspended()) {
            fireDocumentChanging(oldDoc, newDoc);
            fireDocumentChanged(oldDoc, newDoc);
        }

        if (oldDoc == null) {
            return;
        }

        PropertyStore documentStore = oldDoc.propertyStore();

        if (documentStore instanceof HasDocumentState) {
            DocumentState state = ((HasDocumentState) documentStore).getDocumentState();
            if (state.isEditing()) {
                documentErrorBinder.clear();
                try {
                    if (oldDoc instanceof HasValidator) {
                        Validator v = ((HasValidator) oldDoc).getValidator();
                        if (v != null) {
                            v.validate(oldDoc);
                        }
                    }
                } catch (ValidationException ex) {
                    documentErrorBinder.notifyError(ex);
                }
            }
        }
        for (DocumentBinder child : childs) {
            Object d = documentStore.get(child.getChildName());
            if (d == null) {
                child.documentChange(e);
            } else if (d instanceof Document) {
                child.documentChange(e);
            }
        }
    }

    @Override
    public void react(DocumentChangeEvent event) {
        if (event.getAction().equals(DocumentChangeEvent.Action.documentChange)) {
            documentChange(event);
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
/*    public void addDocumentChangeListener(DocumentChangeListener l) {
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
*/
    //@Override
    public void addBinderListener(BinderListener l) {
    }

    //@Override
    @Override
    public void removeBinderListener(BinderListener l) {
    }

    public void remove(PropertyBinder binder) {
        if (binder == null) {
            return;
        }
        binders.remove(binder);
        binder.setContext(null);
        binder.removeBinderListener(this);
        initDefaults();
        
        //TODOsetDocument(document); // to refresh to reflect changes

    }

    protected void updateBinders() {
        for (PropertyBinder b : binders) {
            b.removeBinderListener(this);
        }
        binders.clear();
    }
    @Override
    public boolean add(Binder binder) {
        return false;
    }
    public void add(PropertyBinder binder) {
        if (binder == null) {
            return;
        }
        binder.setContext(this.getContext());
        String propertyName = binder.getBoundProperty();
         if (propertyName == null || binder.getBoundObject() == null) {
            throw new IllegalArgumentException("The PropertyBinder must have a not null boundProperty and boundObject");
         }
         
        if (!binders.contains(binder)) {
            binders.add(binder);
            binder.addBinderListener(this);
        }
        if (propertyName != null) {
            if (isSuspended()) {
                suspend(propertyName);
            } else {
                resume(propertyName);
            }
        }
        
        //setDocument(document); // to refresh the added binder

//        bind(propertyName);

    }

    public void update(PropertyBinder binder) {
        if (binder == null) {
            return;
        }
        String propertyName = binder.getBoundProperty();
        /* 111        if (propertyName == null || binder.getBoundObject() == null) {
         return;
         }
         */
        if (binders.contains(binder)) {
            //binders.add(binder);
            binder.addBinderListener(this);
        }
        if (propertyName != null) {
            if (isSuspended()) {
                suspend(propertyName);
            } else {
                resume(propertyName);
            }
        }


//        bind(propertyName);

    }

    /*    protected void add(PropertyBinder binder, Map<String, List> binderMap) {
     String propertyName = ((PropertyBinder) binder).getBoundProperty();

     List blist = binderMap.get(propertyName);
     if (blist == null) {
     blist = new ArrayList();
     }
     binder.addBinderListener(this);
     if (binder instanceof DocumentChangeListener) {
     addDocumentChangeListener((DocumentChangeListener) binder);
     }
     blist.add(binder);
     binderMap.put(propertyName, blist);
     }

     protected void add(PropertyBinder binder, List binderList) {
     binderList.add(binder);
     binder.addBinderListener(this);
     if (binder instanceof DocumentChangeListener) {
     addDocumentChangeListener((DocumentChangeListener) binder);
     }
     }
     */
    /**
     * Tries to resolve all pending component changes. Is invoked for the old
     * document and only from the documentChange's method body just before a new
     * document is set. Notifies all registered binders of the event of type
     * {@link org.document.DocumentChangeEvent} with an action set to
     * {@link org.document.DocumentChangeEvent.Action#completeChanges}.
     *
     * @see #documentChange(org.document.Document)
     */
    protected void completeChanges() {
        if (getDocument().propertyStore() == null) {
            return;
        }
        DocumentChangeEvent event = new DocumentChangeEvent(this, DocumentChangeEvent.Action.completeChanges);
        for (Binder b : binders) {
            if (b instanceof DocumentChangeListener) {
                ((DocumentChangeListener) b).react(event);
            }
        }
    }

    private void fireDocumentChanging(Document oldDoc, Document newDoc) {
//        if (this.documentListeners == null || documentListeners.isEmpty()) {
//            return;
//        }

        DocumentChangeEvent event = new DocumentChangeEvent(this, DocumentChangeEvent.Action.documentChanging);
        event.setOldValue(oldDoc);
        event.setNewValue(newDoc);
        for ( PropertyBinder b : binders ) {
            if ( b instanceof DocumentChangeListener ) {
                ((DocumentChangeListener)b).react(event);
            }
        }
//        for (DocumentChangeListener l : binders) {
//            l.react(event);
//        }
    }

    private void fireDocumentChanged(Document oldDoc, Document newDoc) {
//        if (this.documentListeners == null || documentListeners.isEmpty()) {
//            return;
//        }

        DocumentChangeEvent event = new DocumentChangeEvent(this, DocumentChangeEvent.Action.documentChange);
        event.setOldValue(oldDoc);
        event.setNewValue(newDoc);
        for (Binder b : binders) {
            if (b instanceof DocumentChangeListener) {
                ((DocumentChangeListener) b).react(event);
            }
        }

//        for (DocumentChangeListener l : documentListeners) {
//            l.react(event);
//        }
    }

    public String getChildName() {
        return this.childName;
    }

    protected void setChildName(String childName) {
        this.childName = childName;
    }

    protected void firePropertyChange(DocumentChangeEvent event) {
        for (Binder b : binders) {
            if (b instanceof DocumentChangeListener) {
                ((DocumentChangeListener) b).react(event);
            }
        }
    }

    public void bind(HasBinder component) {
        Binder b = component.getBinder();
        if (b instanceof PropertyBinder) {
            PropertyBinder pb = (PropertyBinder) b;
            String nm = pb.getBoundProperty();
            if (nm != null) {
                update(pb);
            }
        }
    }

    public void bind(String propertyName, ErrorBinder binder) {
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
       @Override
    public void initBoundObjectDefaults() {
    }

    @Override
    public void addBoundObjectListeners() {
    }

    @Override
    public void removeBoundObjectListeners() {
    }
 
    
}
