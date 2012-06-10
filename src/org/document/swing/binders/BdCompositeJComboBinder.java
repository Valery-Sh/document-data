package org.document.swing.binders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataListener;
import org.document.Document;
import org.document.DocumentChangeEvent;
import org.document.binding.AbstractListDocumentChangeBinder;
import org.document.binding.AbstractListModelBinder;
import org.document.binding.AbstractListSelectionBinder;
import org.document.binding.BindingStateBinder;
import org.document.binding.PropertyBinder;

/**
 *
 * @author V. Shyshkin
 */
public class BdCompositeJComboBinder<E extends Document> extends BindingStateBinder implements ActionListener {

    protected String[] displayProperties;

    public BdCompositeJComboBinder() {
        super();
        initBinders();
    }

    public BdCompositeJComboBinder(JComboBox component, String... properties) {
        super(component);
        this.displayProperties = properties;
        initBinders();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boundObject) {
            JComboSelectionBinder s = (JComboSelectionBinder) selectedBinder;
            s.componentChanged(getBoundObject().getSelectedIndex());
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
        ((JComboModelBinder)documentListBinder).setDefaultComponentModel();
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
        initBinders();
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
    protected PropertyBinder createSelectedBinder() {
        selectedBinder = new BdCompositeJComboBinder.JComboSelectionBinder(this);
        return selectedBinder;
    }

    @Override
    protected PropertyBinder createListModelBinder() {
        documentListBinder = new BdCompositeJComboBinder.JComboModelBinder(this, displayProperties);
        return documentListBinder;
    }

    @Override
    protected PropertyBinder createDocumentChangeEventBinder() {
        documentChangeEventBinder = new BdCompositeJComboBinder.JComboDocumentChangeBinder(this);
        return documentChangeEventBinder;
    }

    public static class JComboDocumentChangeBinder<E extends Document> extends AbstractListDocumentChangeBinder {

        public JComboDocumentChangeBinder(BdCompositeJComboBinder component) {
            super(component);
        }

        @Override
        public BdCompositeJComboBinder getBoundObject() {
            return (BdCompositeJComboBinder) boundObject;
        }

        protected JComboBox getJComboBox() {
            return (JComboBox) getBoundObject().getBoundObject();
        }

        @Override
        protected void notifyComponentOf(DocumentChangeEvent event) {
            if (event == null) {
                return;
            }
            ComboBoxModelImpl model = (ComboBoxModelImpl) getJComboBox().getModel();
            String[] props = model.getProperties();
            if (Arrays.asList(props).contains(event.getPropertyName())) {
                Object selItem = model.getElement((E) event.getSource());
                model.setSelectedItem(selItem);
                getJComboBox().repaint();
            }
        }
    }

    public static class JComboSelectionBinder<E extends Document> extends AbstractListSelectionBinder {

        public JComboSelectionBinder(BdCompositeJComboBinder component) {
            super(component);
        }

        @Override
        public BdCompositeJComboBinder getBoundObject() {
            return (BdCompositeJComboBinder) boundObject;
        }

        protected JComboBox getJComboBox() {
            if (getBoundObject() == null) {
                return null;
            }
            return getBoundObject().getBoundObject();
        }

        @Override
        public void componentChanged(Object o) {
            super.componentChanged(o);
        }

        @Override
        public void propertyChanged(String property, Object propertyValue) {
            super.propertyChanged(property, propertyValue);
            getJComboBox().repaint();// if omitted then doesn't change selected item
        }

        /**
         * Prepends cyclic boundObject modifications.
         *
         * @param value a new value to be checked
         * @return <code>true</code> if the boundObject shouldn't be changed.          <code>false otherwise
         */
        @Override
        protected boolean needChangeComponent(Object value) {
            if ("selected".equals(getBoundProperty())) {
                return true;
            }
            return super.needChangeComponent(value);
        }
        
        @Override
        protected int getComponentSelectedIndex() {
            return getJComboBox().getSelectedIndex();
        }

        @Override
        protected void setComponentSelectedIndex(int selectedIndex) {
            getJComboBox().setSelectedIndex(selectedIndex);
        }
    }//class JListSelectionBinder

    public static class JComboModelBinder extends AbstractListModelBinder {

        protected String[] properties;

        public JComboModelBinder(BdCompositeJComboBinder component, String... properties) {
            super(component);
            this.properties = properties;
        }

        @Override
        public BdCompositeJComboBinder getBoundObject() {
            return (BdCompositeJComboBinder) boundObject;
        }

        protected JComboBox getJComboBox() {
            return getBoundObject().getBoundObject();
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
            return new BdCompositeJComboBinder.ComboBoxModelImpl(properties, getDocuments());
        }

        @Override
        protected void setDefaultComponentModel() {
            getJComboBox().setModel(new DefaultComboBoxModel());
        }
    }//class JListListModelBinder

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
                result = result += " " + d.propertyStore().get(nm);
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
