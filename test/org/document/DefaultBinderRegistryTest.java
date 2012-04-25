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
     * Test of completeChanges method, of class DefaultBinderRegistry.
     */
    @Test
    public void testCompleteChanges() {
        System.out.println("DefaultBinderRegistry.completeChanges");
        Object component = new MockComponent();
        MockDocument doc = new MockDocument();
        doc.put("firstName", "Bill");
        MockBinder binder = (MockBinder)MockBinder.create("firstName", component);
        DefaultBinderRegistry instance = new DefaultBinderRegistry();
        instance.add(binder);
        instance.setDocument(doc, true);
        //
        // Now firstName = "Bill". We set binder.componentValue to "Tom".
        // completeChanges() must set binder.dataValue to "Tom".
        //
        binder.componentValue = "Tom";
        instance.completeChanges();
        assertEquals("Tom",binder.getDataValue());
        //
        //
        //
        
    }
    /**
     * More complex test of completeChanges method, of class DefaultBinderRegistry.
     */
    @Test
    public void testCompleteChanges_1() {
        System.out.println("DefaultBinderRegistry.completeChanges_1");
        DocumentImpl doc = new DocumentImpl();
        doc.put("firstName", "Bill");
        doc.put("height", 175);
        
        Binder binder = new StringBinderImpl("firstName");
        DefaultBinderRegistry instance = new DefaultBinderRegistry();
        instance.add(binder);
        instance.setDocument(doc, true);
        //
        // Now firstName = "Bill". We set binder.componentValue to "Tom".
        // completeChanges() must set doc.firstName to "Tom".
        //
        ((StringBinderImpl)binder).componentValue = "Tom";
        instance.completeChanges();
        assertEquals("Tom",doc.get("firstName"));
        //
        // Add another binder
        //
        Binder intBinder = new IntegerBinderImpl("height");
        instance.add(intBinder);
        doc.put("height",175);
        ((IntegerBinderImpl)intBinder).componentValue = 190;
        assertEquals(175,doc.get("height"));        
        instance.completeChanges();
        assertEquals(190,doc.get("height"));
        //
        // Try embedded document
        //
        DocumentImpl addrDoc = new DocumentImpl();
        addrDoc.put("country", "UK");
        addrDoc.put("zipCode", 99955);
        doc.put("address", addrDoc);
        
        BinderRegistry addrInstance = instance.createChild("address");        
        Binder countryBinder = new StringBinderImpl("country");
        Binder zipCodeBinder = new IntegerBinderImpl("zipCode");        
        addrInstance.add(countryBinder);
        addrInstance.add(zipCodeBinder);      
        
        ((IntegerBinderImpl)zipCodeBinder).componentValue = "99944";
        instance.completeChanges();
        assertEquals(99944,addrDoc.get("zipCode"));
        //
        // Now set zipCode.componentValue = "A99966" => an error
        // Data value remains unchanged (99944)
        //
        ((IntegerBinderImpl)zipCodeBinder).componentValue = "A99966";
        instance.completeChanges();
        assertEquals(99944,addrDoc.get("zipCode"));
        
        
        
        
        
        
        
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
        instance.setDocument(doc,true);
        result = instance.getDocument();
        assertTrue( doc == result);
    }

    /**
     * Test of setDocument method, of class DefaultBinderRegistry.
     */
    @Test
    public void testSetDocument() {
        System.out.println("DefaultBinderRegistry.setDocument(Document,boolean)");
        DefaultBinderRegistry instance = new DefaultBinderRegistry();
        DocumentImpl doc = new DocumentImpl();
        instance.setDocument(doc,true);        
        Document result = instance.getDocument();
        assertTrue( doc == result);
        assertNotNull(doc.handler);        
        //
        // null parameter value
        //
        instance.setDocument(null,true);        
        result = instance.getDocument();
        assertNull( result);
        assertNull(doc.handler);
        //
        // change document
        //
        Object component = new MockComponent();
        Document doc1 = new DocumentImpl();
        doc1.put("firstName", "Jone");
        MockBinder binder = (MockBinder)MockBinder.create("firstName", component);
        
        instance.add(binder);
        instance.setDocument(doc, true);
        //
        // Now firstName = "Bill". We set binder.componentValue to "Tom".
        // completeChanges() must set binder.dataValue to "Tom".
        //
        binder.componentValue = "Tom";
        instance.completeChanges();
        assertEquals("Tom",binder.getDataValue());
        
    }
}
