package org.document.binding;

import java.util.List;
import org.document.Document;

/**
 *
 * @author V. Shyskin
 */
public class BindingContext {
    
   private DocumentDataSource manager;
   
   BindingContext(DocumentDataSource manager) {
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

    protected void setManager(DocumentDataSource manager) {
        this.manager = manager;
    }
   
   
}
