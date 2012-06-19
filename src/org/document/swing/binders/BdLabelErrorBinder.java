package org.document.swing.binders;

import javax.swing.JLabel;

/**
 *
 * @author V. Shyshkin
 */
public class BdLabelErrorBinder extends BdComponentErrorBinder{
    public BdLabelErrorBinder(JLabel label) {
        super(label);
        label.setVisible(false);
        label.repaint();
    }

    @Override
    protected void setMessage(String message) {
        ((JLabel)getComponent()).setText(message);
    }
}
