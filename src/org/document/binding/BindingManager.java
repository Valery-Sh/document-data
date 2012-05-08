package org.document.binding;

import java.util.List;
import org.document.Document;

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