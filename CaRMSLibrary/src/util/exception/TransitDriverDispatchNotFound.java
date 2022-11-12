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
public class TransitDriverDispatchNotFound extends Exception {

    /**
     * Creates a new instance of <code>TransitDriverDispatchNotFound</code>
     * without detail message.
     */
    public TransitDriverDispatchNotFound() {
    }

    /**
     * Constructs an instance of <code>TransitDriverDispatchNotFound</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public TransitDriverDispatchNotFound(String msg) {
        super(msg);
    }
}
