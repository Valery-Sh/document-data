/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import java.util.ArrayList;
import java.util.List;
import org.document.Document;
import org.document.DocumentChangeEvent;
import org.document.DocumentPropertyStore;
import org.document.PropertyStore;
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
public class ListStateTest {
    
    public ListStateTest() {
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
     * Test of propertyStore method, of class ListState.
     */
    @Test
    public void testPropertyStore() {
        System.out.println("propertyStore");
        ListState instance = new ListState();
        PropertyStore result = instance.propertyStore();
        assertNotNull(result);
    }

    /**
     * Test of getDocumentList method, of class ListState.
     */
    @Test
    public void testGetDocumentList() {
        System.out.println("getDocumentList");
        
        ListState instance = new ListState();
        assertNull(instance.getDocumentList());
        List<Document> list = new ArrayList<Document>();

        instance.setDocumentList(list);
        List expResult = list;
        List result = instance.getDocumentList();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDocumentList method, of class ListState.
     */
    @Test
    public void testSetDocumentList() {
        System.out.println("setDocumentList");
        List<Document> list = new ArrayList<Document>();
        ListState instance = new ListState();
        //
        // list == null
        //
        instance.setDocumentList(null);
        assertNull(instance.getDocumentList());
        //
        // empty list
        //
        instance.setDocumentList(list);
        List expResult = list;
        List result = instance.getDocumentList();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of getSelected method, of class ListState.
     */
    @Test
    public void testGetSelected() {
        System.out.println("getSelected");
        ListState instance = new ListState();
        Document expResult = null;
        Document result = instance.getSelected();
        assertNull(result);

        Document doc = new DocumentImpl();
        instance.setSelected(doc);
        
        assertTrue(doc == instance.getSelected());
        
        
    }

    /**
     * Test of setSelected method, of class ListState.
     */
    @Test
    public void testSetSelected() {
        System.out.println("setSelected");
        Document selected = null;
        ListState instance = new ListState();
        instance.setSelected(selected);
        Document result = instance.getSelected();
        assertNull(result);

        Document doc = new DocumentImpl();
        instance.setSelected(doc);
        
        assertTrue(doc == instance.getSelected());
        
    }

    /**
     * Test of getDocumentChangeEvent method, of class ListState.
     */
    @Test
    public void testGetDocumentChangeEvent() {
        System.out.println("getDocumentChangeEvent");
        ListState instance = new ListState();
        DocumentChangeEvent event = new DocumentChangeEvent(this);
        instance.setDocumentChangeEvent(event);
        DocumentChangeEvent result = instance.getDocumentChangeEvent();
        assertTrue(event == result);
//        assertEquals(expResult, result);
    }

    /**
     * Test of setDocumentChangeEvent method, of class ListState.
     */
    @Test
    public void testSetDocumentChangeEvent() {
        System.out.println("setDocumentChangeEvent");
        ListState instance = new ListState();
        DocumentChangeEvent event = new DocumentChangeEvent(this);
        instance.setDocumentChangeEvent(event);
        DocumentChangeEvent result = instance.getDocumentChangeEvent();
        assertTrue(event == result);
    }
}
