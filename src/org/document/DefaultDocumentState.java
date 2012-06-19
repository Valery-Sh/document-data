package org.document;

import java.util.HashMap;
import java.util.Map;

public class DefaultDocumentState implements DocumentState {

    private boolean attached;
    private boolean editing;
    private PropertyStore documentStore;
    private Map beforeEditValues;
    /**
     *
     */
    protected Map dirtyEditValues;

    /**
     *
     * @param documentStore
     */
    public DefaultDocumentState(PropertyStore documentStore) {
        this.documentStore = documentStore;
        beforeEditValues = new HashMap();
        dirtyEditValues = new HashMap();
    }

    @Override
    public boolean isAttached() {
        return attached;
    }

    @Override
    public void setAttached(boolean attached) {
        this.attached = attached;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isEditing() {
        return editing;
    }

    /**
     *
     * @param editing
     */
    @Override
    public void setEditing(boolean editing) {
        if (!attached) {
            return;
        }
        if (this.editing == editing) {
            return;
        }
        if (documentStore.getOwner() == null) {
            return;
        }

        if (this.editing && !editing) {
        } else if (!this.editing) {
            beforeEditValues.clear();
            DataUtils.putAll(beforeEditValues, documentStore.getOwner());
            dirtyEditValues.clear();
            dirtyEditValues.putAll(beforeEditValues);
            this.editing = editing;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public Map<String, Object> getDirtyValues() {
        return this.dirtyEditValues;
    }
}//class DocumentStateImpl
