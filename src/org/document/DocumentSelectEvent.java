/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.util.EventObject;

/**
 *
 * @author Valery
 */
public class DocumentSelectEvent extends EventObject{
    
    private Document oldDocument;
    private Document newDocument;
    
    public DocumentSelectEvent(Object source, Document oldDocument, Document newDocument) {
        super(source);
        this.oldDocument = oldDocument;
        this.newDocument = newDocument;
    }

    public Document getNewDocument() {
        return newDocument;
    }

    public void setNewDocument(Document newDocument) {
        this.newDocument = newDocument;
    }

    public Document getOldDocument() {
        return oldDocument;
    }

    public void setOldDocument(Document oldDocument) {
        this.oldDocument = oldDocument;
    }
    
    
}
