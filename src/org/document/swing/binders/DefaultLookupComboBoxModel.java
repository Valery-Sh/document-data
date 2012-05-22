package org.document.swing.binders;

import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author V. Shyhkin
 */
public class DefaultLookupComboBoxModel<E> implements ComboBoxModel {
        protected E selectedObject;

        private List<E> documents;

        public DefaultLookupComboBoxModel(List<E> documents) {
            this.documents = documents;
        }

        @Override
        public int getSize() {
            return documents.size();
        }

        @Override
        public E getElementAt(int index) {
            return documents.get(index);
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
                selectedObject = (E)anObject;
            } else {
                if (anObject == null) {
                    selectedObject = null;
                }
            }
        }

        @Override
        public E getSelectedItem() {
            return selectedObject;
        }
    
}
