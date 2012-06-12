/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import java.util.List;

/**
 *
 * @author V. Shyshkin
 */
public abstract class BindingStateBinder extends AbstractDocumentBinder<BindingState> {
    protected PropertyBinder selectedBinder;
    protected PropertyBinder documentListBinder;
    protected PropertyBinder documentChangeEventBinder;


    public BindingStateBinder() {
        this(null);
    }

    public BindingStateBinder(Object component) {
        super(component);
        
    }

    /**
     * The method creates objects of type
     * <code>PropertyBinder</code> for such properties as
     * <code>listModel,documentChangeEvent,document</code> and adds them to
     * internal binder collection. It is important that the binder for the
     * property
     * <code>listModel</code> should be added first.
     */
    protected final void initBinders() {
        //
        // create createListModelBinder() must be first
        //
        this.add(createListModelBinder());
        this.add(createDocumentChangeEventBinder());
        this.add(createSelectedBinder());
    }
/*    @Override
    public void unbind() {
        removeBoundObjectListeners();
        if ( binderListener != null ) {
            binderListener.clear();
        }
        document = null;
        for ( PropertyBinder b : binders ) {
            if ( "selected".equals(b.getBoundProperty()) ||
                 "documentList".equals(b.getBoundProperty()) ||
                 "documentChangeEvent".equals(b.getBoundProperty())   
                    ) {
                continue;
            }
//            b.setLocked();
        }
    }
*/
/*    protected void updateBinders(Object boundObject) {
        updateBinder("selected", boundObject);
        updateBinder("documentList", boundObject);
        updateBinder("documentChangeEvent", boundObject);

    }

    @Override
    protected void updateBinders() {
        updateBinder("selected", getBoundObject());
        updateBinder("documentList", getBoundObject());
        updateBinder("documentChangeEvent", getBoundObject());
    }
*/
    public BindingState getBindingState() {
        return super.getDocument();
    }

    public void setBindingState(BindingState state) {
       // super.documentChange(state);
    }

    public Object getBoundObject() {
        return boundObject;
    }

    private PropertyBinder getBinderByName(String propertyName) {
        Object component = getBoundObject();
        for (PropertyBinder b : binders) {

            if (!propertyName.equals(b.getBoundProperty())) {
                continue;
            }
            return b;
        }
        return null;
    }

    @Override
    public void setBoundObject(Object newBoundObject) {
        //updateBinders(component);

        removeBoundObjectListeners();
        initBoundObjectDefaults();
        

/*        PropertyBinder sb = getBinderByName("selected");

        sb.setBoundObject(null);
        PropertyBinder dl = getBinderByName("documentList");
        dl.setBoundObject(null);
        PropertyBinder ce = getBinderByName("documentChangeEvent");
        ce.setBoundObject(null);


        //updateBoundObjectOf("documentList");
        //updateBoundObjectOf("documentChangeEvent");

        this.boundObject = newBoundObject;
        sb.setBoundObject(this);
        dl.setBoundObject(this);
        ce.setBoundObject(this);
*/
        //setDocument(document); // to refresh for changes
        this.boundObject = newBoundObject;
        addBoundObjectListeners();                        
        //setDocument(document); // to refresh for changes
        //     updateBinders(null); // refreshes boundObjects with the same ones
        //     boundObject = newBoundObject;
        //     updateBinders(boundObject); // refreshes bondObjects with the new ones
        //
        // Now we notify a bindingManager in order to rebind with the 
        // new boundObject
        //
        /*if (binderListener == null) {
            return;
        }
        //
        // create a copy of binderListener since some listeners may remove themself 
        //
        List<BinderListener> list = new ArrayList<BinderListener>();
        list.addAll(binderListener);
        for (BinderListener l : list) {
            //l.react(e);
        }
        */
        
    }
    
    /**
     *
     * @param propertyName
     * @param component
     */
/*    protected void updateBinder(String propertyName, Object component) {
        for (PropertyBinder b : binders) {

            if (!propertyName.equals(b.getBoundProperty())) {
                continue;
            }
            //b.removeBoundObject();
            b.setBoundObject(component);
            //b.setBoundObject(component);
        }
    }

    protected void updateBinderOLD(String propertyName, Object component) {
        for (PropertyBinder b : binders) {

            if (!propertyName.equals(b.getBoundProperty())) {
                continue;
            }
            b.setBoundObject(null);
            b.setBoundObject(component);
        }
    }

    protected void updateBinder(String propertyName) {
        Object component = getBoundObject();
        for (PropertyBinder b : binders) {

            if (!propertyName.equals(b.getBoundProperty())) {
                continue;
            }
            b.setBoundObject(null);
            b.setBoundObject(component);
        }
    }
*/
    protected abstract PropertyBinder createSelectedBinder();

    protected abstract PropertyBinder createListModelBinder();

    protected abstract PropertyBinder createDocumentChangeEventBinder();

    protected List getDocuments() {
        if (getDocument() == null) {
            return null;
        }
        return getDocument().getDocumentList();
    }

    @Override
    public void addBinderListener(BinderListener l) {
    }

    @Override
    public void removeBinderListener(BinderListener l) {
    }

    @Override
    public void react(BinderEvent event) {

        switch (event.getAction()) {
            case boundObjectChange:
                BinderEvent.Action action = event.getAction();
                if (((PropertyBinder) event.getSource()).getBoundProperty().equals("selected")) {
                    action = BinderEvent.Action.boundObjectSelectChange;
                }
                BinderEvent e = new BinderEvent(this, action, event.getDataValue(), event.getComponentValue());
                break;
        }
    }

    /*    protected <E extends Document> List<E> getList() {
     return null;
     }

     protected <E extends Document> void  setList(List<E> list) {
     }
     */
}
