package org.document.samples;

import java.util.Date;
import org.document.Document;
import org.document.DocumentStore;
import org.document.PropertyDataStore;

/**
 *
 * @author V. Shyshkin
 */
public class Person implements Document {

    protected DocumentStore document;

    public Person() {
        // DocumentStore is a default PropertyDataStore
        this.document = new DocumentStore(this);
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
    public PropertyDataStore getPropertyDataStore() {
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
        this.firstName = firstName;
        document.put("firstName", firstName);
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
        this.lastName = lastName;
        document.put("lastName", lastName);
    }
}
