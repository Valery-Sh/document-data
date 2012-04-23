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
public class DefaultBinderRegistryTest {
    
    public DefaultBinderRegistryTest() {
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
     * Test of add method, of class DefaultBinderRegistry.
     */
    @Test
    public void testAdd() {
        System.out.println("DefaultBinderRegistry.add(Binder)");
        
        Object component = new MockComponent();
        
        Binder binder = MockBinder.create("firstName", component);
        DefaultBinderRegistry instance = new DefaultBinderRegistry();
        instance.add(binder);
        assertEquals(1,instance.binders.size());
        assertTrue(instance.binders.containsKey("firstName"));
        Object result = instance.getBinders("firstName").get(0);
        assertTrue(result == binder);
        //
        // Add new binder for the same property name
        //
        Object component1 = new MockComponent();
        
        Binder binder1 = MockBinder.create("firstName", component1);
        instance.add(binder1);
        assertEquals(1,instance.binders.size());
        result = instance.getBinders("firstName").get(1);
        assertTrue(result == binder1);
        //
        // Add new binder
        //
        Object component2 = new MockComponent();
        
        Binder binder2 = MockBinder.create("lastName", component2);
        instance.add(binder2);
        assertEquals(2,instance.binders.size());
        result = instance.getBinders("lastName").get(0);
        assertTrue(result == binder2);
        //
        // Try to add null binder
        //
        try {
            instance.add(null);
            fail("Try 'add' with null parameter");            
        } catch(NullPointerException e) {
            System.out.println("Exception when try 'add' with null parameter");
        }
        
        
        
    }

    /**
     * Test of remove method, of class DefaultBinderRegistry.
     */
    @Test
    public void testRemove() {
        System.out.println("DefaultBinderRegistry.remove(Binder)");
        //
        // Add two binders 
        //
        Object component = new MockComponent();
        
        Binder binder = MockBinder.create("firstName", component);
        DefaultBinderRegistry instance = new DefaultBinderRegistry();
        instance.add(binder);
        Object component1 = new MockComponent();
        Binder binder1 = MockBinder.create("firstName", component1);
        instance.add(binder1);
        //
        // remove one of two binders 
        //
        instance.remove(binder);
        Object result = instance.getBinders("firstName").get(0);        
        assertEquals(result,binder1);
        //
        // remove second (last) binder 
        //
        instance.remove(binder1);
        assertNull(instance.getBinders("firstName"));
        
        //
        // Try 'remove' with null parameter
        //
        try {
            instance.remove(null);
            fail("Try 'remove' with null parameter");            
        } catch(NullPointerException e) {
            System.out.println("Exception when try 'remove' with null parameter");
        }
    }

    /**
     * Test of firePropertyChange method, of class DefaultBinderRegistry.
     */
    @Test
    public void testFirePropertyChange() {
        System.out.println("DefaultBinderRegistry.firePropertyChange");
        //
        // Add two binders with the same property name 
        //
        Object component = new MockComponent();
        
        MockBinder binder = (MockBinder)MockBinder.create("firstName", component);
        DefaultBinderRegistry instance = new DefaultBinderRegistry();
        instance.add(binder);
        Object component1 = new MockComponent();
        MockBinder binder1 = (MockBinder)MockBinder.create("firstName", component1);
        instance.add(binder1);
        //
        // Add else one binder with another property name
        //
        Object component2 = new MockComponent();
        MockBinder binder2 = (MockBinder)MockBinder.create("lastName", component2);
        instance.add(binder2);
        
        instance.firePropertyChange("firstName", 1, 1);
        assertTrue(binder.dataChanged);
        assertTrue(binder1.dataChanged);
        assertFalse(binder2.dataChanged);
        instance.firePropertyChange("lastName", 1, 1);        
        assertTrue(binder2.dataChanged);
        
        
    }

    /**
     * Test of getDocument method, of class DefaultBinderRegistry.
     */
    @Test
    public void testGetDocument() {
        System.out.println("DefaultBinderRegistry.getDocument()");
        DefaultBinderRegistry instance = new DefaultBinderRegistry();
        Document doc;
        Document result = instance.getDocument();
        assertNull(result);
        doc = new DocumentTest.DocumentImpl();
        instance.setDocument(doc);
        result = instance.getDocument();
        assertTrue( doc == result);
    }

    /**
     * Test of setDocument method, of class DefaultBinderRegistry.
     */
    @Test
    public void testSetDocument() {
        System.out.println("DefaultBinderRegistry.setDocument(Document)");
        DefaultBinderRegistry instance = new DefaultBinderRegistry();
        DocumentImpl doc = new DocumentImpl();
        instance.setDocument(doc);        
        Document result = instance.getDocument();
        assertTrue( doc == result);
        assertNotNull(doc.handler);        
        //
        // null parameter value
        //
        instance.setDocument(null);        
        result = instance.getDocument();
        assertNull( result);
        assertNull(doc.handler);
        //
        // change document
        //
        
    }
}
