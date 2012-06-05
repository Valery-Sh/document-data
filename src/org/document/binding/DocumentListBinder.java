package org.document.binding;

import java.util.ArrayList;
import java.util.List;
import org.document.Document;
import org.document.DocumentList;
import org.document.samples.Department;
import org.document.samples.Person;

/**
 *
 * @author V. Shyshkin
 */
public abstract class DocumentListBinder<E extends Document> extends BindingStateBinder<PropertyBinder> {
    //protected BindingState bindi
    //private List<? extends Document> list;
    private List list;
    public DocumentListBinder() {
        super();
//        list = new ArrayList<Department>();
        
        
    }

    public DocumentListBinder(Object component) {
        super(component);
    }
    
/*    @Override
    public List<? extends Document> getList() {
        return list;
    }

    @Override
    public void setList(List<? extends Document> list) {
        BindingState bs = new BindingState();
        DocumentList dl = new DocumentList(list);
        bs.setDocumentList(dl);
        setDocument(bs);
        this.list = list;
    }
*/

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        BindingState bs = new BindingState();
        DocumentList dl = new DocumentList(list);
        bs.setDocumentList(dl);
        setDocument(bs);
        this.list = list;
    }
      
}
