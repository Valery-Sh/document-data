package org.document;

import java.util.List;

/**
 *
 * @author V. Shishkin
 */
public class BindingManagerOld<T extends Document> extends AbstractBindingManagerOld {

    protected List<T> sourceList;
    
    public BindingManagerOld(List<T> sourceList) {
        super();
        this.sourceList = sourceList;
    }

}