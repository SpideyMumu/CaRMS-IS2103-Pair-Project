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
public class InvalidAccessRightException extends Exception{

    /**
     * Creates a new instance of <code>InvalidAccessRightException</code>
     * without detail message.
     */
    public InvalidAccessRightException() {
    }

    /**
     * Constructs an instance of <code>InvalidAccessRightException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidAccessRightException(String msg) {
        super(msg);
    }
}
