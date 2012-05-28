package org.document.samples;

import java.util.Date;
import org.document.AbstractObjectDocument;
import org.document.Validator;

/**
 *
 * @author V. Shyshkin
 */
public class Person extends AbstractObjectDocument {
    
    private int id;
    private String firstName;
    private String lastName;
    private Date birthDay;
    private int departmentId;

    public Person() {
        super();
    }

    public Person(Validator validator) {
        super(validator);
    }

    public Person(Validator validator,Integer id, String firstName, String lastName) {
        this(validator);
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
        bind("birthDay", birthDay);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        bind("firstName", firstName);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        bind("id", id);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        bind("lastName", lastName);
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
        bind("departmentId",departmentId);
    }


}
