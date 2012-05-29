/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.swing.binders;

import java.util.List;
import javax.swing.JComboBox;
import org.document.Document;
import org.document.binding.BindingManager;

/**
 *
 * @author Valery
 */
public class BdLookupComboBox extends JComboBox {
    
    private String documentAlias;
    
    private String targetProperty;
    //private List<Document> modelSource;
    private String[] displayProperties;
    private String lookupProperty;
    
    BdLookupComboBinder binder;
    
    private BindingManager bindingManager;
    
    public BdLookupComboBox() {
        super();
    }
    

    public BdLookupComboBox(String targetProperty) {
        this(targetProperty,null);
    }

    public BdLookupComboBox(String targetProperty, List<? extends Document> modelSource) {
        this();
        if ( modelSource != null ) {
            this.setModel(new DocumentComboBoxModel(modelSource));
            
        }
        this.targetProperty = targetProperty;
    }
    
    public List<? extends Document> getModelSource() {
        if ( getModel() instanceof  DocumentComboBoxModel)
            return ((DocumentComboBoxModel)getModel()).getDocuments();
        return null;
    }
    
    public BindingManager getBindingManager() {
        return bindingManager;
    }

    public void setBindingManager(BindingManager bindingManager) {
        BindingManager oldBindingManager = this.bindingManager;
        this.bindingManager = bindingManager;
        if ( bindingManager != null &&
             targetProperty != null &&
             displayProperties != null &&
             getModelSource() != null &&
             documentAlias != null   ) {
            if ( binder == null ) {
                binder = new BdLookupComboBinder(targetProperty,this);
                bindingManager.getDocumentBinder(documentAlias).add(binder);
            } else {
                oldBindingManager.getDocumentBinder(documentAlias).remove(binder);
                binder = new BdLookupComboBinder(targetProperty,this);
                bindingManager.getDocumentBinder(documentAlias).add(binder);
            }
        }
        
    }

    public String getDocumentAlias() {
        return documentAlias;
    }

    public void setDocumentAlias(String documentAlias) {
        String oldAlias = this.documentAlias;
        this.documentAlias = documentAlias;
        if ( bindingManager != null &&
             targetProperty != null &&
             displayProperties != null &&
             getModelSource() != null &&   
             documentAlias != null   ) {
            if ( binder == null ) {
                binder = new BdLookupComboBinder(targetProperty,this);
                bindingManager.getDocumentBinder(documentAlias).add(binder);
            } else {
                bindingManager.getDocumentBinder(oldAlias).remove(binder);
                binder = new BdLookupComboBinder(targetProperty,this);
                bindingManager.getDocumentBinder(documentAlias).add(binder);
                
            }
        }
        
    }


    public String getTargetProperty() {
        return targetProperty;
    }

    public void setTargetProperty(String targetProperty) {
//        String oldTargetProperty = this.targetProperty;
        this.targetProperty = targetProperty;
        if ( bindingManager != null &&
             targetProperty != null &&
             displayProperties != null &&
             getModelSource() != null &&   
             documentAlias != null   ) {
            if ( binder == null ) {
                binder = new BdLookupComboBinder(targetProperty,this);
                bindingManager.getDocumentBinder(documentAlias).add(binder);
            } else {
                bindingManager.getDocumentBinder(documentAlias).remove(binder);
                binder = new BdLookupComboBinder(targetProperty,this);
                bindingManager.getDocumentBinder(documentAlias).add(binder);
                
            }
        }
        
    }

    public void setModel(List<? extends Document> modelSource) {
        setModel(new DocumentComboBoxModel(modelSource));
        if ( bindingManager != null &&
             targetProperty != null &&
             displayProperties != null &&
             getModelSource() != null &&   
             documentAlias != null   ) {
            if ( binder == null ) {
                binder = new BdLookupComboBinder(targetProperty,this);
                bindingManager.getDocumentBinder(documentAlias).add(binder);
            }
        }
    }

    public String[] getDisplayProperties() {
        return displayProperties;
    }

    public void setDisplayProperties(String... displayProperties) {
        this.displayProperties = displayProperties;
        if ( displayProperties != null && displayProperties.length > 0 ) {
             setRenderer(new DocumentListCellRenderer(displayProperties));
        }        
        if ( bindingManager != null &&
             targetProperty != null &&
             displayProperties != null &&
             getModelSource() != null &&   
             documentAlias != null   ) {
            if ( binder == null ) {
                binder = new BdLookupComboBinder(targetProperty,this);
                bindingManager.getDocumentBinder(documentAlias).add(binder);
            }
        }
        
    }

    public String getLookupProperty() {
        return lookupProperty;
    }

    public void setLookupProperty(String lookupProperties) {
        this.lookupProperty = lookupProperties;
    }
    
}
