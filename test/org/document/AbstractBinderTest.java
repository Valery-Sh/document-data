/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import org.junit.*;
import static org.junit.Assert.*;

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
//        BinderRegistry registry = new MockBinderRegistry();
//        instance.setRegistry(registry);
        
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
     * Test of setRegistry method, of class AbstractBinder.
     */
    @Test
    public void testSetRegistry() {
        System.out.println("setRegistry");
        BinderRegistry registry = new MockBinderRegistry();
        AbstractBinder instance = new AbstractBinderImpl();
        instance.setRegistry(registry);
        assertTrue(registry == instance.registry);
    }

    /**
     * Test of componentChanged method, of class AbstractBinder.
     */
    @Test
    public void testComponentChanged() {
        System.out.println("componentChanged");
        Object oldValue = null;
        Object newValue = null;
        AbstractBinder instance = new AbstractBinderImpl();
//        instance.componentChanged(oldValue, newValue);
    }

    /**
     * Test of setComponentValue method, of class AbstractBinder.
     */
    @Test
    public void testSetCompValue() {
        System.out.println("setCompValue");
        Object compValue = null;
        AbstractBinder instance = new AbstractBinderImpl();
        instance.setComponentValue(compValue);
    }

    /**
     * Test of setDataValue method, of class AbstractBinder.
     */
    @Test
    public void testSetDataValue() {
        System.out.println("setDataValue");
        Object dataValue = null;
        AbstractBinder instance = new AbstractBinderImpl();
        BinderRegistry registry = new MockBinderRegistry();
        instance.setRegistry(registry);
        instance.setDataValue(dataValue);
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
