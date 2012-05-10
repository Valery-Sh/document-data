/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.util.Map;
import org.document.binding.BinderListener;

/**
 *
 * @author Valery
 */
public interface DocumentState extends BinderListener{
    boolean isEditing();
    //boolean isAttached();
    //void setAttached(boolean attached);
    void setEditing(boolean editing);
    Map<String,Object> getDirtyValues();
    Map<String,DocumentChangeEvent> getPropertyErrors();
    //void addListChangeListener(ListChangeListener l);
    //void removeListChangeListener(ListChangeListener l);
}
