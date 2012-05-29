/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.swing.binders;

import javax.swing.JLabel;

/**
 *
 * @author Valery
 */
public class JLabelErrorBinder extends JComponentErrorBinder{
    public JLabelErrorBinder(JLabel label) {
        super(label);
    }

    @Override
    protected void setMessage(String message) {
        ((JLabel)getComponent()).setText(message);
    }
}
