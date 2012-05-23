/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Valery
 */
public class ConvertUtilTest {
    
    public ConvertUtilTest() {
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
     * Test of stringOf method, of class ConvertUtil.
     */
    @Test
    public void testStringOf_Default() {
        System.out.println("stringOf(Number) Default");
        Number number = null;
        String expResult = "";
        String result = ConvertUtil.stringOf(number);
        assertEquals(expResult, result);
        
        number = 25;
        expResult = "25";
        result = ConvertUtil.stringOf(number);
        assertEquals(expResult, result);        
    }

    /**
     * Test of default bigDecimalOf method, of class ConvertUtil.
     */
    @Test
    public void testBigDecimalOf() {
        System.out.println("bigDecimalOf(String) default");
        String str = null;
        BigDecimal expResult = null;
        Number result = ConvertUtil.bigDecimalOf(str);
        assertEquals(expResult, result);
        
        str = "";
        result = ConvertUtil.bigDecimalOf(str);
        assertEquals(expResult, result);        
        
        //
        // We allow a string with '+' as a first letter.
        // We allow whitespaces before and/or after a number.
        //
        str = "   +12       ";
        expResult = new BigDecimal("12");
        result = ConvertUtil.bigDecimalOf(str);
        assertEquals(expResult, result);        
        
        str = "   +12.3       ";
        expResult = new BigDecimal("12.3");
        result = ConvertUtil.bigDecimalOf(str);
        assertEquals(expResult, result);        

        str = "   -12.3       ";
        expResult = new BigDecimal("-12.3");
        result = ConvertUtil.bigDecimalOf(str);
        assertEquals(expResult, result);        

        str = "   -12,3       ";
        expResult = new BigDecimal("-12.3");
        result = ConvertUtil.bigDecimalOf(str);
        assertEquals(expResult, result);        
        
        try {
            ConvertUtil.bigDecimalOf("+");
            ConvertUtil.bigDecimalOf("-");
            ConvertUtil.bigDecimalOf("   +    ");
            ConvertUtil.bigDecimalOf("2+");
            ConvertUtil.bigDecimalOf("Bill");
            fail();
        } catch(Exception e) {
            
        }
        //
        // We allow ',' as a decimal separator ( now check decimal group)
        //
/*        str = "   -1,23,456,78.3       ";
        expResult = new BigDecimal("-12345678.3");
        result = ConvertUtil.bigDecimalOf(str);
        assertEquals(expResult, result);        
*/        
        try {
            ConvertUtil.bigDecimalOf(",1");
            ConvertUtil.bigDecimalOf("1,");
            ConvertUtil.bigDecimalOf("1,,2");
            ConvertUtil.bigDecimalOf("1.2,3");
            fail();
        } catch(Exception e) {
            
        }
        //BigDecimal bd = new BigDecimal();
        
    }//bigDecimalOf
    
    /**
     * Test of default bigDecimalOf method, of class ConvertUtil.
     */
    @Test
    public void testToNumber() {
        System.out.println("testToNumber(String) default");
        //
        // When str == null or str == "" and a class is not primitive
        //
        String str = null;
        Object expResult;
        Object result = ConvertUtil.toNumber(str, Byte.class);
        assertNull(result);

        str = "";
        result = ConvertUtil.toNumber(str, Byte.class);
        assertNull(result);
        
        //
        // When str == null or str == "" and a class is primitive
        //
        expResult = (byte)0x0;
        result = ConvertUtil.toNumber(str, byte.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof Byte);

        str = "";
        result = ConvertUtil.toNumber(str, byte.class);
        assertEquals(expResult, result);
        
        
        str = "127";
        expResult = Byte.parseByte(str);
        result = ConvertUtil.toNumber(str, Byte.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof Byte);

        result = ConvertUtil.toNumber(str, byte.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof Byte);
        
        
        expResult = Short.parseShort(str);
        result = ConvertUtil.toNumber(str, Short.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof Short);

        result = ConvertUtil.toNumber(str, short.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof Short);
        
        str = "";
        expResult = (short)0;
        result = ConvertUtil.toNumber(str, short.class);
        assertEquals(expResult, result);
        
        
        str = "123";
        expResult = new Integer(123);
        result = ConvertUtil.toNumber(str, Integer.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof Integer);
        
        result = ConvertUtil.toNumber(str, int.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof Integer);

        str = "";
        expResult = 0;
        result = ConvertUtil.toNumber(str, int.class);
        assertEquals(expResult, result);
        
        str = "123";
        expResult = 123L;
        result = ConvertUtil.toNumber(str, Long.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof Long);

        result = ConvertUtil.toNumber(str, long.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof Long);

        str = "";
        expResult = 0l;
        result = ConvertUtil.toNumber(str, long.class);
        assertEquals(expResult, result);
        
        expResult = new Float("123.4");
        result = ConvertUtil.toNumber("123.4", Float.class);
        assertEquals(expResult, result);
        
        result = ConvertUtil.toNumber("123.4", float.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof Float);

        str = "";
        expResult = 0F;
        result = ConvertUtil.toNumber(str, float.class);
        assertEquals(expResult, result);
        
        expResult = new Double("123.4");
        result = ConvertUtil.toNumber("123.4", Double.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof Double);
        result = ConvertUtil.toNumber("123.4", double.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof Double);
        
        str = "";
        expResult = 0D;
        result = ConvertUtil.toNumber(str, double.class);
        assertEquals(expResult, result);

        expResult = new BigInteger("123");
        result = ConvertUtil.toNumber("123", BigInteger.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof BigInteger);
        
        result = ConvertUtil.toNumber(null, BigInteger.class);
        assertNull( result);
        
        result = ConvertUtil.toNumber("", BigInteger.class);
        assertNull(result);

        
        
        str = "123.01";
        expResult = new BigDecimal("123.01");
        result = ConvertUtil.toNumber(str, BigDecimal.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof BigDecimal);
        
        result = ConvertUtil.toNumber(null, BigDecimal.class);
        assertNull( result);
        result = ConvertUtil.toNumber("", BigDecimal.class);
        assertNull(result);
        
        expResult = "123.01";
        result = ConvertUtil.toNumber("12301E-2", String.class);
        assertEquals(expResult, result);
        assertTrue(result instanceof String);
        
        result = ConvertUtil.toNumber(null, String.class);
        assertNull( result);

        result = ConvertUtil.toNumber("", String.class);
   //     assertNull( result);

        
        //
        // Test Integer and Long max values
        //
                
        String greaterThen_MAX_INT_VALUE = new Long(Integer.MAX_VALUE + 1).toString();
        
        BigInteger bi = new BigInteger(new Long(Long.MAX_VALUE).toString());
        BigInteger addbi = new BigInteger("1");
        bi = bi.add(addbi);
        String greaterThen_MAX_LONG_VALUE = bi.toString();
        try {
            ConvertUtil.toNumber("129",Byte.class);
            ConvertUtil.toNumber(greaterThen_MAX_INT_VALUE,Integer.class);
            ConvertUtil.toNumber(greaterThen_MAX_LONG_VALUE,Long.class);
            fail();
        } catch(Exception e){
            System.out.println("Out of range");
        }
    }    
     /**
     * Test of default sanitizeDate method, of class ConvertUtil.
     */
    @Test
    public void testSanitizeDate() {
        System.out.println("sanitizeDate(String)");
        String str = null;
        String expResult;
        String result = ConvertUtil.sanitizeDate(str);
        assertNull(result);

        result = ConvertUtil.sanitizeDate("");
        assertNull(result);
        
        str = "01.12.2012";
        expResult = "01.12.2012";
        result = ConvertUtil.sanitizeDate(str);
        assertEquals(expResult,result);

        str = "  01 . 12  .  2012   ";
        result = ConvertUtil.sanitizeDate(str);
        assertEquals(expResult,result);
        
        str = "  01 / 12  /  2012   ";
        expResult = "01.12.2012";
        result = ConvertUtil.sanitizeDate(str);
        assertEquals(expResult,result);
        
        str = "  01 / 12  /  2012   ";
        expResult = "01.12.2012";
        result = ConvertUtil.sanitizeDate(str);
        assertEquals(expResult,result);
        //
        //
        //
        str = "2012-12-01";
        expResult = "01.12.2012";
        result = ConvertUtil.sanitizeDate(str);
        assertEquals(expResult,result);

        str = "2012-1-1";
        expResult = "01.01.2012";
        result = ConvertUtil.sanitizeDate(str);
        assertEquals(expResult,result);

//        DateFormat df = DateFormat.getDateInstance();
        
    }    
     /**
     * Test of default toDate method, of class ConvertUtil.
     */
    @Test
    public void testToDate() {
        System.out.println("toDate(String,Class)");
        String str = null;
        Object expResult;
        Object result = ConvertUtil.toDate(str, Date.class );
        assertNull(result);
        Calendar calendar = Calendar.getInstance();
        
        str = "01.12.2012";
        calendar.clear();
        calendar.set(2012, 11, 1,0,0,0);
        //
        // Test result java.util.Date
        //
        expResult = calendar.getTime();
        result = ConvertUtil.toDate(str, Date.class );
        assertEquals(expResult,result);
        //
        // Test result java.sql.Date
        //
        expResult = new java.sql.Date(calendar.getTimeInMillis());
        result = ConvertUtil.toDate(str, java.sql.Date.class );
        assertEquals(expResult,result);
        //
        // Test result java.util.Calendar
        //
        result = ConvertUtil.toDate(str, Calendar.class );
        expResult = calendar;
        assertEquals(expResult,result);
        

    }    
     /**
     * Test of default numberOf method, of class ConvertUtil.
     */
    @Test
    public void testMy() {
        System.out.println("Date = " + (new Date()));
        String str = null;
        NumberFormat nf = new DecimalFormat();
        ((DecimalFormat)nf).toPattern();
        System.out.println("Number = " + nf.format(1234567.78));
        System.out.println("Number pattern = " + ((DecimalFormat)nf).toPattern());
        BigDecimal bd = new BigDecimal("1234.56");
        System.out.println("BigDec = " + bd);        
        try {
            System.out.println("parse pattern = " + ((DecimalFormat)nf).parse("1234567,78"));        
        }catch(Exception e) {
            
        }
        

    }
}
    
