package org.document.binding;

import java.util.ArrayList;
import java.util.List;
import org.document.Document;
import org.document.DocumentChangeEvent;

/**
 *
 * @author Valery
 */
public abstract class AbstractListBinder<T extends Document> implements ListBinder {

    protected List<T> documents;
    protected String[] properties;

    protected List<BinderListener> binderListeners;

    public AbstractListBinder(List<T> documents, String... properties) {
        this.documents = documents;
        binderListeners = new ArrayList<BinderListener>();
        this.properties = properties;
        
        //ListSelectionListener ll = null;

    }
    
    protected final void init() {
        removeComponentListener();
        setComponentModel();
        addComponentListener();
    }

    protected abstract void addComponentListener();
    protected abstract void removeComponentListener();
    protected abstract void setComponentModel();
    /**
     * When extending the class the method is used instead of the 
     * {@link AbstractBinder#getComponentValue() }.
     * 
     * @see #setComponentValue(java.lang.Object)      
     * @see #getComponentValue() 
     * @see #setComponentSelectedIndex(java.lang.Integer) 
     * @return component selected index
     */
    protected abstract int getComponentSelectedIndex();    
    /**
     * When extending the class the method is used instead of the 
     * {@link AbstractBinder#setComponentValue(java.lang.Object) }.
     * 
     * @param selectedIndex the value to be set as an index of a list 
     * like component
     * @see #setComponentValue(java.lang.Object)      
     * @see #getComponentValue() 
     * @see #getComponentSelectedIndex() 
     */
    protected abstract void setComponentSelectedIndex(Integer selectedIndex);    
    
    @Override
    public void react(DocumentChangeEvent event) {
        Document document = (Document) event.getNewValue();
        switch (event.getAction()) {
            case documentChange:
//                if (document != null ) {
                    dataChanged(document);
//                }
                break;
        }//switch
    }
    
    protected void dataChanged(Object newValue) {
        int convertedValue = documents.indexOf((Document)newValue);
        if ( convertedValue == this.getComponentValue()) {
            return;
        }
        setComponentValue(convertedValue);
    }
    
    
    public void setComponentValue(Object value) {

        setComponentSelectedIndex((Integer)value);
    }


    protected void componentChanged(Object newValue) {
        int idx = (Integer)newValue;
        Document o = idx >= 0 ? documents.get(idx) : null;
        fireChangeSelected(o, idx);         
    }
            
    @Override
    public Object getComponentValue() {
        //
        // *** Samplefor ListBoxListBinder: return this.jcomponent.getSelectedIndex();
        //
        return getComponentSelectedIndex();
    }
        
    @Override
    public void addBinderListener(BinderListener l) {
        this.binderListeners.add(l);
    }

    @Override
    public void removeBinderListener(BinderListener l) {
        this.binderListeners.remove(l);
    }

    /**
     * Sample for ListBox
     * 
     *
     *
     *   public void valueChanged(ListSelectionEvent e) {
     *      if (e.getValueIsAdjusting()) {
     *           return;
     *      }
     *      componentChanged(component.getSelectedIndex());
     *   }
     */ 

    /**
     * 
     * @param dataValue
     * @param componentValue 
     */
    protected void fireChangeSelected(Document newDoc, Object componentValue) {
        BinderEvent.Action action = BinderEvent.Action.componentSelectChange;
        BinderEvent event = new BinderEvent(this,action,newDoc,componentValue);
        notifyBinderListeners(event);
    }
    /**
     * Notifies  DocumentListBinding instance that owns the binder of component
     * selection change.
     * 
     * @param event 
     */
    private void notifyBinderListeners(BinderEvent event) {
        for ( BinderListener l : binderListeners) {
            l.react(event);
        }
    }
    /**
     * Sample for ListBox
     * 
     *
     *
     *   public void valueChanged(ListSelectionEvent e) {
     *      if (e.getValueIsAdjusting()) {
     *           return;
     *      }
     *      componentChanged(component.getSelectedIndex());
     *   }
     */ 

}
