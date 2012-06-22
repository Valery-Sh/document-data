package org.document.schema;

import java.lang.reflect.Field;
import java.util.List;

/**
 * When create an instance of the class 
 * using <code>DocUtils.createSchema(Class)</code> 
 * then a <code>SchemaType</code> is added to a list of supported 
 * types of that instance. Thus the field has at least one 
 * supported type registered.
 * 
 * 
 * @author V. Shyshkin
 */
public class SchemaField {
    protected Object propertyName;
    protected boolean required;
    protected boolean notNull;

    protected Class propertyType;
    protected boolean tail;
    protected boolean readOnly;
    protected boolean calculated;
    protected boolean declaredFinal;
    protected boolean hasNoWriteMethod;
    
    protected FieldCalculator calculator;
    
    protected Field field;
    
    public SchemaField(Object name) {
        assert(name != null);
        this.propertyName = name;
        this.propertyType = Object.class;
    }
    public SchemaField(Object name,Class clazz){//,Class ... supported) {
        this(name);
        this.propertyType = clazz;
    }
    
    public SchemaField(Object name,Class clazz,boolean required,boolean notNull){//,Class ... supported) {
        this(name,clazz);
        this.required = required;
        this.notNull = notNull;
    }

    public boolean isCalculated() {
        return calculated;
    }

    public FieldCalculator getCalculator() {
        return calculator;
    }

    public void setCalculator(FieldCalculator calculator) {
        this.calculator = calculator;
    }


    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    public boolean isReadOnly() {
        if ( calculated || declaredFinal || hasNoWriteMethod ) {
            return true;
        }
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isTail() {
        return tail;
    }

    public void setTail(boolean tail) {
        this.tail = tail;
    }

    public Object getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(Object propertyName) {
        this.propertyName = propertyName;
    }

    
    public Class getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(Class propertyType) {
        this.propertyType = propertyType;
    }

    public boolean isRequired() {
        return this.required;
    }

    public boolean isNotNull() {
        return this.notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SchemaField other = (SchemaField) obj;
        if (this.propertyName != other.propertyName && (this.propertyName == null || !this.propertyName.equals(other.propertyName))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.propertyName != null ? this.propertyName.hashCode() : 0);
        hash = 59 * hash + (this.required ? 1 : 0);
        hash = 59 * hash + (this.notNull ? 1 : 0);
        hash = 59 * hash + (this.propertyType != null ? this.propertyType.hashCode() : 0);
        hash = 59 * hash + (this.tail ? 1 : 0);
        hash = 59 * hash + (this.readOnly ? 1 : 0);
        hash = 59 * hash + (this.calculated ? 1 : 0);
        hash = 59 * hash + (this.declaredFinal ? 1 : 0);
        hash = 59 * hash + (this.hasNoWriteMethod ? 1 : 0);
        hash = 59 * hash + (this.calculator != null ? this.calculator.hashCode() : 0);
        return hash;
    }

    
}
