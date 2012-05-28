/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.swing.binders;

import java.util.List;
import javax.swing.JComboBox;
import org.document.Document;

/**
 *
 * @author Valery
 */
public class JLookupComboBox extends JComboBox {

    private String targetProperty;
    //private List<Document> modelSource;
    private String[] displayProperties;

    public JLookupComboBox() {
        super();
    }
    

    public JLookupComboBox(String targetProperty) {
        this(targetProperty,null);
    }

    public JLookupComboBox(String targetProperty, List<? extends Document> modelSource) {
        if ( modelSource != null ) {
            this.setModel(new DocumentComboBoxModel(modelSource));
        }
        this.targetProperty = targetProperty;
    }


    public String getTargetProperty() {
        return targetProperty;
    }

    public void setTargetProperty(String targetProperty) {
        this.targetProperty = targetProperty;
    }

    public void setModel(List<? extends Document> modelSource) {
        setModel(new DocumentComboBoxModel(modelSource));
    }

    public String[] getDisplayProperties() {
        return displayProperties;
    }

    public void setDisplayProperties(String... displayProperties) {
        this.displayProperties = displayProperties;
        if ( displayProperties != null && displayProperties.length > 0 ) {
             setRenderer(new DocumentListCellRenderer(displayProperties));
        }        
        
    }
}
