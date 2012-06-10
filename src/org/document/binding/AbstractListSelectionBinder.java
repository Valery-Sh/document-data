package org.document.binding;

import java.util.List;
import org.document.Document;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractListSelectionBinder extends AbstractEditablePropertyBinder {

    public AbstractListSelectionBinder(Object boundObject) {
        super(boundObject);
        this.boundProperty = "selected";
        initBinder();
    }

    protected List<Document> getDocuments() {
        if (document == null) {
            return null;
        }
        
        return ((BindingState) document).getDocumentList();
    }

    protected final void initBinder() {
        removeBoundObjectListeners();
        addBoundObjectListeners();
    }

    @Override
    public void addBoundObjectListeners(){
    }

    @Override
    public void removeBoundObjectListeners() {
    }

    protected abstract int getComponentSelectedIndex();

    protected abstract void setComponentSelectedIndex(int index);

    @Override
    public void setComponentValue(Object value) {
        setComponentSelectedIndex((Integer) value);
    }

    @Override
    public Object getComponentValue() {
        return getComponentSelectedIndex();
    }
    
        @Override
        protected Object componentValueOf(Object dataValue) {
            Document doc = (Document) dataValue;
            if (doc == null) {
                return -1;
            }
            return getDocuments().indexOf(doc);
        }
       @Override
        protected Object propertyValueOf(Object compValue) {
            int index = (Integer) compValue;
            if (index < 0) {
                return null;
            }
            return getDocuments().get(index);
        }

        @Override
        public void initBoundObjectDefaults() {
        }
 
/*        protected Object componentValueOf(Object dataValue) {
            E doc = (E) dataValue;
            if (doc == null) {
                return -1;
            }
            return getDocuments().indexOf(doc);
        }
  */  
    //
    // ==============================================
    //
}
