package org.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author V. Shishkin
 */
public class AbstractBindingManagerOld<T extends Document> implements DocumentChangeListener {

    protected Map<Object, DocumentBindings> documentBindings;
    protected ListBindings listBinding;
    
    protected T selected;
    protected ValidatorCollection validators;
    protected BindingRecognizer recognizer;
    private List<DocumentSelectListener> selectDocumentListeners;

    protected AbstractBindingManagerOld() {
        documentBindings = new HashMap<Object, DocumentBindings>();
        validators = new ValidatorCollection();
        selectDocumentListeners = new ArrayList<DocumentSelectListener>();
    }
    protected AbstractBindingManagerOld(BindingRecognizer recognizer) {
        this();
        this.recognizer = recognizer;
        if ( recognizer == null ) {
            this.recognizer = new DefaultBindingRecognizer();
        }
        
    }

    public T getSelected() {
        return selected;
    }

    public void setSelected(T selected) {
        this.update(selected);
        T old = this.selected;
        this.selected = selected;
        fireSelectDocument(old, selected);
    }

    public void addBinding(Bindings binding) {
        if (binding instanceof DocumentBindings) {
            Object id = ((DocumentBindings) binding).getId();
            if (id == null) {
                id = "single";
            }
            documentBindings.put(id, (DocumentBindings) binding);
        } else if (binding instanceof ListBindings) {
            this.listBinding = (ListBindings) binding;
            this.listBinding.addDocumentChangeListener(this);
        }
    }

    protected ValidatorCollection getValidators() {
        return validators;
    }

    protected void validate() throws ValidationException {
        //getValidators().validate(selected.getPropertyDataStore());
        getValidators().validate(selected);
    }

    protected PropertyDataStore getDocumentStore() {
        return this.selected.getPropertyDataStore();
    }

    protected void update(T selected) {
        //Document document = newSelected.getPropertyDataStore();
        if (this.selected == selected) {
            return;
        }
        Document old = this.selected;
        if (old != null) {
            DocumentBindings b = getBinding(old);
            if (b != null) {
                //b.setDocument(old);
                b.completeChanges();
            }
        }
        this.selected = selected;
        if (selected != null) {
            DocumentBindings b = getBinding(selected);
            if (b != null) {
                b.setDocument(selected);
            }
            listBinding.setDocument(selected);
        }


    }

    public void setRecognizer(BindingRecognizer recognizer) {
        this.recognizer = recognizer;
    }
    //
    // ===========================================================
    //

    protected DocumentBindings getBinding(Document doc) {
        if (doc == null) {
            return null;
        }

        DocumentBindings result = null;
        if (recognizer != null) {
            result = documentBindings.get(recognizer.getBindingId(doc));
        } else {
            for (Map.Entry<Object, DocumentBindings> b : documentBindings.entrySet()) {
                result = documentBindings.get(b.getKey());
            }
        }
        return result;
    }

    @Override
    public void react(DocumentChangeEvent event) {
        switch (event.getAction()) {
            case selectChange:
                T newDoc = (T)event.getNewValue();
                if (newDoc == this.selected) {
                    return;
                }
                setSelected(newDoc);
                break;

        }
    }

    /**
     * Prepends cyclic data modifications.
     *
     * @param value a new value to be assigned
     * @return
     */
    protected boolean needChangeSelected(Document document) {
        if (document == this.selected) {
            return false;
        }
        return true;
    }

    public void addSelectDocumentListener(DocumentSelectListener l) {
        this.selectDocumentListeners.add(l);
    }

    public void removeSelectDocumentListener(DocumentSelectListener l) {
        this.selectDocumentListeners.remove(l);
    }

    private void fireSelectDocument(T oldSelected, T newSelected) {
        DocumentSelectEvent event = new DocumentSelectEvent(this, oldSelected, newSelected);
        for (DocumentSelectListener l : selectDocumentListeners) {
            l.documentSelect(event);
        }
    }
    
    public static class DefaultBindingRecognizer<T> implements BindingRecognizer<T> {

        @Override
        public Object getBindingId(T document) {
            return document.getClass();
        }
        
    }
}