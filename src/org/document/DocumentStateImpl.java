/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public class DocumentStateImpl implements DocumentState {
    
    private boolean editing;

    @Override
    public boolean isEditing() {
        return editing;
    }
    @Override
    public void setEditing(boolean editing) {
        this.editing = editing;
    }
}
