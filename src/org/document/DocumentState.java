/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.util.Map;

/**
 *
 * @author Valery
 */
public interface DocumentState {
    boolean isEditing();
    void setEditing(boolean editing);
    Map<String,Object> getDirtyValues();
    //Object getEditingObject();
    //void setEditingObject(Object obj);
    //Document getCurrent();
//    void setCurrentObject(Object obj);
    
    
}
