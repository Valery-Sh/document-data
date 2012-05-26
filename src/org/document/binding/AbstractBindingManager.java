package org.document.binding;

import java.util.*;
import org.document.*;
import org.document.PropertyStore.Alias;

/**
 * The class allows to declare any object of type {@link org.document.Document }
 * as <code><i>selected</i></code> and start binding procedure for it.  
 *
 * The method {@link setSelected(Document) } announces the 
 * specified document as a <code><i>selected</i></code>. Method 
 * {@link getSelected() } returns a document that is declared as selected, 
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
 *	<li>DocumentBinder getDocumentBinder(Class clazz)</li>
 *	<li>DocumentBinder getDocumentBinder(Class clazz, String str)</li>
 *	<li>DocumentBinder getDocumentBinder()</li>
 *</ul>
 * </code>
 * <p>
 * Using the values of the supplied parameters, the constructor creates an object
 * of type {@link PersonBinder.Alias}. Each method builds an object array in 
 * its own way. The method <code>getDocumentBinder(Class clazz)</code>
 * builds:
 * <code>
 * <pre>
 *		new Alias(clazz, "default")
 * </pre>
 * </code>
 *
 * The method getDocumentBinder(Class clazz,String str) creates:
 * <code>
 * <pre>
 *		new Alias(clazz, "default")
 * </pre>
 * </code>
 *
 * And the method getDocumentBinder() produces:
 * <code>
 * <pre>
 *		new Alias(Object.clazz, "default")
 * </pre>
 * </code>
 *
 *
 * The resulting object is used as the <code>key</code> to 
 * the map-table of objects of type <code>DocumentBinder</code>.
 *
 * When invoke a method <code>getDocumentBinder</code> , then if the 
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
 * <code>BinidingManager</code> looks for a document that is declared as
 * <code><i>selected</i></code> to the appropriate
 * <code>DocumentBinder</code>.
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
 * Classes of type Document are closely related to objects of type
 * <code>PropertyStore</code>. These objects do have a property
 * <code><i>alias</i></code>. In the standard implementation the 
 * objects that implement <code>PropertyStore</code>, are the 
 * objects of class {@link org.document.DocumentPropertyStore } . 
 * The constructor of this class takes a parameter of type 
 * <code>Document</code> and creates an object of type <code>PropertyStore.Alias</code>. 
 * The constructor accepts two parameters:
 * The first is a subtype of the class <code>Document</code>,
 * and the second is a string of characters with a value of "default".
 * This object becomes the <code>alias</code> property value. When a
 * <code>BindingManager</code> looks for a <code>DocumentBinder</code> 
 * in the table, then, if a <code>BindingRecognizer</code> is not 
 * <code>null</code>, it is used to search for, otherwise,
 * <code>BindingManager</code> retrieves the value of the
 * <code>alias</code> from the document's <code>PropertyStore</code> and 
 * uses that value as the key to the map-table. If the search is 
 * successful then a <code>DocumentBinder</code> returned. If not, 
 * the search continues for the key <code>{Object.class, "default"}</code>.
 * If not found, then <code>null</code> value is returned and the 
 * document will not be used for binding.
 *<p>
 * When and what method should be used in order to get
 * <code>DocumentBinder</code> depends on the application. 
 * If you know that <code>BindingManager</code> will be used
 * for processing the same type of documents, for example,
 * <code>Person</code>, then the method without parameters or
 * getDocumentBinder(Person.class) fit.
 * <p>
 * Why do we need a method with two parameters? Suppose you 
 * run a query to a database that contains objects of 
 * type Person. Querying the SQL database can have a 
 * non predictable set of the returned fields. We can not 
 * create the appropriate bean-object for every possible 
 * request. But we can create a class based on the key/value pairs,
 * for example, using {@link java.util.Map}. Such class already exists 
 * in the library. {@link KeyValueMap } class implements the
 * <code>Document</code> interface, is not a Bean-object and contains methods
 * <code>get</code> and <code>put</code> methods to access the key/value 
 * pairs. The class has a constructor that accepts a parameter
 * <code>subAlias</code> of type </code>String. It's
 * <code>PropertyStore</code> builds  <code>alias</code> as an array:
 * 
 * <code><pre>Object [] {KeyValueMap.class, subAlias}</pre></code>.
 * 
 * @param <T> 
 * @author V. Shishkin
 */
public abstract class AbstractBindingManager<T extends Document> implements BinderListener {

    private DocumentBinderContainer documentBinders;
    /**
     *  Stores a reference to a document that is declared as <code>selected</code>.
     */
    protected T selected;
    /**
     * Contains custom <code>DocumentRecognizer</code> if defined.
     */
    protected BindingRecognizer recognizer;
    /**
     * List of documents set by a constructor call.
     */
    private List<T> sourceList;
    private ListState listState;
    private Map<Object,ListStateBinder> documentListBinders;

    /**
     * Creates an instance of the class for a given list of documents.
     * If the parameter is not <code>null</code> then only 
     * documents that the list contains may be declared 
     * <code>selected</code>.
     * 
     * @param sourceList
     */
    public AbstractBindingManager(List sourceList) {
        this();
        if ( sourceList == null ) {
            return;
        }
        this.sourceList = sourceList;
        this.listState = new ListState();
        listState.setDocumentList(new DocumentList(sourceList));
        //documentListBinders = new BinderMap(this);
        documentListBinders = new HashMap<Object,ListStateBinder>();        
        //validators = new HashMap<Object,Validator>();
    }

    /**
     * Create an instance of the class. 
     * Instances created with this constructor may set
     * any document as <code>selected</code>.
     */
    protected AbstractBindingManager() {
        documentBinders = new DocumentBinderContainer(this);
    }
    /*    public void addValidator(Object alias, Validator validator) {
     validators.put(alias, validator);
     }
     */

    /**
     * 
     * @return an object of type {@link ListState} 
     */
    protected ListState getListState() {
        return this.listState;
    }

    /**
     * @return an object of type BinderMap that serves as a container
     * for objects of type {@link DocumentBinder).
     */
    protected DocumentBinderContainer getBinders() {
        return this.documentBinders;
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
    boolean isSelected(T document) {
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

        T old = this.selected;

        boolean markedNew = false;
        this.documentBinders.setDocument(selected);
        Object o = listState.getSelected();
        this.listState.setSelected(selected);
        this.selected = selected;

        afterSetSelected(old);
    }

    /**
     * Sets a given object <code><i>selected</i></code>.
     * Assigns a new document to all appropriate objects of
     * type {@link DocumentBinder}. 
     * <P>The method provides the same functionality as 
     * {@link #setSelected(org.document.Document) and those two methods may be 
     * used interchangebly.
     * @param selected an object to be set <code>selected</code>
     */
    public void setDocument(T selected) {
        if (sourceList != null) {
            setSelected(selected);
            return;
        }

        T old = this.selected;

        boolean markedNew = false;
        this.documentBinders.setDocument(selected);
    }

    /**
     * May be overriden by subclasses to provide additional 
     * actions for a given old selected document.
     * @param oldSelected an object that has been set 
     * <code>selected</code> before a new one.
     * @see BindingManager#afterSetSelected(org.document.Document)  
     */
    protected abstract void afterSetSelected(T oldSelected);

    /**
     * Registers a given binder.
     * The parameter must be of type {@link ListStateBinder}. 
     * Otherwise the method does nothing.
     * @param binder a binder to be registered
     */
    public void addBinder(ListStateBinder binder) {
          if (sourceList == null) {
                throw new IllegalArgumentException("A List Binders are not supported wnen no source list is defined");
            }
            binder.addBinderListener(this);
            documentListBinders.put(binder.getComponent(),binder);
            ((ListStateBinder) binder).setDocument(getListState());
    }

    /**
     * Unregisters a given binder.
     * The parameter must be of type {@link ListStateBinder}. 
     * Otherwise the method does nothing.
     * @param binder a binder to be unregistered
     */
    public void removeBinder(ListStateBinder binder) {
            binder.removeBinderListener(this);
            documentListBinders.remove(binder);
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

    /**
     * Tries to find a corresponding object of type 
     * <code>DocumentBinder</code> for a given document.
     * @param doc a document, for which the search
     * @return an object of type <code>DocumentBinder</code> or 
     * <code>null</code> if the search is not successful
     */
    public DocumentBinder documentBinderOf(Document doc) {
        if ( recognizer != null ) {
            return recognizer.getBinder(doc);
        }
        Alias key = doc.propertyStore().getAlias();

        DocumentBinder result = (DocumentBinder) documentBinders.get(key);        
        if (result == null ) {
            result = (DocumentBinder)documentBinders.get(new Alias(Object.class,key.getSubAlias()));
        }
        return result;
    }

    /**
     * Returns an existing or new instance of type <code>DocumentBinder</code>
     * for a given class.
     * Delegates method call to the method {@link AbstractBindingManager#getDocumentBinder(java.lang.Class, java.lang.String) 
     * with the <code>clazz</code> as the first parameter and a string value 
     * <code>"default"</code> as the second.
     * If a document binder doesn't exist for a given parameter value
     * then a new one is created with an <code>alias</code> property 
     * set to  <code><pre>new Alias(clazz,"default")</pre></code>.
     * @param clazz an object that a method uses to search or create an object
     * of type <code>DocumentBinder</code>
     * @return an existing or new instance of type <code>DocumentBinder</code>
     */
    public DocumentBinder getDocumentBinder(Class clazz) {
        return getDocumentBinder(clazz,"default");
    }
    /**
     * Returns an existing or new instance of type <code>DocumentBinder</code>
     * for a given class and subAlias.
     * If a document binder doesn't exist for a given parameter values
     * then a new one is created with an <code>alias</code> property 
     * set to  <code><pre>new Alias(clazz,subAlias)</pre></code>.
     * @param clazz an object that a method uses together with the second 
     * parameter to search or create an object of type <code>DocumentBinder</code>
     * @param subAlias an object that a method uses together with the first parameter 
     * to search or create an object of type <code>DocumentBinder</code>
     * @return an existing or new instance of type <code>DocumentBinder</code>
     */
    public DocumentBinder getDocumentBinder(Class clazz, String subAlias) {
        Object key  = new Alias(clazz,subAlias);
        DocumentBinder result = (DocumentBinder) documentBinders.get(key);
        if (result == null) {
            result = new DocumentBinder();
            documentBinders.add(key,result);
        }
        return result;
        
    }
    /**
     * Returns an existing or new instance of type <code>DocumentBinder</code>
     * for a class <code>java.lang.Objectclass</code>  class and "default" subAlias.
     * If a document binder doesn't exist 
     * then a new one is created with an <code>alias</code> property 
     * set to  <code><pre>new Alias(Object.class,"default")</pre></code>.
     * @return an existing or new instance of type <code>DocumentBinder</code>
     */
    public DocumentBinder getDocumentBinder() {
        return getDocumentBinder(Object.class,"default");
    }
    
    /**
     * 
     * Invoked when the {@link ListState } object has changed its state.
     * <code>BindingManages</code> is responsible for handling events
     * that can be fired by {@link ListStateBinder}. When a new binder 
     * of type <code>ListStateBinder</code> is added then the binding manager 
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

        }
    }

    /**
     * A container of objects of type <code>DocumentBinder</code>
     * 
     * @param <T> a type that implements the {@link DocumentBinder } 
     */
    public static class DocumentBinderContainer<T extends DocumentBinder> { //implements BinderContainer<T> {

        private AbstractBindingManager bindingManager;
        private Document selected;
        private Map<Object, T> binders;

        /**
         * 
         * @param bindingManager
         */
        public DocumentBinderContainer(AbstractBindingManager bindingManager) {
            this.binders = new HashMap<Object, T>();
            this.bindingManager = bindingManager;
        }

        public void add(Object key,T binder) {
            binders.put(key, binder);
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
         * @param key
         * @return
         */
        public T get(Object key) {
            return this.binders.get(key);
        }

        /**
         * 
         * @param newDocument
         */
        public void setDocument(Document newDocument) {

            Document oldSelected = this.selected;
            this.selected = newDocument;
            Binder b = bindingManager.documentBinderOf(selected);
            if ( b != null ) {
                DocumentChangeEvent e = new DocumentChangeEvent(this, DocumentChangeEvent.Action.documentChange);
                e.setOldValue(oldSelected);
                e.setNewValue(newDocument);
                b.react(e);
            }
/*            for (T b : binders.values()) {
                DocumentChangeEvent e = new DocumentChangeEvent(this, DocumentChangeEvent.Action.documentChange);
                e.setOldValue(oldSelected);
                e.setNewValue(newDocument);
                b.react(e);
            }
*/ 
        }
    }
}//class AbstractBindingManager