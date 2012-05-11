/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import org.document.binding.PropertyBinder;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Valery
 */
public class DefaultDocumentBindingTest {
    
    public DefaultDocumentBindingTest() {
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
     * Test of add method, of class DocumentBinding
     */
    @Test
    public void testAdd() {
        System.out.println("DefaultDocumentBinding.add(Binder)");
        
        Object component = new MockComponent();
        
        PropertyBinder binder = MockBinder.create("firstName", component);
        DocumentBindingHandler instance = new DocumentBindingHandler();
        instance.add(binder);
        assertEquals(1,instance.binders.size());
        assertTrue(instance.binders.containsKey("firstName"));
        Object result = instance.getBinders("firstName").get(0);
        assertTrue(result == binder);
        //
        // Add new binder for the same property name
        //
        Object component1 = new MockComponent();
        
        PropertyBinder binder1 = MockBinder.create("firstName", component1);
        instance.add(binder1);
        assertEquals(1,instance.binders.size());
        result = instance.getBinders("firstName").get(1);
        assertTrue(result == binder1);
        //
        // Add new binder
        //
        Object component2 = new MockComponent();
        
        PropertyBinder binder2 = MockBinder.create("lastName", component2);
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
     * Test of remove method, of class ObjectDocumentBinding.
     */
    @Test
    public void testRemove() {
        System.out.println("DefaultDocumentBinding.remove(Binder)");
        //
        // Add two binders 
        //
        Object component = new MockComponent();
        
        PropertyBinder binder = MockBinder.create("firstName", component);
        DocumentBindingHandler instance = new DocumentBindingHandler();
        instance.add(binder);
        Object component1 = new MockComponent();
        PropertyBinder binder1 = MockBinder.create("firstName", component1);
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
     * Test of completeChanges method, of class ObjectDocumentBinding.
     */
    @Test
    public void testCompleteChanges() {
        System.out.println("DefaultDocumentBinding.completeChanges");
        Object component = new MockComponent();
        MockDocument doc = new MockDocument();
        doc.put("firstName", "Bill");
        MockBinder binder = (MockBinder)MockBinder.create("firstName", component);
        DocumentBindingHandler instance = new DocumentBindingHandler();
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
     * More complex test of completeChanges method, of class ObjectDocumentBinding.
     */
    @Test
    public void testCompleteChanges_1() {
        System.out.println("DefaultDocumentBinding.completeChanges_1");
        DocumentImpl doc = new DocumentImpl();
        doc.put("firstName", "Bill");
        doc.put("height", 175);
        
        PropertyBinder binder = new StringBinderImpl("firstName");
        DocumentBindingHandler instance = new DocumentBindingHandler();
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
        PropertyBinder intBinder = new IntegerBinderImpl("height");
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
        
        DocumentBindings addrInstance = instance.createChild("address");        
        PropertyBinder countryBinder = new StringBinderImpl("country");
        PropertyBinder zipCodeBinder = new IntegerBinderImpl("zipCode");        
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
     * Test of firePropertyChange method, of class ObjectDocumentBinding.
     */
    @Test
    public void testFirePropertyChange() {
        System.out.println("DefaultDocumentBinding.firePropertyChange");
        //
        // Add two binders with the same property name 
        //
        Object component = new MockComponent();
        
        MockBinder binder = (MockBinder)MockBinder.create("firstName", component);
        DocumentBindingHandler instance = new DocumentBindingHandler();
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
        
/*        instance.firePropertyChange("firstName", 1, 1);
        assertTrue(binder.dataChanged);
        assertTrue(binder1.dataChanged);
        assertFalse(binder2.dataChanged);
        instance.firePropertyChange("lastName", 1, 1);        
        assertTrue(binder2.dataChanged);
*/        
        
    }

    /**
     * Test of getDocumentStore method, of class ObjectDocumentBinding.
     */
    @Test
    public void testGetDocument() {
        System.out.println("DefaultDocumentBinding.getDocument()");
        DocumentBindingHandler instance = new DocumentBindingHandler();
        PropertyStore doc;
        PropertyStore result = instance.getDocumentStore();
        assertNull(result);
        doc = new DocumentTest.DocumentImpl();
        instance.setDocument(doc,true);
        result = instance.getDocumentStore();
        assertTrue( doc == result);
    }

    /**
     * Test of setDocument method, of class DocumentBindingManager.
     */
/*    @Test
    public void testSetDocument() {
        System.out.println("DefaultBindingManager.setDocument(DocumentStore,boolean)");
        DocumentBindingManager instance = new DocumentBindingManager();
        DocumentImpl doc = new DocumentImpl();
        instance.setDocument(doc,true);        
        DocumentStore result = instance.getDocumentStore();
        assertTrue( doc == result);
        assertNotNull(doc.handler);        
        //
        // null parameter value
        //
        instance.setDocument(null,true);        
        result = instance.getDocumentStore();
        assertNull( result);
        assertNull(doc.handler);
        //
        // change document
        //
        Object component = new MockComponent();
        DocumentStore doc1 = new DocumentImpl();
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
 */
    
    /**
     * Test of setDocument method, of class DocumentBindingManager.
     */
    @Test
    public void testSetDocument() {
        System.out.println("DefaultBindingManager.setDocument(Document,boolean)");
        DocumentBindingHandler instance = new DocumentBindingHandler();
        DocumentImpl doc = new DocumentImpl();
        instance.setDocument(doc,true);        
        PropertyStore result = instance.getDocumentStore();
        assertTrue( doc == result);
//        assertNotNull(doc.handler);        
        assertNotNull(doc.docListener);        
        //
        // null parameter value
        //
        instance.setDocument((PropertyStore)null,true);        
        result = instance.getDocumentStore();
        assertNull( result);
        assertNull(doc.docListener);
        //
        // change document
        //
        Object component = new MockComponent();
        PropertyStore doc1 = new DocumentImpl();
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
