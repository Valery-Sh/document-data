/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.swing.binders;

import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import org.document.Document;

/**
 *
 * @author Valery
 */
public class DocumentListBoxModel<E extends Document> implements ListModel {

    protected Object selectedObject;
    private List<E> documents;
    private String[] properties;

    public DocumentListBoxModel(List<E> documents) {
        this.documents = documents;
        if ( documents != null  && ! documents.isEmpty()) {
            this.selectedObject = documents.get(0);
        }
    }

    @Override
    public int getSize() {
        return documents.size();
    }

    public List<E> getDocuments() {
        return documents;
    }

    @Override
    public Object getElementAt(int index) {
        return  documents.get(index);
    }

    public String[] getProperties() {
        return properties;
    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }

}

