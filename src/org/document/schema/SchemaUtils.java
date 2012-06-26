package org.document.schema;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 *
 * @author Valery
 */
public class SchemaUtils {

    public static DocumentSchema createSchema(Class clazz, Class superBoundary) {
        DocumentSchema schema;
        schema = new DefaultSchema(clazz);
        Map<String, Boolean> trfields = new HashMap<String, Boolean>();
        Map<String, Field> allfields = new HashMap<String, Field>();
        Class c = clazz;
System.out.println("---- CLASS: " + clazz.getName());        
System.out.println("---- before1: " + (new Date()).getTime());        
        try {

            while (c != superBoundary) {
                java.lang.reflect.Field[] dfs = c.getDeclaredFields();
                for (java.lang.reflect.Field f : dfs) {

                    String nm = f.getName();
                    allfields.put(nm, f);
                    if (Modifier.isTransient(f.getModifiers())) {
                        trfields.put(f.getName(), Boolean.TRUE);
                    }
                }
                c = c.getSuperclass();
            }

        } catch (Exception e) {
        }
System.out.println("---- after0 : " + (new Date()).getTime());
        
        c = clazz;
        Method addPropertyChangeListener = null;
        Method addBoundPropertyChangeListener = null;
        
        for (Method m : c.getMethods()) {
            if ( m.getName().equals("addPropertyChangeListener")) {
                if ( addPropertyChangeListener == null ) {
                    //consider the upper sup class
                    addPropertyChangeListener = m;
                }
                continue;
            }
            if ( m.getName().equals("addBoundPropertyChangeListener")) {
                if ( addBoundPropertyChangeListener == null ) {
                    //consider the upper sup class
                    addBoundPropertyChangeListener = m;
                }
                continue;
            }
            
            if (!(m.getName().startsWith("is") || m.getName().startsWith("get") || m.getName().startsWith("set"))) {
                continue;
            }
            int idx = 3;
            boolean isGetter = false;
            if (m.getName().startsWith("get")) {
                isGetter = true;
            } else if (m.getName().startsWith("is")) {
                idx = 2;
                isGetter = true;
            }

            String name = m.getName().substring(idx);
            if (name.length() == 0) {
                continue;
            }
            String pname = name.substring(0, 1).toLowerCase() + name.substring(1);

            if (trfields.containsKey(pname) || !allfields.containsKey(pname)) {
                continue;
            }
            SchemaField f = null;
            if (!schema.contains(pname)) {
                Class ptype = isGetter ? m.getReturnType() : m.getParameterTypes()[0];
                f = new SchemaField(pname, ptype);
                schema.getFields().add(f);
                f.setField(allfields.get(pname));
                f.accessors = new ReflectAccessor(null, null);
            } else {
                f = schema.getField(pname);
            }
            
            if ( isGetter ) {
                ((ReflectAccessor)f.getAccessors()).setGetAccessor(m);
            } else {
                ((ReflectAccessor)f.getAccessors()).setSetAccessor(m);
            }
        }
        ((ReflectPropertyChangeAccessors)schema.getPropertyChangeAccessors())
                .setAddPropertyChangeListener(addPropertyChangeListener);
        ((ReflectPropertyChangeAccessors)schema.getPropertyChangeAccessors())
                .setAddBoundPropertyChangeListener(addBoundPropertyChangeListener);
        

        System.out.println("---- after1 : " + (new Date()).getTime());
/*        try {
            BeanInfo binfo = Introspector.getBeanInfo(clazz, Object.class);
            PropertyDescriptor[] props = binfo.getPropertyDescriptors();

            for (int i = 0; i < props.length; i++) {
                Class ptype = props[i].getPropertyType();
                String pname = props[i].getName();
                if (trfields.containsKey(pname) || !allfields.containsKey(pname)) {
                    continue;
                }
                SchemaField f = new SchemaField(pname, ptype);

                if (f != null) {
                    schema.getFields().add(f);
                    f.setField(allfields.get(pname));
                    Method read;
                    Method write;
                    write = props[i].getWriteMethod();
                    read = props[i].getReadMethod();
                    f.accessors = new ReflectAccessor(read, write);
                    if (write == null) {
                        f.hasNoWriteMethod = true;
                    }

                    try {
                        f.declaredFinal = Modifier.isFinal(clazz.getDeclaredField(pname).getModifiers());
                    } catch (Exception e) {
                    }
                }
            }//for

        } catch (IntrospectionException ex) {
        }
        */ 
        //System.out.println("---- after2 : " + (new Date()).getTime());

        return schema;
    }

    public static SchemaField createField(String property, Class clazz, Class superBoundary) {
        Class c = clazz;
        boolean ok = true;
        java.lang.reflect.Field f = null;
        while (ok) {
            try {
                f = c.getDeclaredField(property);
            } catch (Exception ex) {
            }
            ok = c != superBoundary;
            c = c.getSuperclass();
        }
        if (f == null) {
            return null;
        }
        SchemaField result = null;
        try {
            BeanInfo binfo = Introspector.getBeanInfo(clazz, Object.class);
            PropertyDescriptor[] props = binfo.getPropertyDescriptors();

            for (int i = 0; i < props.length; i++) {
                Class ptype = props[i].getPropertyType();
                String pname = props[i].getName();
                if (pname.equals(property)) {
                    result = new SchemaField(pname, ptype);

                    result.setField(f);
                    Method read;
                    Method write;
                    write = props[i].getWriteMethod();
                    read = props[i].getReadMethod();
                    result.accessors = new ReflectAccessor(read, write);

                }
            }//for

        } catch (IntrospectionException ex) {
            result = null;
        }
        return result;
    }
}
