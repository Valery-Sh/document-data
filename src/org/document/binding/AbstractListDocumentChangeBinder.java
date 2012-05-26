package org.document.binding;

import java.util.List;
import org.document.Document;
import org.document.DocumentChangeEvent;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractListDocumentChangeBinder extends AbstractEditablePropertyBinder{
    
    protected Object component;
    
    public AbstractListDocumentChangeBinder(Object component) {
        this.propertyName = "documentChangeEvent";
        this.component = component;
        initBinder();
    }
    
    protected List<Document> getDocuments() {
        if ( document == null ) {
            return null;
        }
        return ((ListState)document).getDocumentList();
    }
    
/*    @Override
    public void propertyChanged(Object propertyValue) {
       this.notifyComponentOf((DocumentChangeEvent)propertyValue);
    }
*/  
    protected abstract void notifyComponentOf(DocumentChangeEvent event);
    
    protected final void initBinder() {
    }

    
    @Override
    public void setComponentValue(Object value) {
        
    }


            
    @Override
    public Object getComponentValue() {
        return "";
    }
    
    
    //
    // ==============================================
    //

    @Override
    protected Object componentValueOf(Object propertyValue) {
        this.notifyComponentOf((DocumentChangeEvent)propertyValue);
        return "";
    }

    @Override
    protected Object propertyValueOf(Object compValue) {
        return "";
    }

    @Override
    public void initComponentDefault() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void addComponentListeners() {
        
    }

    @Override
    protected void removeComponentListeners() {
    }
  
}
