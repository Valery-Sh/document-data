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
//    private Value value;
    public EmbeddedDocumentBinder(Class clazz) {
        super(clazz);
        boundObjectListeners = new ArrayList<BinderListener>();
        getContext().setEmbedded(true);
    }

  /*  public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
*/
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
/*    public static class Value {
        private Document document;
        private EmbeddedDocumentBinder binder;
        public Value(EmbeddedDocumentBinder binder,Document document) {
            this.binder = binder;
            this.document = document;
        }
        public Document getDocument() {
            return document;
        }

        public void setDocument(Document document) {
            this.document = document;
            binder.setDocument(document);
        }
        
    }
    */ 
}//class
