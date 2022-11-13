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
public class CarCategoryNameExistException extends Exception {

    public CarCategoryNameExistException() {
    }

    public CarCategoryNameExistException(String msg) {
        super(msg);
    }
}
