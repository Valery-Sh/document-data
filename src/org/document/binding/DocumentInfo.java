package org.document.binding;

/**
 *
 * @author V. Shyshkin
 */
public class DocumentInfo {
    private int index; // the index at the DocumentList
    private Object alias; // the index at the DocumentList
    private Class clazz; // the index at the DocumentList
    private int size;
    //private int size;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Object getAlias() {
        return alias;
    }

    public void setAlias(Object alias) {
        this.alias = alias;
    }


    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    
}
