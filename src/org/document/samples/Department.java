package org.document.samples;

import org.document.AbstractObjectDocument;

/**
 *
 * @author V. Shyshkin
 */
public class Department extends AbstractObjectDocument {
    int id;
    String name;
    
    public Department() {
        super();
    }
    public Department(int id,String name) {
        super();
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        bind("id",id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        bind("name",name);
    }
    
}
