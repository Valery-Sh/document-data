
package org.document.binding;

import java.util.*;
import java.util.Map.Entry;
import org.document.*;
/**
 * This class allows to mark any object of type {@link org.document.Document }
 * as <code><i>selected</i></code> and start binding procedure for it.  
 *
 * The method {@link setDocument(Document) } announces the 
 * specified document as a <code><i>selected</i></code>. Method 
 * {@link getDocument() } returns a document that is declared as selected, 
 * and the method {@link isSelected(Document) } serves to determine whether
 * a given document is declared <code><i>selected</i></code>. <p> 
 * The class maintains a collection of objects of type 
 * {@link DocumentBinder} . These objects are responsible for the
 * binding properties of the document with the other (visual or not)
 * objects. <p>
 * Class <code>DocumentBinder}</code>  has a <code>document</code> 
 * property and corresponding access methods 
 * {@link DocumentBinder#setDocument(Document)}
 * and {@link DocumentBinder#getDocument() } . When the binding 
 * manager declares the document <code><i>selected</i></code>, 
 * it looks for an appropriate <code>DocumentBinder</code> and 
 * applied to the found one the method <code>setDocument</code>.  
 *
 * The class <code>BindingManager</code> can be created using a 
 * constructor whose parameter is a list (<code>java.util.List</code>)
 * of documents. In this case, only if the document
 * is contained in the list then it can be declared <code><i>selected</i></code>.
 * <p>
 * If the document is created using the constructor without 
 * specifying a list of documents, any document can be declared as
 * <code><i>selected</i></code>.
 *<p>
 * <code>BindingManager</code> class is a factory of objects of type
 * <code>DocumentBinder</code>. These objects are stored in the 
 * Map-table. To access  the object one of three overloaded 
 * methods may be used:
 * <code>
 *<ul>
 *	<li>DocumentBinder documentBinderOf(String alias)</li> 
 *	<li>DocumentBinder documentBinderOf(Class clazz)</li>
 *	<li>DocumentBinder documentBinderOf()</li>
 *</ul>
 * </code>
 * <p>
 * The second methods just calls the first with a parameter value that equals to 
 * <code>clazz.getName()</code>. The third method also calls the first 
 * with a string parameter value that equals to  <code>"default"</code>.
 * The resulting object is used as the <code>key</code> to 
 * the map-table of objects of type <code>DocumentBinder</code>.
 *
 * When invoke a method <code>documentBinderOf</code> , then if the 
 * table already has an object with the appropriate key, then it 
 * returns. Otherwise, the method creates a new instance of the 
 * <code>DocumentBinder</code> and put it in the map-table.
 * <p>
 *
 * <code>BindingManager</code> does not impose any restrictions 
 * on the actual document classes. The only requirement is that 
 * every document should be a class that implements the interface
 * <code>Document</code>.
 * <p>
 * This raises a problem related to the way in which
 * <code>BinidingManager</code> looks for an appropriate
 * <code>DocumentBinder</code> to a given document. For this purpose serves a 
 * method named {@link #documentBinderOf(org.document.Document) }.
 *
 * The {@link org.document.binding.BindingRecognizer } interface helps to solve this 
 * problem. The application developer can create a class that 
 * implements this interface and assign it's instance to the 
 * <code>BindingManager</code>. It may be necessary if the 
 * built-in recognition does not satisfy the requirements of the
 * application.
 * <p>
 * Built-in lookup of an object of type <code>DocumentBinder</code>
 * is based on the property with a name <code><i>alias</i></code>,
 * which the {@link org.document.PropertyStore} defines.
 * <p>
 * Classes of type <code>Document</code> are closely related to objects of type
 * <code>PropertyStore</code>. These objects do have a property
 * <code><i>alias</i></code>. In the standard implementation the 
 * objects that implement <code>PropertyStore</code>, are the 
 * objects of class {@link org.document.DocumentPropertyStore } . 
 * The constructor of this class takes a parameter of type 
 * <code>Document document</code> creates a string  
 * <code>document.getClass().getName()</code> and set it as a value of 
 * <code>alias</code>.
 *  When a <code>BindingManager</code> looks for a <code>DocumentBinder</code> 
 * in the table, then, if a <code>BindingRecognizer</code> is not 
 * <code>null</code>, it is used to search for, otherwise,
 * <code>BindingManager</code> retrieves the value of the
 * <code>alias</code> from the document's <code>PropertyStore</code> and 
 * uses that value as the key to the map-table. If the search is 
 * successful then a <code>DocumentBinder</code> returned. If not, 
 * the search continues for the key <code>"default"</code>.
 * If not found, then <code>null</code> value is returned and the 
 * document will not be used for binding.
 *<p>
 * When and what method should be used in order to get
 * <code>DocumentBinder</code> depends on the application. 
 * If you know that <code>BindingManager</code> will be used
 * for processing the same type of documents, for example,
 * <code>Person</code>, then the method without parameters or
 * documentBinderOf(Person.class) fit.
 * 
 * @author V. Shishkin
 */


public class DocumentDataSource<T extends Document>  implements BinderListener,ListChangeListener {

    private BindingContext bindingContext;
    
    private DocumentDataSource.Container documentBinders;
    /**
     *  Stores a reference to a document that is declared as <code>selected</code>.
     */
    protected T selected;
    
    private boolean active;
    /**
     * Contains custom <code>DocumentRecognizer</code> if defined.
     */
    protected BindingRecognizer recognizer;
    /**
     * List of documents set by a constructor call.
     */
    private List<T> sourceList;
    private BindingState bindingState;
    private Map<Object,BindingStateBinder> documentListBinders;

    /**
     * Create an instance of the class. 
     * Instances created with this constructor may set
     * any document as <code>selected</code>.
     */
    public DocumentDataSource() {
        documentBinders = new DocumentDataSource.Container();
        bindingContext = new BindingContext(this);
    }
    
    /**
     * Creates an instance of the class for a given list of documents.
     * If the parameter is not <code>null</code> then only 
     * documents that the list contains may be declared 
     * <code>selected</code>. <p>
     * First of all a new object of type {@link DocumentList} is created. It is
     * a wrapper list of the parameter <code>sourceList</code>. Each document 
     * in the list is assigned a listener of an event of type 
     * {@link DocumentChangeEvent}. From this point the binding manager becomes
     * aware of every change in a document content.
     * 
     * @param sourceList the list of documents
     */
    public DocumentDataSource(List<T> sourceList) {
        this();
        if ( sourceList == null ) {
            return;
        }
        this.sourceList = sourceList;
        this.bindingState = new BindingState();

        initSourceList(sourceList);
    }
    private void initSourceList(List sourceList) {
        //this.sourceList = sourceList;
        //this.bindingState = new BindingState();
        //bindingState.setDocumentList(new DocumentList(sourceList));
        DocumentList<T> dl = new DocumentList(sourceList);
        //if ( bindingState.getDocumentList() != null ) {
        
            //for ( T d : (DocumentList<T>)bindingState.getDocumentList()) {
            for( T d : dl ) {
                //d.propertyStore().addDocumentChangeListener(listState.documentChangeHandler());
                updateAttachState(d, true);
            }
        //}
        if ( documentListBinders == null ) {
            documentListBinders = new HashMap<Object,BindingStateBinder>(); 
        }
        bindingState.setDocumentList(dl);
        
        for ( BindingStateBinder bs : documentListBinders.values()) {
            //bs.setBindingState(bindingState);
            bind_1(bs);
            
        }
        getDocuments().addListChangeListener(this);
        
    //    bindingState.setDocumentList(dl);
        setSelected(dl.get(0));
        if ( ! isActive() ) {
            //setActive(true);
        }
        
    }
    public DocumentList getDocuments() {
        if ( getBindingState() == null ) {
            return null;
        }
        return (DocumentList)getBindingState().getDocumentList();
    }
    
    protected void updateAttachState(T doc, boolean attached) {
        
        if ( doc.propertyStore() instanceof HasDocumentState ) {
            DocumentState st = ((HasDocumentState)doc.propertyStore()).getDocumentState();
            if ( st.isAttached() ) {
                doc.propertyStore().removeDocumentChangeListener(getBindingState().documentChangeHandler());
            }
            if ( attached ) {
                doc.propertyStore().addDocumentChangeListener(getBindingState().documentChangeHandler());                
            }
            st.setAttached(attached);
        }
    }

    public List<T> getSourceList() {
        return sourceList;
    }
    
    public void setSourceList(List sourceList) {
        
        if ( documentListBinders != null ) {
            for ( BindingStateBinder b : documentListBinders.values()) {
                b.unbind();
            }
        }
        if ( documentBinders != null ) {
            for ( Object o : documentBinders.documentBinders ){
                ((DocumentBinder)o).unbind();
            }
        }
        
        if ( getDocuments() != null ) {
            getDocuments().removeListChangeListener(this);
        }
        
        this.sourceList = sourceList;        
        this.bindingState = new BindingState();

        initSourceList(sourceList);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        if ( this.active == active ) {
            return;
        }
        if ( ! active ) {
            setSelected(null);
            this.active = active;
        } else {
            this.active = active;
            if ( sourceList != null ) {
                if ( sourceList.isEmpty() ) {
                    setSelected(null);
                } else {
                    setSelected(sourceList.get(0));
                }
            } else {
                setSelected(selected);
            }
        }
    }
    
    /**
     * 
     * @return an object of type {@link BindingState} 
     */
    protected BindingState getBindingState() {
        return this.bindingState;
    }
    /**
     * @return an object of type {@link org.document.Document)
     * or it's subtype that currently is set as <i><code>selected</code></i>.
     */
    public T getSelected() {
        return selected;
    }
    /**
     * Indicates whether a given document is <code><i>selected</i></code>.
     * @param document an object to be checked
     * @return <code>true</code> if a given object is set <code>selected</code>. <code>false</code>
     * otherwise.
     */
    public boolean isSelected(T document) {
        return document == getSelected() && document != null ? true : false;
    }

    /**
     * Sets a given object <code><i>selected</i></code>.
     * Assigns a new document to all appropriate objects of
     * type {@link DocumentBinder}.
     * @param selected an object to be set <code>selected</code>
     */
    public void setSelected(T selected) {
        if (sourceList == null) {
            setDocument(selected);
            return;
        }
        if ( ! isActive() ) {
            return;
        }    
        this.bindingState.setSelected(selected);
        
        this.selected = selected;

        //afterSetSelected(old);
    }

    protected void afterSetSelected(T oldSelected) {
    }

    /**
     * Sets a given object <code><i>selected</i></code>.
     * Assigns a new document to all appropriate objects of
     * type {@link DocumentBinder}. 
     * <P>The method provides the same functionality as 
     * {@link #setDocument(org.document.Document) and those two methods may be 
     * used interchangebly.
     * @param selected an object to be set <code>selected</code>
     */
    public void setDocument(T selected) {
        if (sourceList != null) {
            setSelected(selected);
            return;
        }
        this.active = true;
        T old = this.selected;
        if ( old != null && (old.propertyStore() instanceof HasDocumentState) ) {
            ((HasDocumentState)old.propertyStore()).getDocumentState().setAttached(false);
        }
        if ( selected != null && (selected.propertyStore() instanceof HasDocumentState) ) {
            ((HasDocumentState)selected.propertyStore()).getDocumentState().setAttached(true);
        }
        this.documentBinders.setDocument(selected);
    }
    public void bind(PropertyBinder binder) {
        documentBinders.addBinder(binder);
    }
    public void registerAlias(String alias, Class documentClass) {
        DocumentBinder b = documentBinderOf(alias);
        String s = documentClass == null ? "" : documentClass.getName();
        b.setClassName(s);
        b.setDocument(selected);
    }
    /**
     * Registers a given binder.
     * 
     * @param binder a binder to be registered
     * @see BindingStateBinder
     * @throws an exception of type @{@link java.lang.IllegalArgumentException}
     * in case when the binding manager was created without a document list 
     * specified
     */
    public void bind(BindingStateBinder binder) {
          if (sourceList == null) {
                throw new IllegalArgumentException("A List Binders are not supported wnen no source list is defined");
           }
            
           binder.addBinderListener(this);
           documentListBinders.put(binder.getBoundObject(),binder);
           binder.setBindingState(getBindingState());
           binder.addBoundObjectListeners();
    }    
    public void bind_1(BindingStateBinder binder) {
          if (sourceList == null) {
                throw new IllegalArgumentException("A List Binders are not supported wnen no source list is defined");
           }
            
           binder.addBinderListener(this);
           //documentListBinders.put(binder.getBoundObject(),binder);
           binder.setBindingState(getBindingState());
           binder.addBoundObjectListeners();
    }    
    
    /**
     * Unregisters a given binder.
     * @param binder the binder of type {@link BindingStateBinder} to be unregistered
     */
    public void remove(BindingStateBinder binder) {
            binder.removeBinderListener(this);
            documentListBinders.remove(binder.getBoundObject());
            binder.updateBinders();
            //binder.removeComponentListeners();
    }
    private void remove(BindingStateBinder binder,Object oldBoundObject,Object newBoundObject) {
//            oldBoundObject.removeComponentListeners();
            binder.removeBinderListener(this);
            documentListBinders.remove(oldBoundObject);
    }
    
    public void bind(String propertyName,String alias,HasBinder object) {
        //getPropertyBinders(alias).bind(object);
        documentBinders.documentBinderOf(alias).bind(object);
    }

    public void bind(String propertyName,Class alias,HasBinder object) {
        this.bind(propertyName,alias.getName(),object);
    }
/*    public void bind(Class alias,HasBinder object) {
        documentBinderOf(alias).bind(object);
    }
    */ 
    public void bind(HasBinder object) {
        documentBinders.documentBinderOf("default").bind(object);
    }
    
    /**
     * Sets a custom recognizer that is used to associate an object
     * of type <code>Document</code> with a corresponding object of type
     * <code>DocumentBinder</code>.
     * If the parameter is <code>null</code> than no custom 
     * recognizee is used
     * @param recognizer an object to be set as a recognizer
     */
    public void setRecognizer(BindingRecognizer recognizer) {
        this.recognizer = recognizer;
    }

    //
    // ===========================================================
    //

    protected String aliasOf(Document doc) {
        return "default";
    }
    
    /**
     * Returns an existing or new instance of type <code>DocumentBinder</code>
     * for a class <code>java.lang.Objectclass</code>  class and "default" subAlias.
     * If a document binder doesn't exist 
     * then a new one is created with an <code>alias</code> property 
     * set to  <code><pre>new Alias(Object.class,"default")</pre></code>.
     * @return an existing or new instance of type <code>DocumentBinder</code>
     */

    public DocumentBinder documentBinderOf(String alias) {
        DocumentBinder b =  documentBinders.register(alias);
        return b;
    }

    /**
     * 
     * Invoked when the {@link BindingState } object has changed its state.
     * <code>BindingManages</code> is responsible for handling events
     * that can be fired by {@link BindingStateBinder}. When a new binder 
     * of type <code>BindingStateBinder</code> is added then the binding manager 
     * registers itself as a listener of events of type {@link BinderEvent}.
     * <p>
     * if  <code>event</code> represents an action
     * {@link BinderEvent.Action#componentSelectChange } then the method set a
     * new <code><i>selected</i></code> document.
     * 
     * @param event an event object of type {@link BinderEvent}
     */
    @Override
    public void react(BinderEvent event) {
        switch (event.getAction()) {
            case componentSelectChange:
                T newDoc = (T) event.getDataValue();
                if (newDoc == this.selected) {
                    return;
                }
                setSelected(newDoc);
                break;
/*            case boundObjectReplace:
                if ( event.getSource() instanceof BindingStateBinder ) {
                    BindingStateBinder b;
                    if ( documentListBinders.containsKey(event.getOldValue()) ) {
                        b = documentListBinders.get(event.getOldValue());
                       // remove(b,event.getOldValue(),event.getNewValue());
                    } else {
                        b = (BindingStateBinder)event.getSource();
                    }
                    
                    if ( event.getNewValue() != null ) {
                       // bind(b);
                    }
                }
                break;
                */ 
        }
    }
    /**
     * Returns an existing or new instance of type <code>DocumentErrorBinder</code>
     * for a given alias.
     * If a document error binder doesn't exist 
     * then a new one is created with an <code>alias</code> property 
     * set to  <code>alias</code> parameter value.
     * @return an existing or new instance of type <code>DocumentErrorBinder</code>
     * @see org.document.binding.DocumentErrorBinder 
     * @see #errorBinders() 
     * @see #errorBinders(java.lang.Class) 
     */
    public DocumentErrorBinder errorBinderOf(String alias) {
        DocumentBinder db = documentBinders.register(alias);
        return db.getDocumentErrorBinder();
    }

    protected boolean isEditing(T doc) {
        boolean b = false;
        if (doc instanceof HasDocumentState) {
            b = ((HasDocumentState) doc).getDocumentState().isEditing();
        }
        return b;
    }
    /**
     * Handles events of type <code>ListChangeEvent</code>.
     * The method is called in response of applying of one of the following 
     * methods: 
     * <ul>
     *   <li>{@link DocumentList#update(java.lang.Object) }</li>
     *   <li>{@link DocumentList#update(int, java.lang.Object) } </li>
     *   <li>{@link DocumentList#addAll(java.util.Collection) }</li>
     *   <li>{@link DocumentList#addAll(int, java.util.Collection) }</li>
     *   <li>{@link DocumentList#remove(java.lang.Object) }</li>
     *   <li>{@link DocumentList#remove(int) }</li> 
     *   <li>{@link DocumentList#updateBoundObject(java.util.Collection) }</li>
     *   <li>{@link DocumentList#retainAll(java.util.Collection) }</li> 
     *   <li>{@link DocumentList#addSelect)}</li> 
     *   <li>{@link DocumentList#addSelect(org.document.Document) }</li> 
     *   <li>{@link DocumentList#clear()  }</li> 
     * 
     * </ul>
     * @param event the event to be handled
     * @see ListChangeEvent
     * @see ListChangeListener
     */
   @Override
    public void listChanged(ListChangeEvent event) {
        if ( event.getAction() == ListChangeEvent.Action.beforeClear ||
             event.getAction() == ListChangeEvent.Action.beforeRemoveAll ||
             event.getAction() == ListChangeEvent.Action.beforeRetainAll   ) {
            return;
        }
        updateDocumentState(event);
        T newSel = selected;
        DocumentList<T> list = (DocumentList)event.getSource();
        if ( event.getAction() == ListChangeEvent.Action.addAndSelect && (Boolean)event.getResult() ) {
            newSel = list.getLast();
        } else if ( ! list.contains(selected) ) {
            T oldSel = null;
            if ( event.getAction() == ListChangeEvent.Action.set ) {
                 oldSel = (T)event.getResult();
            }
            if (oldSel == selected ) {
                newSel = list.get(event.getIndex());
            } else {
                newSel = list.isEmpty() ? null : list.get(0);
            }
            if ( selected != null ) {
                DocumentBinder b = documentBinders.documentBinderOf(selected);
                if ( b != null ) {
                    selected.propertyStore().removeDocumentChangeListener(b);
                }
            }
        }
        // We assign the same list
        getBindingState().setDocumentList(list);
        
        setSelected(newSel);
    }
    protected void updateDocumentState(ListChangeEvent e) {
        switch(e.getAction()) {
            case add : 
            case append :
                if ( (Boolean)e.getResult() ) {
                    this.updateAttachState((T)e.getElement(), true);
                }
                break;
            case addAndSelect : 
                break;
            case removeObject :
                if ( (Boolean)e.getResult() ) {
                    this.updateAttachState((T)e.getElement(), false);
                }
                break;
            case remove :
                this.updateAttachState((T)e.getElement(), false);
                break;
            case set : 
                if (e.getResult() != null) {
                    //old document
                    this.updateAttachState((T)e.getResult(), false);
                } 
                if (e.getElement() != null) {
                    //new document
                    this.updateAttachState((T)e.getElement(), true);
                } 
                break;
            case beforeClear : 
                DocumentList<T> l = (DocumentList)e.getCollection();
                for ( T d : l ) {
                    updateAttachState(d, false);
                }
                break;
            case appendAll :
            case addAll :    
                l = (DocumentList)e.getCollection();
                if ( (Boolean)e.getResult() ) {
                    for ( T d : l ) {
                        updateAttachState(d, true);
                    }
                }
                break;
            case beforeRemoveAll : 
                if ( getDocuments() == null ) {
                    break;
                }
                l = (DocumentList)e.getCollection();
                DocumentList dl = getDocuments();
                if ( (Boolean)e.getResult() ) {
                    for ( T d : l ) {
                        if ( dl.contains(d) ) {
                            updateAttachState(d, false);
                        }    
                    }
                }
                break;
            case beforeRetainAll : 
                if ( getDocuments() == null ) {
                    break;
                }
                l = (DocumentList)e.getCollection();
                dl = getDocuments();
                if ( (Boolean)e.getResult() ) {
                    for ( T d : l ) {
                        if ( ! dl.contains(d) ) {
                            updateAttachState(d, false);
                        }    
                    }
                }
                break;
                
        }//switch
    }
    public class Registry {
        
        private Map<String,ContainerBinder> containerBinders;
        private Map<String,DocumentBinder> documentBinders;        
        
        private List<Binder> binders;
        
        public Registry() {
            documentBinders = new HashMap<String,DocumentBinder>();
            containerBinders = new HashMap<String,ContainerBinder>();
            binders = new ArrayList<Binder>();
        }
        public void notify(DocumentChangeEvent e) {
            for ( Binder b : documentBinders.values() ) {
                if ( b instanceof DocumentChangeListener ) {
                    ((DocumentChangeListener)b).react(e);
                }
            }
            
            for ( Binder b : containerBinders.values() ) {
                if ( b instanceof DocumentChangeListener ) {
                    ((DocumentChangeListener)b).react(e);
                }
            }
            for ( Binder b : binders ) {
                if ( b instanceof DocumentChangeListener ) {
                    ((DocumentChangeListener)b).react(e);
                }
            }
            
        }
        public void notify(ListChangeEvent e) {
            
            for ( Binder b : containerBinders.values() ) {
                if ( b instanceof ListChangeListener ) {
                    ((ListChangeListener)b).listChanged(e);
                }
            }
            for ( Binder b : documentBinders.values() ) {
                if ( b instanceof ListChangeListener ) {
                    ((ListChangeListener)b).listChanged(e);
                }
            }
            
            for ( Binder b : binders ) {
                if ( b instanceof ListChangeListener ) {
                    ((ListChangeListener)b).listChanged(e);
                }
            }
        }
        /**
         * Creates  an object of type <code>DocumentBinder</code>,
         * with the specified alias name and document class name and
         * and adds it to the collection of binders.
         * If the collection already contains a binder with the specified
         * <code>alias</code> then the method throws an exception.
         * 
         * @param alias
         * @param documentClass
         * @return 
         */
        public DocumentBinder add(String alias,Class documentClass) {
            if ( containerBinders.containsKey(alias) ) {
                // TODO THROW
            }
            
            String name = documentClass == null ? null : documentClass.getName();
            DocumentBinder result =  new DocumentBinder();            
            
            result.setAlias(alias);
            result.setClassName(name);
            result.setContext(bindingContext);
            return result;
        }
        /**
         * Removes the container of of type 
         * <code>DocumentBinder</code> for the specified <code>alias</code>.
         * Sets the property with a name <code>context</code> to <code>null</code>
         * for the <code>DocumentBinder</code> itself and all contained
         * binders.
         * 
         * @param alias to search for.
         */
        public void removeDocumentBinder(String alias) {
            DocumentBinder cb = documentBinders.remove(alias);
            if ( cb != null ) {
               cb.setContext(null);
               cb.initDefaults();

               for ( Object b : cb ) {
                  ((HasContext)b).setContext(null);
               }
            }
        }
        /**
         * Removes the container of of type 
         * <code>ContainerBinder</code> for the specified <code>alias</code>.
         * If the the <code>ContainerBinder</code> implements 
         * {@link HasContext} then sets the property with a name <code>context</code> to <code>null</code>.
         * 
         * For all contained binders that implement <code>HasBinder</code>
         * sets the property with a name <code>context</code> to <code>null</code>.
         * 
         * @param alias to search for.
         */
        public void removeContainerBinder(String alias) {
            ContainerBinder cb = containerBinders.remove(alias);
            if ( cb != null && (cb instanceof HasContext)) {
               ((HasContext)cb).setContext(null);
               cb.initDefaults();
                for ( Object b : cb ) {
                    if (b instanceof HasContext) {
                        ((HasContext)b).setContext(null);
                    }
                    ((Binder)b).initDefaults();
                }
            }
            
        }
        
        /**
         * Looks for the specified binder in all internal collections
         * an removes it.
         * @param binder the object to be removed
         * @return <code>true</code> if the <code>binder</code> exists.
         * <code>false</code>otherwise</code>
         */
        public boolean remove(Binder binder) {
            boolean result = false;
            for ( DocumentBinder cb : documentBinders.values() ) {
                if ( cb.remove(binder)  ) {
                    result = true;
                    break;
                }
            }
            if ( ! result ) {
                for ( ContainerBinder cb : containerBinders.values() ) {
                    if ( cb.remove(binder)  ) {
                        result = true;
                        break;
                    }
                }
            }
            if ( ! result ) {
                if ( binders.remove(binder)){
                   result = true;
                }
            }
            if ( result ) {
                if ( binder instanceof HasContext ) {
                    ((HasContext)binder).setContext(null);
                }
            }
            return result;

        }
        /**
         * Looks for an existing or creates a new container for the 
         * specified <code>alias</code> and adds the specified 
         * <code>binder</code> to it.
         * 
         * @param alias to search for
         * @param binder the binder to be added.
         * @return  <code>true</code> if success
         * @throws IllegalArgumentException in case when the specified binder
         *  has already been registered
         * @throws IllegalArgumentException in case when the specified binder
         *  has the alias name whose value differ from the specified
         */
        public boolean add(String alias, PropertyBinder binder) {
            boolean result = true;
            // lookup for each collection
            if ( exists(binder) ) {
                throw new IllegalArgumentException("The the specified property "
                        + " binder has already been registered");

            }
            alias = alias == null || alias.trim().isEmpty() ? "default" : alias;
            if ( ! alias.equals(binder.getAlias())) {
                throw new IllegalArgumentException("The binder has the alias name that differ from"
                        + " the specified)" );
            }

            DocumentBinder target = documentBinders.get(alias);
            if ( target == null ) {
                target = new DocumentBinder();
                target.setAlias(alias);
                documentBinders.put(alias, target);
                target.setContext(bindingContext);
            }
            binder.setAlias(alias);
            target.add(binder); // DocumentBinder will set context
            return result;
        }
        
        /**
         * Looks for a container for the 
         * specified <code>alias</code> and adds the specified 
         * <code>binder</code> to it.
         * Assigns the  <code>context</code> property value of the given 
         * <code>binder</code>.
         * @param alias to search for
         * @param binder the binder to be added.
         * @return  <code>true</code> if success
         * @throws IllegalArgumentException in case when the specified binder
         *  has already been registered
         * @throws IllegalArgumentException in case when the container binder
         *  cannot be found for the given <code>alias<code>
         */
        public boolean add(String alias, Binder binder) {
            boolean result = true;
            if ( exists(binder) ) {
                throw new IllegalArgumentException("The the specified property "
                        + " binder has already been registered");

            }
            alias = alias == null || alias.trim().isEmpty() ? "default" : alias;
            ContainerBinder target = containerBinders.get(alias);
            if ( target == null ) {
                throw new IllegalArgumentException("The container binder for  the specified " +
                        " alias='" + alias + "' cannot be found");

            }
            target.add(binder);
            if (binder instanceof HasContext) {
                ((HasContext)binder).setContext(bindingContext);
            }
            return result;
        }
        
        
        public DocumentBinder put(String alias,DocumentBinder binder) {
            return documentBinders.put(alias,binder);
        }
        
        public ContainerBinder put(String alias,ContainerBinder binder) {
            return containerBinders.put(alias,binder);
        }

       /**
         * Adds the specified <code>binder</code> to the 
         * internal collection.
         *  The collection contains objects of type {@link Binder}
         *  and may contains the binders of any type.
         * 
         * Assigns the  <code>context</code> property value of the given 
         * <code>binder</code>.
         * @param binder the binder to be added.
         * @return  <code>true</code> if success
         * @throws IllegalArgumentException in case when the specified binder
         *  has already been registered
         */
        public boolean add(Binder binder) {
            boolean result = true;
            if ( exists(binder) ) {
                // TODO
            }
            binders.add(binder);
            if ( result && (binder instanceof HasContext)) {
                ((HasContext)binder).setContext(bindingContext);
            }
            return result;
        }
        /**
         * Looks for an existing or creates a new container for the 
         * <code>alias</code> extracted from the specified <code>binder</code>
         * and adds the <code>binder</code> to it.
         * 
         * @param binder the binder to be added.
         * @return  <code>true</code> if success
         * @throws IllegalArgumentException in case when the specified binder
         *  has already been registered
         */
        public boolean add(PropertyBinder binder) {
            return add(binder.getAlias(),binder);
        }        
        
        private boolean exists(Binder binder) {
            boolean result = false;
            for ( DocumentBinder b : documentBinders.values() ) {
                if ( b == binder ) {
                    result = true;
                    break;
                }
                if ( b.contains(binder) ) {
                    result = true;
                    break;
                }
            }
            
            for ( ContainerBinder b : containerBinders.values() ) {
                if ( b == binder ) {
                    result = true;
                    break;
                }
                if ( b.contains(binder) ) {
                    result = true;
                    break;
                }
            }
            if ( ! result ) {
                if ( binders.contains(binder)){
                   result = true;
                }
            }
            return result;

        }
        
        private String extractAlias(Binder binder) {
            String alias = null;
            if ( binder instanceof HasAlias ) {
                alias = ((HasAlias)binder).getAlias();
                if ( alias == null || alias.trim().isEmpty() ) {
                    alias = "default";
                }
            }
            return alias;
            
        }
    }//Registry
    
    private boolean removeFromCollection(Binder binder, Map<String,ContainerBinder> c) {
        boolean result = true;
        for ( ContainerBinder cb : c.values() ) {
            
        }
        return result;
    }
    /**
     * A container of objects of type <code>DocumentBinder</code>
     * 
     * @param <T> a type that implements the {@link DocumentBinder } 
     */
    
    public static class Container<T extends DocumentBinder> { //implements BinderContainer<T> {

        private Document selected;
        //private Map<Object, T> binders;
        private List<Binder> binders;
        private List<DocumentBinder> documentBinders;
        private DocumentBinder defaultBinder; // allways exists
        /**
         * 
         * @param bindingManager
         */
        public Container() {
            //this.binders = new HashMap<Object, Binder>();
            this.binders = new ArrayList<Binder>();
            documentBinders = new ArrayList<DocumentBinder>();
            defaultBinder = new DocumentBinder();
            defaultBinder.setAlias("default");
        }

        public void add(Object key,T binder) {
            binders.add(binder);
        }
        
        /**
         * 
         * @param binder
         */
        public void remove(T binder) {
            binders.remove(binder);
        }
    /**
     * 
     * @param binder
     * @param alias 
     */
    protected void addBinder(PropertyBinder binder) {
        if ( binder.getAlias() == null || binder.getAlias().toString().trim().isEmpty() ) {
            binder.setAlias("default");
            defaultBinder.add(binder);
            return;
        }
        DocumentBinder target = null;
        for ( DocumentBinder db : documentBinders ) {
            for ( Object b : db.binders ) {
               if ( b == binder ) {
                   PropertyBinder pb = (PropertyBinder)b;
                   
                   throw new IllegalArgumentException("The binder already exists. old property '" + pb.getBoundProperty() +
                            ", new property '" + binder.getBoundProperty() + 
                            ", old alias '" + pb.getAlias() + 
                            ", new alias '" + binder.getAlias());
               }
            }
            if ( db.getAlias().equals(binder.getAlias()) ) {
                target = db;
                break;
            }
        }
        if ( target == null ) {
            target = new DocumentBinder();
            target.setAlias(binder.getAlias());
            addDocumentBinder(target);
        }           
        target.add(binder);
    }
    public void addDocumentBinder(DocumentBinder binder) {
        documentBinders.add(binder);
    }
    public DocumentBinder register(String alias) {
        if ( alias == null || alias.isEmpty() || "default".equals(alias) ) {
            return defaultBinder;
        }

        DocumentBinder b = documentBinderOf(alias);
        if ( b == null) {
            b = new DocumentBinder();
            addDocumentBinder(b);
        }
        b.setAlias(alias);
        return b;
    }
    
    public DocumentBinder documentBinderOf(String alias) {
        if ( alias == null || alias.isEmpty() || "default".equals(alias) ) {
            return defaultBinder;
        }
        DocumentBinder result = null;
        for ( DocumentBinder db : documentBinders ) {
            if ( alias.equals(db.getAlias())) {
                result = db;
                break;
            }
        }
        return result;
    }    
    public DocumentBinder documentBinderOf(Document document ) {
            boolean accepted =  false;
            DocumentBinder result = null;
            for ( DocumentBinder db : documentBinders ) {
                if ( db.canAccept(document) ) {
                    accepted = true;
                    result = db;
                    break;
                }
            }
            if ( ! accepted ) {
                result = defaultBinder;
            }
            return result;
    }    
        /**
         * 
         * @param addSelect
         */
        public void setDocument(Document newDocument) {

            Document oldSelected = this.selected;
            this.selected = newDocument;
            
            for ( Binder b : binders ) {
            if ( b != null && (b instanceof DocumentChangeListener)) {
                DocumentChangeEvent e = new DocumentChangeEvent(this, DocumentChangeEvent.Action.documentChange);
                e.setOldValue(oldSelected);
                e.setNewValue(newDocument);
                ((DocumentChangeListener)b).react(e);
            }
            }
            boolean accepted =  false;
            for ( DocumentBinder db : documentBinders ) {
                if ( db.accept(newDocument) ) {
                    accepted = true;
                }
            }
            if ( ! accepted ) {
                defaultBinder.accept(newDocument);
            }
            
        }
    }
    
}//class DocumentDataSource

