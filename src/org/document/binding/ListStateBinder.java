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
    
    public ListStateBinder(Object alias) {
        super(alias);
        initBinders();
    }
    
    private void initBinders() {
        this.add(createSelectedBinder());
        this.add(createListModelBinder());
        
    }
    
    protected abstract T createSelectedBinder();
    protected abstract T createListModelBinder();
    
    protected List<Document> getDocuments() {
        if ( getDocument() == null ) {
            return null;
        }
        return ((ListState)getDocument()).getDocumentList();
    }
    
    @Override
    public void react(BinderEvent event) {

        switch (event.getAction()) {
            case componentValueChange:

                if (!needChangeData(event.getPropertyName(), event.getDataValue())) {
                    return;
                }
                getDocumentStore().put(event.getPropertyName(), event.getDataValue());
                BinderEvent.Action action = event.getAction();
                if ( ((PropertyBinder)event.getSource()).getPropertyName().equals("selected")) {
                    action = BinderEvent.Action.componentSelectChange;
                }
                BinderEvent e = new BinderEvent(this,action,event.getDataValue(),event.getComponentValue());
                for ( Object l : binderListeners) {
                    BinderListener bl = (BinderListener)l;
                    bl.react(event);
                }
                break;
        }
    }

}
