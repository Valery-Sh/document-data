/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document;

import java.util.HashMap;
import java.util.Map;

public class DocumentImpl implements Document {

    Map values = new HashMap();
    PropertyChangeHandler handler;

    @Override
    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException("The 'key' parameter cannot be null");
        }
        return values.get(key);
    }

    @Override
    public void put(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException("The 'key' parameter cannot be null");
        }
        Object oldValue = this.get(key);
        values.put(key, value);
        if (handler != null) {
            handler.firePropertyChange(key.toString(), oldValue, value);
        }
    }

    @Override
    public void setPropertyChangeHandler(PropertyChangeHandler handler) {
        this.handler = handler;
    }

/*    public class PropertyChangeHandlerImpl implements PropertyChangeHandler {

        protected boolean isFired;

        @Override
        public void firePropertyChange(String propPath, Object oldValue, Object newValue) {
            isFired = true;
        }
    }
*/
}