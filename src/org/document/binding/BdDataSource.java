/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import java.util.List;
import org.document.Document;
import org.document.DocumentList;

/**
 *
 * @author Valery
 */
public class BdDataSource<T extends Document> {//extends JComponent{
    private BindingManager bindingManager;
    private List<T>  sourceList;
    private boolean active;
    public BdDataSource() {
        bindingManager = new BindingManager();
//        this.setVisible(false);
    }
    public BdDataSource(List<T> sourceList) {
        bindingManager = new BindingManager(sourceList);
//        this.setVisible(false);
        
        
    }

    public boolean isActive() {
        return bindingManager.isActive();
    }

    public void setActive(boolean active) {
        bindingManager.setActive(active);
    }

    public List<T> getSourceList() {
        return bindingManager.getSourceList();
    }

    public void setSourceList(List<T> sourceList) {
        //this.sourceList = sourceList;
        bindingManager.setSourceList(sourceList);
    }

    public BindingManager getBindingManager() {
        return bindingManager;
    }
    
    /**
     * Registers a given binder.
     * 
     * @param binder a binder to be registered
     * @see ListStateBinder
     * @throws an exception of type @{@link java.lang.IllegalArgumentException}
     * in case when the binding manager was created without a document list 
     * specified
     */
    public void bind(ListStateBinder binder) {
        this.bindingManager.bind(binder);
    }

    /**
     * Unregisters a given binder.
     * @param binder the binder of type {@link ListStateBinder} to be unregistered
     */
    public void unbind(ListStateBinder binder) {
        this.bindingManager.unbind(binder);
    }
    
    public void bind(String propertyName,String alias,HasBinder object) {
        this.bindingManager.bind(propertyName, alias, object);
    }

    public void bind(String propertyName,Class alias,HasBinder object) {
        this.bindingManager.bind(propertyName, alias, object);
    }
    public void bind(Class alias,HasBinder object) {
        this.bindingManager.bind(alias, object);
    }
    public void bind(HasBinder object) {
        this.bindingManager.bind(object);
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
        bindingManager.setRecognizer(recognizer);
    }
    public int selectedIndex() {
        if ( getSelected() == null ) {
            return -1;
        }
        return getDocuments().indexOf(getSelected());
    }
    public T getSelected() {
        return (T)bindingManager.getSelected();
    }

    public void setSelected(T selected) {
        bindingManager.setSelected(selected);
    }

    public DocumentList getDocuments() {
        return bindingManager.getDocuments();
    }
    
}
