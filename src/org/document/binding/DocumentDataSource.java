/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.document.Document;
import org.document.DocumentList;
import org.document.DocumentState;
import org.document.HasState;
import org.document.ListChangeEvent;
import org.document.ListChangeListener;

/**
 *
 * @author Valery
 */
public class DocumentDataSource<T extends Document> implements ListChangeListener {

    private DataSourceContext bindingContext;
    private Registry registry;
    private BinderEventHandler binderEventHandler;
    private PropertyChangeHandler documentChangeHandler;
    private ListChangeHandler listChangeHandler;
    /**
     * Stores a reference to a document that is declared as
     * <code>selected</code>.
     */
    protected T selected;
    private boolean active;
    /**
     * Contains custom
     * <code>DocumentRecognizer</code> if defined.
     */
    protected BindingRecognizer recognizer;
    /**
     * List of documents set by a constructor call.
     */
    private List<T> sourceList;
    private DocumentList documentList;

    /**
     * Create an instance of the class. Instances created with this constructor
     * may set any document as
     * <code>selected</code>.
     */
    public DocumentDataSource() {
        registry = new DocumentDataSource.Registry();
        binderEventHandler = new BinderEventHandler();
        documentChangeHandler = new PropertyChangeHandler();
        listChangeHandler = new ListChangeHandler();
        bindingContext = new DataSourceContext(this);
    }

    /**
     * Creates an instance of the class for a given list of documents. If the
     * parameter is not
     * <code>null</code> then only documents that the list contains may be
     * declared
     * <code>selected</code>. <p> First of all a new object of type
     * {@link DocumentList} is created. It is a wrapper list of the parameter
     * <code>sourceList</code>. Each document in the list is assigned a listener
     * of an event of type {@link DocumentChangeEvent}. From this point the
     * binding manager becomes aware of every change in a document content.
     *
     * @param sourceList the list of documents
     */
    public DocumentDataSource(List<T> sourceList) {
        this();
        if (sourceList == null) {
            return;
        }
        this.sourceList = sourceList;

        initSourceList(sourceList);
    }

    private void initSourceList(List sourceList) {
        DocumentList<T> dl = new DocumentList(sourceList);
        for (T d : dl) {
            d.propertyStore().addPropertyChangeListener(documentChangeHandler);
            updateAttachState(d, true);
        }

        this.documentList = dl;
        setSelected(dl.isEmpty() ? null : dl.get(0));
        getDocuments().addListChangeListener(listChangeHandler);


        if (!isActive()) {
            //setActive(true);
        }
    }

    /**
     * Looks for an existing or creates a new container for the
     * <code>alias</code> extracted from the specified
     * <code>binder</code> and adds the
     * <code>binder</code> to it.
     *
     * @param binder the binder to be added.
     * @return  <code>true</code> if success
     * @throws IllegalArgumentException in case when the specified binder has
     * already been registered
     */
    public boolean add(PropertyBinder binder) {
        return registry.add(binder.getAlias(), binder);
    }
    public DocumentBinder add(String alias,DocumentBinder binder) {
        binder.setAlias(alias);
        return registry.put(alias, binder);
    }
    
    public DocumentBinder add(DocumentBinder binder) {
        binder.setAlias("default");
        return registry.put("default", binder);
    }
    public ContainerBinder add(ContainerBinder binder) {
        binder.setAlias("default");
        return registry.put("default",binder);
    }
    public ContainerBinder add(String alias,ContainerBinder binder) {
        binder.setAlias(alias);
        return registry.put(alias,binder);
    }

    public boolean add(Binder binder) {
        return registry.add(binder);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
//TODO        if (this.active == active) {
//            return;
//        }
        this.active = active;
        Document oldDoc = selected;
        if (active) {
            this.active = active;
            if (sourceList != null) {
                if (sourceList.isEmpty()) {
                    setSelected(null);
                } else {
                    setSelected(sourceList.get(0));
                }
            } else {
                setSelected(selected);
            }
        }
        ContextEvent e = new ContextEvent(bindingContext, ContextEvent.Action.activeStateChange);
        e.setNewSelected(selected);
        e.setOldSelected(oldDoc);
        registry.notifyAll(e);

    }

    public DocumentList getDocuments() {
        return documentList;
    }

    /**
     * @return an object of type {@link org.document.Document)
     * or it's subtype that currently is set as <i><code>selected</code></i>.
     */
    public T getSelected() {
        return (T) selected;
    }

    /**
     * Sets a given object
     * <code><i>selected</i></code>. Assigns a new document to all appropriate
     * objects of type {@link DocumentBinder}.
     *
     * @param selected an object to be set <code>selected</code>
     */
    public void setSelected(T selected) {
        if (sourceList == null) {
            setDocument(selected);
            return;
        }
        if (!isActive()) {
            return;
        }
        Document old = selected;
        //
        // Notify BEFORE new selected is set
        //
        fireDocumentChanging(old, selected);
        this.selected = selected;
        //
        // Notify AFTER new selected is set
        //
        fireDocumentChange(old, selected);
    }

    /*    void fireDocumentChanging(Document oldSelected, Document newSelected) {
     DocumentChangeEvent e = new DocumentChangeEvent(this);
     e.setAction(DocumentChangeEvent.Action.documentChanging);
     e.setNewValue(newSelected);
     e.setOldValue(oldSelected);
     registry.notifyAll(e);
     }

     void fireDocumentChange(Document oldSelected, Document newSelected) {
     DocumentChangeEvent e = new DocumentChangeEvent(this);
     e.setAction(DocumentChangeEvent.Action.documentChange);
     e.setNewValue(newSelected);
     e.setOldValue(oldSelected);
     registry.notifyAll(e);
     }
     */
    void fireDocumentChanging(Document oldSelected, Document newSelected) {
        ContextEvent e = new ContextEvent(bindingContext, ContextEvent.Action.documentChanging);
        e.setNewSelected(newSelected);
        e.setOldSelected(oldSelected);
        registry.notifyAll(e);
    }

    void fireDocumentChange(Document oldSelected, Document newSelected) {
        ContextEvent e = new ContextEvent(bindingContext, ContextEvent.Action.documentChange);
        e.setNewSelected(newSelected);
        e.setOldSelected(oldSelected);
        registry.notifyAll(e);
    }

    /**
     * Sets a given object
     * <code><i>selected</i></code>. Assigns a new document to all appropriate
     * objects of type {@link DocumentBinder}. <P>The method provides the same
     * functionality as null null null null null null null null null null null
     * null null null null     {@link #beforeDocumentChange(org.document.Document) and those two methods may be 
     * used interchangebly.
     *
     * @param selected an object to be set <code>selected</code>
     */
    public void setDocument(T selected) {
        if (sourceList != null) {
            setSelected(selected);
            return;
        }
        this.active = true;
        T old = this.selected;
        if (old != null && (old.propertyStore() instanceof HasState)) {
            ((HasState) old.propertyStore()).getDocumentState().setAttached(false);
        }
        if (selected != null && (selected.propertyStore() instanceof HasState)) {
            ((HasState) selected.propertyStore()).getDocumentState().setAttached(true);
        }
        // TODO this.registry.beforeDocumentChange(selected);
    }

    protected void updateAttachState(T doc, boolean attached) {

        if (doc.propertyStore() instanceof HasState) {
            DocumentState st = ((HasState) doc.propertyStore()).getDocumentState();
            if (st.isAttached()) {
//TODO                 doc.propertyStore().removeDocumentChangeListener(documentChangeHandler());
            }
            if (attached) {
//TODO                doc.propertyStore().addDocumentChangeListener(documentChangeHandler());                
            }
            st.setAttached(attached);
        }
    }

    public List<T> getSourceList() {
        return sourceList;
    }

    public void setSourceList(List sourceList) {
        boolean oldActive = active;
        if (active) {
            setActive(false);
        }

        if (getDocuments() != null) {
            getDocuments().removeListChangeListener(this);
        }

        this.sourceList = sourceList;

        initSourceList(sourceList);
        setActive(oldActive);
    }

    @Override
    public void listChanged(ListChangeEvent event) {
        if (event.getAction() == ListChangeEvent.Action.beforeClear
                || event.getAction() == ListChangeEvent.Action.beforeRemoveAll
                || event.getAction() == ListChangeEvent.Action.beforeRetainAll) {
            return;
        }
        updateDocumentState(event);

        T newSel = selected;
        DocumentList<T> list = (DocumentList) event.getSource();
        if (event.getAction() == ListChangeEvent.Action.addAndSelect && (Boolean) event.getResult()) {
            newSel = list.getLast();
        } else if (!list.contains(selected)) {
            T oldSel = null;
            if (event.getAction() == ListChangeEvent.Action.set) {
                oldSel = (T) event.getResult();
            }
            if (oldSel == selected) {
                newSel = list.get(event.getIndex());
            } else {
                newSel = list.isEmpty() ? null : list.get(0);
            }
            if (selected != null) {
//TODO                 DocumentBinder b = registry.documentBinderOf(selected);
//TODO                if ( b != null ) {
//TODO                    selected.propertyStore().removeDocumentChangeListener(b);
//TODO                }
            }
        }
        // We assign the same list
//        getBindingState().setDocumentList(list);

        setSelected(newSel);

    }

    protected void updateDocumentState(ListChangeEvent e) {
        switch (e.getAction()) {
            case add:
            case append:
                if ((Boolean) e.getResult()) {
                    this.updateAttachState((T) e.getElement(), true);
                }
                break;
            case addAndSelect:
                break;
            case removeObject:
                if ((Boolean) e.getResult()) {
                    this.updateAttachState((T) e.getElement(), false);
                }
                break;
            case remove:
                this.updateAttachState((T) e.getElement(), false);
                break;
            case set:
                if (e.getResult() != null) {
                    //old document
                    this.updateAttachState((T) e.getResult(), false);
                }
                if (e.getElement() != null) {
                    //new document
                    this.updateAttachState((T) e.getElement(), true);
                }
                break;
            case beforeClear:
                DocumentList<T> l = (DocumentList) e.getCollection();
                for (T d : l) {
                    updateAttachState(d, false);
                }
                break;
            case appendAll:
            case addAll:
                l = (DocumentList) e.getCollection();
                if ((Boolean) e.getResult()) {
                    for (T d : l) {
                        updateAttachState(d, true);
                    }
                }
                break;
            case beforeRemoveAll:
                if (getDocuments() == null) {
                    break;
                }
                l = (DocumentList) e.getCollection();
                DocumentList dl = getDocuments();
                if ((Boolean) e.getResult()) {
                    for (T d : l) {
                        if (dl.contains(d)) {
                            updateAttachState(d, false);
                        }
                    }
                }
                break;
            case beforeRetainAll:
                if (getDocuments() == null) {
                    break;
                }
                l = (DocumentList) e.getCollection();
                dl = getDocuments();
                if ((Boolean) e.getResult()) {
                    for (T d : l) {
                        if (!dl.contains(d)) {
                            updateAttachState(d, false);
                        }
                    }
                }
                break;

        }//switch
    }

    public class Registry {

        private Map<String, ContainerBinder> containerBinders;
        private Map<String, DocumentBinder> documentBinders;
        private List<Binder> binders;

        public Registry() {
            documentBinders = new HashMap<String, DocumentBinder>();
            containerBinders = new HashMap<String, ContainerBinder>();
            binders = new ArrayList<Binder>();
        }

        public void notifyAll(ContextEvent e) {
            if ( e.getAction() == ContextEvent.Action.register ||
                 e.getAction() == ContextEvent.Action.unregister   ) {
                return;
            }
            for (Binder b : documentBinders.values()) {
                if (b instanceof ContextListener) {
                    ((ContextListener) b).react(e);
                }
            }

            for (Binder b : containerBinders.values()) {
                if (b instanceof ContextListener) {
                    ((ContextListener) b).react(e);
                }
            }
            for (Binder b : binders) {
                if (b instanceof ContextListener) {
                    ((ContextListener) b).react(e);
                }
            }

        }

        public void notifyAll(PropertyChangeEvent e) {
            for (Binder b : documentBinders.values()) {
                if (b instanceof PropertyChangeListener) {
                    ((PropertyChangeListener) b).propertyChange(e);
                }
            }

            for (Binder b : containerBinders.values()) {
                if (b instanceof PropertyChangeListener) {
                    ((PropertyChangeListener) b).propertyChange(e);
                }
            }
            for (Binder b : binders) {
                if (b instanceof PropertyChangeListener) {
                    ((PropertyChangeListener) b).propertyChange(e);
                }
            }

        }

        /*        public void notifyAll(BinderEvent e) {
         for (Binder b : documentBinders.values()) {
         if (b instanceof BinderListener) {
         ((BinderListener) b).react(e);
         }
         }

         for (Binder b : containerBinders.values()) {
         if (b instanceof BinderListener) {
         ((BinderListener) b).react(e);
         }
         }
         for (Binder b : binders) {
         if (b instanceof BinderListener) {
         ((BinderListener) b).react(e);
         }
         }

         }
         */
        public void notify(DocumentBinder binder, ContextEvent e) {
            binder.react(e);
        }
        public void notify(Binder binder, ContextEvent e) {
            if ( binder instanceof ContextListener ) {
                ((ContextListener)binder).react(e);
            }
        }
        public void notify(ContainerBinder binder, ContextEvent e) {
            if ( binder instanceof ContextListener ) {
                ((ContextListener)binder).react(e);
            }
        }
        
        public void notifyAll(ListChangeEvent e) {

            for (Binder b : containerBinders.values()) {
                if (b instanceof ListChangeListener) {
                    ((ListChangeListener) b).listChanged(e);
                }
            }
            for (Binder b : documentBinders.values()) {
                if (b instanceof ListChangeListener) {
                    ((ListChangeListener) b).listChanged(e);
                }
            }

            for (Binder b : binders) {
                if (b instanceof ListChangeListener) {
                    ((ListChangeListener) b).listChanged(e);
                }
            }
        }

        /**
         * Creates an object of type
         * <code>DocumentBinder</code>, with the specified alias name and
         * document class name and and adds it to the collection of binders. If
         * the collection already contains a binder with the specified
         * <code>alias</code> then the method throws an exception.
         *
         * @param alias
         * @param documentClass
         * @return
         */
        public DocumentBinder add(String alias, Class documentClass) {
            if (containerBinders.containsKey(alias)) {
                throw new IllegalArgumentException("The the specified alias "
                        + "  has already been registered");
            }

            String name = documentClass == null ? null : documentClass.getName();
            DocumentBinder result = new DocumentBinder();

            result.setAlias(alias);
            result.setClassName(name);
//            result.setContext(bindingContext);
            put(alias, result);

            return result;
        }

        /**
         * Removes the container of of type
         * <code>DocumentBinder</code> for the specified
         * <code>alias</code>. Sets the property with a name
         * <code>context</code> to
         * <code>null</code> for the
         * <code>DocumentBinder</code> itself and all contained binders.
         *
         * @param alias to search for.
         */
        public void removeDocumentBinder(String alias) {
            
            DocumentBinder cb = documentBinders.remove(alias);
            if (cb != null) {
                // We we must unregister before remove binderListener
                ContextEvent e = new ContextEvent(bindingContext, ContextEvent.Action.unregister);
                e.setNewSelected(null);
                registry.notify(cb,e);

                //cb.initDefaults();

            }
        }

        /**
         * Removes the container of of type
         * <code>ContainerBinder</code> for the specified
         * <code>alias</code>. If the the
         * <code>ContainerBinder</code> implements {@link HasContext} then sets
         * the property with a name
         * <code>context</code> to
         * <code>null</code>.
         *
         * For all contained binders that implement
         * <code>HasBinder</code> sets the property with a name
         * <code>context</code> to
         * <code>null</code>.
         *
         * @param alias to search for.
         */
        public void removeContainerBinder(String alias) {
            ContainerBinder cb = containerBinders.remove(alias);
            if (cb != null) {
                ContextEvent e = new ContextEvent(bindingContext, ContextEvent.Action.unregister);
                e.setNewSelected(null);
                registry.notify(cb,e);
            }

        }

        /**
         * Looks for the specified binder in all internal collections an removes
         * it.
         *
         * @param binder the object to be removed
         * @return <code>true</code> if the <code>binder</code> exists.
         * <code>false</code>otherwise</code>
         */
        public boolean remove(Binder binder) {
            boolean result = false;
            ContainerBinder toRemove = null;
            for (DocumentBinder cb : documentBinders.values()) {
                if (cb.contains(binder)) {
                    toRemove = cb;
                    break;
                }
            }
            if (toRemove != null) {
                toRemove.remove(binder);
                return true;
            }

            for (ContainerBinder cb : containerBinders.values()) {
                if (cb.contains(binder)) {
                    toRemove = cb;
                    break;
                }
            }
            if (toRemove != null) {
                toRemove.remove(binder);
                return true;
            }

            if (binders.remove(binder)) {
                result = true;
                ContextEvent e = new ContextEvent(bindingContext, ContextEvent.Action.unregister);
                e.setNewSelected(bindingContext.getSelected());
                registry.notify(binder,e);
                binder.removeBinderListener(binderEventHandler);

            }
            return result;
        }

        /**
         * Looks for an existing or creates a new container for the specified
         * <code>alias</code> and adds the specified
         * <code>binder</code> to it.
         *
         * @param alias to search for
         * @param binder the binder to be added.
         * @return  <code>true</code> if success
         * @throws IllegalArgumentException in case when the specified binder
         * has already been registered
         * @throws IllegalArgumentException in case when the specified binder
         * has the alias name whose value differ from the specified
         */
        public boolean add(String alias, PropertyBinder binder) {
            boolean result = true;
            // lookup for each collection
            if (exists(binder)) {
                throw new IllegalArgumentException("The the specified property "
                        + " binder has already been registered");

            }
            alias = alias == null || alias.trim().isEmpty() ? "default" : alias;
            if (!alias.equals(binder.getAlias())) {
                throw new IllegalArgumentException("The binder has the alias name that differ from"
                        + " the specified)");
            }

            DocumentBinder target = documentBinders.get(alias);
            if (target == null) {
                target = new DocumentBinder();
                target.setAlias(alias);
                put(alias, target);
//                target.setContext(bindingContext);
            }
            binder.setAlias(alias);
            //binder.setContext(bindingContext);
            target.add(binder); // DocumentBinder will set context
            return result;
        }

        /**
         * Looks for a container for the specified
         * <code>alias</code> and adds the specified
         * <code>binder</code> to it. Assigns the
         * <code>context</code> property value of the given
         * <code>binder</code>.
         *
         * @param alias to search for
         * @param binder the binder to be added.
         * @return  <code>true</code> if success
         * @throws IllegalArgumentException in case when the specified binder
         * has already been registered
         * @throws IllegalArgumentException in case when the container binder
         * cannot be found for the given <code>alias<code>
         */
        public boolean add(String alias, Binder binder) {
            boolean result = true;
            if (exists(binder)) {
                throw new IllegalArgumentException("The the specified property "
                        + " binder has already been registered");

            }
            alias = alias == null || alias.trim().isEmpty() ? "default" : alias;
            ContainerBinder target = containerBinders.get(alias);
            if (target == null) {
                throw new IllegalArgumentException("The container binder for  the specified "
                        + " alias='" + alias + "' cannot be found");

            }
            target.add(binder);
/*            if (binder instanceof HasContext) {
                ((HasContext) binder).setContext(bindingContext);
            }
*/ 
            return result;
        }

        public DocumentBinder put(String alias, DocumentBinder binder) {
            DocumentBinder result = documentBinders.put(alias, binder);
            // We musr register before add or remove binder listener
            ContextEvent e = new ContextEvent(bindingContext, ContextEvent.Action.register);
            e.setNewSelected(bindingContext.getSelected());
            registry.notify(binder,e);
            
            binder.removeBinderListener(binderEventHandler);
            binder.addBinderListener(binderEventHandler);

            return result;
        }

        public ContainerBinder put(String alias, ContainerBinder binder) {
            ContainerBinder result =  containerBinders.put(alias, binder);
            ContextEvent e = new ContextEvent(bindingContext, ContextEvent.Action.register);
            e.setNewSelected(bindingContext.getSelected());
            registry.notify(binder,e);
            
            binder.removeBinderListener(binderEventHandler);
            binder.addBinderListener(binderEventHandler);            
            return result;
        }

        /**
         * Adds the specified
         * <code>binder</code> to the internal collection. The collection
         * contains objects of type {@link Binder} and may contains the binders
         * of any type.
         *
         * Assigns the
         * <code>context</code> property value of the given
         * <code>binder</code>.
         *
         * @param binder the binder to be added.
         * @return  <code>true</code> if success
         * @throws IllegalArgumentException in case when the specified binder
         * has already been registered
         */
        public boolean add(Binder binder) {
            boolean result = true;
            if (exists(binder)) {
                // TODO
            }
            binders.add(binder);
//            if (result && (binder instanceof HasContext)) {
//                ((HasContext) binder).setContext(bindingContext);
//            }
            ContextEvent e = new ContextEvent(bindingContext, ContextEvent.Action.register);
            e.setNewSelected(bindingContext.getSelected());
            registry.notify(binder,e);

            binder.addBinderListener(binderEventHandler);
            
            return result;
        }

        /**
         * Looks for an existing or creates a new container for the
         * <code>alias</code> extracted from the specified
         * <code>binder</code> and adds the
         * <code>binder</code> to it.
         *
         * @param binder the binder to be added.
         * @return  <code>true</code> if success
         * @throws IllegalArgumentException in case when the specified binder
         * has already been registered
         */
        public boolean add(PropertyBinder binder) {
            return add(binder.getAlias(), binder);
        }

        private boolean exists(Binder binder) {
            boolean result = false;
            for (DocumentBinder b : documentBinders.values()) {
                if (b == binder) {
                    result = true;
                    break;
                }
                if (b.contains(binder)) {
                    result = true;
                    break;
                }
            }

            for (ContainerBinder b : containerBinders.values()) {
                if (b == binder) {
                    result = true;
                    break;
                }
                if (b.contains(binder)) {
                    result = true;
                    break;
                }
            }
            if (!result) {
                if (binders.contains(binder)) {
                    result = true;
                }
            }
            return result;

        }

        /*        private String extractAlias(Binder binder) {
         String alias = null;
         if (binder instanceof HasAlias) {
         alias = ((HasAlias) binder).getAlias();
         if (alias == null || alias.trim().isEmpty()) {
         alias = "default";
         }
         }
         return alias;

         }
         */
    }//Registry

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
            registry.notifyAll(e);
        }
    }

    public class BinderEventHandler implements BinderListener {

        BinderEvent.Action action; // for test only purpose

        public BinderEventHandler() {
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
//                    if ( isActive() ) {
                    registry.notify((ContainerBinder)event.getSource(),new ContextEvent(bindingContext, ContextEvent.Action.updateContainerContext));
//                    }
                    break;
                case boundObjectChange:
                    if ("*selected".equals(event.getPropertyName())) {
                        if (event.getNewValue() instanceof Document) {
                            setSelected((T) event.getNewValue());
                        }
                        break;
                    } else if ("*documentList".equals(event.getPropertyName())) {
                        break;
                    }


/*                    try {
                        if (getSelected() != null) {
                            getSelected().propertyStore().putValue(event.getBoundProperty(), event.getNewValue());
                        }
                    } catch (Exception e) {
                        System.out.println("ERRRRRRRRRRRR");
                    }
                    break;
*/ 
            }
        }
    }

    /**
     * Handles events of type
     * <code>ListChangeEvent</code>.
     *
     * @param event the event to be handled
     * @see org.document.ListChangeEvent
     */
    public class ListChangeHandler implements ListChangeListener {

        ListChangeEvent.Action action; // for test only purpose

        public ListChangeHandler() {
        }

        /**
         * Handles events of type
         * <code>ListChangeEvent</code>.
         *
         * @param event the event to be handled
         * @see org.document.ListChangeEvent
         */
        @Override
        public void listChanged(ListChangeEvent event) {
        }
    }
}//class
