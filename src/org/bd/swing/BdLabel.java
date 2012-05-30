package org.bd.swing;

import java.io.Serializable;
import javax.swing.JLabel;
import org.document.binding.Binder;
import org.document.binding.HasBinder;
import org.document.binding.PropertyBinder;
import org.document.swing.binders.BdLabelBinder;
/**
 *
 * @author V. Shyshkin
 */
public class BdLabel extends JLabel implements Serializable, HasBinder{
    
//    public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";
    private String documentAlias;
    private PropertyBinder binder;
    private String propertyName;
    
    public BdLabel() {
        super();
    }


    public String getDocumentAlias() {
        return documentAlias;
    }

    public void setDocumentAlias(String documentAlias) {
        this.documentAlias = documentAlias;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
        if ( binder != null ) {
            binder.setPropertyName(propertyName);
        }
    }

    @Override
    public Binder getBinder() {
        if ( this.binder == null ) {
            binder = new BdLabelBinder(this);
        }
        if ( propertyName != null ) {
            binder.setPropertyName(propertyName);
        }
        return binder;
    }
    
    
}
