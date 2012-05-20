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
    void setEditing(boolean editing);
    Map<String,Object> getDirtyValues();
}
