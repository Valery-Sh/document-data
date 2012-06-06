package org.document.binding;

import java.util.List;
import org.document.Document;
import org.document.DocumentChangeEvent;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractListDocumentChangeBinder extends AbstractEditablePropertyBinder {


    public AbstractListDocumentChangeBinder(Object component) {
        this.boundProperty = "documentChangeEvent";
        this.boundObject = component;
        initBinder();
    }

    protected List<Document> getDocuments() {
        if (document == null) {
            return null;
        }
        return ((BindingState) document).getDocumentList();
    }

    protected abstract void notifyComponentOf(DocumentChangeEvent event);

    protected final void initBinder() {
    }

    @Override
    public void setComponentValue(Object value) {
    }

    @Override
    public Object getComponentValue() {
        return null;
    }

    //
    // ==============================================
    //
    @Override
    protected Object componentValueOf(Object propertyValue) {
        this.notifyComponentOf((DocumentChangeEvent) propertyValue);
        return null;
    }

    @Override
    protected Object propertyValueOf(Object compomentValue) {
        return null;
    }

    @Override
    public void initComponentDefault() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void addComponentListeners() {
    }

    @Override
    protected void removeComponentListeners() {
    }
}
