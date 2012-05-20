package org.document.binding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.document.DocumentChangeEvent;

/**
 *
 * @author V. Shyshkin
 */
public abstract class AbstractMultiErrorBinder extends AbstractErrorBinder {

    private Set<String> propertySet;
    protected List<String> errorProperties;
    protected List<Exception> exceptions;

    public AbstractMultiErrorBinder() {
        propertyName = "*";
        propertySet = new HashSet<String>();
        errorProperties = new ArrayList<String>();
        exceptions = new ArrayList<Exception>();
    }

    public Set<String> getPropertySet() {
        return propertySet;
    }

    @Override
    public String getPropertyName() {
        return "*";
    }

    @Override
    public void notifyError(Exception e) {
    }

    public void notifyError(String property, Exception e) {
        if (property == null) {
            errorProperties.clear();
            exceptions.clear();
            exception = null;
            setVisible(false);
            propertyChanged(false);
        }
        if (!propertySet.contains(property)) {
            return;
        }
        if (e == null) {
            int idx = errorProperties.indexOf(property);
            if (errorProperties.contains(property)) {
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
