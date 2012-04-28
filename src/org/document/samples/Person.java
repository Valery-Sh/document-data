/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.samples;

import java.util.Date;
import org.document.BeanBasedDocument;
import org.document.Document;
import org.document.HasDocument;

/**
 *
 * @author V. Shyshkin
 */
public class Person implements HasDocument {

    protected Document document;

    public Person() {
        this.document = new BeanBasedDocument(this);
    }

    public Person(int id, String firstName, String lastName) {
        this();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public Document getDocument() {
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
