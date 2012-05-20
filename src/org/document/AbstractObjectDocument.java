package org.document;


/**
 *
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
    public PropertyStore getPropertyStore() {
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
