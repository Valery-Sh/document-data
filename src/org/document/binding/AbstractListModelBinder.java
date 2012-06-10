/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import java.util.List;
import org.document.Document;

/**
 *
 * @author Valery
 */
public abstract class AbstractListModelBinder extends AbstractPropertyBinder {

    public AbstractListModelBinder() {
        this("documentList");
        initBinder();
    }

    public AbstractListModelBinder(Object boundObject) {
        super(boundObject);
        this.boundProperty = "documentList";
        initBinder();
    }

    protected List<Document> getDocuments() {
        if (document == null) {
            return null;
        }
        return ((BindingState) document).getDocumentList();
    }

    protected final void initBinder() {
        removeBoundObjectListeners();
        addBoundObjectListeners();
    }


    @Override
    public void addBoundObjectListeners() {
    }

    @Override
    public void removeBoundObjectListeners() {
    }

    @Override
    public void initBoundObjectDefaults() {
    }

    @Override
    public void setComponentValue(Object value) {
        if ( value == null ) {
            return;
        }
        setModel(value);
    }

    @Override
    public Object getComponentValue() {
        return getModel();
    }

    protected abstract Object getModel();

    protected abstract void setModel(Object model);
    
    protected abstract Object createComponentModel();
    
    protected abstract void setDefaultComponentModel();

    
//    protected abstract void setModel(Object model);

    /*    @Override
     protected Object propertyValueOf(Object compValue) {
     if (compValue == null) {
     return null;
     }
     return ((BdDocumentListJComboBinder.ComboBoxModelImpl) getBoundObject().getModel()).documents;
     }
     */
    @Override
    protected Object propertyValueOf(Object compValue) {
        if (compValue == null) {
            return null;
        }
        return getDocuments();
        //return ((BdDocumentListJComboBinder.ComboBoxModelImpl) getBoundObject().getModel()).documents;
    }

        @Override
        protected Object componentValueOf(Object dataValue) {
            if (getDocuments() == null) {
                return null;
            }
            return createComponentModel();
        }

/*    @Override
    public void setBoundObject(Object bo) {
        if (getBoundObject() == bo) {
            return;
        }
        if (getBoundObject() != null) {
            removeBoundObjectListeners();
            setDefaultComponentModel();
        }
        boundObject = bo;
    }
*/ 
    //
    // ==============================================
    //
}
