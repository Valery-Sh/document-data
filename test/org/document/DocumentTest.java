/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.util.HashMap;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Valery
 */
public class DocumentTest {

    public DocumentTest() {
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
     * Test of get method, of class Document.
     */
    @Test
    public void testGet() {
        System.out.println("Document.get(Object)");
        Object key = null;
        Document instance = new DocumentImpl();
        //
        // The 'key' parameter cannot be null
        //
        try {
            instance.get(key);
            fail("The 'key' parameter cannot be null");
        } catch (NullPointerException e) {
            System.out.println("Test if 'key' parameter is null");
        }
        //
        // No put value => must return null for any key
        //
        Object expResult;
        key = 1;
        Object result = instance.get(key);
        assertNull(result);
        //
        // put value and then get it
        //
        key = 1;
        expResult = "GET 1";
        instance.put(key, "GET 1");
        result = instance.get(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of put method, of class Document.
     */
    @Test
    public void testPut() {
        System.out.println("Document.put(Object,Object");
        Object key = null;
        Object value = "PUT 1";
        Document instance = new DocumentImpl();
        //
        // The 'key' parameter cannot be null
        //
        try {
            instance.put(key, value);
            fail("The 'key' parameter cannot be null");
        } catch (NullPointerException e) {
            System.out.println("Test if 'key' parameter is null");
        }
        //
        // put and then get
        //
        key = 1;
        instance.put(key, value);
        Object expResult = "PUT 1";
        Object result = instance.get(key);
        assertEquals(expResult, result);

        //
        // When an objects's key already exists then replace it's value
        //
        key = 1;
        value = "PUT 2";
        instance.put(key, value);

        expResult = "PUT 2";
        result = instance.get(key);
        assertEquals(expResult, result);

    }

    /**
     * Test of addDocumentListener method, of class Document.
     */
    @Test
    public void testAddDocumentListener() {
        System.out.println("setAddDocumentListener");
        DocumentListener l = null;
        Document instance = new DocumentImpl();
        instance.addDocumentListener(l);
    }

    public static class DocumentImpl implements Document {

        Map values = new HashMap();
        DocumentListener docListener;
        
        @Override
        public Object get(Object key) {
            if (key == null) {
                throw new NullPointerException("The 'key' parameter cannot be null");
            }
            return values.get(key);
        }

        @Override
        public void put(Object key, Object value) {
            if (key == null) {
                throw new NullPointerException("The 'key' parameter cannot be null");
            }
            Object oldValue = this.get(key);
            values.put(key, value);
            validate(key,value);
            if ( docListener != null ) {
                DocumentEvent event = new DocumentEvent(this,DocumentEvent.Action.propertyChange);
                event.setPropertyName(key.toString());
                event.setOldValue(oldValue);
                event.setNewValue(value);
                docListener.react(event);

            }
        }
        

        @Override
        public void addDocumentListener(DocumentListener listener) {
            this.docListener = listener;
        }

        @Override
        public void removeDocumentListener(DocumentListener listener) {
            this.docListener = null;
        }


        protected void validate(Object key, Object value) {
            if ( docListener != null ) {
                DocumentEvent event = new DocumentEvent(this,DocumentEvent.Action.validate);
                event.setPropertyName(key.toString());
                //event.setOldValue(oldValue);
                event.setNewValue(value);
                docListener.react(event);
            }

        }
    }//class DocumentImpl
}
