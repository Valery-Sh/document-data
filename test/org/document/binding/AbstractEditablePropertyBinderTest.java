/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import org.document.DocumentChangeEvent;
import org.document.ValidationException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Valery
 */
public class AbstractEditablePropertyBinderTest {
    
    public AbstractEditablePropertyBinderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of componentChanged method, of class AbstractEditablePropertyBinder.
     */
    @Test
    public void testComponentChanged_boolean_Object() {
        System.out.println("componentChanged(boolean,Object");

        Object componentValue = null;
        ComponentImpl component = new ComponentImpl();
        DocumentImpl doc = new DocumentImpl();
        //
        // Test when propertyValueOf can convert a string value to an integer value
        //
        AbstractEditablePropertyBinder instance = new PropertyBinderImpl("value",component);
        instance.document = doc;
        instance.componentChanged(true, componentValue);
        assertEquals(0,doc.propertyStore().get("value"));
        componentValue = "123";
        instance.componentChanged(true, componentValue);
        assertEquals(123,doc.propertyStore().get("value"));
        //
        // Test when propertyValueOf throws exception(Cannot convert a string 
        // value to an integer value).
        // So componentChanged() encounters an error and the 'doc' document
        // doesn't change
        //
        doc.propertyStore().put("value", 0);
        Object expResult = doc.propertyStore().get("value");
        componentValue ="A123";
        instance.componentChanged(true, componentValue);
        assertEquals(expResult,doc.propertyStore().get("value"));
        
    }

    /**
     * Test of react method, of class AbstractEditablePropertyBinder.
     */
    @Test
    public void testReact() {
        System.out.println("react(DocumentChangeEvent)");
        ComponentImpl component = new ComponentImpl();
        AbstractPropertyBinder binder = new PropertyBinderImpl("value", component);
        DocumentChangeEvent event = new DocumentChangeEvent(this);
        //
        // Test if the binder sets the new document
        //
        DocumentImpl doc = new DocumentImpl();
        event.setAction(DocumentChangeEvent.Action.documentChange);
        event.setNewValue(doc);
        binder.react(event);
        assertTrue(binder.getDocument() == doc);
        //
        // Test that the binder sets defalt component value
        // when the new document is null
        //
        event.setNewValue(null);
        binder.react(event);
        assertEquals("999",component.getValue());
    }


    /**
     * Test of propertyChanged method, of class AbstractEditablePropertyBinder.
     */
    @Test
    public void testPropertyChanged() {
        System.out.println("propertyChanged");
        Object propertyValue = null;
        ComponentImpl component = new ComponentImpl();
        component.setValue("999");
        AbstractPropertyBinder binder = new PropertyBinderImpl("value", component);
        binder.propertyChanged(propertyValue);
        assertNull(component.getValue());
        binder.propertyChanged(999);
        assertEquals(999,component.getValue());
        
    }


    public class PropertyBinderImpl extends AbstractEditablePropertyBinder {
        private ComponentImpl component;
        public PropertyBinderImpl(String propertyName,ComponentImpl component) {
            this.boundProperty = propertyName;
            this.component = component;
        }
        @Override
        public void addComponentListeners() {
        }

        @Override
        public void removeComponentListeners() {
        }

        @Override
        protected Object getComponentValue() {
            return component.getValue();
        }

        @Override
        protected void setComponentValue(Object componentValue) {
            component.setValue(componentValue);
        }

        @Override
        protected Object componentValueOf(Object propertyValue) {
            return propertyValue;
                    
        }

        @Override
        protected Object propertyValueOf(Object componentValue) {
            return componentValue == null ? 0 : Integer.parseInt(componentValue.toString());
        }

        @Override
        protected void initComponentDefault() {
            component.setValue("999");
        }
    }
}
