/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

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
     * Test of dataChanged method, of class AbstractBinder.
     */
    @Test
    public void testDataChanged() {
        System.out.println("dataChanged");
        Object oldValue = null;
        Object newValue = null;
        //
        // oldValue == null && newValue == null
        //
        AbstractBinder instance = new AbstractBinderImpl();
//        BindingManager bindingManager = new MockBindingManager();
//        instance.setBindingManager(bindingManager);
        
        instance.dataChanged(oldValue, newValue);
        assertNull(instance.getComponentValue());
        //
        // oldValue == null && newValue != null
        //
        newValue = "Bill";
        instance.dataChanged(oldValue, newValue);
        assertEquals("Bill",instance.getComponentValue());
        //
        // oldValue != null && newValue == null
        //
        newValue = null;
        oldValue = "Bill";
        instance.dataChanged(oldValue, newValue);
        assertNull(instance.getComponentValue());
        //
        // oldValue != null && newValue != null
        //
        newValue = "Tom";
        oldValue = "Bill";
        instance.dataChanged(oldValue, newValue);
        assertEquals("Tom",instance.getComponentValue());
    }

    /**
     * Test of setBindingManager method, of class AbstractBinder.
     */
    @Test
    public void testBindingManager() {
        System.out.println("setBindingManager");
        BindingManager bindingManager = new MockBindingManager();
        AbstractBinder instance = new AbstractBinderImpl();
        instance.setBindingManager(bindingManager);
        assertTrue(bindingManager == instance.bindingManager);
    }

    /**
     * Test of componentChanged method, of class AbstractBinder.
     */
    @Test
    public void testComponentChanged() {
        System.out.println("componentChanged");
        Object oldValue = null;
        Object newValue = null;
        //
        // oldValue == null && newValue == null
        //
        AbstractBinder instance = new AbstractBinderImpl();
        BindingManager bindingManager = new MockBindingManager();
        instance.setBindingManager(bindingManager);
        
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
     * Test of protected setComponentValue method, of class AbstractBinder.
     */
    @Test
    public void testSetComponentValue() {
        System.out.println("setComponentValue");
        Object compValue = "Bill";
        AbstractBinder instance = new AbstractBinderImpl();
        instance.setComponentValue(compValue);
        assertEquals("Bill",instance.getComponentValue());
    }

    /**
     * Test of protected getComponentValue method, of class AbstractBinder.
     */
    @Test
    public void testGetComponentValue() {
        System.out.println("getComponentValue");
        Object compValue = "Bill";
        AbstractBinder instance = new AbstractBinderImpl();
        instance.setComponentValue(compValue);
        assertEquals("Bill",instance.getComponentValue());
    }
    
    /**
     * Test of protected setDataValue method, of class AbstractBinder.
     */
    @Test
    public void testSetDataValue() {
        System.out.println("setDataValue");
        Object dataValue = "Bill";
        AbstractBinder instance = new AbstractBinderImpl();
        BindingManager bindingManager = new MockBindingManager();
        instance.setBindingManager(bindingManager);
        instance.setDataValue(dataValue);
        assertEquals("Bill",instance.getDataValue());
        
    }
    
    /**
     * Test of protected getDataValue method, of class AbstractBinder.
     */
    @Test
    public void testGetDataValue() {
        System.out.println("getDataValue");
        Object dataValue = "Bill";
        AbstractBinder instance = new AbstractBinderImpl();
        BindingManager bindingManager = new MockBindingManager();
        instance.setBindingManager(bindingManager);
        instance.setDataValue(dataValue);
        assertEquals("Bill",instance.getDataValue());
        
    }
    
    /**
     * Test of componentValueOf method, of class AbstractBinder.
     */
    @Test
    public void testComponentValueOf() {
        System.out.println("componentValueOf");
        Object dataValue = null;
        AbstractBinder instance = new AbstractBinderImpl();
        Object expResult = null;
        Object result = instance.componentValueOf(dataValue);
        assertEquals(expResult, result);
        dataValue = "Bill";
        expResult = "Bill";
        result = instance.componentValueOf(dataValue);
        assertEquals(expResult, result);
                
    }

    /**
     * Test of dataValueOf method, of class AbstractBinder.
     */
    @Test
    public void testDataValueOf() {
        System.out.println("dataValueOf");
        Object compValue = null;
        AbstractBinder instance = new AbstractBinderImpl();
        Object expResult = null;
        Object result = instance.dataValueOf(compValue);
        assertEquals(expResult, result);
        compValue = "Bill";
        expResult = "Bill";
        result = instance.dataValueOf(compValue);
        assertEquals(expResult, result);
        
    }

    public class AbstractBinderImpl extends AbstractBinder {
        
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
        public Object dataValueOf(Object compValue) {
            return compValue;
        }

        @Override
        public Object getComponentValue() {
            return this.componentValue;
        }

    }
}
