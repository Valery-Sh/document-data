/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public class MockPropertyChangeHandler implements PropertyChangeHandler{
    protected boolean isFired;
    @Override
    public void firePropertyChange(Document doc, String propPath, Object oldValue, Object newValue) {
        isFired = true;
    }
    
}
