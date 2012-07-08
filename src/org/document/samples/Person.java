package org.document.samples;

import java.util.Date;
import org.document.BoundPropertyListener;

/**
 *
 * @author V. Shyshkin
 */
public class Person  {
    private Address address;
    
    private int id;
    private String firstName;
    private String lastName;
    private Date birthDay;
    private int departmentId;
    private BoundPropertyListener boundPropertyListener;

    public Person() {
        super();
    }


    public Person(Integer id, String firstName, String lastName) {
        this();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        bind("address", address);
    }
    
    public void addBoundPropertyChangeListener(BoundPropertyListener l) {
        boundPropertyListener = l;
    }
    private void bind(String name, Object value) {
        if ( boundPropertyListener != null ) {
            boundPropertyListener.bind(name, value);
        }
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
