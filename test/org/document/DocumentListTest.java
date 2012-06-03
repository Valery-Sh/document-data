package org.document;

import java.util.ArrayList;
import java.util.List;
import org.document.binding.BindingManager;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author V. Shyshkin
 */
public class DocumentListTest {
    protected List list;

    public DocumentListTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        list = new ArrayList();
    }
    
    @After
    public void tearDown() {
    }
    protected void prepare() {
        Document doc0 = new DocumentImpl();
        Document doc1 = new DocumentImpl();
        Document doc2 = new DocumentImpl();
        
        list.add(doc0);
        list.add(doc1);        
        list.add(doc2);
        
    }
    
    
    /**
     * Test of addSelect method, of class DocumentList.
     */
    @Test
    @SuppressWarnings("empty-statement")
    public void testAddAndSelect() {
        System.out.println("addAndSelect");
        prepare();
        DocumentList instance = new DocumentList(list);
        Document expResult = new DocumentImpl();
        Object result = instance.addSelect(expResult);
        assertTrue(expResult == result);
    }

    /**
     * Test of getLast method, of class DocumentList.
     */
    @Test
    public void testGetLast() {
        System.out.println("getLast");
        prepare();
        DocumentList instance = new DocumentList(list);
        Object expResult = instance.get(instance.size()-1);
        Object result = instance.getLast();
        assertTrue(expResult == result);
    }

    /**
     * Test of createAddAndSelect method, of class DocumentList.
     */
    @Test
    public void testCreateAddAndElementState() {
        System.out.println("createAddAndElementState");
        //
        // We intrested in properties: action,index,element,result.  
        //
        Document doc = new DocumentImpl();
        DocumentList instance = new DocumentList(list);
        ListChangeEvent e = instance.createAddAndSelect(doc,true);
        
        ListChangeEvent.Action action = e.getAction();
        Object expResult = ListChangeEvent.Action.addAndSelect;
        assertEquals(expResult,action);
        //
        // e.getResult() must be equals to second parameter value
        //
        assertTrue((Boolean)e.getResult());
        //
        // if getResult == true then index mustbe size()-1
        // 
        assertEquals(instance.size()-1, e.getIndex());
        //
        // if getResult == false then index mustbe -1
        // 
        e = instance.createAddAndSelect(doc,true);
        assertEquals(-1, e.getIndex());
        
    }
    //
    // Test ValidateHandlerImpl class
    //
    @Test
    public void testValidate() {
        //
        // test validate for an event of set(int,Document) method
        // The new element cannot be null and is not already contaned by the list
        //
        prepare();
        DocumentList instance = new DocumentList(list);
        //
        // simulates set(1,list.get(0)) 
        //
        ListChangeEvent e = instance.createSet(1, list.get(0), list.get(0));
        assertFalse(instance.validate(e));
        //
        // simulates set(1,null) 
        //
        e = instance.createSet(1, null, null);
        assertFalse(instance.validate(e));

        //
        // simulates add(list.get(0)) 
        //
        e = instance.createAppend(list.get(0), true);
        assertFalse(instance.validate(e));
        //
        // simulates add(null) 
        //
        e = instance.createAppend(null, true);
        assertFalse(instance.validate(e));
        //
        // simulates add(1,list.get(0)) 
        //
        e = instance.createAdd(1,list.get(0));
        assertFalse(instance.validate(e));
        //
        // simulates add(null) 
        //
        e = instance.createAdd(1,null);
        assertFalse(instance.validate(e));

        //
        // simulates addAll(int,Collection )) 
        //
        List l = new ArrayList();
        l.add(list.get(0));
        l.add(new DocumentImpl());
        e = instance.createAddAll(1,l,true);
        assertFalse(instance.validate(e));
        //
        // simulates add(1,null) 
        //
        l.add(null);
        e = instance.createAddAll(1,l,true);
        assertFalse(instance.validate(e));

        //
        // simulates addAll(list.get(0)) 
        //
        l = new ArrayList();
        l.add(list.get(0));
        l.add(new DocumentImpl());
        e = instance.createAppendAll(l,true);
        assertFalse(instance.validate(e));
        //
        // simulates add(1,null) 
        //
        l.add(null);
        e = instance.createAppendAll(l,true);
        assertFalse(instance.validate(e));
        
    }
    

}
