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
     * Test of getBindingManager method, of class DocumentList.
     */
    @Test
    public void testGetBindingManager() {
        
        System.out.println("getBindingManager");
        DocumentList instance = new DocumentList();
        
        BindingManager result = instance.getBindingManager();
        assertNull(result);
        
        BindingManager expResult = new BindingManager<>();        
        DocumentList l = new DocumentList(list,expResult);
        
        result = l.getBindingManager();
        assertTrue(result == expResult);
        
    }

    /**
     * Test of addAndSelect method, of class DocumentList.
     */
    @Test
    public void testAddAndSelect() {
        System.out.println("addAndSelect");
        prepare();
        DocumentList instance = new DocumentList(list);
        Document expResult = new DocumentImpl();;
        Object result = instance.addAndSelect(expResult);
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
        
        
    }


}
