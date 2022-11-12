/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarRentalCustomer;
import javax.ejb.Local;
import util.exception.CustomerMobilePhoneExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author kathleen
 */
@Local
public interface CarRentalCustomerSessionBeanLocal {

    public Long createNewCarRentalCustomer(CarRentalCustomer carRentalCustomer) throws CustomerMobilePhoneExistException, UnknownPersistenceException;

    public CarRentalCustomer retrieveCarRentalCustomerById(Long carRentalCustomerId) throws CustomerNotFoundException;

    public void updateCarRentalCustomer(CarRentalCustomer carRentalCustomer);

    public void deleteCarRentalCustomer(Long carRentalCustomerId) throws CustomerNotFoundException;

    public CarRentalCustomer retrieveCarRentalCustomerByMobileNumber(String mobileNumber) throws CustomerNotFoundException;

    public CarRentalCustomer carRentalCustomerLogin(String mobileNumber, String password) throws InvalidLoginCredentialException;
    
}
