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
public class CreateNewEmployeeException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewEmployeeException</code> without
     * detail message.
     */
    public CreateNewEmployeeException() {
    }

    /**
     * Constructs an instance of <code>CreateNewEmployeeException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewEmployeeException(String msg) {
        super(msg);
    }
}
