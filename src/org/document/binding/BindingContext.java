package org.document.binding;

import org.document.Document;

/**
 *
 * @author Valery
 */
public interface BindingContext {
    boolean isEmbedded();
    void setEmbedded(boolean embedded);
    Document getSelected();
}
