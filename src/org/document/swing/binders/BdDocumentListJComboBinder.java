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
public class BdDocumentListJComboBinder<E extends Document> extends BindingStateBinder {

    protected String[] displayProperties;

    public BdDocumentListJComboBinder() {
        super();
        initBinders();
    }

    public BdDocumentListJComboBinder(JComboBox component, String... properties) {
        super(component);
        this.displayProperties = properties;
        initBinders();
    }

    public JComboBox getComboBox() {
        return (JComboBox) getBoundObject();
    }

    public void setComboBox(JComboBox comboBox) {
        setBoundObject(comboBox);
        initBinders();
    }

    public String[] getDisplayProperties() {
        return displayProperties;
    }

    public void setDisplayProperties(String... displayProperties) {
        this.displayProperties = displayProperties;
    }
    @Override
    public void updateBinders() {
        super.updateBinder("selected");
        super.updateBinder("documentList", ((JComboBox)getBoundObject()).getModel());
        super.updateBinder("documentChangeEvent", null);
            
    }
/*    @Override
    protected void updateBinder(String propertyName, Object component) {
        for (PropertyBinder b : binders) {
            
            if ( ! propertyName.equals(b.getBoundProperty())) {
                continue;
            }
            b.setBoundObject(null);
            b.setBoundObject(component);
        }
    }
*/
    @Override
    protected PropertyBinder createSelectedBinder() {
        return new BdDocumentListJComboBinder.JComboSelectionBinder((JComboBox) getBoundObject());
    }

    @Override
    protected PropertyBinder createListModelBinder() {
        return new BdDocumentListJComboBinder.JComboModelBinder((JComboBox) getBoundObject(), displayProperties);
    }

    @Override
    protected PropertyBinder createDocumentChangeEventBinder() {
        return new BdDocumentListJComboBinder.JComboDocumentChangeBinder((JComboBox) getBoundObject());
    }

    public static class JComboDocumentChangeBinder<E extends Document> extends AbstractListDocumentChangeBinder {

        public JComboDocumentChangeBinder(JComboBox component) {
            super(component);
        }
public void propertyChanged(String p, Object v) {
    super.propertyChanged(p,v);
}
        protected JComboBox getJComboBox() {
            //return (JComboBox) boundObject;
            return (JComboBox) getComponent();
        }

        @Override
        protected void notifyComponentOf(DocumentChangeEvent event) {
            if (event == null) {
                return;
            }
            ComboBoxModelImpl model = (ComboBoxModelImpl) getJComboBox().getModel();
            String[] props = model.getProperties();
            if (Arrays.asList(props).contains(event.getPropertyName())) {
                //Document d = (())
                Object selItem = model.getElement((E) event.getSource());
                model.setSelectedItem(selItem);
                getJComboBox().repaint();
            }
        }
    }

    public static class JComboSelectionBinder<E extends Document> extends AbstractListSelectionBinder implements ActionListener {

        public JComboSelectionBinder(JComboBox component) {
            super(component);
        }

        protected JComboBox getJComboBox() {
            return (JComboBox) boundObject;
        }

        @Override
        public void propertyChanged(String property,Object propertyValue) {
            super.propertyChanged(property,propertyValue);
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
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == getJComboBox()) {
                componentChanged(getComponentSelectedIndex());
            }
        }

        @Override
        protected void addComponentListeners() {
            getJComboBox().addActionListener(this);

        }

        @Override
        protected void removeComponentListeners() {
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
            E doc = (E) dataValue;
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
            //throw new UnsupportedOperationException("Not supported yet.");
        }
    }//class JListSelectionBinder

    public static class JComboModelBinder extends AbstractListModelBinder {

        protected String[] properties;

        public JComboModelBinder(JComboBox component, String... properties) {
            this.boundObject = component;
            this.properties = properties;
        }
        @Override
        public JComboBox getBoundObject() {
            return (JComboBox) boundObject;
        }
/*
        public void setJComboBox(JComboBox component) {
            setBoundObject(component);
        }
*/
/*        @Override
        protected void addComponentListeners() {
        }

        @Override
        protected void removeComponentListeners() {
        }

        @Override
        public void setComponentValue(Object value) {
            getBoundObject().setModel(new BdDocumentListJComboBinder.ComboBoxModelImpl(properties, getDocuments()));
        }

        @Override
        public Object getComponentValue() {
            return getBoundObject().getModel();
        }
*/ 
        @Override
        protected Object getModel() {
            return getBoundObject().getModel();
        }
        @Override
        protected void setModel(Object model){
            getBoundObject().setModel(new BdDocumentListJComboBinder.ComboBoxModelImpl(properties, getDocuments()));            
        } 
        
        @Override
        protected Object componentValueOf(Object dataValue) {
            if (getDocuments() == null) {
                return null;
            }
            return new BdDocumentListJComboBinder.ComboBoxModelImpl(properties, getDocuments());
        }

        @Override
        protected Object propertyValueOf(Object compValue) {
            if (compValue == null) {
                return null;
            }
            return ((BdDocumentListJComboBinder.ComboBoxModelImpl) getBoundObject().getModel()).documents;
        }


        @Override
        protected void createDefaultComponentModel() {
            ((JComboBox) getBoundObject()).setModel(new DefaultComboBoxModel());
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
