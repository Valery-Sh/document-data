package org.document.binding;

import org.document.Document;

/**
 *
 * @author V. Shyshkin
 */
public class EmbeddedDocumentBinder extends DocumentBinder {

    private Document defaultDocument;

    public EmbeddedDocumentBinder(Class clazz) {
        super(clazz);
        try {
            defaultDocument = (Document) clazz.newInstance();
        } catch (Exception e) {
        }
    }

    @Override
    public void react(BinderEvent event) {
        switch (event.getAction()) {
            case boundObjectChange:
                // try {
                if (getDocument() != null) {
                    getDocument().propertyStore().putValue(event.getBoundProperty(), event.getNewValue());
                } else {
                    
                }
        }
    }
}//class
