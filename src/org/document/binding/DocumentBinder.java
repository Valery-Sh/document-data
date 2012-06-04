package org.document.binding;

/**
 *
 * @author V. Shyskin
 */
public class DocumentBinder<T extends PropertyBinder>  extends AbstractDocumentBinder {

    protected DocumentBinder() {
        super();
    }

    @Override
    protected DocumentBinder create() {
        return new DocumentBinder();
    }
    
    @Override
    public void addBinderListener(BinderListener l) {
    }

    @Override
    public void removeBinderListener(BinderListener l) {
    }

}
