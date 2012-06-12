package org.document.binding;

import org.document.Document;
import org.document.DocumentList;

/**
 *
 * @author V. Shyskin
 */
public class BindingContext {

    private DocumentDataSource manager;

    BindingContext(DocumentDataSource manager) {
        this.manager = manager;
    }
    public boolean isActive() {
        return manager.isActive();
    }
    
    public Document getSelected() {
        if (manager == null || !manager.isActive()) {
            return null;
        }
        return manager.getSelected();
    }

    public DocumentList<Document> getDocumentList() {
        if (manager == null || !manager.isActive()) {
            return null;
        }
        return manager.getDocuments();
    }

    protected void setManager(DocumentDataSource manager) {
        this.manager = manager;
    }
}
