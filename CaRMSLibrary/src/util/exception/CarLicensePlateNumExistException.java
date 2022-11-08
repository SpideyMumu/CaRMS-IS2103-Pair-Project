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
public class CarLicensePlateNumExistException extends Exception {

    /**
     * Creates a new instance of <code>CarLicensePlateNumExistException</code>
     * without detail message.
     */
    public CarLicensePlateNumExistException() {
    }

    /**
     * Constructs an instance of <code>CarLicensePlateNumExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CarLicensePlateNumExistException(String msg) {
        super(msg);
    }
}
