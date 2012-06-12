package org.document.binding;

import org.document.Document;

/**
 *
 * @author V. Shyskin
 */
public class DocumentBinder<E extends Document> extends AbstractDocumentBinder<Document> {
    


    protected DocumentBinder() {
        super();
    }
    
    @Override
    public E getDocument() {
        return (E)super.getDocument();
    }
    protected boolean accept(E document) {
        boolean b;
        if ( document == null || "default".equals(getAlias()) ) {
            b = true;
        } else  if ( document.getClass().getName().equals(getClassName())) {
            b = true;
            if ( document.propertyStore().getAlias() != null ) {
                String psa = document.propertyStore().getAlias().toString();
                if ( ! psa.equals(getAlias())) {
                    b = false;
                }
            }
        } else {
            b = acceptSuper(document);
        }
        if ( b ) {
 //TODO          super.documentChange(document);
        }
        return b;
    }
    /**
     * @TODO
     * @param document
     * @return 
     */
    protected boolean acceptSuper(E document) {
        return false;
    }

    public boolean canAccept(E document) {
        boolean b;
        if ( document == null || "default".equals(getAlias()) ) {
            b = true;
        } else  if ( document.getClass().getName().equals(getClassName())) {
            b = true;
            if ( document.propertyStore().getAlias() != null ) {
                String psa = document.propertyStore().getAlias().toString();
                if ( ! psa.equals(getAlias())) {
                    b = false;
                }
            }
        } else {
            b = canAcceptSuper(document);
        }
        return b;
    }
    /**
     * @TODO
     * @param document
     * @return 
     */
    protected boolean canAcceptSuper(E document) {
        return false;
    }
    

    public void documentChange(Document document) {
        accept((E)document);
    }

    @Override
    public boolean add(Binder binder) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
