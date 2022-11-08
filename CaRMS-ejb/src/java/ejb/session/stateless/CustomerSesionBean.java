/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Reservation;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.CustomerMobilePhoneExistException;
import util.exception.CustomerNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author kathleen
 */
@Stateless
public class CustomerSesionBean implements CustomerSesionBeanRemote, CustomerSesionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createNewCustomer(Customer newCustomer) throws CustomerMobilePhoneExistException, UnknownPersistenceException
    {
        try
        {
            em.persist(newCustomer);
            em.flush();

            return newCustomer.getCustomerId();
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new CustomerMobilePhoneExistException();
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else
            {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public Customer retrieveCustomerById(Long customerId) throws CustomerNotFoundException
    {
        Customer customer = em.find(Customer.class, customerId);
        if (customer != null)
        {
            return customer;
        } else
        {
            throw new CustomerNotFoundException();
        }
    }
    
    @Override
    public void updateCustomer(Customer customer)
    {
        em.merge(customer);
    }
    
    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException//throws StaffNotFoundException
    {
       Customer customer = retrieveCustomerById(customerId);
        em.remove(customer);
    }


}
