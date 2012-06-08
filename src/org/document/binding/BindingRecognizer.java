package org.document.binding;

import org.document.Document;

/**
 *
 * @author V. Shyshkin
 */
public interface BindingRecognizer {
    DocumentBinder getBinder(Document document);
    Object getAlias(Document document);
}
