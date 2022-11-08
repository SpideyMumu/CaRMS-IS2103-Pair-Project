/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author kathleen
 */
public class ReservationNotFoundException extends Exception {

    public ReservationNotFoundException() {
    }

    public ReservationNotFoundException(String msg) {
        super(msg);
    }
}
