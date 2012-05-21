package org.document.binding;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author V. Shyskin
 */
public class NumberUtil {

    public static String stringOf(Number number) {
        return number == null ? "" : number.toString();
    }

    /**
     *
     * @param str
     * @return
     */
    public static String sanitizeNumber(String str) {

        if (str == null || str.trim().length() == 0) {
            return "0";
        }

        StringBuilder sb = new StringBuilder(str.trim());
        StringBuilder sbt = new StringBuilder(str.trim().length());

        boolean isFloat = false;

        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (c == '+' && i == 0) {
                continue;
            }
            if (Character.isWhitespace(c)) {
                continue;
            }
            sbt.append(c);
        }
        String s = sbt.toString();
        sb = new StringBuilder(sbt.length());
        
        
        if (s.contains(",")) {
            for (int i = 0; i < sbt.length(); i++) {
                char c = sbt.charAt(i);
                if ( c == '.' ) {
                    sb.append(sbt, i, sbt.length());
                    break;
                }
                if (c == ',' && i != 0 && i < sbt.length()-1 
                    && Character.isDigit(sbt.charAt(i-1)) &&
                    Character.isDigit(sbt.charAt(i+1)) ){
                    continue;
                }
                sb.append(c);
            }
            return sb.toString();
        }
        return s;
    }

    /**
     *
     * @param str
     * @return Long or Double
     * @throws NumberFormatException
     */
    public static Number numberOf(String str) throws NumberFormatException {
        String s = sanitizeNumber(str);
        s = s.toUpperCase();
        if (s.contains(".") || s.contains("E")) {
            return Double.parseDouble(s);
        }
        return Long.parseLong(s);
    }
    
    /**
     *
     * @param str
     * @return 
     * @throws NumberFormatException
     */
    public static BigDecimal bigDecimalOf(String str) throws NumberFormatException {
        String s = sanitizeNumber(str);
        return new BigDecimal(s);
    }
    
    public static <T> T toNumber(String value, T clazz) {
        
        BigDecimal bd = bigDecimalOf(value);
        
        if ( clazz.equals(Byte.class)) {
            return (T)(Byte)bd.byteValueExact();
        } if ( clazz.equals(Short.class)) {
            return (T)(Short)bd.shortValueExact();
        } else if ( clazz.equals(Integer.class)) {
            return (T)(Integer)bd.intValueExact();
        } else if ( clazz.equals(Long.class)) {
            return (T)(Long)bd.longValueExact();
        } else if ( clazz.equals(Float.class)) {
            return (T)(Float)bd.floatValue();
        } else if ( clazz.equals(Double.class)) {
            return (T)(Double)bd.doubleValue();
        } else if ( clazz.equals(BigInteger.class)) {
            return (T)(BigInteger)bd.toBigIntegerExact();
        } else if ( clazz.equals(BigDecimal.class)) {
            return (T)bd;
        } else if ( clazz.equals(String.class)) {
            return (T)(String)bd.toPlainString();
        }

        return null;
    }
}
