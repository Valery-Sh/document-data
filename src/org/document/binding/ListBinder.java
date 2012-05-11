package org.document.binding;

import org.document.DocumentChangeListener;
import org.document.ListChangeListener;

/**
 *
 * @author V. Shyshkin
 */
public interface ListBinder extends Binder,DocumentChangeListener,ListChangeListener{
     void clearSelection();
}
