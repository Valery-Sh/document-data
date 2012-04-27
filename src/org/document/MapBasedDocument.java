package org.document;

/**
 *
 * @author V. Shyshkin
 */
public class MapBasedDocument implements Document, HasDocumentState {

    protected SimpleMapBasedDocument baseDocument;
    protected transient DocumentState state;

    public MapBasedDocument() {
        baseDocument = new SimpleMapBasedDocument();
        this.state = new MapDocumentState(baseDocument);
    }

    @Override
    public Object get(Object key) {
        return getDocumentState().getCurrent().get(key);
    }

    @Override
    public void put(Object key, Object value) {
        DocumentState st = getDocumentState();
        if ( ! state.isEditing()) {
            state.setEditing(true);
        }
        state.getCurrent().put(key, value);
    }

    @Override
    public void addDocumentListener(DocumentListener listener) {
        this.baseDocument.addDocumentListener(listener);
    }

    @Override
    public void removeDocumentListener(DocumentListener listener) {
        this.baseDocument.removeDocumentListener(listener);
    }

    @Override
    public DocumentState getDocumentState() {
        return state;

    }

    protected static class MapDocumentState implements DocumentState{

        private boolean editing;
        
        private Document editingDocument;
        private Document currentDocument;
        private Document unchangedDocument;

        public MapDocumentState(Document document) {
            this.unchangedDocument = document;
            this.currentDocument   = document;
            
        }
        
        @Override
        public boolean isEditing() {
            return editing;
        }

        @Override
        public void setEditing(boolean editing) {
            if (this.editing == editing) {
                return;
            }
            if (this.editing && !editing) {
                unchangedDocument = editingDocument;
                currentDocument = unchangedDocument;
                editingDocument = null;
            } else if (!this.editing) {
                editingDocument = new SimpleMapBasedDocument();
                ((SimpleMapBasedDocument)editingDocument).values.putAll(((SimpleMapBasedDocument)unchangedDocument).values);
                //((SimpleMapBasedDocument)editingDocument).docListeners.addAll(((SimpleMapBasedDocument)unchangedDocument).docListeners); 
                ((SimpleMapBasedDocument)editingDocument).docListeners = ((SimpleMapBasedDocument)unchangedDocument).docListeners;                 
                currentDocument = editingDocument;
            }

            this.editing = editing;

        }


        @Override
        public Document getCurrent() {
            return this.currentDocument;
        }
    }
}
