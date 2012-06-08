package org.document.swing.binders;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
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

    protected String[] displayProperties;
    protected KeyListener keyListener;
    protected JLabel locator;

    public BdDocumentListJListBinder(JList component, String... properties) {
        super(component);
        this.displayProperties = properties;
        initBinders();
    }

    @Override
    protected void addComponentListeners() {
        getJList().removeKeyListener(keyListener);
        keyListener = new KeySelectionManagerImpl();
        getJList().addKeyListener(keyListener);

    }

    @Override
    protected void removeComponentListeners() {
        getJList().removeKeyListener(keyListener);
    }

    public JLabel getLocator() {
        return locator;
    }

    public void setLocator(JLabel locator) {
        this.locator = locator;
    }

    public JList getJList() {
        return (JList) boundObject;
    }

    public void setJList(JList jList) {
        setBoundObject(jList);
    }

    public String[] getDisplayProperties() {
        return displayProperties;
    }

    public void setDisplayProperties(String... displayProperties) {
        this.displayProperties = displayProperties;
        if (getJList().getModel() != null && (getJList().getModel() instanceof ListBoxModelImpl)) {
            ((ListBoxModelImpl) getJList().getModel()).setProperties(displayProperties);
        }
        getJList().repaint();
    }

    @Override
    protected PropertyBinder createSelectedBinder() {
        return new JListSelectionBinder((JList) getBoundObject());
    }

    @Override
    protected PropertyBinder createListModelBinder() {
        return new JListModelBinder((JList) getBoundObject(), displayProperties);
    }

    @Override
    protected PropertyBinder createDocumentChangeEventBinder() {
        return new JListDocumentChangeBinder((JList) getBoundObject());
    }

    public static class JListDocumentChangeBinder extends AbstractListDocumentChangeBinder {

        public JListDocumentChangeBinder(JList component) {
            super(component);
        }

        public JList getJList() {
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

        public JListSelectionBinder(JList component) {
            super(component);
        }

        @Override
        public JList getBoundObject() {
            return (JList) boundObject;
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
            if (getBoundObject() == null) {
                return;
            }
            ListSelectionListener[] listeners = getBoundObject().getListSelectionListeners();
            if (listeners != null) {
                for (ListSelectionListener l : listeners) {
                    getBoundObject().removeListSelectionListener(l);
                }
            }
        }

        @Override
        protected int getComponentSelectedIndex() {
            return getBoundObject().getSelectedIndex();
        }

        @Override
        protected void setComponentSelectedIndex(int selectedIndex) {
            getBoundObject().setSelectedIndex(selectedIndex);
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
        protected void setDefaultComponentModel() {
            DefaultListModel m = new DefaultListModel();
            m.clear();
            getBoundObject().setModel(m);
        }

        @Override
        protected Object createComponentModel() {
            return new ListBoxModelImpl(properties, getDocuments());
        }

        @Override
        protected Object getModel() {
            return getBoundObject().getModel();
        }

        @Override
        protected void setModel(Object model) {
            getBoundObject().clearSelection();
            getBoundObject().setModel((ListModel) model);
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

        public void setProperties(String[] properties) {
            this.properties = properties;

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
    }//class ListBoxModelImpl

    public class KeySelectionManagerImpl implements KeyListener {

        public void selectionForKey(char c) {
            int idx = getJList().getSelectedIndex();
            int sel = idx;
            if (locator == null) {
                sel = selectionForKey(c, idx);
            }
            if (sel == -1) {
                sel = idx;
            }

            getJList().setSelectedIndex(sel);

        }

        public int selectionForKey(char c, int startPos) {
            ListBoxModelImpl model = (ListBoxModelImpl) getJList().getModel();

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

        public void locatorSelection() {
            ListBoxModelImpl model = (ListBoxModelImpl) getJList().getModel();
            String text = locator.getText();
            int idx = -1;
            if (text == null) {//Character.isWhitespace(c) || Character.isSpaceChar(idx)) {
                return;
            }
            if (displayProperties != null && displayProperties.length > 0) {
                for (int i = 0; i < model.getSize(); i++) {

                    Object o = model.getElementAt(i);
                    if (o == null) {
                        continue;
                    }
                    String s = o.toString().trim();
                    if (s.length() == 0) {
                        idx = 0;
                        break;
                    }
                    if (s.toUpperCase().startsWith(text.toUpperCase())) {
                        idx = i;
                        break;
                    }
                }
            }
            if (idx >= 0) {
                getJList().setSelectedIndex(idx);
            }


            //getComboBox().setSelectedIndex(idx);
        }

        @Override
        public void keyTyped(KeyEvent ke) {
            char c = ke.getKeyChar();
            String tx = locator.getText();
            int idx = getJList().getSelectedIndex();
            if (locator == null) {
                selectionForKey(c);
            } else {
                switch (c) {
                    case KeyEvent.VK_BACK_SPACE:
                        if (!tx.isEmpty()) {
                            locator.setText(tx.substring(0, tx.length() - 1));
                        }
                        break;
                    case KeyEvent.VK_HOME:
                        locator.setText("");
                        getJList().setSelectedIndex(idx >= 0 ? 0 : idx);
                        break;
                    case KeyEvent.VK_END:
                        locator.setText("");
                        getJList().setSelectedIndex(getJList().getModel().getSize() - 1);
                        break;
                    default:
                        locator.setText(tx += Character.toString(c));
                        locatorSelection();
                }//switch
            }
        }

        @Override
        public void keyPressed(KeyEvent ke) {
        }

        @Override
        public void keyReleased(KeyEvent ke) {
        }
    }
}
