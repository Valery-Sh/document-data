package org.document.schema;

import java.util.List;

/**
 *
 * @author V. Shyshkin
 */
public interface DocumentSchema<K> {
    Class getMappingType();
    List<Field> getFields();
    Field getField(Object fieldName);
}
