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
public class UpdateModelException extends Exception {

    /**
     * Creates a new instance of <code>UpdateModelException</code> without
     * detail message.
     */
    public UpdateModelException() {
    }

    /**
     * Constructs an instance of <code>UpdateModelException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateModelException(String msg) {
        super(msg);
    }
}
