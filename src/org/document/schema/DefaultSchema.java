/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.schema;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Valery
 */
public class DefaultSchema implements DocumentSchema{
    private Class mappingType;
    private List<SchemaField> fields;
    private PropertyChangeAccessors propertyChangeAccessors;
    
    public DefaultSchema() {
        fields = new ArrayList(10);
    }
    public DefaultSchema(Class mappingType ) {
        this();
        this.mappingType = mappingType;
        propertyChangeAccessors = new ReflectPropertyChangeAccessors();
        create(mappingType, Object.class);
    }    

    @Override
    public PropertyChangeAccessors getPropertyChangeAccessors() {
        return propertyChangeAccessors;
    }

    public void setPropertyChangeAccessors(PropertyChangeAccessors propertyChangeAccessors) {
        this.propertyChangeAccessors = propertyChangeAccessors;
    }
    
    @Override
    public Class getMappingType() {
        return this.mappingType;
    }

    @Override
    public List<SchemaField> getFields() {
        return this.fields;
    }

    @Override
    public SchemaField getField(Object fieldName) {
        for ( SchemaField f : fields) {
            if ( f.getPropertyName().equals(fieldName)) {
                return f;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DefaultSchema other = (DefaultSchema) obj;
        if (this.mappingType != other.mappingType && (this.mappingType == null || !this.mappingType.equals(other.mappingType))) {
            return false;
        }
        if (this.fields != other.fields && (this.fields == null || !this.fields.equals(other.fields))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + (this.mappingType != null ? this.mappingType.hashCode() : 0);
        hash = 13 * hash + (this.fields != null ? this.fields.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        String s = "";
        s += "================================================\n";  
        s += "mappingType: " + mappingType.getName() + "\n";      
        List<SchemaField> fl = getFields();
        int n = 0;
        for( SchemaField f : fl ) {
            s += (n++) + "). propertyName = " + f.propertyName + "; \n";
            s += "      propertyType = " + f.propertyType + "; \n";
            s += "      calculated = " + f.calculated + "; \n";
            s += "      declaredFinal = " + f.declaredFinal + "; \n";            
            s += "      hasNoWriteMethod = " + ! f.getAccessors().hasSetter() + "; \n";            
            s += "      notNull = " + f.notNull + "; \n";            
            s += "      readOnly = " + f.readOnly + "; \n";                        
            s += "      required = " + f.required + "; \n";                        
            s += "      tail = " + f.tail + "; \n";                        
        }
        s += "================================================\n";          
        
        return s;
    }

    @Override
    public boolean contains(Object fieldName) {
        for ( SchemaField f : fields) {
            if ( f.getPropertyName().equals(fieldName) ) {
                return true;
            }
        }
        return false;
    }
    
    protected void create(Class clazz, Class superBoundary) {
        Map<String, Boolean> trfields = new HashMap<String, Boolean>();
        Map<String, Field> allfields = new HashMap<String, Field>();
        Class c = clazz;
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
            if (!contains(pname)) {
                Class ptype = isGetter ? m.getReturnType() : m.getParameterTypes()[0];
                f = new SchemaField(pname, ptype);
                getFields().add(f);
                f.setField(allfields.get(pname));
                f.accessors = new ReflectAccessor(null, null);
            } else {
                f = getField(pname);
            }
            
            if ( isGetter ) {
                ((ReflectAccessor)f.getAccessors()).setGetAccessor(m);
            } else {
                ((ReflectAccessor)f.getAccessors()).setSetAccessor(m);
            }
        }
        ((ReflectPropertyChangeAccessors)getPropertyChangeAccessors())
                .setAddPropertyChangeListener(addPropertyChangeListener);
        ((ReflectPropertyChangeAccessors)getPropertyChangeAccessors())
                .setAddBoundPropertyChangeListener(addBoundPropertyChangeListener);
        

    }
    
}
