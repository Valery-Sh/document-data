package org.document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author V. Shishkin
 */
public class AbstractBindingManager implements DocumentListener {

    protected Map<Object, DocumentBinding> documentBindings;
    protected ListBinding listBinding;
    
    protected Document document;
    protected ValidatorCollection validators;
    protected BindingRecognizer recognizer;
    
    protected AbstractBindingManager() {
        documentBindings = new HashMap<Object,DocumentBinding>();
        validators = new ValidatorCollection();
    }
    
    public void addBinding(Binding binding) {
        if ( binding instanceof DocumentBinding) {
            Object id = ((DocumentBinding)binding).getId();
            if ( id == null ) {
                id = "single";
            }
            documentBindings.put(id, (DocumentBinding)binding);
        } else if ( binding instanceof ListBinding) {
            this.listBinding = (ListBinding)binding;
        }
    }

    public ValidatorCollection getValidators() {
        return validators;
    }

    public void validate() throws ValidationException {
        getValidators().validate(document);
    }

    protected Document getDocument() {
        return this.document;
    }

    protected void setDocument(Document document) {
        if ( this.document == document) {
            return;
        }
        Document old = this.document;
        if (old != null) {
            DocumentBinding b = getBinding(old);
            if ( b != null ) {
                //b.setDocument(old);
                b.completeChanges();
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
    

    public void setRecognizer(BindingRecognizer recognizer) {
        this.recognizer = recognizer;
    }
    //
    // ===========================================================
    //

    protected DocumentBinding getBinding(Document doc) {
        if ( doc == null ) {
            return null;
        }

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

    protected DocumentBinding getBinding(HasDocument hasdoc) {
        if ( hasdoc == null ) {
            return null;
        }
        return getBinding(hasdoc.getDocument());
    }

    @Override
    public void react(DocumentEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}//class BindingManager
