package org.document.swing.binders;

import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.document.Document;
import org.document.Registry;
import org.document.binding.DocumentListBinder;
import org.document.schema.DocumentSchema;
import org.document.schema.SchemaField;

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
    }

    @Override
    protected void setDefaultComponentModel() {
        DefaultTableModel m = new DefaultTableModel();
        getJTable().setModel(m);
    }

    @Override
    protected Object createComponentModel() {
        List<Document> documents = getContext().getDocumentList();
        
        if (documents.size() > 0 && displayProperties != null && displayProperties.length > 0) {
            List<SchemaField> fields = Registry.getSchema(documents.get(0).propertyStore().getOwner()).getFields();            
            DocumentSchema sc = Registry.getSchema(documents.get(0).propertyStore().getOwner());
            
            TableColumnModel tcm = new DefaultTableColumnModel();
            for ( int i=0; i < displayProperties.length; i++ ) {
                SchemaField f = sc.getField(displayProperties[i]);
                TableColumn tc = new TableColumn(fields.indexOf(f));
                tcm.addColumn(tc);
                tc.setHeaderValue(f.getDisplayName());
            }
            getJTable().setAutoCreateColumnsFromModel(false);
            getJTable().setColumnModel(tcm);
        }
        
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
        getJTable().setAutoscrolls(true);
        getJTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

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
        protected DocumentSchema schema;
        
        public TableModelImpl(List<E> documents) {
            this(null,documents);
            //this.properties = properties;
        }

        public TableModelImpl(String[] properties, List<E> documents) {
            this.documents = documents;
            this.properties = properties;
            createSchema();
        }
        protected final void createSchema() {
            if ( documents.size() > 0 ) {
                schema = Registry.getSchema(documents.get(0).propertyStore().getOwner());                
            }
        }
        protected DocumentSchema getSchema() {
            if ( schema != null ) {
                return schema;
            }
            createSchema();
            return schema;
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
        public String getColumnName(int index) {
            if ( getSchema() != null ) {
                return getSchema().getFields().get(index).getDisplayName();                
            }
            return null;
        }
        
        @Override
        public Class getColumnClass(int index) {
            if ( getSchema() != null ) {
                return getSchema().getFields().get(index).getPropertyType();                
            }
            return null;
        }

        @Override
        public int getColumnCount() {
            int c = 0;
            if ( getSchema() != null ) {
                c = getSchema().getFields().size();
            }
            return c;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Document d = documents.get(rowIndex);
            if (d == null) {
                return null;
            }
            String nm = getSchema().getFields().get(columnIndex).getPropertyName().toString();
            return d.propertyStore().getValue(nm);
        }
    }//class TableModelImpl
}
