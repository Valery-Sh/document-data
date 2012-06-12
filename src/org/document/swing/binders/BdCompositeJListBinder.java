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
import org.document.binding.Binder;
import org.document.binding.DocumentListBinder;
import org.document.binding.PropertyBinder;

/**
 *
 * @author V. Shyshkin
 */
public class BdCompositeJListBinder<E extends Document> extends DocumentListBinder implements ListSelectionListener {//BindingStateBinder {

    protected String[] displayProperties;
    protected KeyListener keyListener;
    protected JLabel locator;

    public BdCompositeJListBinder(JList component, String... properties) {
        super(component);
        this.displayProperties = properties;
        initBinders();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        ((JListSelectionBinder) selectedBinder).boundObjectChanged(getBoundObject().getSelectedIndex());
    }

    @Override
    public JList getBoundObject() {
        return (JList) boundObject;
    }

    @Override
    public void addBoundObjectListeners() {
        getJList().removeKeyListener(keyListener);
        keyListener = new BdCompositeJListBinder.KeySelectionManagerImpl();
        getJList().addKeyListener(keyListener);
        getJList().addListSelectionListener(this);
    }

    @Override
    public void removeBoundObjectListeners() {
        getJList().removeKeyListener(keyListener);
        if (getJList() == null) {
            return;
        }
        ListSelectionListener[] listeners = getJList().getListSelectionListeners();
        if (listeners != null) {
            for (ListSelectionListener l : listeners) {
                getJList().removeListSelectionListener(l);
            }
        }

    }
    
    @Override
    public void initBoundObjectDefaults() {
        ((JListModelBinder)documentListBinder).setDefaultComponentModel();
    }
/*    @Override
    protected void addBoundObjectListeners() {
        getJList().addListSelectionListener(getBoundObject());
    }

    @Override
    protected void removeBoundObjectListeners() {
        if (getJList() == null) {
            return;
        }
        ListSelectionListener[] listeners = getJList().getListSelectionListeners();
        if (listeners != null) {
            for (ListSelectionListener l : listeners) {
                getJList().removeListSelectionListener(l);
            }
        }
    }
*/
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
        if (getJList().getModel() != null && (getJList().getModel() instanceof BdCompositeJListBinder.ListBoxModelImpl)) {
            ((BdCompositeJListBinder.ListBoxModelImpl) getJList().getModel()).setProperties(displayProperties);
        }
        getJList().repaint();
    }

    @Override
    protected PropertyBinder createSelectedBinder() {
        selectedBinder = new BdCompositeJListBinder.JListSelectionBinder(this);
        return selectedBinder;
    }

    @Override
    protected PropertyBinder createListModelBinder() {
        documentListBinder = new BdCompositeJListBinder.JListModelBinder(this, displayProperties);
        return documentListBinder;
    }

    @Override
    protected PropertyBinder createDocumentChangeEventBinder() {
        documentChangeEventBinder = new BdCompositeJListBinder.JListDocumentChangeBinder(this);
        return documentChangeEventBinder;
    }

    @Override
    public boolean add(Binder binder) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static class JListDocumentChangeBinder extends AbstractListDocumentChangeBinder {

        public JListDocumentChangeBinder(BdCompositeJListBinder component) {
            super(component);
        }

/*        @Override
        public BdCompositeJListBinder getBoundObject() {
            return (BdCompositeJListBinder) boundObject;
        }
*/
        @Override
        protected void notifyComponentOf(DocumentChangeEvent event) {
            if (event == null) {
                return;
            }
            JList ls = (JList) ((BdCompositeJListBinder) boundObject).getBoundObject();
            BdCompositeJListBinder.ListBoxModelImpl model = (BdCompositeJListBinder.ListBoxModelImpl) ls.getModel();
            String[] props = model.getProperties();
            if (Arrays.asList(props).contains(event.getPropertyName())) {
                ls.repaint();
            }
        }
    }

    public static class JListSelectionBinder<E extends Document> extends AbstractListSelectionBinder {//implements ListSelectionListener {

        public JListSelectionBinder(BdCompositeJListBinder component) {
            super(component);
        }

        @Override
        public BdCompositeJListBinder getBoundObject() {
            return (BdCompositeJListBinder) boundObject;
        }

        protected JList getJList() {
            if (getBoundObject() == null) {
                return null;
            }
            return getBoundObject().getBoundObject();
        }

        @Override
        public void boundObjectChanged(Object o) {
            super.boundObjectChanged(o);
        }

        @Override
        protected int getComponentSelectedIndex() {
            return getJList().getSelectedIndex();
        }

        @Override
        protected void setComponentSelectedIndex(int selectedIndex) {
            getJList().setSelectedIndex(selectedIndex);
        }

    }//class JListSelectionBinder

    public static class JListModelBinder extends AbstractListModelBinder {

        protected String[] properties;

        public JListModelBinder(BdCompositeJListBinder component, String... properties) {
            super(component);
            this.properties = properties;
        }
        protected JList getJList() {
            return ((BdCompositeJListBinder) boundObject).getBoundObject();
        }

        @Override
        protected void setDefaultComponentModel() {
            DefaultListModel m = new DefaultListModel();
            m.clear();
            getJList().setModel(m);
        }

        @Override
        protected Object createComponentModel() {
            return new BdCompositeJListBinder.ListBoxModelImpl(properties, getDocuments());
        }

        @Override
        protected Object getModel() {
            return getJList().getModel();
        }

        @Override
        protected void setModel(Object model) {
            getJList().clearSelection();
            getJList().setModel((ListModel) model);
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
            BdCompositeJListBinder.ListBoxModelImpl model = (BdCompositeJListBinder.ListBoxModelImpl) getJList().getModel();

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
            BdCompositeJListBinder.ListBoxModelImpl model = (BdCompositeJListBinder.ListBoxModelImpl) getJList().getModel();
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
