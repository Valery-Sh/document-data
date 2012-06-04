package org.document.binding;

import org.document.Document;
import org.document.DocumentChangeEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Valery
 */
public class AbstractPropertyBinderTest {
    
    public AbstractPropertyBinderTest() {
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
     * Test of getConverter method, of class AbstractPropertyBinder.
     */
    @Test
    public void testGetConverter() {
        System.out.println("getConverter");
        AbstractPropertyBinder instance = new AbstractPropertyBinderImpl();
        instance.setConverter(new BinderConverter() {

            @Override
            public Object propertyValueOf(Object componentValue) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Object componentValueOf(Object propertyValue) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        BinderConverter result = instance.getConverter();
        assertNotNull(result);
    }

    /**
     * Test of setConverter method, of class AbstractPropertyBinder.
     */
    @Test
    public void testSetConverter() {
        System.out.println("setConverter");
        BinderConverter converter = null;
        AbstractPropertyBinder instance = new AbstractPropertyBinderImpl();
        instance.setConverter(new BinderConverter() {

            @Override
            public Object propertyValueOf(Object componentValue) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Object componentValueOf(Object propertyValue) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        assertNotNull(instance.getConverter());
    }

    /**
     * Test of getDocument method, of class AbstractPropertyBinder.
     */
    @Test
    public void testGetDocument() {
        System.out.println("getDocument");
        AbstractPropertyBinder instance = new AbstractPropertyBinderImpl();
        Document expResult = null;
        Document result = instance.getDocument();
        assertEquals(expResult, result);
        Document doc = new DocumentImpl();
        instance.react(createDocumentEvent(doc));
        result = instance.getDocument();
        assertEquals(doc,result);
    }
    DocumentChangeEvent createDocumentEvent(Document doc) {
        DocumentChangeEvent e  = new DocumentChangeEvent(this,DocumentChangeEvent.Action.documentChange);
        e.setNewValue(doc);
        return e;
    }
    /**
     * Test of addBinderListener method, of class AbstractPropertyBinder.
     */
    @Test
    public void testAddBinderListener() {
        System.out.println("addBinderListener");
        BinderListener l = null;
        AbstractPropertyBinder instance = new AbstractPropertyBinderImpl();
        instance.addBinderListener(l);
        assertEquals(instance.binderListeners.size(), 1);
    }

    /**
     * Test of removeBinderListener method, of class AbstractPropertyBinder.
     */
    @Test
    public void testRemoveBinderListener() {
        System.out.println("removeBinderListener");
        BinderListener l = null;
        AbstractPropertyBinder instance = new AbstractPropertyBinderImpl();
        instance.addBinderListener(l);
        instance.removeBinderListener(l);
        assertEquals(instance.binderListeners.size(), 0);
    }

    /**
     * Test of react method, of class AbstractPropertyBinder.
     */
    @Test
    public void testReact() {
        System.out.println("react");
        DocumentChangeEvent event = null;
        AbstractPropertyBinder instance = new AbstractPropertyBinderImpl();
        try {
            instance.react(event);
            fail(" 'event' parameter cannot be null");
        } catch(Exception e) {
            System.out.println("Passed when event is null");
        }
        
        event = this.createDocumentEvent(new DocumentImpl());
        instance.react(event);
        assertNotNull(instance.getDocument());
        event.setNewValue(null);
        instance.react(event);
        assertNull(instance.getDocument());
        Document doc = new DocumentImpl();
        doc.propertyStore().put("testName", "TEST_STRING");
        event.setAction(DocumentChangeEvent.Action.propertyChange);
        event.setNewValue("TEST_STRING");
        event.setOldValue(null);
        instance.react(event);
        Object result = instance.getComponentValue();
        assertEquals("TEST_STRING", result);
        
        
        
    }

    /**
     * Test of getPropertyName method, of class AbstractPropertyBinder.
     */
    @Test
    public void testGetBoundProperty() {
        System.out.println("getBoundProperty");
        AbstractPropertyBinder instance = new AbstractPropertyBinderImpl();
        String expResult = "testName";
        String result = instance.getBoundProperty();
        assertEquals(expResult, result);
    }


    /**
     * Test of propertyChanged method, of class AbstractPropertyBinder.
     */
    @Test
    public void testPropertyChanged() {
        System.out.println("propertyChanged");
        Object propertyValue = "TEST_STRING";
        AbstractPropertyBinder instance = new AbstractPropertyBinderImpl();
        Object oldComponentValue = instance.getComponentValue();  
        instance.propertyChanged(propertyValue);
        assertFalse(instance.getComponentValue().equals(oldComponentValue));
    }

    /**
     * Test of getComponentValue method, of class AbstractPropertyBinder.
     */
    @Test
    public void testGetComponentValue() {
        System.out.println("getComponentValue");
        AbstractPropertyBinder instance = new AbstractPropertyBinderImpl();
        Object expResult = null;
        Object result = instance.getComponentValue();
        assertEquals(expResult, result);
        
        
        instance.setComponentValue("TEST_STRING");
        expResult = "TEST_STRING";
        result = instance.getComponentValue();
        assertEquals(expResult, result);        
    }

    /**
     * Test of setComponentValue method, of class AbstractPropertyBinder.
     */
    @Test
    public void testSetComponentValue() {
        System.out.println("setComponentValue");
        AbstractPropertyBinder instance = new AbstractPropertyBinderImpl();
        Object expResult;
        
        instance.setComponentValue("TEST_STRING");
        expResult = "TEST_STRING";
        Object result = instance.getComponentValue();
        assertEquals(expResult, result);        
    }

    
    public class AbstractPropertyBinderImpl extends AbstractPropertyBinder {
        private Object componentValue; 
        private Object propertyValue;
        
        @Override
        public String getBoundProperty() {
            return "testName";
        }
        @Override
        public Object getComponentValue() {
            return this.componentValue;
        }

        @Override
        public void setComponentValue(Object componentValue) {
            this.componentValue = componentValue;
        }

        @Override
        public Object componentValueOf(Object propertyValue) {
            return propertyValue == null ? null : propertyValue.toString();
        }

        @Override
        public Object propertyValueOf(Object componentValue) {
            return componentValue == null ? null : componentValue;
        }

        @Override
        public void initComponentDefault() {
            
        }
    }

}
