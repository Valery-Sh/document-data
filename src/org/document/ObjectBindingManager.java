package org.document;

import java.util.List;

/**
 *
 * @author V. Shyshkin
 */
public class ObjectBindingManager<T extends HasDocument> extends AbstractBindingManager{
    
    protected List<T> sourceList;
    protected T selected;
    
    public ObjectBindingManager(List<T> sourceList) {
        super();
        this.sourceList = sourceList;
    }
    public T getSelected() {
        return selected;
    }
    public void setSelected(T selected) {
        this.selected = selected;
        this.setDocument(selected.getDocument());
    }
    
}
