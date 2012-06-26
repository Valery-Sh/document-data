package org.document;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.document.schema.DefaultSchema;
import org.document.schema.DocumentSchema;

/**
 *
 * @author V.Shyshkin
 */
public class Registry {
    
    private static final Map<Class,DocumentSchema> schemas = Collections.synchronizedMap( new HashMap<Class,DocumentSchema>() );    
    
    /**
     * Returns an existing <code>DocumentSchema</code> instance for the 
     * specified class, or creates a new one.
     * @param c the class which schema to be returned
     * @return the object of type {@link org.document.schema.DocumentSchema}
     */
    public static DocumentSchema getSchema( Class c ){
        DocumentSchema s = schemas.get( c );
        if ( s == null ){
            s = new DefaultSchema( c );
            schemas.put( c , s );
        }
        return s;
    }
    public static DocumentSchema getSchema( Object o ){
        return o != null ? getSchema(o.getClass()) : null;
    }    
    
    
}
