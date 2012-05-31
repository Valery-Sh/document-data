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
public abstract class ListStateBinder<T extends PropertyBinder>  extends DocumentBinder {
    protected Object component;
    public ListStateBinder(Object component) {
        super();
        this.component = component;
    }
    
    protected final void initBinders() {
        this.add(createSelectedBinder());
        this.add(createListModelBinder());
        this.add(createDocumentChangeEventBinder());
    }

    public Object getComponent() {
        return component;
    }
    
    protected abstract T createSelectedBinder();
    protected abstract T createListModelBinder();
    protected abstract T createDocumentChangeEventBinder();
    
    protected List<Document> getDocuments() {
        if ( getDocument() == null ) {
            return null;
        }
        return ((ListState)getDocument()).getDocumentList();
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

}
