package org.document.binding;

import org.document.Document;
import org.document.DocumentState;
import org.document.Documents;
import org.document.HasState;
import org.document.ValidationException;

/**
 *
 * @author V. Shyshkin
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
        return (EmbeddedDocumentBinder) boundObject;
    }

    @Override
    protected Object getBoundObjectValue() {
        //return ((DocumentBinder)boundObject).getDocument(); 
        //return getEmbeddedDocumentBinder().getValue();
        return getEmbeddedDocumentBinder().getDocument();
    }

    @Override
    protected void setBoundObjectValue(Object componentValue) {
        getEmbeddedDocumentBinder().setDocument((Document) componentValue);
        //getEmbeddedDocumentBinder().setValue((EmbeddedDocumentBinder.Value) componentValue);
    }
    @Override
    protected void completeChanges(BinderEvent e) {
        boundObjectChanged(false, getBoundObjectValue());
    }

    /**
     *
     * @param propertyValue the value
     * @return
     */
    @Override
    public Object componentValueOf(Object propertyValue) {
        Object result;
        if ( propertyValue == null ) {
            result = getInstance();
        } else {
            result = Documents.create(propertyValue);            
        }
        return result;
    }
/*    protected void propertyChanged(String property, Object propertyValue, boolean forceRefresh) {
        super.propertyChanged(property, propertyValue, forceRefresh);
    }
*/ 
    @Override
    public Object propertyValueOf(Object componentValue) {
        Document d = getContext().getSelected(); // parent document
        if (d == null || componentValue == null) {
            return null;
        }
        return ((Document)componentValue).propertyStore().getOwner();
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
        if (b.getClassName() != null) {
            try {
                Class c = Class.forName(b.getClassName());
                result = Documents.create(c.newInstance());
            } catch (Exception e) {
                System.out.println("Cannot create default document instance. " + e.getMessage());
            }
        }
        return result;
    }

    @Override
    public void initBoundObjectDefaults() {
        DocumentBinder b = (DocumentBinder) boundObject;
        if (b.getClassName() != null) {
            try {
                Class c = Class.forName(b.getClassName());
                b.setDocument((Document) c.newInstance());
            } catch (Exception e) {
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
            if (getDocument() == null) {
                return;
            }
            switch (event.getAction()) {
                case boundObjectChange:
                    //Document d = (Document) getDocument().propertyStore().getValue(boundProperty);
                    Document d = (Document)getBoundObjectValue();
                    if (d == null) {
                        d = getInstance();
                        d.propertyStore().putValue(event.getBoundProperty(), event.getNewValue());
                        /*if (event.getSource() instanceof BinderConverter) {
                            updateInstance(d, event);
                        }*/
                    }
                    boundObjectChanged(d);
                    break;
            }
        }

        private void updateInstance(Document d, BinderEvent e) {
            try {

                if (d.propertyStore() instanceof HasState) {
                    DocumentState state = ((HasState) d.propertyStore()).getDocumentState();
                    state.getDirtyValues().put(boundProperty, e.getComponentValue());
                }

                fireClearPropertyError();

                Object convertedValue;
                Object oldDataValue = null;
                convertedValue = ((BinderConverter) e.getSource()).propertyValueOf(e.getComponentValue());
                fireClearPropertyError();
                //binderIsStillChangingProperty = false;
                //updateComponentView(convertedValue);
            } catch (ValidationException ex) {
                firePropertyError(ex);
            } catch (Exception ex) {
                ValidationException ve = new ValidationException(boundProperty, "Property name= '" + e.getBoundProperty() + "'. Invalid value: " + e.getComponentValue(), getDocument());
                firePropertyError(ve);
            }

        }
    }//class BoundObjectEventHandler
}//class
