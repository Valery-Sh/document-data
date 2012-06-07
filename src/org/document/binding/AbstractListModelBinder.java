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
        removeComponentListeners();
        addComponentListeners();
    }

    protected abstract void createDefaultComponentModel();

    @Override
    protected void addComponentListeners() {
    }

    @Override
    protected void removeComponentListeners() {
    }

    @Override
    public void initComponentDefault() {
    }

    @Override
    public void setComponentValue(Object value) {
        setModel(value);
    }

    @Override
    public Object getComponentValue() {
        return getModel();
    }

    protected abstract Object getModel();

    protected abstract void setModel(Object model);

    @Override
    public void setBoundObject(Object bo) {
        if (getBoundObject() == bo) {
            return;
        }
        if (getBoundObject() != null) {
            removeComponentListeners();
            createDefaultComponentModel();
        }
        boundObject = bo;
    }
    //
    // ==============================================
    //
}
