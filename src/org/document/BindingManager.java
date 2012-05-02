package org.document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author V. Shishkin
 */
public class BindingManager implements DocumentListBinding {

    protected Map<Object, DocumentBinding> documentBindings;
    protected Document document;
    protected ValidatorCollection validators;
    protected List<Document> sourceList;
            
    public BindingManager(List<Document> sourceList) {
        this.sourceList = sourceList;
        documentBindings = new HashMap<Object,DocumentBinding>();
        validators = new ValidatorCollection();
    }
    
    @Override
    public void put(Object bindingId, DocumentBinding binding) {
        this.documentBindings.put(bindingId, binding);
    }

    @Override
    public void add(Binder binder) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(Binder binder) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void completeChanges() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValidatorCollection getValidators() {
        return validators;
    }

    @Override
    public void validate() throws ValidationException {
        getValidators().validate(document);
    }

    @Override
    public Document getDocument() {
        return this.document;
    }

    @Override
    public void setDocument(Document document) {
        if ( this.document == document) {
            return;
        }
        Document old = this.document;
        if (old != null) {
            DocumentBinding b = getBinding(old);
            if ( b != null ) {
                b.setDocument(old);
            }
        }
        if (document != null) {
            DocumentBinding b = getBinding(document);
            if ( b != null ) {
                b.setDocument(document);
            }
        }

        this.document = document;


    }
    protected BindingRecognizer recognizer;

    public void setRecognizer(BindingRecognizer recognizer) {
        this.recognizer = recognizer;
    }
    //
    // ===========================================================
    //

    protected DocumentBinding getBinding(Document doc) {
        DocumentBinding result = null;
        if (recognizer != null) {
            result = documentBindings.get(recognizer.getBindingId(doc));
        } else {
            for (Map.Entry<Object, DocumentBinding> b : documentBindings.entrySet()) {
                result = documentBindings.get(b.getKey());
            }
        }
        return result;
    }

}//class BindingManager
