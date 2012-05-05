package org.document;

import java.util.ArrayList;
import java.util.List;

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
        setComponentModel();
        addComponentListener();
    }

    protected abstract void addComponentListener();
    protected abstract void setComponentModel();
    /**
     * Sample for ListBoxListBinder: return this.component.getSelectedIndex();
     * @return 
     */
    protected abstract int getComponentSelectedIndex();    
    protected abstract void setComponentSelectedIndex(Integer selectedIndex);    
    
    @Override
    public void react(DocumentEvent event) {
        Document document = (Document) event.getNewValue();
        switch (event.getAction()) {
            case documentChange:
                if (document != null ) {
                    dataChanged(document);
                }
                break;
        }//switch
    }
    @Override
    public void dataChanged(Object newValue) {
        int convertedValue = documents.indexOf((Document)newValue);
        if ( convertedValue == this.getComponentValue()) {
            return;
        }
        setComponentValue(convertedValue);
    }
    
    
    public void setComponentValue(Object value) {
        //
        // *** Samplefor ListBoxListBinder: this.jcomponent.setSelectedIndex((Integer)value);
        //
        setComponentSelectedIndex((Integer)value);
    }

    @Override
    public void componentChanged(Object newValue) {
        int idx = (Integer)newValue;
        fireChangeSelected(documents.get(idx), idx);         
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
        BinderEvent.Action action = BinderEvent.Action.selectChange;
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
