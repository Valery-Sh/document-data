package org.document.binding;

import java.beans.PropertyChangeListener;
import java.util.List;
import org.document.Document;

/**
 *
 * @author V. Shyshkin
 */
public abstract class DocumentListBinder<E extends Document> extends AbstractBinder implements ContextListener, PropertyChangeListener {

    private DataSourceContext context;
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

    /*    public List<E> getList() {
     return list;
     }

     public void setList(List<E> list) {
     DocumentList dl = new DocumentList(list);
     this.list = list;
     setSelected(0);
     //setDocument(bs);
        
     }
     */
    public void setSelected(int index) {
        if (index < 0 || context.getDocumentList() == null || index > context.getDocumentList().size() - 1) {
            return;
        }
        if (context.getDocumentList() != null && ! context.getDocumentList().isEmpty()) {
            if (index >= 0 && index < context.getDocumentList().size()) {
                requestSelect((E) context.getDocumentList().get(index));
            }
        }
//        getBindingState().setSelected((E)list.get(index));
    }

    public E getSelected() {
        return (E) context.getSelected();
    }

    public int getSelectedIndex() {
        if (context.getDocumentList() == null || context.getDocumentList().isEmpty()) {
            return -1;
        }
        if (context.getSelected() == null) {
            return -1;
        }
        return context.getDocumentList().indexOf(context.getSelected());
    }

    public DataSourceContext getContext() {
        return context;
    }

    public BinderListener getBinderListener() {
        return binderListener;
    }

    public void requestSelect(E document) {
        BinderEvent.Action action =
                BinderEvent.Action.boundObjectChange;
        BinderEvent event = new BinderEvent(this, action, "*selected");
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
        context = (DataSourceContext) event.getSource();
        switch (event.getAction()) {
            case documentChanging:
                break;
            case documentChange:
                removeBoundObjectListeners();
                setComponentSelectedIndex(getSelectedIndex());
                addBoundObjectListeners();
                break;
            case activeStateChange:
                removeBoundObjectListeners();
                if (context.isActive()) {
                    setModel(createComponentModel());
                    setComponentSelectedIndex(getSelectedIndex());
                } else {
                    initDefaults();
                }
                addBoundObjectListeners();
                break;
        }
    }

    protected abstract void setDefaultComponentModel();

    protected abstract Object createComponentModel();

    protected abstract Object getModel();

    protected abstract void setModel(Object model);
    
    protected abstract int getComponentSelectedIndex();
     
    protected abstract void setComponentSelectedIndex(int selectedIndex);
    
}//class
