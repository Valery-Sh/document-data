package org.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author V. Shishkin
 */
public class AbstractBindingManager<T extends Document> implements DocumentChangeListener {

    protected Map<Object, DocumentBindings> documentBindings;
    protected ListBindings listBinding;
    
    protected T selected;
    protected ValidatorCollection validators;
    protected BindingRecognizer recognizer;
    private List<DocumentSelectListener> selectDocumentListeners;

    protected AbstractBindingManager() {
        documentBindings = new HashMap<Object, DocumentBindings>();
        validators = new ValidatorCollection();
        selectDocumentListeners = new ArrayList<DocumentSelectListener>();
        listBinding = new DocumentListBindings();
        listBinding.addDocumentChangeListener(this);
    }
    protected AbstractBindingManager(BindingRecognizer recognizer) {
        this();
        this.recognizer = recognizer;
/*        if ( recognizer == null ) {
            this.recognizer = new AbstractBindingManager.DefaultBindingRecognizer();
        }
*/        
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

    public void addBinder(Object docTypeId, Binder binder) {
        assert(docTypeId != null);
        assert(binder != null);
        
        if ( binder instanceof PropertyBinder) {
            addPropertyBinder(docTypeId, binder);
        } else if ( binder instanceof ErrorBinder) {
            addErrorBinder(docTypeId, binder);
        } else if ( binder instanceof ListBinder) {
            addListBinder(docTypeId, binder);            
        }
    }
    public void addBinder(Binder binder) {
        addBinder("default doc type",binder);
    }
    
    protected void addPropertyBinder(Object docTypeId, Binder binder) {
        if ( ! documentBindings.containsKey(docTypeId)) {
            DocumentBindings db = new DocumentBindingHandler();
            documentBindings.put(docTypeId, db);
        }
        documentBindings.get(docTypeId).add(binder);
    }
    protected void addErrorBinder(Object docTypeId, Binder binder) {
        addPropertyBinder(docTypeId, binder);
    }
    
    protected void addListBinder(Object docTypeId, Binder binder) {
        listBinding.add(binder);
    }
    public ValidatorCollection getValidators(Object docTypeId) {
        DocumentBindings b = documentBindings.get(docTypeId);
        if ( b == null ) {
            return null;
        }
        return b.getValidators();
    }
    
    public ValidatorCollection getValidators() {
        return getValidators("default doc type");
    }
    public DocumentBindings getDocumentBindings(Object docTypeId) {
        return documentBindings.get(docTypeId);
    }
    public DocumentBindings getDocumentBindings() {
        return documentBindings.get("default doc type");
    }
    
/*    protected ValidatorCollection getValidators() {
        return validators;
    }

    protected void validate() throws ValidationException {
        //getValidators().validate(selected.getPropertyDataStore());
        getValidators().validate(selected);
    }
*/
    protected PropertyDataStore getPropertyDataStore() {
        return this.selected.getPropertyDataStore();
    }

    protected void update(T selected) {
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
        
        DocumentBindings result;
        if (recognizer != null) {
            result = documentBindings.get(recognizer.getBindingId(doc));
        } else {
            result = documentBindings.get("default doc type");
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
    
    public static class DefaultBindingRecognizerNew<T> implements BindingRecognizer<T> {

        @Override
        public Object getBindingId(T document) {
            return document.getClass();
        }
        
    }
}