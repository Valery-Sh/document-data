package org.document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author V. Shishkin
 */
public class BindingManager<T extends HasDocument> extends AbstractBindingManager {

    protected List<T> sourceList;
    protected T selected;
    
    public BindingManager(List<T> sourceList) {
        super();
        this.sourceList = sourceList;
    }
    public T getSelected() {
        return selected;
    }
    public void setSelected(T selected) {
        this.setDocument(selected.getDocument());
        this.selected = selected;
    }

}//class BindingManager
