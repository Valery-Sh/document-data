
package org.document.schema;

import org.document.Document;

/**
 *
 * @author Valery
 */
public interface DocumentGroup {
//    Document create();
//    Document create(DocumentSchema schema);    
//    ObjectDocument create(Object javaBean);
//    void addSchema(DocumentSchema schema);
//    DocumentSchema createSchema(Class clazz);
//    DocumentSchema createSchema(Map map);
    DocumentSchema getSchema(Document doc);
    
}
