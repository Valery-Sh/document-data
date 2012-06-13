/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import java.util.EventObject;
import org.document.Document;
import org.document.DocumentList;

/**
 *
 * @author Valery
 */
public class ContextEvent extends EventObject{
    private ContextEvent.Action action;
    private Document oldSelected;
    private Document newSelected;
    private DocumentList oldDocumentList;
    private DocumentList newDocumentList;
    private boolean active;
    
    public ContextEvent(BindingContext source, Action action) {
        super(source);
        this.action = action;
    }

    public ContextEvent.Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Document getOldSelected() {
        return oldSelected;
    }

    public void setOldSelected(Document oldValue) {
        this.oldSelected = oldValue;
    }

    public Document getNewSelected() {
        return newSelected;
    }

    public void setNewSelected(Document newValue) {
        this.newSelected = newValue;
    }

    public DocumentList getOldDocumentList() {
        return oldDocumentList;
    }

    public void setOldDocumentList(DocumentList oldDocumentList) {
        this.oldDocumentList = oldDocumentList;
    }

    public DocumentList getNewDocumentList() {
        return newDocumentList;
    }

    public void setNewDocumentList(DocumentList newDocumentList) {
        this.newDocumentList = newDocumentList;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public enum Action {
        documentChange,
        documentChanging,
        activeStateChange,
        documentListChange,
        updateContext,
        register,
        unregister
    }
}//class ContextEvent

