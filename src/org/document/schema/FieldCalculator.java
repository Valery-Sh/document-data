package org.document.schema;

import org.document.Document;

/**
 *
 * @author V. Shyshkin
 */
public interface FieldCalculator {
    Object getValue(Document doc);
}
