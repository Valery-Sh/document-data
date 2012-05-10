package org.document.binding;

import java.util.List;
import org.document.Document;
import org.document.DocumentList;
import org.document.HasDocumentState;

/**
 *
 * @author V. Shishkin
 */
public class BindingManager<T extends Document> extends AbstractBindingManager<T> {

    protected DocumentList<T> documents;

    public BindingManager(List<T> sourceList) {
        super(sourceList);
        this.documents = new DocumentList<T>(sourceList);
        //this.sourceList = sourceList;
    }

    public DocumentList<T> getDocuments() {
        return documents;
    }

    @Override
    protected void afterSetSelected(T oldSelected) {
        if (documents.isNew(oldSelected) && !isEditing(oldSelected)) {
            documents.cancelNew();
        }
    }

    protected boolean isEditing(T doc) {
        boolean b = false;
        if (doc instanceof HasDocumentState) {
            b = ((HasDocumentState) doc).getDocumentState().isEditing();
        }
        return b;
    }
}