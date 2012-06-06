package org.document.binding;

import java.util.List;
import org.document.Document;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractListSelectionBinder extends AbstractEditablePropertyBinder {
    
    
    public AbstractListSelectionBinder(Object component) {
        this.boundProperty = "selected";
        this.boundObject = component;
        initBinder();
    }
    
    protected List<Document> getDocuments() {
        if ( document == null ) {
            return null;
        }
        return ((BindingState)document).getDocumentList();
    }
    

    protected final void initBinder() {
        removeComponentListeners();
        addComponentListeners();
    }

    @Override
    protected abstract void addComponentListeners();
    @Override
    protected abstract void removeComponentListeners();

    
    
    @Override
    public abstract void setComponentValue(Object value);


            
    @Override
    public abstract Object getComponentValue();
    
    
    //
    // ==============================================
    //
    
}
