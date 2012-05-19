package org.document.samples;

import java.util.Date;
import org.document.Document;
import org.document.DocumentPropertyStore;
import org.document.PropertyStore;

/**
 *
 * @author V. Shyshkin
 */
public class Person implements Document {

    protected DocumentPropertyStore document;

    public Person() {
        // DocumentPropertyStore is a default PropertyStore
        this.document = new DocumentPropertyStore(this);
    }

    public Person(int id, String firstName, String lastName) {
        this();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    //
    // Document interface implementation
    //
    @Override
    public PropertyStore getPropertyStore() {
        return this.document;
    }
    //
    // ===================================================
    //
    private int id;
    private String firstName;
    private String lastName;
    private Date birthDay;

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
        document.put("birthDay", birthDay);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        document.validate("firstName", firstName);
        //document.set("firstName", firstName);
        this.firstName = firstName;        
        document.set("firstName", firstName);
        
        
        
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        document.put("id", id);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
//        this.lastName = lastName;
//        document.set("lastName", lastName);
        this.lastName = lastName;
        document.set("lastName", lastName);
    }
}
