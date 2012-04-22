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
        AbstractBinder instance = new AbstractBinderImpl();
        instance.dataChanged(oldValue, newValue);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setRegistry method, of class AbstractBinder.
     */
    @Test
    public void testSetRegistry() {
        System.out.println("setRegistry");
        BinderRegistry registry = null;
        AbstractBinder instance = new AbstractBinderImpl();
        instance.setRegistry(registry);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        instance.componentChanged(oldValue, newValue);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCompValue method, of class AbstractBinder.
     */
    @Test
    public void testSetCompValue() {
        System.out.println("setCompValue");
        Object compValue = null;
        AbstractBinder instance = new AbstractBinderImpl();
        instance.setCompValue(compValue);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setDataValue method, of class AbstractBinder.
     */
    @Test
    public void testSetDataValue() {
        System.out.println("setDataValue");
        Object dataValue = null;
        AbstractBinder instance = new AbstractBinderImpl();
        instance.setDataValue(dataValue);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class AbstractBinderImpl extends AbstractBinder {

            
        @Override
        public void setCompValue(Object compValue) {
        }

        @Override
        public void setDataValue(Object dataValue) {
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
        protected Object getComponentValue() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
