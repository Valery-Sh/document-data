package org.document.binding;

import org.document.Document;

/**
 *
 * @author V. Shyshkin
 */
public class DocumentInfoBinder extends DocumentListBinder {
    
            
    public DocumentInfoBinder(DocumentInfo boundObject) {
        super(boundObject);
        add(createSelectedBinder());
    }
    @Override
    protected PropertyBinder createSelectedBinder() {
        return new SelectedBinder((DocumentInfo)getBoundObject());
    }

    @Override
    protected PropertyBinder createListModelBinder() {
        return null;
    }

    @Override
    protected PropertyBinder createDocumentChangeEventBinder() {
        return null;
    }
    
    public class SelectedBinder extends AbstractListSelectionBinder {
        
        public SelectedBinder(DocumentInfo info) {
            super(info);
        }
        @Override
        public void propertyChanged(String property,Object value) {
            super.propertyChanged(property, value);
            createInfo();
        }
        protected void createInfo() {
            DocumentInfo info = getInfo();
            
            BindingState state = (BindingState)getDocument();
            if ( state != null ) {
                info.setIndex(state.getDocumentList().indexOf(state.getSelected()));
                info.setClazz(state.getSelected().getClass());
                info.setSize(state.getDocumentList().size());
                info.setAlias(state.getAlias());
            }
            System.out.println("alias = " + info.getAlias() + ";\n");
            System.out.println("index = " + info.getIndex() + ";\n");
            System.out.println("size = " + info.getSize() + ";\n");
            System.out.println("class = " + info.getClazz() + ";\n");            
            System.out.println("______________________________ \n");            
            
        }
        
        @Override
        protected void addComponentListeners() {
        }

        @Override
        protected void removeComponentListeners() {
        }

        @Override
        protected int getComponentSelectedIndex() {
            return 0;
        }
        public DocumentInfo getInfo() {
            return (DocumentInfo)getBoundObject();
        }
        @Override
        protected void setComponentSelectedIndex(int index) {
            
        }
        
    }
}
