package org.document.binding;

import java.util.List;
import org.document.Document;
import org.document.DocumentList;

/**
 *
 * @author V. Shyshkin
 */
public abstract class DocumentListBinder<E extends Document> extends AbstractBinder implements ContextListener {
    
    private BindingContext context;
    
    private List list;

    private BinderListener binderListener;
    
    private String propertyName;
    
    public DocumentListBinder() {
        super();
    }

    public DocumentListBinder(Object component) {
        super(component);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        DocumentList dl = new DocumentList(list);
        this.list = list;
        setSelected(0);
        //setDocument(bs);
        
    }
    
    public void setSelected(int index) {
        if ( index < 0 || context.getDocumentList() == null || index > context.getDocumentList().size()-1 ) {
            return;
        }
        if ( context.getDocumentList() != null && context.getDocumentList().isEmpty() ) {
            if ( index >= 0 && index < context.getDocumentList().size() ) {
                requestSelect((E)context.getDocumentList().get(index));
            }
        }
//        getBindingState().setSelected((E)list.get(index));
    }
    public E getSelected() {
        return (E)context.getSelected();
    }
    public int getSelectedIndex() {
        if ( context.getDocumentList() == null || context.getDocumentList().isEmpty() ) {
            return -1;
        }
        if ( context.getSelected() == null ) {
            return -1;
        }
        return context.getDocumentList().indexOf(context.getSelected());
    }
    
    public void requestSelect(E document) {
        BinderEvent.Action action =
                BinderEvent.Action.propertyChangeRequest;
        BinderEvent event = new BinderEvent(this, action,"*selected");
        event.setNewValue(document);
        if (binderListener == null) {
            return;
        }
        binderListener.react(event);
    }
       @Override
    public void addBinderListener(BinderListener l) {
        binderListener = l;
    }

    @Override
    public void removeBinderListener(BinderListener l) {
        binderListener = null;
    }
 
/*    public void setSelected(E document) {
        if ( getList() == null ) {
            return;
        }
        getBindingState().setSelected(document);
    }
*/    
    @Override
    public void react(ContextEvent event) {
        context = (BindingContext)event.getSource();
        switch (event.getAction()) {
            case documentChanging:
                break;
            case documentChange:
  //              afterDocumentChange(event);
                break;
            case activeStateChange:
                if ( context.isActive()) {
//                   notifyAll(new BinderEvent(this,BinderEvent.Action.refresh)); 
                } else {
                   initDefaults();
                }
                break;
    
        }
    }
    
}
