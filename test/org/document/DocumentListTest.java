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
public class DocumentListTest {
    
    public DocumentListTest() {
    }

    protected ObservableList instance;
    
    protected ListChangeEvent listEvent;
            

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        instance = new ObservableList();
    }
    
    @After
    public void tearDown() {
    }
    protected void prepare() {
        Document doc0 = new ObservableListTest.DocumentImpl(0);
        Document doc1 = new ObservableListTest.DocumentImpl(1);
        Document doc2 = new ObservableListTest.DocumentImpl(2);
        
        instance.add(doc0);
        instance.add(doc1);        
        instance.add(doc2);
        //instance.addListChangeListener(new ObservableListTest.ListChangeListenerImpl());

    }
    protected Document createDoc(int id) {
        return new ObservableListTest.DocumentImpl(id);
    }
    /**
     * Test of newDocument method, of class DocumentList.
     */
    @Test
    public void testNewDocument() {
        System.out.println("newDocument");
        Document e = createDoc(100);
        boolean expResult = false;
        boolean result = instance.newDocument();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateState method, of class DocumentList.
     */
    @Test
    public void testUpdateState() {
        System.out.println("updateState");
        Object e = null;
        DocumentList instance = new DocumentList();
        instance.updateState(e);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fireNewDocument method, of class DocumentList.
     */
    @Test
    public void testFireNewDocument() {
        System.out.println("fireNewDocument");
        Object e = null;
        boolean result_2 = false;
        DocumentList instance = new DocumentList();
        instance.fireNewDocument(e, result_2);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fireEvent method, of class DocumentList.
     */
    @Test
    public void testFireEvent() {
        System.out.println("fireEvent");
        ListChangeEvent event = null;
        DocumentList instance = new DocumentList();
        instance.fireEvent(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of listChanged method, of class DocumentList.
     */
    @Test
    public void testListChanged() {
        System.out.println("listChanged");
        ListChangeEvent event = null;
        DocumentList instance = new DocumentList();
        instance.listChanged(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
