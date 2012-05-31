package org.document.swing.binders;

import javax.swing.JLabel;

/**
 *
 * @author V. Shyshkin
 */
public class BdLabelErrorBinder extends BdComponentErrorBinder{
    public BdLabelErrorBinder(JLabel label) {
        super(label);
    }

    @Override
    protected void setMessage(String message) {
        ((JLabel)getComponent()).setText(message);
    }
}
