package org.document.binding;

import java.util.*;
import javax.swing.JLabel;
import org.document.DocumentChangeEvent;
import org.document.ValidationException;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractMultiErrorBinder extends AbstractErrorBinder {
    protected String propertyWithError;
    private Set<String> propertySet;
    protected List<String> errorProperties;
    protected List<Exception> exceptions;
    /**
     * Indicates that all properties will use the binder
     */
    protected boolean allProperties;
    
    public AbstractMultiErrorBinder() {
        propertyName = "*";
        propertySet = new HashSet<String>();
        errorProperties = new ArrayList<String>();
        exceptions = new ArrayList<Exception>();
    }

    public AbstractMultiErrorBinder(String... propertyNames) {
        this();
        getPropertySet().addAll(Arrays.asList(propertyNames));
    }   
    public AbstractMultiErrorBinder(boolean allProperies) {
        this();
        this.allProperties = allProperies;
    }   
    
    public final Set<String> getPropertySet() {
        return propertySet;
    }

    @Override
    public String getPropertyName() {
        return "*";
    }

    @Override
    public void notifyError(ValidationException e) {
    }

    public void notifyError(String property, Exception e) {
        propertyWithError = property;
        if (property == null) {
            errorProperties.clear();
            exceptions.clear();
            exception = null;
            setVisible(false);
            propertyChanged(false);
        }
        if ( (! allProperties) && !propertySet.contains(property)){
            return;
        }
        if (e == null) {
            int idx = errorProperties.indexOf(property);
            if (idx >= 0) {
                errorProperties.remove(idx);
                exceptions.remove(idx);
            }
            if (errorProperties.isEmpty()) {
                exception = null;
                setVisible(false);
                propertyChanged(false);
            } else {
                exception = exceptions.get(exceptions.size() - 1);
                setVisible(true);
                propertyChanged(true);
            }
        } else {
            errorProperties.add(property);
            exceptions.add(e);
            exception = e;
            setVisible(true);
            propertyChanged(true);
        }
    }

    @Override
    public void react(DocumentChangeEvent event) {
        switch (event.getAction()) {
            case documentError:
                this.notifyError(event.getException());
                break;
            case documentChange:
                //notifyError(null, null);
                break;
            case documentChanging:
                notifyError(null, null);
                break;
                
            case propertyError:
                if (event.getPropertyName() == null) {
                    break;
                }
                notifyError(event.getPropertyName(), event.getException());
                break;
            case propertyChange:
                if (event.getPropertyName() == null) {
                    break;
                }
                notifyError(event.getPropertyName(), null);

        }//switch
    }

    protected abstract void setVisible(boolean visible);
}
