package org.document;

import java.util.Map;

/**
 *
 * @author V. Shyshkin
 */
public class MapDocument implements Document {
    
    protected DefaultDocumentStore documentStore;
    
    private Map map;
    
    public MapDocument(Map map) {
        this.documentStore = new DefaultDocumentStore(this);
        this.map = map;
    }
    
    public Map getMap() {
        return map;
    }

    @Override
    public DocumentStore getDocumentStore() {
        return this.documentStore;
    }
    
    
}
