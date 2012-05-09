/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import org.document.binding.BinderListener;
import java.util.Map;

/**
 *
 * @author Valery
 */
public interface DocumentState extends BinderListener, ListChangeListener{
    boolean isEditing();
    boolean isAttached();
    void setAttached(boolean attached);
    void setEditing(boolean editing);
    Map<String,Object> getDirtyValues();
    Map<String,DocumentChangeEvent> getPropertyErrors();
    void addListChangeListener(ListChangeListener l);
    void removeListChangeListener(ListChangeListener l);
}
