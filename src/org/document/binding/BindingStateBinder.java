/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import java.util.List;
import org.document.Document;

/**
 *
 * @author V. Shyshkin
 */
public abstract class BindingStateBinder  extends DocumentBinder<BindingState> {
    protected Object component;

    public BindingStateBinder() {
        super();
        
    }
    
    public BindingStateBinder(Object component) {
        super();
        this.component = component;
    }
    /**
     * The method creates objects of type <code>PropertyBinder</code> for such properties
     * as <code>listModel,documentChangeEvent,document</code> and adds them to
     * internal binder collection.
     * It is important that the binder for the property <code>listModel</code>
     * should be added first. 
     */
    protected final void initBinders() {
        //
        // create createListModelBinder() must be first
        //
        this.add(createListModelBinder());
        this.add(createDocumentChangeEventBinder());
        this.add(createSelectedBinder());
        
    }

    public Object getComponent() {
        return component;
    }
    public void setComponent(Object component) {
        this.component = component;
        initBinders();
    }
    
    protected abstract PropertyBinder createSelectedBinder();
    protected abstract PropertyBinder createListModelBinder();
    protected abstract PropertyBinder createDocumentChangeEventBinder();
    
    protected List getDocuments() {
        if ( getDocument() == null ) {
            return null;
        }
        return getDocument().getDocumentList();
    }
    
    @Override
    public void addBinderListener(BinderListener l) {
        getBinderListeners().add(l);
        if (getBinderListeners().size() > 1 ) {
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
                if ( ((PropertyBinder)event.getSource()).getBoundProperty().equals("selected")) {
                    action = BinderEvent.Action.componentSelectChange;
                }
                BinderEvent e = new BinderEvent(this,action,event.getDataValue(),event.getComponentValue());
                for ( Object l : binderListeners) {
                    BinderListener bl = (BinderListener)l;
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
