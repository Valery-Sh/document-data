/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import org.document.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Valery
 */
public class AbstractDocumentBinderTest {

    public AbstractDocumentBinderTest() {
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
     * Test of add method, of class DocumentBinder
     */
    @Test
    public void testAdd() {
        System.out.println("DocumentBinder.add(Binder)");
        DocumentBinder instance = new DocumentBinder();
        PropertyBinder binder = new MockPropertyBinder("firstName");
        instance.add(binder);
        //assertTrue(instance.getBinders("firstName").get(0) == binder);
    }

    /**
     * Test of remove method, of class DocumentBinder.
     */
    @Test
    public void testRemove() {
        System.out.println("DocumentBinder.remove(Binder)");
        DocumentBinder instance = new DocumentBinder();
        PropertyBinder binder1 = new MockPropertyBinder("firstName");
        instance.add(binder1);
        PropertyBinder binder2 = new MockPropertyBinder("lastName");
        instance.add(binder2);

        instance.remove(binder1);
//        assertNull(instance.getBinders("firstName"));
//        assertTrue(instance.getBinders("lastName").get(0) == binder2);
        instance.remove(binder2);
//        assertNull(instance.getBinders("lastName"));


    }

    /**
     * More complex test of completeChanges method, of class DocumentBinder.
     */
    @Test
    public void testCompleteChanges() {
        System.out.println("DocumentBinder.completeChanges");

        PropertyBinder binder = new MockPropertyBinder("firstName");
        DocumentBinder instance = new DocumentBinder();
        instance.document = new DocumentImpl();
        PropertyStore store = instance.document.propertyStore();
        store.put("firstName", "Bill");
        store.put("height", 175);

        instance.add(binder);

        //
        // Now firstName = "Bill". We set binder.componentValue to "Tom".
        // completeChanges() must set doc.firstName to "Tom".
        //
        instance.completeChanges();
        //
        // All binders are notified of DataChangeEvent with action==completeChanges
        //
        assertEquals(((MockPropertyBinder) binder).reactAction, DocumentChangeEvent.Action.completeChanges);
        //
        // Add another binder
        //
        PropertyBinder intBinder = new MockPropertyBinder("height");
        instance.add(intBinder);
        instance.completeChanges();
        assertEquals(((MockPropertyBinder) intBinder).reactAction, DocumentChangeEvent.Action.completeChanges);
    }

    /**
     * Test of getDocumentStore method, of class DocumentBinder.
     */
    @Test
    public void testGetDocument() {
        System.out.println("DocumentBinder.getDocument()");
        DocumentBinder instance = new DocumentBinder();
        Document doc = new DocumentImpl();
        instance.setDocument(doc);
        assertTrue(instance.getDocument() == doc);

    }

    /**
     * Test of setDocument method, of class DocumentBindingManager.
     */
    @Test
    public void testSetDocument() {
        System.out.println("DefaultBindingManager.setDocument(Document,boolean)");
        PropertyBinder binder = new MockPropertyBinder("firstName");
        DocumentBinder personDocumentBinder = new DocumentBinder();
        DocumentBinder addrDocumentBinder = personDocumentBinder.createChild("address");

        Document doc = new DocumentImpl();
        personDocumentBinder.setDocument(doc);

        PropertyStore store = doc.propertyStore();
        store.put("firstName", "Bill");
        store.put("height", 175);

        personDocumentBinder.add(binder);
        //
        // Try embedded document. 
        //
        DocumentImpl addrDoc = new DocumentImpl();
        addrDoc.propertyStore.put("country", "UK");
        addrDoc.propertyStore.put("zipCode", 99955);


        Document newDoc = new DocumentImpl();
        newDoc.propertyStore().put("address", addrDoc);


        PropertyBinder countryBinder = new MockPropertyBinder("country");
        PropertyBinder zipCodeBinder = new MockPropertyBinder("zipCode");
        addrDocumentBinder.add(countryBinder);
        addrDocumentBinder.add(zipCodeBinder);

        personDocumentBinder.setDocument(newDoc);
        assertTrue(newDoc == personDocumentBinder.getDocument());
        //
        // The DocumentBinder of an embedded document should get it's new document too.
        //
        assertTrue(addrDoc == addrDocumentBinder.getDocument());

    }
}
