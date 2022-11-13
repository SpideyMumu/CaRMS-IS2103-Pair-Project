/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarRentalCustomer;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerMobilePhoneExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author kathleen
 */
@Stateless
public class CarRentalCustomerSessionBean implements CarRentalCustomerSessionBeanRemote, CarRentalCustomerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
        
    public CarRentalCustomerSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
 
    @Override
    public Long createNewCarRentalCustomer(CarRentalCustomer carRentalCustomer) throws CustomerMobilePhoneExistException, UnknownPersistenceException, InputDataValidationException
    {
        
        Set<ConstraintViolation<CarRentalCustomer>>constraintViolations = validator.validate(carRentalCustomer);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(carRentalCustomer);
                em.flush();

                return carRentalCustomer.getCustomerId();
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
        else
        {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }

    }
    
    public CarRentalCustomer retrieveCarRentalCustomerById(Long carRentalCustomerId) throws CustomerNotFoundException
    {
        
        CarRentalCustomer carRentalCustomer = em.find(CarRentalCustomer.class, carRentalCustomerId);
        if (carRentalCustomer != null)
        {
            return carRentalCustomer;
        } else
        {
            throw new CustomerNotFoundException();
        }
    }
    
    public CarRentalCustomer retrieveCarRentalCustomerByMobileNumber(String mobileNumber) throws CustomerNotFoundException
    {
        Query query = em.createQuery("SELECT c FROM CarRentalCustomer c WHERE c.mobileNumber = :inMobileNumber");
        query.setParameter("inMobileNumber", mobileNumber);
        
        try
        {
            return (CarRentalCustomer)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new CustomerNotFoundException("Customer mobile number " + mobileNumber + " does not exist!");
        }
    }
    
    @Override
    public void updateCarRentalCustomer(CarRentalCustomer carRentalCustomer)
    {
        em.merge(carRentalCustomer);
    }
    
    @Override
    public void deleteCarRentalCustomer(Long carRentalCustomerId) throws CustomerNotFoundException
    {
        CarRentalCustomer carRentalCustomer = retrieveCarRentalCustomerById(carRentalCustomerId);
        em.remove(carRentalCustomer);
    }
    
     public CarRentalCustomer carRentalCustomerLogin(String mobileNumber, String password) throws InvalidLoginCredentialException
    {
        try
        {
            CarRentalCustomer carRentalCustomer = retrieveCarRentalCustomerByMobileNumber(mobileNumber);
            
            if(carRentalCustomer.getMobileNumber().equals(mobileNumber))
            {
                carRentalCustomer.getReservations().size();                
                return carRentalCustomer;
            }
            else
            {
                throw new InvalidLoginCredentialException("Mobile number does not exist or invalid password!");
            }
        }
        catch(CustomerNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Mobile number does not exist or invalid password!");
        }
    }
     
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CarRentalCustomer>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    

}
