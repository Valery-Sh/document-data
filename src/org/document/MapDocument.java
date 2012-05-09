package org.document;

import java.util.Map;

/**
 *
 * @author V. Shyshkin
 */
public class MapDocument implements Document {
    
    protected DocumentPropertyStore documentStore;
    protected Object typeId;
    
    private Map map;
    
    public MapDocument(Map map) {
        this.documentStore = new DocumentPropertyStore(this);
        this.map = map;
    }
    public MapDocument(Map map, Object typeId) {
        this.documentStore = new DocumentPropertyStore(this);
        this.map = map;
    }

    public Object getTypeId() {
        return typeId;
    }
    
    public Map getMap() {
        return map;
    }

    @Override
    public PropertyStore getPropertyStore() {
        return this.documentStore;
    }
    
    
}
