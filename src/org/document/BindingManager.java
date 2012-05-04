package org.document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author V. Shishkin
 */
public class BindingManager<T extends ObjectDocument> extends AbstractBindingManager {

    protected List<T> sourceList;
    
    public BindingManager(List<T> sourceList) {
        super();
        this.sourceList = sourceList;
    }

}//class BindingManager
