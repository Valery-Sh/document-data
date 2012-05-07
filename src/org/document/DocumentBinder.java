/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

/**
 *
 * @author Valery
 */
public interface DocumentBinder<T extends PropertyBinder> extends Binder,BinderListener, BinderSet<T>, HasDocumentAlias{
    
}
