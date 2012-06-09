package org.document.binding;

import org.document.Document;

/**
 *
 * @author V. Shyskin
 */
public class DocumentBinder<E extends Document> extends AbstractDocumentBinder<Document> {
    
    private String documentClassName;
    private String alias;

    protected DocumentBinder() {
        super();
    }
    
    @Override
    public E getDocument() {
        return (E)super.getDocument();
    }
    protected boolean accept(E document) {
        boolean b;
        if ( document == null || "default".equals(alias) ) {
            b = true;
        } else  if ( document.getClass().getName().equals(documentClassName)) {
            b = true;
            if ( document.propertyStore().getAlias() != null ) {
                String psa = document.propertyStore().getAlias().toString();
                if ( ! psa.equals(alias)) {
                    b = false;
                }
            }
        } else {
            b = acceptSuper(document);
        }
        if ( b ) {
           super.setDocument(document);
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
        if ( document == null || "default".equals(alias) ) {
            b = true;
        } else  if ( document.getClass().getName().equals(documentClassName)) {
            b = true;
            if ( document.propertyStore().getAlias() != null ) {
                String psa = document.propertyStore().getAlias().toString();
                if ( ! psa.equals(alias)) {
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
    
    @Override
    public void setDocument(Document document) {
        accept((E)document);
    }

    public String getDocumentClassName() {
        return documentClassName;
    }

    public void setDocumentClassName(String documentClassName) {
        this.documentClassName = documentClassName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    
}
