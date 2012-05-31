package org.document;

import java.io.Serializable;

/**
 *
 * @author V. Shyshkin
 */
public interface DocumentChangeListener extends Serializable {
   void react(DocumentChangeEvent event); 
}
