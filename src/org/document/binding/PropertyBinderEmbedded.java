/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.document.Document;

/**
 *
 * @author Valery
 */
public class PropertyBinderEmbedded extends AbstractEditablePropertyBinder {
    
    private PropertyChangeHandler embeddedPropertyHandler;
    
    public PropertyBinderEmbedded(String propertyName, DocumentBinder boundObject) {
        super(boundObject);
        boundProperty = propertyName;
        embeddedPropertyHandler = new PropertyChangeHandler();
        boundObject.getDefaultDocument().propertyStore().addPropertyChangeListener(embeddedPropertyHandler);
    }
    
    @Override
    protected Object getBoundObjectValue() {
        return ((DocumentBinder)boundObject).getDocument(); 
    }

    @Override
    protected void setBoundObjectValue(Object componentValue) {
        ((DocumentBinder)boundObject).setDocument((Document)componentValue);
    }
    /**
     * 
     * @param propertyValue the value
     * @return 
     */
    @Override
    protected Object componentValueOf(Object propertyValue) {
        ((DocumentBinder)boundObject).setDocument((Document)propertyValue);
        return boundObject;
    }

    @Override
    protected Object propertyValueOf(Object componentValue) {
        Document d = getContext().getSelected();
        if ( d == null ) {
            return null;
        }
        Document ed = null;
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
    }
    
    private Document getInstance() {
        DocumentBinder b = (DocumentBinder)boundObject;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeBoundObjectListeners() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public class PropertyChangeHandler implements PropertyChangeListener {

        public PropertyChangeHandler() {
        }

        /**
         * Handles events of type
         * <code>DocumentChangeEvent</code>. When any property value of a
         * document changes the method is called as an implementation of the {@link org.document.DocumentChangeListener
         * }. listener The special case is when an editing state changes. The
         * event has it's property
         * {@link org.document.DocumentChangeEvent#propertyName} equals to
         * <code>"document.state.editing"</code>. In this case the method
         * cancels <i>newMark<i> of a selected document if the last is marked as
         * <i>new</i>..
         *
         * @param event the event to be handled
         * @see org.document.DocumentChangeEvent
         */
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            //registry.notify(e);
        }
    }//class PropertyChangeHandler
    
}//class
