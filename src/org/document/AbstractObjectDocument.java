package org.document;


/**
 *
 * @author V. Shyshkin
 */
public class AbstractObjectDocument implements Document, HasValidator {

    protected DocumentPropertyStore document;
    private Validator validator;

    public AbstractObjectDocument() {
        // DocumentPropertyStore is a default PropertyStore
        this.document = new DocumentPropertyStore(this);
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
        return this.document;
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
