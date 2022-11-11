/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author muhdm
 */
public class CreateNewModelException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewModelException</code> without
     * detail message.
     */
    public CreateNewModelException() {
    }

    /**
     * Constructs an instance of <code>CreateNewModelException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewModelException(String msg) {
        super(msg);
    }
}
