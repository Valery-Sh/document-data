package org.document;

import java.util.Map;

/**
 *
 * @author V. Shyshkin
 */
public class KeyValueMap implements Document {
    
    protected DocumentPropertyStore documentStore;
    protected Object alias;
    
    private Map<String,Object> map;
    
    public KeyValueMap(Map<String,Object> map) {
        alias = "default";
        this.documentStore = new DocumentPropertyStore(this);
        this.documentStore.setAlias(alias);
        this.map = map;
    }
    public KeyValueMap(Map<String,Object> map, Object alias) {
        this(map);
        this.documentStore.setAlias(alias);
        this.map = map;
    }

    public Object getAlias() {
        return alias;
    }
    public void setAlias(Object alias) {
        this.alias = alias;
    }
    
    public Map<String,Object> getMap() {
        return map;
    }

    @Override
    public PropertyStore propertyStore() {
        return this.documentStore;
    }
    
    public Object get(String propertyName) {
        return map.get(propertyName);
    }
    
    public void put(String propertyName, Object value) {
        map.put(propertyName,value);
        documentStore.bind(propertyName, value);
    }
    
}
