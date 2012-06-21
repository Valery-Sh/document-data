package org.document.binding;

import java.util.ArrayList;
import java.util.List;
import org.document.Document;

/**
 *
 * @author V. Shyshkin
 */
public class EmbeddedDocumentBinder extends DocumentBinder {

    //private Document defaultDocument;
    private List<BinderListener> boundObjectListeners;
    private DocumentBinder parent;
    
    public EmbeddedDocumentBinder(Class clazz) {
        super(clazz);
        boundObjectListeners = new ArrayList<BinderListener>();
        getContext().setEmbedded(true);
/*        try {
            defaultDocument = (Document) clazz.newInstance();
        } catch (Exception e) {
        }
*/ 
    }

    public DocumentBinder getParent() {
        return parent;
    }

    public void setParent(DocumentBinder parent) {
        this.parent = parent;
    }

    @Override
    public void react(BinderEvent event) {
        super.react(event);
        
        switch (event.getAction()) {
            case boundObjectChange:
                for ( BinderListener l : boundObjectListeners) {
                    l.react(event);
                }
        }
    }
    
    public void addBoundObjectListener(BinderListener l) {
        boundObjectListeners.add(l);
    }
    public void removeBoundObjectListener(BinderListener l) {
        boundObjectListeners.remove(l);
    }
    
}//class
