package org.document.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.document.Document;
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
public abstract class AbstractDocumentBinder<E extends Document> extends AbstractBinder implements BinderListener, ContextListener, ContainerBinder, PropertyChangeListener {//BinderContainer<T> {//extends AbstractDocumentBinder {
    private Document defaultDocument;

    private String className;
    private String alias;
    private boolean suspended;
    //protected List<BinderListener> contextOwner;
    protected BinderListener contextOwner;
    protected BindingContext context;
  //  protected String childName;
  //  protected List<DocumentBinder> childs;
    protected List<PropertyBinder> binders;
    protected DocumentErrorBinder documentErrorBinder;
    private boolean embedded;
//    private List<PropertyChangeListener> boundObjectListeners;

    protected AbstractDocumentBinder() {
        this(null, null);
    }

    protected AbstractDocumentBinder(Class supportedClass) {
        this(null, supportedClass);
    }

    protected AbstractDocumentBinder(String alias, Class supportedClass) {
        super(null);

        context = new DocumentBindingContext();
        contextOwner = new InternalBinderEventHandler();
        if (supportedClass != null) {
            className = supportedClass.getName();
        }
        this.alias = alias;
        if (alias == null || alias.trim().isEmpty()) {
            this.alias = "default";
        }
        //binderListener = new ArrayList<BinderListener>();
//        childs = new ArrayList<DocumentBinder>();
        documentErrorBinder = new DocumentErrorBinder();
        binders = new ArrayList<PropertyBinder>();

    }

    @Override
    public void initDefaults() {
        for (PropertyBinder b : binders) {
            b.initDefaults();
        }
    }

    public boolean isEmbedded() {
        return embedded;
    }

    public void setEmbedded(boolean embedded) {
        this.embedded = embedded;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        notifyAll(e);
    }

    public boolean canAccept(E document) {
        boolean b;
        if (document == null || "default".equals(getAlias())) {
            b = true;
        } else if (document.getClass().getName().equals(getClassName())) {
            b = true;
            if (document.propertyStore().getAlias() != null) {
                String psa = document.propertyStore().getAlias().toString();
                if (!psa.equals(getAlias())) {
                    b = false;
                }
            }
        } else {
            b = canAcceptSuper(document);
        }
        return b;
    }

    /**
     * @TODO @param document
     * @return
     */
    protected boolean canAcceptSuper(E document) {
        return false;
    }

    @Override
    public Iterator<PropertyBinder> iterator() {
        return binders.iterator();
    }

    @Override
    public String getAlias() {
        return alias == null ? "default" : alias;
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
        if (binder instanceof PropertyBinder) {
            return binders.contains((PropertyBinder) binder);
        }
        return false;
    }

    @Override
    public boolean remove(Binder binder) {
        if (binder instanceof PropertyBinder) {
            return binders.remove((PropertyBinder) binder);
        }
        return false;
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

        BinderEvent event = new BinderEvent(this, BinderEvent.Action.suspendBinding);
        notifyAll(event);

    }

    private void notifyAll(PropertyChangeEvent e) {
        for (Binder b : binders) {
            if (b instanceof PropertyChangeListener) {
                ((PropertyChangeListener) b).propertyChange(e);
            }
        }
    }

    private void notifyAll(BinderEvent e) {
        for (Binder b : binders) {
            if (b instanceof BinderListener) {
                ((BinderListener) b).react(e);
            }
        }
    }

    private void notifyAll(ContextEvent e) {
        for (Binder b : binders) {
            if (b instanceof ContextListener) {
                ((ContextListener) b).react(e);
            }
        }

    }

    public void resume(String propertyName) {
        BinderEvent event = new BinderEvent(this, BinderEvent.Action.resumeBinding);
        if (getContext() == null) {
            return;
        }
        event.setNewValue(getContext().getSelected());
        notifyAll(event);
    }

    protected E getDocument() {
        if (getContext() == null) {
            return null;
        }
        return (E) getContext().getSelected();
    }

    public Document getDefaultDocument() {
        return defaultDocument;
    }
    
    protected BindingContext getContext() {
        return context;
    }

    protected void beforeDocumentChange(ContextEvent e) {
        E newDoc = (E) e.getNewSelected();
        E oldDoc = (E) e.getOldSelected();
        PropertyStore oldDocumentStore = null;
        if (oldDoc != null) {
            oldDocumentStore = oldDoc.propertyStore();
        }

        if (oldDocumentStore != null) {
            completeChanges(e);
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
            notifyAll(e);
            //fireDocumentChanging(oldDoc, newDoc);
            //fireDocumentChanged(oldDoc, newDoc);
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
/*        for (DocumentBinder child : childs) {
            Object d = documentStore.getValue(child.getChildName());
            if (d == null) {
                child.beforeDocumentChange(e);
            } else if (d instanceof Document) {
                child.beforeDocumentChange(e);
            }
        }
        */ 
    }

    protected void afterDocumentChange(ContextEvent e) {
        E newDoc = (E) e.getNewSelected();
        E oldDoc = (E) e.getNewSelected();

        if (!isSuspended()) {
            notifyAll(e);
            //fireDocumentChanged(oldDoc, newDoc);
        }
/*        for (DocumentBinder child : childs) {
            Object d = newDoc.propertyStore().getValue(child.getChildName());
            if (d == null) {
                child.beforeDocumentChange(e);
            } else if (d instanceof Document) {
                child.beforeDocumentChange(e);
            }
        }
*/ 
    }
    
    private Document getInstance() {
        Document result = null;
        if ( getClassName() != null ) {
            try {
                Class c = Class.forName(getClassName());
                result = (Document)c.newInstance();
            } catch(Exception e) {
                System.out.println("Cannot create default document instance. " + e.getMessage());
            }
        }
        return result;
    }

    /**
     * May be called from the contained binders of type
     * <code>PropertyBinder</code>.
     *
     * @param event
     */
    @Override
    public void react(BinderEvent event) {
        if (getContext() == null || !canAccept(getDocument())) {
            return;
        }
        switch (event.getAction()) {
            /*            case refresh:
             fireRefresh(event);
             break;
             */
            case boundObjectChange:
                if (getDocument() != null) {
                    getDocument().propertyStore().putValue(event.getBoundProperty(), event.getNewValue());
                } else if ( isEmbedded() ) {
                    if ( defaultDocument == null ) {
                        defaultDocument = getInstance();
                    }
                    defaultDocument.propertyStore().putValue(event.getBoundProperty(), event.getNewValue());
                }
                documentErrorBinder.clear(event.getBoundProperty());
                break;
            case clearError:
                documentErrorBinder.clear(event.getBoundProperty());
                break;
            case propertyError:
                documentErrorBinder.notifyError(event.getBoundProperty(), event.getException());
                break;
            case boundObjectReplace:
            case boundPropertyReplace:
                fireRefreshBinder((PropertyBinder) event.getSource());
                break;
        }
    }

    public void setDocument(E document) {
        ContextEvent e = new ContextEvent(context, ContextEvent.Action.documentChanging);
        e.setNewSelected(document);
        e.setOldSelected(context.getSelected());
        react(e);
        ((DocumentBindingContext) context).setSelected(document);
        if (context.getSelected() != null) {
            context.getSelected().propertyStore().addPropertyChangeListener(this);
        }
        e.setAction(ContextEvent.Action.documentChange);
        react(e);
    }

    @Override
    public void react(ContextEvent event) {

        BindingContext c = (BindingContext) event.getSource();
        if (event.getAction() == ContextEvent.Action.register) {
            if (getDocument() != null) {
                getDocument().propertyStore().removePropertyChangeListener(this);
            }
            context = c;
//            event.setAction(ContextEvent.Action.updateContainerContext);
        } else if (event.getAction() == ContextEvent.Action.unregister) {
            context = new DocumentBindingContext();
            contextOwner = new InternalBinderEventHandler();
//                event.setAction(ContextEvent.Action.updateContext);
        } else {
            context = c;
        }
        if (!canAccept(getDocument())) {
            return;
        }

        switch (event.getAction()) {
            case documentChanging:
                beforeDocumentChange(event);
                break;
            case documentChange:
                afterDocumentChange(event);
                break;
            case updateContainerContext:
                notifyAll(event);
            case activeStateChange:
                notifyAll(event);
                break;

        }
    }

    protected void fireRefreshBinder(PropertyBinder binder) {
        BinderEvent e = new BinderEvent(this, BinderEvent.Action.refresh);
        ((BinderListener) binder).react(e);

    }

    @Override
    public void addBinderListener(BinderListener l) {
        contextOwner = l;
    }

    @Override
    public void removeBinderListener(BinderListener l) {
        contextOwner = null;
    }

    public void remove(PropertyBinder binder) {
        if (binder == null) {
            return;
        }
        binders.remove(binder);
//        binder.setContext(null);
        binder.removeBinderListener(this);
        initDefaults();

        //TODOsetDocument(document); // to refresh to reflect changes

    }

    public void add(PropertyBinder binder) {
        if (binder == null) {
            return;
        }
//        binder.setContext(this.context());
        String propertyName = binder.getBoundProperty();
        if (propertyName == null || binder.getBoundObject() == null) {
            throw new IllegalArgumentException(
                    "The PropertyBinder must have a not null boundProperty and boundObject");
        }

        if (!binders.contains(binder)) {
            binders.add(binder);
            binder.addBinderListener(this);
        }
        BinderEvent e = new BinderEvent(this, BinderEvent.Action.containerBinderContent);
        contextOwner.react(e);

        if (propertyName != null) {
            if (isSuspended()) {
                suspend(propertyName);
            } else {
                resume(propertyName);
            }
        }
    }

    /**
     * Tries to resolve all pending component changes. Is invoked for the old
     * document and only from the beforeDocumentChange's method body just before
     * a new document is set. Notifies all registered binders of the event of
     * type {@link org.document.DocumentChangeEvent} with an action set to
     * {@link org.document.DocumentChangeEvent.Action#completeChanges}.
     *
     * @see #beforeDocumentChange(org.document.Document)
     */
    protected void completeChanges(ContextEvent e) {
        E oldDoc = (E) e.getOldSelected();
        if (oldDoc.propertyStore() == null) {
            return;
        }
        BinderEvent event = new BinderEvent(this, BinderEvent.Action.completeChanges);
        event.setOldValue(oldDoc);
        notifyAll(event);
    }

    private void fireRefresh(BinderEvent event) {
        notifyAll(event);
    }

/*    public String getChildName() {
        return this.childName;
    }

    protected void setChildName(String childName) {
        this.childName = childName;
    }
*/
    /*    protected void firePropertyChangeRequest(BinderEvent event) {
     if (contextOwner != null) {
     contextOwner.react(event);
     }
     }
     */
    public DocumentErrorBinder getDocumentErrorBinder() {
        return documentErrorBinder;
    }

    public DocumentErrorBinder errorBinders() {
        return documentErrorBinder;
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

    public class InternalBinderEventHandler implements BinderListener {

        BinderEvent.Action action; // for test only purpose

        public InternalBinderEventHandler() {
        }

        /**
         * Handles events of type
         * <code>DocumentChangeEvent</code>. When any property value of a
         * document changes the method is called as an implementation of the {@link org.document.DocumentChangeListener
         * }. listener The special case is when an editing state changes. The
         * event has it's property
         * {@link org.document.DocumentChangeEvent#propertyName} equals to
         * <code>"document.state.editing"</code>. In this case the method
         * cancels <i>newMark<i> of a selected document if the last is marked as
         * <i>new</i>..
         *
         * @param event the event to be handled
         * @see org.document.DocumentChangeEvent
         */
        @Override
        public void react(BinderEvent event) {
            switch (event.getAction()) {
                case containerBinderContent:
                    /*                    if ( isActive() ) {
                     registry.notify(new ContextEvent(bindingContext,ContextEvent.Action.updateContext));
                     }
                     */
                    break;
                /*case boundObjectChange:
                 if ( "*selected".equals(event.getPropertyName()) ) {
                 if ( event.getNewValue() instanceof Document ) {
                 setSelected((T)event.getNewValue());
                 }
                 break;
                 } else if ( "*documentList".equals(event.getPropertyName()) ) {
                 break;
                 }
                            
                            
                 try {
                 if (getSelected() != null) {
                 getSelected().propertyStore().putValue(event.getBoundProperty(), event.getNewValue());
                 }
                 } catch (Exception e) {
                 System.out.println("ERRRRRRRRRRRR");
                 }
                 */
            }
        }
    }//class InternalBinderEventHandler

    public class PropertyChangeHandler implements PropertyChangeListener {

        public PropertyChangeHandler() {
        }

        /**
         * Handles events of type
         * <code>DocumentChangeEvent</code>. When any property value of a
         * document changes the method is called as an implementation of the {@link org.document.DocumentChangeListener
         * }. listener The special case is when an editing state changes. The
         * event has it's property
         * {@link org.document.DocumentChangeEvent#propertyName} equals to
         * <code>"document.state.editing"</code>. In this case the method
         * cancels <i>newMark<i> of a selected document if the last is marked as
         * <i>new</i>..
         *
         * @param event the event to be handled
         * @see org.document.DocumentChangeEvent
         */
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            //registry.notify(e);
        }
    }//class PropertyChangeHandler

    public class DocumentBindingContext implements BindingContext {

        private Document selected;

        DocumentBindingContext() {
        }

        @Override
        public Document getSelected() {
            return selected;
        }

        public void setSelected(Document document) {
            selected = document;
        }
    }//class DocumentBindingContext
}//class AbstractDocumentBinder
