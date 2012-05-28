package org.document.swing.binders;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import org.document.Document;
import org.document.PropertyStore;

/**
 *
 * @author V. Shyshkin
 */
public class DocumentListCellRenderer extends JPanel implements ListCellRenderer<Object> {

    private String[] properties;
    private Color listBackground;

    public DocumentListCellRenderer(String... properties) {
        this.properties = properties;
        setOpaque(false);
        setLayout(new java.awt.GridLayout());
        listBackground = Color.WHITE;
        for (int i = 0; i < properties.length; i++) {
            JLabel lb = new JLabel();
            lb.setOpaque(false);
            this.add(lb);
        }
    }

    @Override
    @SuppressWarnings("empty-statement")
    public Component getListCellRendererComponent(JList<?> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        if (value == null) {
            return this;
        }
        PropertyStore ps = ((Document) value).propertyStore();

        for (int i = 0; i < getComponents().length; i++) {
            if (!(getComponents()[i] instanceof JLabel)) {
                continue;
            }
            JLabel lb = (JLabel) getComponents()[i];
            lb.setOpaque(false);

            Object o = ps.get(properties[i]);
            String s = o == null ? "" : o.toString();

            lb.setText(s);
        }
        Color background;
        Color foreground;

        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation
                != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {

            background = Color.BLUE;
            foreground = Color.WHITE;

            // check if this cell is selected
        } else if (isSelected) {
            foreground = list.getSelectionForeground();//Color.WHITE;
            background = list.getSelectionBackground();
        } else {
            background = getListBackground();
            foreground = list.getForeground();
        }
        this.setOpaque(true);
        this.setBackground(background);
        this.setForeground(foreground);
        return this;
    }

    public String[] getProperties() {
        return properties;
    }

    public void setProperties(String[] properties) {
        this.properties = properties;
    }

    public Color getListBackground() {
        return listBackground;
    }

    public void setListBackground(Color listBackground) {
        this.listBackground = listBackground;
    }
}
