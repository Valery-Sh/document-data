package org.document.swing.binders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataListener;
import org.document.Document;
import org.document.binding.DocumentListBinder;

/**
 *
 * @author V. Shyshkin
 */
public class BdCompositeJComboBinder<E extends Document> extends DocumentListBinder implements ActionListener {

    protected String[] displayProperties;

    public BdCompositeJComboBinder() {
        super();
    }

    public BdCompositeJComboBinder(JComboBox component, String... properties) {
        super(component);
        this.displayProperties = properties;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boundObject) {
            setSelected(getJComboBox().getSelectedIndex());            
  //          JComboSelectionBinder s = (JComboSelectionBinder) selectedBinder;
//            s.boundObjectChanged(getBoundObject().getSelectedIndex());
        }
    }

    @Override
    public void addBoundObjectListeners() {
        getJComboBox().setKeySelectionManager(new KeySelectionManagerImpl());
        getJComboBox().addActionListener(this);

    }

    @Override
    public void removeBoundObjectListeners() {
        getJComboBox().setKeySelectionManager(null);
        if (getJComboBox() == null) {
            return;
        }
        getJComboBox().getActionListeners();
        ActionListener[] listeners = getJComboBox().getActionListeners();
        if (listeners != null) {
            for (ActionListener l : listeners) {
                getJComboBox().removeActionListener(l);
            }
        }
    }
    @Override
    public void initBoundObjectDefaults() {
  //      ((JComboModelBinder)documentListBinder).setDefaultComponentModel();
    }

    @Override
    public JComboBox getBoundObject() {
        return (JComboBox) boundObject;
    }

    public JComboBox getJComboBox() {
        return (JComboBox) boundObject;
    }

    public void setJComboBox(JComboBox comboBox) {
        setBoundObject(comboBox);
//        initBinders();
    }

    public String[] getDisplayProperties() {
        return displayProperties;
    }

    public void setDisplayProperties(String... displayProperties) {
        this.displayProperties = displayProperties;
        if (getJComboBox().getModel() != null && (getJComboBox().getModel() instanceof ComboBoxModelImpl)) {
            int idx = getJComboBox().getSelectedIndex();
            ((ComboBoxModelImpl) getJComboBox().getModel()).setProperties(displayProperties);
            // Set the same model. Just to notify the combo box of the model change
            getJComboBox().setModel(getJComboBox().getModel());
            // Set the same selectedIndex.
            getJComboBox().setSelectedIndex(idx);
            getJComboBox().repaint();
        }
    }

        @Override
        protected Object getModel() {
            return getJComboBox().getModel();
        }

        @Override
        protected void setModel(Object model) {
            getJComboBox().setModel((ComboBoxModelImpl) model);
        }

        @Override
        protected Object createComponentModel() {
            return new BdCompositeJComboBinder.ComboBoxModelImpl(getDisplayProperties(), getContext().getDocumentList());
        }

        @Override
        protected void setDefaultComponentModel() {
            getJComboBox().setModel(new DefaultComboBoxModel());
        }


    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if ( getModel() == null || !(getModel() instanceof ComboBoxModelImpl)) {
            return;
        }
        removeBoundObjectListeners();
        Object d = ((ComboBoxModelImpl)getModel()).getElement(getSelected());
        ((ComboBoxModelImpl)getModel()).selectedObject = d;
        getJComboBox().repaint();
        addBoundObjectListeners();
    }

    @Override
    protected int getComponentSelectedIndex() {
        return getJComboBox().getSelectedIndex();
    }

    @Override
    protected void setComponentSelectedIndex(int selectedIndex) {
        getJComboBox().setSelectedIndex(selectedIndex);
        getJComboBox().repaint();
    }

  
  
    public static class ComboBoxModelImpl<E extends Document> implements ComboBoxModel {

        protected Object selectedObject;
        private List<E> documents;
        private String[] properties;

        public ComboBoxModelImpl(String[] properties, List<E> documents) {
            this.documents = documents;
            this.properties = properties;
        }

        @Override
        public int getSize() {
            return documents.size();
        }

        @Override
        public Object getElementAt(int index) {
            E d = documents.get(index);
            return getElement(d);
            
        }

        public Object getElement(E d) {

            if (d == null) {
                return null;
            }
            String result = "";
            for (String nm : this.properties) {
                result = result += " " + d.propertyStore().getValue(nm);
            }
            return result;
        }

        public String[] getProperties() {
            return properties;
        }

        public void setProperties(String[] properties) {
            this.properties = properties;
        }

        @Override
        public void addListDataListener(ListDataListener l) {
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
        }

        @Override
        public void setSelectedItem(Object anObject) {
//            selectedObject = anObject;
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
    }

    public class KeySelectionManagerImpl implements JComboBox.KeySelectionManager {

        @Override
        public int selectionForKey(char c, ComboBoxModel cbm) {
            ComboBoxModelImpl impl = (ComboBoxModelImpl) cbm;
            int idx = getJComboBox().getSelectedIndex();
            int sel = idx;
            sel = selectionForKey(c, idx, impl);
            if (sel == -1) {
                sel = idx;
            }
            return sel;
        }

        public int selectionForKey(char c, int startPos, ComboBoxModelImpl model) {

            int idx = -1;
            if (Character.isWhitespace(c) || Character.isSpaceChar(idx)) {
                return 0;
            }
            if (displayProperties != null && displayProperties.length > 0) {
                for (int i = startPos; i < model.getSize(); i++) {

                    Object o = model.getElementAt(i);
                    if (o == null) {
                        continue;
                    }
                    String s = o.toString().trim();
                    if (s.length() == 0) {
                        idx = 0;
                        break;
                    }
                    if (s.toUpperCase().startsWith(Character.toString(c).toUpperCase())) {
                        idx = i;
                        break;
                    }
                }
            }
            return idx;
        }
    }//class KeySelectionManagerImpl
}
