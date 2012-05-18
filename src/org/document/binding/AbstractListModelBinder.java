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
public abstract class AbstractListModelBinder  extends AbstractBinder {

    public AbstractListModelBinder() {
        this.propertyName = "documentList";
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

