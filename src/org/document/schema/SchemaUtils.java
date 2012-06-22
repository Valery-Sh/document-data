package org.document.schema;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 *
 * @author Valery
 */
public class SchemaUtils {
    public static DocumentSchema createSchema(Class clazz, Class restrictSuper) {
        DocumentSchema schema;
/*        if ( DataUtils.isMapType(clazz)) {
            return new MapSchema(clazz);
        } else if ( isSetType(clazz)) {
            return new NoFieldSchema(clazz);
        }
*/
        schema = new DefaultSchema(clazz);
        Map<String,Boolean> trfields = new HashMap<String,Boolean>();
        Map<String,Field> allfields = new HashMap<String,Field>();
        Class c = clazz;
        try {
            
            while (  c != restrictSuper ) {
               java.lang.reflect.Field[] dfs = c.getDeclaredFields();
               for ( java.lang.reflect.Field f : dfs) {
                   
                   String nm = f.getName();
                   allfields.put(nm, f);
                   if ( Modifier.isTransient(f.getModifiers())) {
                       trfields.put(f.getName(), Boolean.TRUE);
                   }
               }
               c = c.getSuperclass();
            }
            
        } catch(Exception e) {
            
        }
        
        try {
            BeanInfo binfo = Introspector.getBeanInfo(clazz, Object.class);
            PropertyDescriptor[] props = binfo.getPropertyDescriptors();

            for (int i = 0; i < props.length; i++) {
                Class ptype = props[i].getPropertyType();
                String pname = props[i].getName();
                if (trfields.containsKey(pname) || ! allfields.containsKey(pname)) {
                    continue;
                }
                SchemaField f = new SchemaField(pname, ptype);
                
                if (f != null) {
                    schema.getFields().add(f);
                    f.setField(allfields.get(pname));
                    if ( props[i].getWriteMethod() == null ) {
                        f.hasNoWriteMethod = true;
                    }
                    try {
                        f.declaredFinal = Modifier.isFinal(clazz.getDeclaredField(pname).getModifiers());
                    } catch(Exception e) {}
                }
            }//for

        } catch (IntrospectionException ex) {
        }
        return schema;
    }
    
}
