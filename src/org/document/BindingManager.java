package org.document;

import java.util.List;

/**
 *
 * @author V. Shishkin
 */
public class BindingManager<T extends Document> extends AbstractBindingManager {

    protected List<T> sourceList;
    
    public BindingManager(List<T> sourceList) {
        super();
        this.sourceList = sourceList;
    }

}