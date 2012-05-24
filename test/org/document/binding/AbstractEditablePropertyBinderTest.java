/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.document.Document;
import org.document.DocumentChangeEvent;
import org.document.PropertyStore;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Valery
 */
public class AbstractEditablePropertyBinderTest {
    
    public AbstractEditablePropertyBinderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of componentChanged method, of class AbstractEditablePropertyBinder.
     */
    @Test
    public void testComponentChanged() {
        System.out.println("componentChanged");
        Document doc = new DocumentImpl();
        PropertyStore store = doc.propertyStore();
        
        Object componentValue = "Bill";
        AbstractEditablePropertyBinder instance = EditablePropertyBinderImpl.create("firstName", new ComponentImpl());
        instance.document = doc;
        
        instance.componentChanged(componentValue);
        assertEquals("Bill",store.get("firstName"));
    }

    /**
     * Test of updateComponentView method, of class AbstractEditablePropertyBinder.
     */
    @Test
    public void testUpdateComponentView() {
        System.out.println("updateComponentView");
        Object propertyValue = null;
        AbstractEditablePropertyBinder instance = EditablePropertyBinderImpl.create("firstName", new ComponentImpl());
        instance.updateComponentView(propertyValue);
    }

    /**
     * Test of react method, of class AbstractEditablePropertyBinder.
     */
    @Test
    public void testReact() {
        System.out.println("react");
        DocumentChangeEvent event = null;
        AbstractEditablePropertyBinder instance = EditablePropertyBinderImpl.create("firstName", new ComponentImpl());
//        instance.react(event);
    }

    /**
     * Test of propertyChanged method, of class AbstractEditablePropertyBinder.
     */
    @Test
    public void testPropertyChanged() {
        System.out.println("propertyChanged");
        Object propertyValue = null;
        AbstractEditablePropertyBinder instance = EditablePropertyBinderImpl.create("firstName", new ComponentImpl());
        instance.propertyChanged("Tom");
        assertEquals("Tom",instance.getComponentValue());
    }


    public static class EditablePropertyBinderImpl extends AbstractEditablePropertyBinder implements PropertyChangeListener {
        
        protected ComponentImpl component;
        
        public static EditablePropertyBinderImpl create(String propertyName, ComponentImpl component) {
            return new EditablePropertyBinderImpl(propertyName, component);
        }
        
        public EditablePropertyBinderImpl(String propertyName, ComponentImpl component){
            this.propertyName = propertyName;
            this.component = component;
        }        
        @Override
        public void addComponentListeners() {
            this.component.addPropertyChangeListener(this);
        }
        
        @Override
        public void removeComponentListeners() {
            this.component.removePropertyChangeListener(this);
        }

        @Override
        public Object getComponentValue() {
            return component.getText();
        }

        @Override
        protected void setComponentValue(Object compValue) {
            component.setText((String)compValue);
        }

        @Override
        protected Object componentValueOf(Object dataValue) {
            return dataValue == null ? "" : dataValue.toString();
        }

        @Override
        protected Object propertyValueOf(Object compValue) {
            return compValue == null ||  "".equals(compValue) ? null : compValue.toString();
        }

        @Override
        public void initComponentDefault() {
            this.component.setText("");
        }

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            this.componentChanged( ((ComponentImpl)pce.getSource()).getText());
        }
    }
}
