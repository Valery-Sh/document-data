/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import java.math.BigDecimal;
import java.math.BigInteger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;
import org.junit.*;

/**
 *
 * @author Valery
 */
public class NumberUtilTest {
    
    public NumberUtilTest() {
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
     * Test of stringOf method, of class NumberUtil.
     */
    @Test
    public void testStringOf_Default() {
        System.out.println("stringOf(Number) Default");
        Number number = null;
        String expResult = "";
        String result = NumberUtil.stringOf(number);
        assertEquals(expResult, result);
        
        number = 25;
        expResult = "25";
        result = NumberUtil.stringOf(number);
        assertEquals(expResult, result);        
    }

    /**
     * Test of default numberOf method, of class NumberUtil.
     */
    @Test
    public void testNumberOf() {
        System.out.println("numberOf(String) default");
        String str = null;
        Number expResult = 0L;
        Number result = NumberUtil.numberOf(str);
        assertEquals(expResult, result);
        
        str = "";
        expResult = 0L;
        result = NumberUtil.numberOf(str);
        assertEquals(expResult, result);        
        
        //
        // We allow a string with '+' as a first letter.
        // We allow whitespaces before and/or after a number.
        //
        str = "   +12       ";
        expResult = 12L;
        result = NumberUtil.numberOf(str);
        assertEquals(expResult, result);        
        
        str = "   +12.3       ";
        expResult = 12.3;
        result = NumberUtil.numberOf(str);
        assertEquals(expResult, result);        

        str = "   -12.3       ";
        expResult = -12.3;
        result = NumberUtil.numberOf(str);
        assertEquals(expResult, result);        
        
        try {
            NumberUtil.numberOf("+");
            NumberUtil.numberOf("-");
            NumberUtil.numberOf("   +    ");
            NumberUtil.numberOf("2+");
            NumberUtil.numberOf("Bill");
            fail();
        } catch(Exception e) {
            
        }
        //
        // We allow ',' as a decimal separator ( now check decimal group)
        //
        str = "   -1,23,456,78.3       ";
        expResult = -12345678.3;
        result = NumberUtil.numberOf(str);
        assertEquals(expResult, result);        
        
        try {
            NumberUtil.numberOf(",1");
            NumberUtil.numberOf("1,");
            NumberUtil.numberOf("1,,2");
            NumberUtil.numberOf("1.2,3");
            fail();
        } catch(Exception e) {
            
        }
        //BigDecimal bd = new BigDecimal();
        
    }

    /**
     * Test of default bigDecimalOf method, of class NumberUtil.
     */
    @Test
    public void testBigDecimalOf() {
        System.out.println("bigDecimalOf(String) default");
        String str = null;
        BigDecimal expResult = new BigDecimal(0);
        Number result = NumberUtil.bigDecimalOf(str);
        assertEquals(expResult, result);
        
        str = "";
        result = NumberUtil.bigDecimalOf(str);
        assertEquals(expResult, result);        
        
        //
        // We allow a string with '+' as a first letter.
        // We allow whitespaces before and/or after a number.
        //
        str = "   +12       ";
        expResult = new BigDecimal("12");
        result = NumberUtil.bigDecimalOf(str);
        assertEquals(expResult, result);        
        
        str = "   +12.3       ";
        expResult = new BigDecimal("12.3");
        result = NumberUtil.bigDecimalOf(str);
        assertEquals(expResult, result);        

        str = "   -12.3       ";
        expResult = new BigDecimal("-12.3");
        result = NumberUtil.bigDecimalOf(str);
        assertEquals(expResult, result);        
        
        try {
            NumberUtil.bigDecimalOf("+");
            NumberUtil.bigDecimalOf("-");
            NumberUtil.bigDecimalOf("   +    ");
            NumberUtil.bigDecimalOf("2+");
            NumberUtil.bigDecimalOf("Bill");
            fail();
        } catch(Exception e) {
            
        }
        //
        // We allow ',' as a decimal separator ( now check decimal group)
        //
        str = "   -1,23,456,78.3       ";
        expResult = new BigDecimal("-12345678.3");
        result = NumberUtil.bigDecimalOf(str);
        assertEquals(expResult, result);        
        
        try {
            NumberUtil.bigDecimalOf(",1");
            NumberUtil.bigDecimalOf("1,");
            NumberUtil.bigDecimalOf("1,,2");
            NumberUtil.bigDecimalOf("1.2,3");
            fail();
        } catch(Exception e) {
            
        }
        //BigDecimal bd = new BigDecimal();
        
    }//bigDecimalOf
    
    /**
     * Test of default bigDecimalOf method, of class NumberUtil.
     */
    @Test
    public void testToNumber() {
        System.out.println("testToNumber(String) default");
        String str = "123";
        
        Object expResult = new Integer(123);
        Object result = NumberUtil.toNumber(str, Integer.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof Integer);
        
        str = "123.01";
        expResult = new BigDecimal("123.01");
        result = NumberUtil.toNumber(str, BigDecimal.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof BigDecimal);
        
        
        str = "127";
        expResult = Byte.parseByte(str);
        result = NumberUtil.toNumber(str, Byte.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof Byte);
        

        expResult = 127L;
        result = NumberUtil.toNumber(str, Long.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof Long);
        
        expResult = new Float("127.1");
        result = NumberUtil.toNumber("127.1", Float.class);
        assertEquals(expResult, result);
        
        expResult = new Double("127.1");
        result = NumberUtil.toNumber("127.1", Double.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof Double);
        

        expResult = new BigInteger("127");
        result = NumberUtil.toNumber("127", BigInteger.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof BigInteger);

        expResult = "127.01";
        result = NumberUtil.toNumber("12701E-2", String.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof String);
        
        //
        // Test Integer and Long max values
        //
                
        String greaterThen_MAX_INT_VALUE = new Long(Integer.MAX_VALUE + 1).toString();
        
        BigInteger bi = new BigInteger(new Long(Long.MAX_VALUE).toString());
        BigInteger addbi = new BigInteger("1");
        bi = bi.add(addbi);
        String greaterThen_MAX_LONG_VALUE = bi.toString();
        try {
            NumberUtil.toNumber("129",Byte.class);
            NumberUtil.toNumber(greaterThen_MAX_INT_VALUE,Integer.class);
            NumberUtil.toNumber(greaterThen_MAX_LONG_VALUE,Long.class);
            fail();
        } catch(Exception e){
            System.out.println("Out of range");
        }
    }    
     /**
     * Test of default numberOf method, of class NumberUtil.
     */
    @Test
    public void testMy() {
        System.out.println("BIGDECIMAL");
        String str = null;
        Number expResult = 0L;
        Number result = NumberUtil.numberOf(str);
        assertEquals(expResult, result);
        
        BigDecimal bd = new BigDecimal("123");
        //bd.scale();
        System.out.println("SCALE: " + bd.scale());
        assertEquals(bd.scale(), 0);

        bd = new BigDecimal("-123");
        System.out.println("SCALE: " + bd.scale());
        assertEquals(bd.scale(), 0);
        
        bd = new BigDecimal("123.1");
        System.out.println("BiDec=" +bd + "; SCALE: " + bd.scale());
        assertEquals(bd.scale(), 1);
        
        // set scale return new BigDecimal and doesn't affect the source BigDecimal

        bd = bd = new BigDecimal("123.000");
        System.out.println("BiDec=" +bd + "; SCALE: " + bd.scale());
        assertEquals(bd.scale(), 3);
        
    }
}
    
