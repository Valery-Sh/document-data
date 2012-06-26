package org.document.schema;

import java.util.List;

/**
 *
 * @author V. Shyshkin
 */
public interface DocumentSchema {
    Class getMappingType();
    List<SchemaField> getFields();
    SchemaField getField(Object fieldName);
    boolean contains(Object fieldName);
    PropertyChangeAccessors getPropertyChangeAccessors();
}
