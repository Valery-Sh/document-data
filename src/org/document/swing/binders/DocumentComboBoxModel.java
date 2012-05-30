package org.document.swing.binders;

import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import org.document.Document;

/**
 *
 * @author V. Shyshkin
 */
public class DocumentComboBoxModel<E extends Document> implements ComboBoxModel {

    protected Object selectedObject;
    private List<E> documents;
    private String[] properties;

    public DocumentComboBoxModel(List<E> documents) {
        this.documents = documents;
        if ( documents != null  && ! documents.isEmpty()) {
            this.selectedObject = documents.get(0);
        }
        //this.properties = properties;

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

/*    public Object getElement(Document d) {

        if (d == null) {
            return null;
        }
        String result = "";
        for (String nm : this.properties) {
            result = result += " " + d.propertyStore().get(nm);
        }
        return result;
    }
*/
    public String[] getProperties() {
        return properties;
    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }

    @Override
    public void setSelectedItem(Object anObject) {
//        System.out.println("0) Combo setSelectedItem " + anObject);        
        if ((selectedObject != null && !selectedObject.equals(anObject))
                || selectedObject == null && anObject != null) {
            selectedObject = anObject;
        } else {
            if (anObject == null) {
                selectedObject = anObject;
            }
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectedObject;
    }
    
    public class DocumentItem {
        
    }
}
