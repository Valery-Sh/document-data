package org.document.swing.binders;

import org.document.binding.ListBinder;
import org.document.binding.AbstractListBinder;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.document.*;

/**
 *
 * @author V. Shyshkin
 */
public class ListBoxListBinder<T extends Document> extends AbstractListBinder implements ListBinder, ListSelectionListener {

    protected JComponent jcomponent;
    //protected JList jcomponent;

    public ListBoxListBinder(List<T> documents, javax.swing.JComponent component, String... properties) {
        super(documents, properties);
        this.jcomponent = component;
        init();
    }

    protected JList getJList() {
        return (JList) jcomponent;
    }

    @Override
    protected void addComponentListener() {
        getJList().addListSelectionListener(this);

    }
    @Override
    protected void removeComponentListener() {
        ListSelectionListener[] listeners = getJList().getListSelectionListeners();
        if ( listeners != null ) {
            for ( ListSelectionListener l : listeners ) {
                getJList().removeListSelectionListener(l);
            }
        }
        
    }

    @Override
    protected void setComponentModel() {
        getJList().setModel(new ListModelImpl(properties, documents));
    }

    @Override
    protected int getComponentSelectedIndex() {
        return getJList().getSelectedIndex();
    }

    @Override
    protected void setComponentSelectedIndex(Integer selectedIndex) {
        getJList().setSelectedIndex(selectedIndex);
        //getJList().repaint();
        
        
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        componentChanged(getJList().getSelectedIndex());
        //System.out.println("Selected: " + getJList().getSelectedIndex());
    }

    @Override
    public void clearSelection() {
        getJList().getSelectionModel().clearSelection();
    }

    @Override
    public void listChanged(ListChangeEvent event) {
       getJList().setModel(new ListModelImpl(properties, (DocumentList)event.getSource())); 
    }

    public static class ListModelImpl<E extends Document> implements ListModel {

        private List<E> documents;
        private String[] properties;

        public ListModelImpl(String[] properties, List<E> documents) {
            this.documents = documents;
            this.properties = properties;
        }

        @Override
        public int getSize() {
            return documents.size();
        }

        @Override
        public Object getElementAt(int index) {
            Document d = documents.get(index);
            if (d == null) {
                return null;
            }
            String result = "";
            for (String nm : this.properties) {

                result = result += " " + d.getPropertyStore().get(nm);
            }
            return result;
        }

        @Override
        public void addListDataListener(ListDataListener l) {
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
        }
    }
}
