package org.document;

import java.util.List;

/**
 *
 * @author V.Shyshkin
 */
public class Documents {
    public static Document create(Object o) {
        return new DefaultDocument(o);
    }
    public static List<Document> create(List list) {
        if ( list == null ) {
            return null;
        }
        List l = null;
        try {
            l = list.getClass().newInstance();
            for ( Object o : list ) {
                l.add(create(o));
            }
        } catch(Exception e) {
            throw new IllegalArgumentException("Cannot create a list of documents by the specified list  " + list );
        }
        return l;
    }    
}
