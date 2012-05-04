package org.document;

import java.util.Map;

/**
 *
 * @author V. Shyshkin
 */
public class MapDocument implements Document {
    
    protected BeanBasedDocument document;
    
    private Map map;
    
    public MapDocument(Map map) {
        this.document = new BeanBasedDocument(this);
        this.map = map;
    }
    
    public Map getMap() {
        return map;
    }

    @Override
    public DocumentStore getDocumentStore() {
        return this.document;
    }
    
    
}
