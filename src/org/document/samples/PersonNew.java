/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.samples;

import java.util.Date;
import org.document.BoundPropertyChangeListener;
import org.document.Validator;

/**
 *
 * @author Valery
 */
public class PersonNew {
    
    private int id;
    private String firstName;
    private String lastName;
    private Date birthDay;
    private int departmentId;
    
    private BoundPropertyChangeListener boundPropertyListener;
    
    public PersonNew() {
        super();
    }
    public void addBoundPropertyListener(BoundPropertyChangeListener l) {
        boundPropertyListener = l;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
        boundPropertyListener.bind("birthDay", birthDay);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        boundPropertyListener.bind("firstName", firstName);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        boundPropertyListener.bind("id", id);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        boundPropertyListener.bind("lastName", lastName);
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
        boundPropertyListener.bind("departmentId",departmentId);
    }


}
