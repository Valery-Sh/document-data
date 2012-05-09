/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.swing.binders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataListener;
import org.document.binding.AbstractListBinder;
import org.document.Document;

/**
 *
 * @author V. Shyshkin
 */
public class ComboBoxListBinder<T extends Document> extends AbstractListBinder implements ActionListener {

    protected JComboBox jcomponent;
    protected ComboBoxModel model;

    public ComboBoxListBinder(List<T> documents, javax.swing.JComponent component, String... properties) {
        super(documents, properties);
        jcomponent = (JComboBox) component;
        init();
    }

    @Override
    protected void addComponentListener() {
        this.jcomponent.addActionListener(this);

    }

    @Override
    protected void setComponentModel() {
        jcomponent.setModel(model = new ComboBoxModelImpl(this.jcomponent, properties, documents));
    }

    @Override
    protected int getComponentSelectedIndex() {
        return jcomponent.getSelectedIndex();

    }

    @Override
    protected void setComponentSelectedIndex(Integer selectedIndex) {
        jcomponent.setSelectedIndex(selectedIndex);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jcomponent) {
            componentChanged(getComponentSelectedIndex());
            jcomponent.repaint(); // if omitted then doesn't change presentation
        }
    }

    public static class ComboBoxModelImpl<E extends Document> implements ComboBoxModel {

        protected Object selectedObject;
        protected Object displaySelectedObject;
        private List<E> documents;
        private String[] properties;
        private JComboBox comboBox;

        public ComboBoxModelImpl(JComboBox comboBox, String[] properties, List<E> documents) {
            this.documents = documents;
            this.properties = properties;
            this.comboBox = comboBox;
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

        @Override
        public void addListDataListener(ListDataListener l) {
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
        }
    }
}
