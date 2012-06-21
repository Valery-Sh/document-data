/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import org.document.Document;

/**
 *
 * @author Valery
 */
public class PropertyBinderEmbedded extends AbstractEditablePropertyBinder {
    
    private BoundObjectEventHandler boundObjectHandler;
    
    public PropertyBinderEmbedded(String propertyName, EmbeddedDocumentBinder boundObject) {
        super(boundObject);
        boundProperty = propertyName;
        boundObjectHandler = new BoundObjectEventHandler();
        addBoundObjectListeners();
        
        //embeddedPropertyHandler = new PropertyChangeHandler();
        //boundObject.getDefaultDocument().propertyStore().addPropertyChangeListener(embeddedPropertyHandler);
    }
    
    protected EmbeddedDocumentBinder getEmbeddedDocumentBinder() {
        return (EmbeddedDocumentBinder)boundObject;
    }
    @Override
    protected Object getBoundObjectValue() {
        //return ((DocumentBinder)boundObject).getDocument(); 
        return getEmbeddedDocumentBinder().getDocument(); 
    }

    @Override
    protected void setBoundObjectValue(Object componentValue) {
        getEmbeddedDocumentBinder().setDocument((Document)componentValue); 
    }
    /**
     * 
     * @param propertyValue the value
     * @return 
     */
    @Override
    public Object componentValueOf(Object propertyValue) {
       return (Document)propertyValue;
    }

    @Override
    public Object propertyValueOf(Object componentValue) {
        Document d = getContext().getSelected(); // parent document
        if ( d == null ) {
            return null;
        }
        return componentValue;
/*        Document ed = null;
        if ( d.propertyStore().getValue(boundProperty) == null ) {
             ed = getInstance();
        }
        if ( ed == null ) {
            return null;
        }
        DocumentBinder b = (DocumentBinder)boundObject;        
        for ( Object o : b) {
            PropertyBinder pb = (PropertyBinder)o;
            
        }
        return ed;
        */ 
    }
    
    private Document getInstance() {
        DocumentBinder b = getEmbeddedDocumentBinder();
        Document result = null;
        if ( b.getClassName() != null ) {
            try {
                Class c = Class.forName(b.getClassName());
                result = (Document)c.newInstance();
            } catch(Exception e) {
                System.out.println("Cannot create default document instance. " + e.getMessage());
            }
        }
        return result;
    }
    @Override
    public void initBoundObjectDefaults() {
        DocumentBinder b = (DocumentBinder)boundObject;
        if ( b.getClassName() != null ) {
            try {
                Class c = Class.forName(b.getClassName());
                b.setDocument((Document)c.newInstance());
            } catch(Exception e) {
                System.out.println("Cannot create default document instance. " + e.getMessage());
            }
        }
        //((DocumentBinder)boundObject).getClass();
    }

    @Override
    public void addBoundObjectListeners() {
        getEmbeddedDocumentBinder().addBoundObjectListener(boundObjectHandler);
    }

    @Override
    public void removeBoundObjectListeners() {
        getEmbeddedDocumentBinder().removeBoundObjectListener(boundObjectHandler);
    }
    public class BoundObjectEventHandler implements BinderListener {

        public BoundObjectEventHandler() {
        }

        @Override
        public void react(BinderEvent event) {
            if ( getDocument() == null ) {
                return;
            }
            switch(event.getAction()) {
                case boundObjectChange:
                    Document d = (Document)getDocument().propertyStore().getValue(boundProperty);
                    if ( d == null ) {
                        d = getInstance();
                    }
                    boundObjectChanged(d);
                    break;
            }
        }
    }//class BoundObjectEventHandler
    
/*    public static class BoundObject {
        
        private EmbeddedDocumentBinder documentBinder;
        
        private Document document;
        
        public BoundObject(EmbeddedDocumentBinder ebinder) {
            documentBinder = ebinder;
        }

        public Document getDocument() {
            return document;
        }

        public void setDocument(Document document) {
            this.document = document;
            documentBinder.setDocument(document);
        }

        public EmbeddedDocumentBinder getDocumentBinder() {
            return documentBinder;
        }

        public void setDocumentBinder(EmbeddedDocumentBinder documentBinder) {
            this.documentBinder = documentBinder;
        }
    }
    */ 
}//class
