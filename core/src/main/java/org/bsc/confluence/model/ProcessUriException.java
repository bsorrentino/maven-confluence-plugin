/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.model;

/**
 *
 * @author bsorrentino
 */
@SuppressWarnings("serial")
public class ProcessUriException extends Exception {

    public ProcessUriException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public ProcessUriException(String string) {
        super(string);
    }
}
