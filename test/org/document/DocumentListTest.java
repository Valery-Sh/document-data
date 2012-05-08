/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Valery
 */
public class DocumentListTest {
    
    protected DocumentList instance;
    
    protected ListChangeEvent listEvent;
            
    public DocumentListTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        instance = new DocumentList();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of size method, of class DocumentList.
     */
    @Test
    public void testSize() {
        System.out.println("DocumentListTest : size()");
        
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
        instance.getBaseList().add( new DocumentImpl());
        
        expResult = instance.size();
        result = instance.getBaseList().size();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of isEmpty method, of class DocumentList.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        boolean result = instance.isEmpty();
        assertTrue(result);
        
        instance.getBaseList().add( new DocumentImpl());
        
        boolean expResult = instance.isEmpty();
        result = instance.getBaseList().isEmpty();
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of contains method, of class DocumentList.
     */
    @Test
    public void testContains() {
        System.out.println("contains");
        Object o = null;
        
        boolean expResult = instance.getBaseList().contains(o);
        boolean result = instance.contains(o);
        assertEquals(expResult, result);
        
        o = new DocumentImpl();
        instance.add(new DocumentImpl());
        
        expResult = instance.getBaseList().contains(o);
        result = instance.contains(o);
        assertEquals(expResult, result);

        
    }

    /**
     * Test of iterator method, of class DocumentList.
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");
        DocumentList instance = new DocumentList();

        Iterator result = instance.iterator();
        assertNotNull(result);
    }

    /**
     * Test of toArray method, of class DocumentList.
     */
    @Test
    public void testToArray_0args() {
        System.out.println("toArray");
        Object[] expResult = instance.getBaseList().toArray();
        Object[] result = instance.toArray();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of toArray method, of class DocumentList.
     */
    @Test
    public void testToArray_GenericType() {
        System.out.println("toArray");
        String[] s = new String[] {"1","2"};        
        Object[] expResult = instance.getBaseList().toArray( s );
        Object[] result = instance.toArray(s);
        assertTrue(expResult == result);
    }

    /**
     * Test of add method, of class DocumentList.
     */
    @Test
    public void testAdd_GenericType() {
        System.out.println("add");
        Document e = new DocumentImpl();
        
        instance.add(e);
        int result = instance.size();
        int expResult = instance.getBaseList().size();
        assertEquals(expResult, result);
        
        Document e1 = new DocumentImpl();
        assertTrue(instance.add(e));
        
    }

    /**
     * Test of fireAppend method, of class DocumentList.
     *              for add(E e)
     */
    @Test
    public void testFireAppend() {
        System.out.println("fireAppend (add(E e))");
        //
        // We intrested in properties: action,element,result.  
        //
        prepare();
        Object doc = new DocumentImpl(3);
        instance.add((Document)doc);
        Object result = listEvent.getAction();
        Object expResult = ListChangeEvent.Action.append;
        
        assertEquals(result,expResult);
        result = listEvent.getElement();
        expResult = doc;
        assertEquals(result,expResult);
        result = listEvent.getResult();
        assertTrue((Boolean)result);
        assertTrue((Boolean)result);
    }

    /**
     * Test of remove method, of class DocumentList.
     */
    @Test
    public void testRemove_Object() {
        System.out.println("remove");
        Object o = new DocumentImpl();

        boolean expResult = false;
        boolean result = instance.remove(o);
        assertFalse(result);
        
        result = instance.add((Document)o);
        assertTrue(result);
        
    }

    /**
     * Test of fireRemove method, of class DocumentList.
     * boolean remove(Object)
     */
    @Test
    public void testFireRemove_Object_boolean() {
        System.out.println("fireRemove (boolean remove(Object)");
        //
        // We intrested in properties: action,object,result.  
        // action == removeObject
        //
        prepare();
        
        Object to_remove = instance.get(1);
        instance.remove(to_remove);
        
        Object result = listEvent.getAction();
        Object expResult = ListChangeEvent.Action.removeObject;
        assertEquals(result,expResult);
        
        result = listEvent.getResult();
        expResult = true;
        assertEquals(result,expResult);
        
        result = listEvent.getObject();
        expResult = to_remove;
        assertEquals(result,expResult);
    }

    /**
     * Test of containsAll method, of class DocumentList.
     */
    @Test
    public void testContainsAll() {
        System.out.println("containsAll");
        
        Document doc0 = new DocumentImpl();
        Document doc1 = new DocumentImpl();
        Document doc2 = new DocumentImpl();
        Document doc3 = new DocumentImpl();
        Document doc4 = new DocumentImpl();
        instance.add(doc0);
        instance.add(doc1);
        Collection c = new HashSet();        
        
        instance.add(doc2);
        c.add(doc0);
        c.add(doc3);
        
        boolean result = instance.containsAll(c);
        assertFalse(result);

        instance.add(doc3);
        instance.add(doc4);
        
        result = instance.containsAll(c);
        assertTrue(result);

    }

    /**
     * Test of addAll method, of class DocumentList.
     */
    @Test
    public void testAddAll_Collection() {
        System.out.println("addAll");

        Document doc0 = new DocumentImpl();
        Document doc1 = new DocumentImpl();
        Document doc2 = new DocumentImpl();        
        instance.add(doc0);
        instance.add(doc1);
        instance.add(doc2);

        Collection<Document> c = new HashSet();

        Document doc3 = new DocumentImpl();                
        Document doc4 = new DocumentImpl();                
        c.add(doc3);
        c.add(doc4);
        
        
        boolean result = instance.addAll(c);
        assertTrue(result);
        assertTrue(instance.size() == 5);
        
        result = instance.containsAll(c);
        assertTrue(result);
    }

    /**
     * Test of addAll method, of class DocumentList.
     */
    @Test
    public void testAddAll_int_Collection() {
        System.out.println("addAll(int,Collection");

        Collection c = new HashSet();
        
        Document doc0 = new DocumentImpl(0);
        Document doc1 = new DocumentImpl(1);
        Document doc2 = new DocumentImpl(2);        
        
        instance.add(doc0);
        instance.add(doc1);
        instance.add(doc2);
        Document doc3 = new DocumentImpl(3);
        Document doc4 = new DocumentImpl(4);        

        c.add(doc3);
        c.add(doc4);
       
        
        int index = 1;
        boolean result = instance.addAll(index, c);
        assertTrue(result);
        
//        assertTrue( doc0 == instance.getBaseList().get(0));
//        assertEquals(doc3,instance.getBaseList().get(1));
//        assertTrue( doc4 == instance.getBaseList().get(2));                        
//        assertTrue( doc1 == instance.getBaseList().get(3));                
//        assertTrue( doc2 == instance.getBaseList().get(4)); 
        
        
    }
    
    /**
     * Test of fireAppendAll method, of class DocumentList.
     */
    @Test
    public void testFireAppendAll() {
        System.out.println("fireAppendAll (addAll(Collection))");
        //
        // We intrested in properties: action,collection,result.  
        //
        prepare();
        
        Object doc = new DocumentImpl(3);
        Collection c = new HashSet();        
        c.add(doc);
        
        instance.addAll(c);
        
        Object result = listEvent.getAction();
        Object expResult = ListChangeEvent.Action.appendAll;
        assertEquals(result,expResult);
        
        result = listEvent.getResult();
        expResult = true;
        
        assertEquals(result,expResult);
        
        result = listEvent.getCollection();
        expResult = c;
        assertEquals(result,expResult);
    }


    /**
     * Test of fireAddAll method, of class DocumentList.
     * addAll(int,Collection)
     */
    @Test
    public void testFireAddAll() {
        System.out.println("fireAdd (addAll(int,Collection))");
        //
        // We intrested in properties: action,index,collection,result.  
        //
        prepare();
        
        Object doc = new DocumentImpl(3);
        Collection c = new HashSet();        
        c.add(doc);
        
        instance.addAll(1,c);
        
        Object result = listEvent.getAction();
        Object expResult = ListChangeEvent.Action.addAll;
        assertEquals(result,expResult);
        
        result = listEvent.getIndex();
        expResult = 1;
        assertEquals(result,expResult);
        
        result = listEvent.getResult();
        expResult = true;
        assertEquals(result,expResult);
        
        result = listEvent.getCollection();
        expResult = c;
        assertEquals(result,expResult);
    }

    /**
     * Test of removeAll method, of class DocumentList.
     */
    @Test
    public void testRemoveAll() {
        System.out.println("removeAll");
        Collection c = new HashSet();
        
        Document doc0 = new DocumentImpl(0);
        Document doc1 = new DocumentImpl(1);
        Document doc2 = new DocumentImpl(2);        
        
        instance.add(doc0);
        instance.add(doc1);
        instance.add(doc2);

        Document doc3 = new DocumentImpl(3);
        Document doc4 = new DocumentImpl(4);        
        instance.add(doc3);
        instance.add(doc4);

        c.add(doc0);
        c.add(doc4);
        
        int index = 1;
        boolean result = instance.removeAll(c);
        assertTrue(result);
        
        assertTrue(instance.size() == 3);
        
        assertTrue( doc1 == instance.getBaseList().get(0));
        assertTrue( doc2 == instance.getBaseList().get(1));        
        assertTrue( doc3 == instance.getBaseList().get(2));                        

    }

    /**
     * Test of fireRemoveAll method, of class DocumentList.
     * removeAll(Collection)
     */
    @Test
    public void testFireRemoveAll() {
        System.out.println("fireRemoveAll (removeAll(Collection))");
        //
        // We intrested in properties: action,collection,result.  
        //
        prepare();
        
        
        Collection c = new HashSet();        
        c.add(instance.get(2)); // doc2
        
        instance.removeAll(c);
        
        Object result = listEvent.getAction();
        Object expResult = ListChangeEvent.Action.removeAll;
        assertEquals(result,expResult);
        
        result = listEvent.getResult();
        expResult = true;
        
        assertEquals(result,expResult);
        
        result = listEvent.getCollection();
        expResult = c;
        assertEquals(result,expResult);
    }

    /**
     * Test of retainAll method, of class DocumentList.
     */
    @Test
    public void testRetainAll() {
        System.out.println("retainAll");
        Collection c = new HashSet();
        
        Document doc0 = new DocumentImpl(0);
        Document doc1 = new DocumentImpl(1);
        Document doc2 = new DocumentImpl(2);        
        
        instance.add(doc0);
        instance.add(doc1);
        instance.add(doc2);

        Document doc3 = new DocumentImpl(3);
        Document doc4 = new DocumentImpl(4);        
        instance.add(doc3);
        instance.add(doc4);

        c.add(doc0);
        c.add(doc4);
        
        int index = 1;
        boolean result = instance.retainAll(c);
        assertTrue(result);
        
        assertTrue(instance.size() == 2);
        
        assertTrue( doc0 == instance.getBaseList().get(0));
        assertTrue( doc4 == instance.getBaseList().get(1));        
    }

    /**
     * Test of fireRetainAll method, of class DocumentList.
     * boolean retainAll(Collection)
     */
    @Test
    public void testFireRetainAll() {
        System.out.println("fireRetainAll (boolean retainAll(Collection))");
        //
        // We intrested in properties: action,collection,result.  
        //
        prepare();
        
        
        Collection c = new HashSet();        
        c.add(instance.get(2)); // doc2
        
        instance.retainAll(c);
        
        Object result = listEvent.getAction();
        Object expResult = ListChangeEvent.Action.retainAll;
        assertEquals(result,expResult);
        
        result = listEvent.getResult();
        expResult = true;
        assertEquals(result,expResult);
        
        result = listEvent.getCollection();
        expResult = c;
        assertEquals(result,expResult);
    }

    /**
     * Test of clear method, of class DocumentList.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        Document doc0 = new DocumentImpl(0);
        instance.add(doc0);
        instance.clear();
        assertTrue( instance.size() == 0 );
    }

    /**
     * Test of fireClear method, of class DocumentList.
     */
    @Test
    public void testFireClear() {
        System.out.println("fireClear");
    }

    /**
     * Test of get method, of class DocumentList.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        int index = 0;
        
        Document doc0 = new DocumentImpl(0);
        Object expResult = doc0;
        instance.add(doc0);
        Object result = instance.get(0);
        assertTrue( expResult == result );
    }

    /**
     * Test of set method, of class DocumentList.
     */
    @Test
    public void testSet() {
        System.out.println("set");
        int index = 0;
        Object element = null;
        
        Document doc0 = new DocumentImpl(0);
        Document doc1 = new DocumentImpl(1);
        Document doc2 = new DocumentImpl(2);
        
        Object expResult = doc2;
        
        instance.add(doc0);
        instance.add(doc1);        
        
        Object result = instance.set(index,doc2);
        assertTrue( doc0 == result );
        assertTrue( doc2 == instance.get(index) );
        
  //      Object result = instance.set(index, element);
    }

    /**
     * Test of fireSet method, of class DocumentList.
     * E set(int index,E e)
     */
    @Test
    public void testFireSet() {
        System.out.println("fireAdd (set(int index,E e))");
        //
        // We intrested in properties: action,index,element,result.  
        // action == set
        // result == return value 
        // element == e
        //
        prepare();
        Object oldValue = instance.get(1);
        Object doc = new DocumentImpl(3);
        instance.set(1,(Document)doc);
        Object result = listEvent.getAction();
        Object expResult = ListChangeEvent.Action.set;
        assertEquals(result,expResult);
        
        result = listEvent.getIndex();
        expResult = 1;
        assertEquals(result,expResult);
        
        result = listEvent.getElement();
        expResult = doc;
        assertEquals(result,expResult);

        result = listEvent.getResult();
        expResult = oldValue;
        assertEquals(result,expResult);
        
        
    }

    /**
     * Test of add method, of class DocumentList.
     */
    @Test
    public void testAdd_int_GenericType() {
        System.out.println("add");
        int index = 0;
        
        Document doc0 = new DocumentImpl(0);
        
        Object expResult = doc0;
        
        instance.add(index,doc0);
        assertTrue( doc0 == instance.get(index) );
        Document doc1 = new DocumentImpl(1);
        // index still 0
        instance.add(index,doc1);
        assertTrue(instance.get(0) == doc1);
        assertTrue(instance.get(1) == doc0);
    }
    
    protected void prepare() {
        Document doc0 = new DocumentImpl(0);
        Document doc1 = new DocumentImpl(1);
        Document doc2 = new DocumentImpl(2);
        
        instance.add(doc0);
        instance.add(doc1);        
        instance.add(doc2);
        instance.addListChangeListener(new ListChangeListenerImpl());

    }
    /**
     * Test of fireAdd method, of class DocumentList.
     *   in response to: add(int index,E e)
     */
    @Test
    public void testFireAdd() {
        System.out.println("fireAdd (add(int index,E e))");
        //
        // We intrested in properties: action,index,element.  
        //
        prepare();
        Object doc = new DocumentImpl(3);
        instance.add(1,(Document)doc);
        Object result = listEvent.getAction();
        Object expResult = ListChangeEvent.Action.add;
        assertEquals(result,expResult);
        
        result = listEvent.getIndex();
        expResult = 1;
        
        assertEquals(result,expResult);
        
        result = listEvent.getElement();
        expResult = doc;
        assertEquals(result,expResult);
        
        
    }

    /**
     * Test of remove method, of class DocumentList.
     */
    @Test
    public void testRemove_int() {
        System.out.println("remove");
        int index = 0;
        
        Document doc0 = new DocumentImpl(0);
        Document doc1 = new DocumentImpl(1);
        
        Object expResult = doc0;
        
        instance.add(doc0);
        instance.add(doc1);
        
        // index still 0
        instance.remove(index);
        assertTrue(instance.size() == 1);
        assertTrue(instance.get(0) == doc1);
    }

    /**
     * Test of fireRemove method, of class DocumentList.
     */
    @Test
    public void testFireRemove_int_GenericType() {
            System.out.println("fireRemove (E remove(int)");
        //
        // We intrested in properties: action,index,element,result.  
        // action == removeObject
        //
        prepare();
        
        Object to_remove = instance.get(1);
        instance.remove(1);
        
        Object result = listEvent.getAction();
        Object expResult = ListChangeEvent.Action.remove;
        assertEquals(result,expResult);
        
        result = listEvent.getIndex();
        expResult = 1;
        assertEquals(result,expResult);

        result = listEvent.getElement();
        expResult = to_remove;
        assertEquals(result,expResult);
        
        result = listEvent.getResult();
        expResult = to_remove;
        assertEquals(result,expResult);
        

    }

    /**
     * Test of indexOf method, of class DocumentList.
     */
    @Test
    public void testIndexOf() {
        System.out.println("indexOf");
        
        Document doc0 = new DocumentImpl(0);
        Document doc1 = new DocumentImpl(1);
        
        Object o = doc1;
        
        instance.add(doc0);
        instance.add(doc1);

        int result = instance.indexOf(o);
        assertEquals(1,result);
    }

    /**
     * Test of lastIndexOf method, of class DocumentList.
     */
    @Test
    public void testLastIndexOf() {
        System.out.println("lastIndexOf");
        Document doc0 = new DocumentImpl(0);
        Document doc1 = new DocumentImpl(1);
        Document doc2 = new DocumentImpl(2);
        
        instance.add(doc0);
        instance.add(doc0);
        instance.add(doc0);

        int result = instance.lastIndexOf(doc0);
        assertEquals(2,result);
    }

    /**
     * Test of listIterator method, of class DocumentList.
     */
    @Test
    public void testListIterator_0args() {
        System.out.println("listIterator");
        DocumentList instance = new DocumentList();
        assertNotNull(instance.listIterator());
    }

    /**
     * Test of listIterator method, of class DocumentList.
     */
    @Test
    public void testListIterator_int() {
        System.out.println("listIterator");
        Document doc0 = new DocumentImpl(0);
        Document doc1 = new DocumentImpl(1);
        Document doc2 = new DocumentImpl(2);
        
        
        instance.add(doc0);
        instance.add(doc1);
        instance.add(doc2);
        
        ListIterator result = instance.listIterator(1);
        assertNotNull(result);
    }

    /**
     * Test of subList method, of class DocumentList.
     */
    @Test
    public void testSubList() {
        System.out.println("subList");
        int fromIndex = 1;
        int toIndex = 2;
        Document doc0 = new DocumentImpl(0);
        Document doc1 = new DocumentImpl(1);
        Document doc2 = new DocumentImpl(2);
        
        
        instance.add(doc0);
        instance.add(doc1);
        instance.add(doc2);
        
        
        List result = instance.subList(fromIndex, toIndex);
        assertTrue(result.size() == 1);
        assertTrue(result.get(0) == doc1);
    }

    /**
     * Test of fireEvent method, of class DocumentList.
     */
    @Test
    public void testFireEvent() {
        System.out.println("fireEvent");
        ListChangeEvent event = null;
    }

    /**
     * Test of addListChangeListener method, of class DocumentList.
     */
    @Test
    public void testAddListChangeListener() {
        System.out.println("addListChangeListener");
        ListChangeListener l = new ListChangeListener() {
            @Override
            public void listChanged(ListChangeEvent event) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            
        };
        instance.addListChangeListener(l);
        assertEquals(1, instance.getListChangeListener().size());
    }

    /**
     * Test of removeListChangeListener method, of class DocumentList.
     */
    @Test
    public void testRemoveListChangeListener() {
        System.out.println("removeListChangeListener");
        ListChangeListener l = new ListChangeListener() {
            @Override
            public void listChanged(ListChangeEvent event) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            
        };
        instance.addListChangeListener(l);
        instance.removeListChangeListener(l);
        assertEquals(0, instance.getListChangeListener().size());
    }

    /**
     * Test of getListChangeListener method, of class DocumentList.
     */
    @Test
    public void testGetListChangeListener() {
        System.out.println("getListChangeListener");
        List result = instance.getListChangeListener();
        assertNotNull(result);
    }
    public static class DocumentImpl implements Document {
        public int _id = 0;
        protected PropertyDataStore store;
        
        public DocumentImpl() {
            store = new PropertyDataStoreImpl();
        }
        public DocumentImpl(int id) {
            this();
            _id = id;
        }
        
        @Override
        public PropertyDataStore getPropertyDataStore() {
            return store;
        }
    }
    public static class PropertyDataStoreImpl implements PropertyDataStore{

        @Override
        public Object get(Object key) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void put(Object key, Object value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void addDocumentChangeListener(DocumentChangeListener listener) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void removeDocumentChangeListener(DocumentChangeListener listener) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    public class ListChangeListenerImpl implements ListChangeListener{

        @Override
        public void listChanged(ListChangeEvent event) {
            listEvent = event;
        }
        
    }
}// TEST
