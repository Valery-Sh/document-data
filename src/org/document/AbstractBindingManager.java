package org.document;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author V. Shishkin
 */
public class AbstractBindingManager<T extends Document> implements DocumentListener {

    protected Map<Object, DocumentBinding> documentBindings;
    protected ListBinding listBinding;
    
    protected T selected;
    
    protected ValidatorCollection validators;
    protected BindingRecognizer recognizer;
    
    protected AbstractBindingManager() {
        documentBindings = new HashMap<Object,DocumentBinding>();
        validators = new ValidatorCollection();
    }
    public T getSelected() {
        return selected;
    }
    public void setSelected(T selected) {
        this.update(selected);
        this.selected = selected;
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
        //getValidators().validate(selected.getDocumentStore());
        getValidators().validate(selected);
    }

    protected DocumentStore getDocumentStore() {
        return this.selected.getDocumentStore();
    }

    protected void update(T selected) {
        //Document document = newSelected.getDocumentStore();
        if ( this.selected == selected) {
            return;
        }
        Document old = this.selected;
        if (old != null) {
            DocumentBinding b = getBinding(old);
            if ( b != null ) {
                //b.setDocument(old);
                b.completeChanges();
            }
        }
        if (selected != null) {
            DocumentBinding b = getBinding(selected);
            if ( b != null ) {
                b.setDocument(selected);
            }
        }

        this.selected = selected;


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


    @Override
    public void react(DocumentEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}//class BindingManager
