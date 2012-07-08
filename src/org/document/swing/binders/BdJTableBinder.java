package org.document.swing.binders;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import org.document.Document;
import org.document.Registry;
import org.document.binding.DocumentListBinder;

/**
 *
 * @author V. Shyshkin
 */
public class BdJTableBinder<E extends Document> extends DocumentListBinder implements ListSelectionListener {//BindingStateBinder {

    protected String[] displayProperties;
    protected KeyListener keyListener;
    protected JLabel locator;

    public BdJTableBinder(JTable component, String... properties) {
        super(component);
        component.setRowSelectionAllowed(true);
        component.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //JTableHeader th = new JTableHeader;
        
        this.displayProperties = properties;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        setSelected(getJTable().getSelectedRow());
    }

    @Override
    public JTable getBoundObject() {
        return (JTable) boundObject;
    }

    @Override
    public void addBoundObjectListeners() {
        getJTable().getSelectionModel().addListSelectionListener(this);
    }

    @Override
    public void removeBoundObjectListeners() {
        if (getJTable() == null) {
            return;
        }
        getJTable().getSelectionModel().removeListSelectionListener(this);
    }

    @Override
    public void initBoundObjectDefaults() {
        //((JListModelBinder)documentListBinder).setDefaultComponentModel();
    }

    public JLabel getLocator() {
        return locator;
    }

    public void setLocator(JLabel locator) {
        this.locator = locator;
    }

    public void setJTable(JTable jTable) {
        setBoundObject(jTable);
    }

    public String[] getDisplayProperties() {
        return displayProperties;
    }

    public void setDisplayProperties(String... displayProperties) {
        this.displayProperties = displayProperties;
        if (getJTable().getModel() != null && (getJTable().getModel() instanceof BdCompositeJListBinder.ListBoxModelImpl)) {
            ((BdCompositeJListBinder.ListBoxModelImpl) getJTable().getModel()).setProperties(displayProperties);
        }
        getJTable().repaint();
    }

    @Override
    protected void setDefaultComponentModel() {
        DefaultTableModel m = new DefaultTableModel();
        getJTable().setModel(m);
    }

    @Override
    protected Object createComponentModel() {
        return new TableModelImpl(getContext().getDocumentList());
    }

    @Override
    protected Object getModel() {
        return getJTable().getModel();
    }

    @Override
    protected void setModel(Object model) {
        getJTable().clearSelection();
        getJTable().setModel((TableModel) model);
    }

    @Override
    protected int getComponentSelectedIndex() {
        return getJTable().getSelectedRow();
    }

    @Override
    protected void setComponentSelectedIndex(int selectedIndex) {
        getJTable().setRowSelectionInterval(selectedIndex, selectedIndex);
    }

    protected JTable getJTable() {
        if (getBoundObject() == null) {
            return null;
        }
        return getBoundObject();
    }

    @Override
    public void initDefaults() {
        setDefaultComponentModel();
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        removeBoundObjectListeners();
        getJTable().repaint();
        addBoundObjectListeners();
    }

    public static class TableModelImpl<E extends Document> extends AbstractTableModel {

        private List<E> documents;
        private String[] properties;

        public TableModelImpl(List<E> documents) {
            this.documents = documents;
            //this.properties = properties;
        }
        
        public TableModelImpl(String[] properties, List<E> documents) {
            this.documents = documents;
            this.properties = properties;
        }

        @Override
        public int getRowCount() {
            return documents.size();
        }

        public String[] getProperties() {
            return properties;
        }

        public void setProperties(String[] properties) {
            this.properties = properties;

        }

        @Override
        public int getColumnCount() {
            int c = 0;
            if ( documents.size() > 0 ) {
                c = Registry.getSchema(documents.get(0).propertyStore().getOwner()).getFields().size();
            }
            return c;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Document d = documents.get(rowIndex);
            if (d == null) {
                return null;
            }
            String nm = Registry.getSchema(documents.get(0).propertyStore().getOwner()).getFields().get(columnIndex).getPropertyName().toString();
            return d.propertyStore().getValue(nm);
        }
    }//class ListBoxModelImpl

}
