package org.document.swing.binders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataListener;
import org.document.Document;
import org.document.binding.AbstractListModelBinder;
import org.document.binding.AbstractListSelectionBinder;
import org.document.binding.ListStateBinder;
import org.document.binding.PropertyBinder;

/**
 *
 * @author V. Shyshkin
 */
public class JComboBoxListBinder<T extends PropertyBinder> extends ListStateBinder {

    protected String[] properties;

    public JComboBoxListBinder(JComboBox component, String... properties) {
        super(component);
        this.properties = properties;
        initBinders();
    }

    @Override
    protected PropertyBinder createSelectedBinder() {
        return new JComboBoxListBinder.JComboSelectionBinder((JComboBox) getAlias());
    }

    @Override
    protected PropertyBinder createListModelBinder() {
        return new JComboBoxListBinder.JComboModelBinder((JComboBox) getAlias(), properties);
    }

    public static class JComboSelectionBinder extends AbstractListSelectionBinder implements ActionListener {

        //protected JList component;
        public JComboSelectionBinder(JComboBox component) {
            super(component);
        }

        protected JComboBox getJComboBox() {
            return (JComboBox) component;
        }

        @Override
        public void propertyChanged(Object propertyValue) {
            super.propertyChanged(propertyValue);
            getJComboBox().repaint();// if omitted then doesn't change selected item
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == getJComboBox()) {
                componentChanged(getComponentSelectedIndex());
                //getJComboBox().repaint(); // if omitted then doesn't change presentation
            }
        }

        @Override
        protected void addComponentListeners() {
            getJComboBox().addActionListener(this);

        }

        @Override
        protected void removeComponentListeners() {
            getJComboBox().getActionListeners();
            ActionListener[] listeners = getJComboBox().getActionListeners();
            if (listeners != null) {
                for (ActionListener l : listeners) {
                    getJComboBox().removeActionListener(l);
                }
            }

        }

        @Override
        public void setComponentValue(Object value) {
            setComponentSelectedIndex((Integer) value);
        }

        protected int getComponentSelectedIndex() {
            return getJComboBox().getSelectedIndex();
        }

        protected void setComponentSelectedIndex(Integer selectedIndex) {
            getJComboBox().setSelectedIndex(selectedIndex);
        }

        @Override
        public Object getComponentValue() {
            return getComponentSelectedIndex();
        }

        @Override
        protected Object componentValueOf(Object dataValue) {
            Document doc = (Document) dataValue;
            if (doc == null) {
                return -1;
            }
            return getDocuments().indexOf(doc);

        }

        @Override
        protected Object propertyValueOf(Object compValue) {
            int index = (Integer) compValue;
            if (index < 0) {
                return null;
            }
            return getDocuments().get(index);
        }

        @Override
        public void initComponentDefault() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }//class JListSelectionBinder

    public static class JComboModelBinder extends AbstractListModelBinder {

        protected JComboBox component;
        protected String[] properties;

        public JComboModelBinder(JComboBox component, String... properties) {
            super();
            this.component = component;
            this.properties = properties;
        }


        @Override
        protected void addComponentListeners() {
        }

        @Override
        protected void removeComponentListeners() {
        }

        @Override
        public void setComponentValue(Object value) {
            //component.clearSelection();
            component.setModel(new JComboBoxListBinder.ComboBoxModelImpl(properties, getDocuments()));
        }

        @Override
        public Object getComponentValue() {
            return component.getModel();
        }

        @Override
        protected Object componentValueOf(Object dataValue) {
            if (getDocuments() == null) {
                return null;
            }
            return new JComboBoxListBinder.ComboBoxModelImpl(properties, getDocuments());
        }

        @Override
        protected Object propertyValueOf(Object compValue) {
            if (compValue == null) {
                return null;
            }
            return ((JComboBoxListBinder.ComboBoxModelImpl) component.getModel()).documents;
        }

        @Override
        public void initComponentDefault() {
            throw new UnsupportedOperationException("Not supported yet.");
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
            Document d = documents.get(index);
            if (d == null) {
                return null;
            }
            String result = "";
            for (String nm : this.properties) {
                result = result += " " + d.propertyStore().get(nm);
            }
            return result;
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
}
