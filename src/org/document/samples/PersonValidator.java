package org.document.samples;

import org.document.AbstractValidator;
import org.document.Document;
import org.document.ValidationException;

public class PersonValidator extends AbstractValidator {

    @Override
    public void validate(Document document) {
        Person p = (Person) document;
        String lastName = p.getLastName();
        if (lastName == null || lastName.isEmpty()) {
            throw new ValidationException("  lastName cannot be null or empty",document);
        }
    }

    @Override
    public void validate(Object key, Object value, Document document) {
        if (key.equals("firstName")) {
            if (value != null && value.toString().length() == 1) {
                throw new ValidationException("  firstName", "'firstName'=" + value + " length cannot be 1",document);
            }
        } else if (key.equals("lastName")) {
            if (value != null && value.toString().length() > 9 ) {
                throw new ValidationException("  lastName", "lastName='" + value + "' length cannot be > 9",document);
            }
        }
    }
}//class PersonValidator
