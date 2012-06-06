package org.document.swing.binders;

import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.document.Document;
import org.document.DocumentChangeEvent;
import org.document.binding.AbstractListDocumentChangeBinder;
import org.document.binding.AbstractListModelBinder;
import org.document.binding.AbstractListSelectionBinder;
import org.document.binding.DocumentListBinder;
import org.document.binding.PropertyBinder;

/**
 *
 * @author V. Shyshkin
 */
public class BdDocumentListJListBinder<E extends Document> extends DocumentListBinder {//BindingStateBinder {

    protected String[] properties;

    public BdDocumentListJListBinder(JList component, String... properties) {
        super(component);
        this.properties = properties;
        initBinders();
    }

    @Override
    protected PropertyBinder createSelectedBinder() {
        return new JListSelectionBinder((JList) getBoundObject());
    }

    @Override
    protected PropertyBinder createListModelBinder() {
        return new JListModelBinder((JList) getBoundObject(), properties);
    }

    @Override
    protected PropertyBinder createDocumentChangeEventBinder() {
        return new JListDocumentChangeBinder((JList) getBoundObject());
    }

    public static class JListDocumentChangeBinder extends AbstractListDocumentChangeBinder {

        public JListDocumentChangeBinder(JList component) {
            super(component);
        }

        protected JList getJList() {
            return (JList) boundObject;
        }

        @Override
        protected void notifyComponentOf(DocumentChangeEvent event) {
            if (event == null) {
                return;
            }

            BdDocumentListJListBinder.ListBoxModelImpl model = (BdDocumentListJListBinder.ListBoxModelImpl) getJList().getModel();
            String[] props = model.getProperties();
            if (Arrays.asList(props).contains(event.getPropertyName())) {
                getJList().repaint();

            }
        }
    }

    public static class JListSelectionBinder<E extends Document> extends AbstractListSelectionBinder implements ListSelectionListener {

        //protected JList boundObject;
        public JListSelectionBinder(JList component) {
            super(component);
        }

        @Override
        public JList getBoundObject() {
            return (JList) component;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }
            componentChanged(getBoundObject().getSelectedIndex());

        }

        @Override
        protected void addComponentListeners() {
            getBoundObject().addListSelectionListener(this);

        }

        @Override
        protected void removeComponentListeners() {
            ListSelectionListener[] listeners = getBoundObject().getListSelectionListeners();
            if (listeners != null) {
                for (ListSelectionListener l : listeners) {
                    getBoundObject().removeListSelectionListener(l);
                }
            }

        }

        @Override
        public void setComponentValue(Object value) {
            setComponentSelectedIndex((Integer) value);
        }

        protected int getComponentSelectedIndex() {
            return getBoundObject().getSelectedIndex();
        }

        protected void setComponentSelectedIndex(Integer selectedIndex) {
            getBoundObject().setSelectedIndex(selectedIndex);
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

    public static class JListModelBinder extends AbstractListModelBinder {

        protected String[] properties;

        public JListModelBinder(JList component, String... properties) {
            super();
            this.boundObject = component;
            this.properties = properties;
        }

        @Override
        public JList getBoundObject() {
            return (JList) boundObject;
        }

        @Override
        protected void addComponentListeners() {
        }

        @Override
        protected void removeComponentListeners() {
        }

        @Override
        public void setComponentValue(Object value) {
            getBoundObject().clearSelection();
            getBoundObject().setModel(new ListBoxModelImpl(properties, getDocuments()));
        }

        @Override
        protected void initComponentDefault() {
        }

        @Override
        public void setBoundObject(Object bo) {
            if ( getBoundObject() != null ) {
                removeComponentListeners();
                DefaultListModel m = new DefaultListModel();
                m.clear();
                getBoundObject().setModel(m);
                
            }
        }
        /*        @Override
         public void react(DocumentChangeEvent event) {
         super.react(event);
         switch (event.getAction()) {
         case unbind:
         DefaultListModel m = new DefaultListModel();
         m.clear();
         getBoundObject().setModel(m);
         break;
         case bind:
         if (event.getPropertyName() != null && !event.getPropertyName().equals(boundProperty)) {
         break;
         }
         break;
         }
         }
*/
         @Override
         public Object getComponentValue() {
         return getBoundObject().getModel();
         }

         @Override
         protected Object componentValueOf(Object dataValue) {
         if (getDocuments() == null) {
         return null;
         }
         return new ListBoxModelImpl(properties, getDocuments());
         }

         @Override
         protected Object propertyValueOf(Object compValue) {
         if (compValue == null) {
         return null;
         }
         return ((ListBoxModelImpl) getBoundObject().getModel()).documents;
         }
         }//class JListListModelBinder

         public static class ListBoxModelImpl<E extends Document> implements ListModel {

         private List<E> documents;
         private String[] properties;

         public ListBoxModelImpl(String[] properties, List<E> documents) {
         this.documents = documents;
         this.properties = properties;
         }

         @Override
         public int getSize() {
         return documents.size();
         }

         public String[] getProperties() {
         return properties;
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
         }
              }
