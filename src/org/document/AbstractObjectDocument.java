package org.document;

/**
 * This class implements {@link org.document.Document } interface and provides
 * some common behavior that should be suitable for most concrete
 * <code>Document</code> implementations. Subclasses might provide definitions
 * of fields and there access methods. <h1>Example.</h1>
 * <pre>
 * public class Person extends AbstractObjectDocument {
 *
 *   private String firstName;
 *   private String lastName;
 *   private Date birthDay;
 *
 *
 *   public Person() {
 *       super();
 *   }
 *
 *   public Date getBirthDay() {
 *       return birthDay;
 *   }
 *
 *   public void setBirthDay(Date birthDay) {
 *       this.birthDay = birthDay;
 *       bind("birthDay", birthDay);
 *   }
 *
 *   public String getFirstName() {
 *       return firstName;
 *   }
 *
 *  public void setFirstName(String firstName) {
 *       this.firstName = firstName;
 *       bind("firstName", firstName);
 *   }
 * }
 * </pre>
 * @see org.document.samples.Person
 * @author V. Shyshkin
 */
public class AbstractObjectDocument implements Document, HasValidator {

    protected transient DocumentPropertyStore propertyStore;
    private transient Validator validator;

    public AbstractObjectDocument() {
        // DocumentPropertyStore is a default PropertyStore
        this.propertyStore = new DocumentPropertyStore(this);
    }

    public AbstractObjectDocument(Validator validator) {
        this();
        this.validator = validator;
    }

    //
    // Document interface implementation
    //
    @Override
    public PropertyStore propertyStore() {
        return this.propertyStore;
    }

    public void bind(String propertyName, Object value) {
        propertyStore.bind(propertyName, value);
    }

    @Override
    public Validator getValidator() {
        return validator;
    }

    @Override
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    //
    // =========== Here follows an implementation code ========================================
    //
}
