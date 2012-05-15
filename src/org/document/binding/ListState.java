package org.document.binding;

import java.util.Date;
import java.util.List;
import org.document.Document;
import org.document.DocumentPropertyStore;
import org.document.PropertyStore;

/**
 *
 * @author V. Shyshkin
 */
public class ListState implements Document {

    protected DocumentPropertyStore document;

    public ListState() {
        // DocumentPropertyStore is a default PropertyStore
        this.document = new DocumentPropertyStore(this);
    }

    
    //
    // Document interface implementation
    //
    @Override
    public PropertyStore getPropertyStore() {
        return this.document;
    }
    //
    // ===================================================
    //
    private Document selected;
    private List<Document> listModel;

    public List<Document> getListModel() {
        return listModel;
    }

    public void setListModel(List<Document> listModel) {
        this.listModel = listModel;
        document.put("listModel", listModel);
    }

    public Document getSelected() {
        return selected;
    }

    public void setSelected(Document selected) {
        this.selected = selected;
        document.put("selected", selected);
 
    }


 
}
