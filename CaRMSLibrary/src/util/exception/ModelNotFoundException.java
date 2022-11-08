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
public class ModelNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ModelNotFoundException</code> without
     * detail message.
     */
    public ModelNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ModelNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ModelNotFoundException(String msg) {
        super(msg);
    }
}
