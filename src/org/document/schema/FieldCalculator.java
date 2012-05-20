/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.document.schema;

import org.document.Document;

/**
 *
 * @author Valery
 */
public interface FieldCalculator {
    Object getValue(Document doc);
}
