/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import org.document.binding.AbstractPropertyBinder;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Valery
 */
public class AbstractBinderTest {
    
    public AbstractBinderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of dataChanged method, of class AbstractPropertyBinder.
     */
    @Test
    public void testDataChanged() {
        System.out.println("dataChanged");
        Object oldValue = null;
        Object newValue = null;
        //
        // oldValue == null && newValue == null
        //
        AbstractPropertyBinder instance = new AbstractBinderImpl();
        
        instance.dataChanged(newValue);
        assertNull(instance.getComponentValue());
        //
        // oldValue == null && newValue != null
        //
        newValue = "Bill";
        instance.dataChanged(newValue);
        assertEquals("Bill",instance.getComponentValue());
        //
        // oldValue != null && newValue == null
        //
        newValue = null;
        oldValue = "Bill";
        instance.dataChanged(newValue);
        assertNull(instance.getComponentValue());
        //
        // oldValue != null && newValue != null
        //
        newValue = "Tom";
        oldValue = "Bill";
        instance.dataChanged(newValue);
        assertEquals("Tom",instance.getComponentValue());
    }

    /**
     * Test of setDocumentBinding method, of class AbstractPropertyBinder.
     */
    @Test
    public void testSetDocumentBinding() {
        System.out.println("setDocumentBinding");
        DocumentBindings binding = new MockDocumentBinding();
        AbstractPropertyBinder instance = new AbstractBinderImpl();
        instance..setDocumentBinding(binding);
        assertTrue(binding == instance.binding);
    }

    /**
     * Test of componentChanged method, of class AbstractPropertyBinder.
     */
    @Test
    public void testComponentChanged() {
        System.out.println("componentChanged");
        Object oldValue = null;
        Object newValue = null;
        //
        // oldValue == null && newValue == null
        //
        AbstractPropertyBinder instance = new AbstractBinderImpl();
        DocumentBindings binding = new MockDocumentBinding();
        instance.setDocumentBinding(binding);
        
        instance.componentChanged(oldValue, newValue);
        assertNull(instance.getDataValue());
        
        //
        // oldValue == null && newValue != null
        //
        newValue = "Bill";
        instance.componentChanged(oldValue, newValue);
        assertEquals("Bill",instance.getDataValue());
        //
        // oldValue != null && newValue == null
        //
        newValue = null;
        oldValue = "Bill";
        instance.componentChanged(oldValue, newValue);
        assertNull(instance.getDataValue());
        
        //
        // oldValue != null && newValue != null
        //
        newValue = "Tom";
        oldValue = "Bill";
        instance.componentChanged(oldValue, newValue);
        assertEquals("Tom",instance.getDataValue());

    }

    /**
     * Test of protected setComponentValue method, of class AbstractPropertyBinder.
     */
    @Test
    public void testSetComponentValue() {
        System.out.println("setComponentValue");
        Object compValue = "Bill";
        AbstractPropertyBinder instance = new AbstractBinderImpl();
        instance.setComponentValue(compValue);
        assertEquals("Bill",instance.getComponentValue());
    }

    /**
     * Test of protected getComponentValue method, of class AbstractPropertyBinder.
     */
    @Test
    public void testGetComponentValue() {
        System.out.println("getComponentValue");
        Object compValue = "Bill";
        AbstractPropertyBinder instance = new AbstractBinderImpl();
        instance.setComponentValue(compValue);
        assertEquals("Bill",instance.getComponentValue());
    }
    
    /**
     * Test of protected setDataValue method, of class AbstractPropertyBinder.
     */
    @Test
    public void testSetDataValue() {
        System.out.println("setDataValue");
        Object dataValue = "Bill";
        AbstractPropertyBinder instance = new AbstractBinderImpl();
        DocumentBindings binding = new MockDocumentBinding();
        instance.setDocumentBinding(binding);
        instance.setDataValue(dataValue);
        assertEquals("Bill",instance.getDataValue());
        
    }
    
    /**
     * Test of protected getDataValue method, of class AbstractPropertyBinder.
     */
    @Test
    public void testGetDataValue() {
        System.out.println("getDataValue");
        Object dataValue = "Bill";
        AbstractPropertyBinder instance = new AbstractBinderImpl();
        DocumentBindings binding = new MockDocumentBinding();
        instance.setDocumentBinding(binding);
        instance.setDataValue(dataValue);
        assertEquals("Bill",instance.getDataValue());
        
    }
    
    /**
     * Test of componentValueOf method, of class AbstractPropertyBinder.
     */
    @Test
    public void testComponentValueOf() {
        System.out.println("componentValueOf");
        Object dataValue = null;
        AbstractPropertyBinder instance = new AbstractBinderImpl();
        Object expResult = null;
        Object result = instance.componentValueOf(dataValue);
        assertEquals(expResult, result);
        dataValue = "Bill";
        expResult = "Bill";
        result = instance.componentValueOf(dataValue);
        assertEquals(expResult, result);
                
    }

    /**
     * Test of propertyValueOf method, of class AbstractPropertyBinder.
     */
    @Test
    public void testDataValueOf() {
        System.out.println("dataValueOf");
        Object compValue = null;
        AbstractPropertyBinder instance = new AbstractBinderImpl();
        Object expResult = null;
        Object result = instance.propertyValueOf(compValue);
        assertEquals(expResult, result);
        compValue = "Bill";
        expResult = "Bill";
        result = instance.propertyValueOf(compValue);
        assertEquals(expResult, result);
        
    }

    public class AbstractBinderImpl extends AbstractPropertyBinder {
        
        protected Object componentValue;
        protected Object dataValue;
            
        @Override
        public void setComponentValue(Object compValue) {
            this.componentValue = compValue;
        }


        @Override
        public Object componentValueOf(Object dataValue) {
            return dataValue;
        }

        @Override
        public Object propertyValueOf(Object compValue) {
            return compValue;
        }

        @Override
        public Object getComponentValue() {
            return this.componentValue;
        }

    }
}
