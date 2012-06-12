package org.document;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

/**
 *
 * @author V. Shyshkin
 */
public interface PropertyStore extends Serializable {
    
    Object getAlias();
    
    Object get(Object key);

    void put(Object key, Object value);

    
    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    
    //void addDocumentChangeListener(DocumentChangeListener listener);

    //void removeDocumentChangeListener(DocumentChangeListener listener);
    
    public static class Alias {
        protected Object clazz;
        protected String subAlias;
        public Alias(Object clazz, String subAlias) {
            this.clazz = clazz;
            this.subAlias = subAlias;
        }

        public Object getClazz() {
            return clazz;
        }

        public String getSubAlias() {
            return subAlias;
        }

        public void setClazz(Object clazz) {
            this.clazz = clazz;
        }

        public void setSubAlias(String subAlias) {
            this.subAlias = subAlias;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + (this.clazz != null ? this.clazz.hashCode() : 0);
            hash = 67 * hash + (this.subAlias != null ? this.subAlias.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Alias other = (Alias) obj;
            if (this.clazz != other.clazz && (this.clazz == null || !this.clazz.equals(other.clazz))) {
                return false;
            }
            if ((this.subAlias == null) ? (other.subAlias != null) : !this.subAlias.equals(other.subAlias)) {
                return false;
            }
            return true;
        }
        
    }
    
}
