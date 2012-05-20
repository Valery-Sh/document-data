/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.schema;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Valery
 */
public class DefaultSchema implements DocumentSchema{
    private Class mappingType;
    private List<Field> fields;
    
    public DefaultSchema() {
        fields = new ArrayList(10);
    }
    public DefaultSchema(Class mappingType ) {
        this();
        this.mappingType = mappingType;
    }    
    
    @Override
    public Class getMappingType() {
        return this.mappingType;
    }

    @Override
    public List getFields() {
        return this.fields;
    }

    @Override
    public Field getField(Object fieldName) {
        for ( Field f : fields) {
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
        List<Field> fl = getFields();
        int n = 0;
        for( Field f : fl ) {
            s += (n++) + "). propertyName = " + f.propertyName + "; \n";
            s += "      propertyType = " + f.propertyType + "; \n";
            s += "      calculated = " + f.calculated + "; \n";
            s += "      declaredFinal = " + f.declaredFinal + "; \n";            
            s += "      hasNoWriteMethod = " + f.hasNoWriteMethod + "; \n";            
            s += "      notNull = " + f.notNull + "; \n";            
            s += "      readOnly = " + f.readOnly + "; \n";                        
            s += "      required = " + f.required + "; \n";                        
            s += "      tail = " + f.tail + "; \n";                        
        }
        s += "================================================\n";          
        
        return s;
    }
}
