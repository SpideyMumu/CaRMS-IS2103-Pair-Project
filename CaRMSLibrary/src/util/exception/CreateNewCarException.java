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
public class CreateNewCarException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewCarException</code> without
     * detail message.
     */
    public CreateNewCarException() {
    }

    /**
     * Constructs an instance of <code>CreateNewCarException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewCarException(String msg) {
        super(msg);
    }
}
