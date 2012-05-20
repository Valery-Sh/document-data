package org.document.samples;

import java.util.Date;
import org.document.*;

/**
 *
 * @author V. Shyshkin
 */
public class Person implements Document, HasValidator {

    protected DocumentPropertyStore document;
    private Validator validator;

    public Person() {
        // DocumentPropertyStore is a default PropertyStore
        this.document = new DocumentPropertyStore(this);
    }

    public Person(Validator validator) {
        this();
        this.validator = validator;
    }

    public Person(Validator validator,int id, String firstName, String lastName) {
        this(validator);
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
        document.set("birthDay", birthDay);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        //document.validate("firstName", firstName);
        //document.set("firstName", firstName);
        this.firstName = firstName;
        document.set("firstName", firstName);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        document.set("id", id);
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

    @Override
    public Validator getValidator() {
        return validator;
    }

    @Override
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

}
