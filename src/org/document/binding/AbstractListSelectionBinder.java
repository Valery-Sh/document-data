package org.document.binding;

import java.util.List;
import org.document.Document;
import org.document.ValidationException;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractListSelectionBinder extends AbstractBinder {

    public AbstractListSelectionBinder() {
        this.propertyName = "selected";
        initBinder();
    }
    
    protected List<Document> getDocuments() {
        if ( document == null ) {
            return null;
        }
        return ((ListState)document).getDocumentList();
    }
    

    protected final void initBinder() {
        removeComponentListeners();
        addComponentListeners();
    }

    protected abstract void addComponentListeners();
    protected abstract void removeComponentListeners();

    
    
    @Override
    public abstract void setComponentValue(Object value);


            
    @Override
    public abstract Object getComponentValue();
    
    
    //
    // ==============================================
    //
    
}
