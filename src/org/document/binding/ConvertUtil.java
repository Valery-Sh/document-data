package org.document.binding;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.*;
import java.util.Arrays;
import java.util.Calendar;

/**
 *
 * @author V. Shyskin
 */
public class ConvertUtil {
    
    public static String stringOf(Boolean bool) {
        if (bool == null) {
            return "";
        }
        return bool ? "true" : "false";
    }
    
    public static String sanitizeBoolean(String str) {
        if (str == null || str.trim().length() == 0) {
            return "";
        }
        String s = str.trim().toLowerCase();
        String result = s;
        
        if ( s.equals("true") || s.equals("yes") ||  s.equals("y")) {
            result = "true";
        } else  if (s.equals("false") || s.equals("no") || s.equals("n")) {
            result = "false";
        }
        return result;
        
    }
    public static Boolean toBoolean(String value, Class clazz) {
        String s = sanitizeBoolean(value);
        Boolean result;
        if (s.length() == 0 && (((Class) clazz).isPrimitive()) ) {
            result = false;
        } else if (s.length() == 0){
            result =  null;
        } else {
            result = Boolean.valueOf(s);
        }
        return result;
    }

    public static String stringOf(Number number) {
        DecimalFormat df = new DecimalFormat();
        if (number == null) {
            return "";
        }
        return df.format(number);
    }

    public static String stringOf(java.util.Date date) {
        if (date == null) {
            return "";
        }
        DateFormat df = DateFormat.getDateInstance();
        return df.format(date);
    }

    public static String stringOf(java.sql.Date date) {
        if (date == null) {
            return "";
        }
        DateFormat df = DateFormat.getDateInstance();
        return df.format(date);
    }

    public static String stringOf(java.util.Calendar date) {
        if (date == null) {
            return "";
        }
        DateFormat df = DateFormat.getDateInstance();
        return df.format(date);
    }

    /**
     *
     * @param str
     * @return
     */
    public static String sanitizeNumber(String str) {

        if (str == null || str.trim().length() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(str.trim());
        StringBuilder sbt = new StringBuilder(str.trim().length());
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        char decSeparator = dfs.getDecimalSeparator();
        char groupSeparator = dfs.getGroupingSeparator();

        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (c == '+' && i == 0) {
                continue;
            }
            if ( c == groupSeparator ) {
                continue;
            }
            
            if (Character.isWhitespace(c)) {
                continue;
            }
            sbt.append(c);
        }
        if ( decSeparator != '.') {
            int idx = sbt.indexOf(String.valueOf(decSeparator));
            if ( idx >=0 ) {
                sbt.setCharAt(idx, '.');
            }
        }
        return sbt.toString();
    }

    /**
     *
     * @param str
     * @return
     * @throws NumberFormatException
     */
    public static BigDecimal bigDecimalOf(String str) throws NumberFormatException {
        String s = sanitizeNumber(str);
        if (str == null || s.length() == 0) {
            return null;
        }
        return new BigDecimal(s);
    }

    public static <T> T toNumber(String value,Class clazz) {
        BigDecimal bd = bigDecimalOf(value);
        boolean isNull = false;
        if (bd == null && (((Class) clazz).isPrimitive() || clazz.equals(String.class))) {
            bd = new BigDecimal(0);
            isNull = true;
        }
        if (bd == null) {
            return (T) null;
        }

        if (clazz.equals(Byte.class) || clazz.equals(byte.class)) {
            return (T) (Byte) bd.byteValueExact();
        }
        if (clazz.equals(Short.class) || clazz.equals(short.class)) {
            return (T) (Short) bd.shortValueExact();
        } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return (T) (Integer) bd.intValueExact();
        } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
            return (T) (Long) bd.longValueExact();
        } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
            return (T) (Float) bd.floatValue();
        } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
            return (T) (Double) bd.doubleValue();
        } else if (clazz.equals(BigInteger.class)) {
            if (isNull) {
                return null;
            }
            return (T) (BigInteger) bd.toBigIntegerExact();
        } else if (clazz.equals(BigDecimal.class)) {
            if (isNull) {
                return null;
            }
            return (T) bd;
        } else if (clazz.equals(String.class)) {
            if (isNull) {
                return null;
            }
            return (T) bd.toPlainString();
        }

        return (T) null;
    }

    public static String sanitizeDate(String str) {

        if (str == null || str.trim().length() == 0) {
            return null;
        }

        String ts = str.trim();
        DateFormat df = SimpleDateFormat.getDateInstance();
        String pattern = ((SimpleDateFormat) df).toPattern();


        char separator = '/';

        if (pattern.contains(".")) {
            separator = '.';
        } else if (pattern.contains("/")) {
            separator = '/';
        }
        if (separator != '-' && ts.contains("-") && ts.indexOf('-') == 4
                && (ts.lastIndexOf('-') == 6 || ts.lastIndexOf('-') == 7)) {
            int i1 = ts.substring(5).indexOf('-');
            int i2 = ts.substring(5).lastIndexOf('-');
            if (i1 != i2) {
                return str;
            }
            i1 = ts.indexOf('-');
            i2 = ts.lastIndexOf('-');

            int y = Integer.parseInt(ts.substring(0, i1));
            int m = Integer.parseInt(ts.substring(5, i2)) - 1;
            int d = Integer.parseInt(ts.substring(i2 + 1));
            Calendar cal = Calendar.getInstance();
            cal.set(y, m, d);
            return df.format(cal.getTime());
        }
        StringBuilder sb = new StringBuilder(ts.length());
        for (int i = 0; i < ts.length(); i++) {
            char c = ts.charAt(i);
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (c == '.' || c == '/' || c == separator) {
                sb.append(separator);
                continue;
            } else if (!Character.isDigit(c)) {
                return str;
            }
            sb.append(c);
        }

        if (sb.indexOf(Character.toString(separator)) < 0 && sb.length() == 8) {
            int[] a = new int[] {pattern.indexOf("dd"),pattern.indexOf("MM"),pattern.indexOf("yyyy")};
            if (a[0] >= 0 && a[1] >= 0 && a[2] >= 0) {
                Arrays.sort(a);
                sb.insert(a[2]-2, separator);
                sb.insert(a[1]-1, separator);
            }
        }
        return sb.toString();


    }

    public static <T> T toDate(String value, Class clazz) {
        String s = sanitizeDate(value);
        if (s == null) {
            return null;
        }
        DateFormat df = DateFormat.getDateInstance();
        java.util.Date d = null;
        try {
            d = df.parse(s);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid Date format " + "'" + value + "'");
        }
        if (clazz.equals(java.util.Date.class)) {
            return (T) d;
        } else if (clazz.equals(java.sql.Date.class)) {
            return (T) (new java.sql.Date(d.getTime()));
        } else if (clazz.equals(Calendar.class)) {
            Calendar cl = Calendar.getInstance();
            cl.setTime(d);
            return (T) cl;
        }
        return null;
    }
}
