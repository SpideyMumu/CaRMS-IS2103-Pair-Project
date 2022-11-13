/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Remote;
import util.exception.CustomerMobilePhoneExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author kathleen
 */
@Remote
public interface CustomerSessionBeanRemote {
    public Customer retrieveCustomerById(Long customerId) throws CustomerNotFoundException;
        
    public void updateCustomer(Customer customer);

    public void deleteCustomer(Long customerId) throws CustomerNotFoundException;
    
    public Long createNewCustomer(Customer newCustomer) throws CustomerMobilePhoneExistException, UnknownPersistenceException, InputDataValidationException;
}
