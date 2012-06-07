/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author V. Shyshkin
 */
public abstract class BindingStateBinder extends AbstractDocumentBinder<BindingState> {

    protected Object boundObject;

    public BindingStateBinder() {
        super();
    }

    public BindingStateBinder(Object component) {
        super();
        this.boundObject = component;
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

    @Override
    public void adjustRemove() {
        updateBinder("selected", getBoundObject());
        updateBinder("documentList", getBoundObject());
        updateBinder("documentChangeEvent", getBoundObject());
    }

    public BindingState getBindingState() {
        return super.getDocument();
    }

    public void setBindingState(BindingState state) {
        super.setDocument(state);
    }

    public Object getBoundObject() {
        return boundObject;
    }

    public void setBoundObject(Object newBoundObject) {
        //updateBinders(component);

        BinderEvent e = new BinderEvent(this, BinderEvent.Action.boundObjectReplace);
        e.setNewBoundObject(newBoundObject);
        e.setOldBoundObject(boundObject);

        adjustRemove(); // refreshes boundObjects with the same ones
        boundObject = newBoundObject;
        adjustRemove(); // refreshes bondObjects with the new ones

        //
        // Now we notify a bindingManager in order to rebind with the 
        // new boundObject
        //
        if (binderListeners == null) {
            return;
        }
        //
        // create a copy of binderListeners since some listeners may remove themself 
        //
        List<BinderListener> list = new ArrayList<BinderListener>();
        list.addAll(binderListeners);
        for (BinderListener l : list) {
            l.react(e);
        }
    }

    /**
     *
     * @param propertyName
     * @param component
     */
    private void updateBinder(String propertyName, Object component) {
        for (PropertyBinder b : binders) {
            
            if ( ! propertyName.equals(b.getBoundProperty())) {
                continue;
            }
            b.setBoundObject(null);
            b.setBoundObject(component);
        }
    }

    private void updateBinders(Object component) {
        updateBinder("selected", component);
        updateBinder("documentList", component);
        updateBinder("documentChangeEvent", component);
    }

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
        getBinderListeners().add(l);
        if (getBinderListeners().size() > 1) {
            throw new IndexOutOfBoundsException("AbstractDocumentBinder. Only one BinderListener can be registered");
        }

    }

    @Override
    public void removeBinderListener(BinderListener l) {
        binderListeners.remove(l);
    }

    @Override
    public void react(BinderEvent event) {

        switch (event.getAction()) {
            case componentChange:
                BinderEvent.Action action = event.getAction();
                if (((PropertyBinder) event.getSource()).getBoundProperty().equals("selected")) {
                    action = BinderEvent.Action.componentSelectChange;
                }
                BinderEvent e = new BinderEvent(this, action, event.getDataValue(), event.getComponentValue());
                for (Object l : binderListeners) {
                    BinderListener bl = (BinderListener) l;
                    bl.react(e);
                }
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
