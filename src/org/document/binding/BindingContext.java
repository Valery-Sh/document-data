package org.document.binding;

import java.util.List;
import org.document.Document;

/**
 *
 * @author V. Shyskin
 */
public class BindingContext {
    
   private BindingManager manager;
   
   BindingContext(BindingManager manager) {
       this.manager = manager;
   }

    public Document getSelected() {
        if ( manager == null ) {
            return null;
        }
        return manager.getBindingState().getSelected();
    }

    public List<Document> getDocumentList() {
        if ( manager == null ) {
            return null;
        }
        return manager.getBindingState().getDocumentList();
    }

    protected void setManager(BindingManager manager) {
        this.manager = manager;
    }
   
   
}
