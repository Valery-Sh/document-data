package org.document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author V. Shishkin
 */
public class BindingManager extends AbstractBindingManager {

    protected List<HasDocument> sourceList;
    protected HasDocument selected;
    
    public BindingManager(List<HasDocument> sourceList) {
        super();
        this.sourceList = sourceList;
    }
    public HasDocument getSelected() {
        return selected;
    }
    public void setSelected(HasDocument selected) {
        this.setDocument(selected.getDocument());
        this.selected = selected;
    }

}//class BindingManager
