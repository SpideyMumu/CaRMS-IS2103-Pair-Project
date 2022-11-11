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
public class UpdateCarException extends Exception {

    /**
     * Creates a new instance of <code>UpdateCarException</code> without detail
     * message.
     */
    public UpdateCarException() {
    }

    /**
     * Constructs an instance of <code>UpdateCarException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateCarException(String msg) {
        super(msg);
    }
}
