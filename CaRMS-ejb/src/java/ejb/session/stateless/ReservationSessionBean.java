/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.CarRentalCustomer;
import entity.Customer;
import entity.Outlet;
import entity.Reservation;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.CreateReservationException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author kathleen
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB(name = "CarCategorySessionBeanLocal")
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;

    @EJB
    private CarRentalCustomerSessionBeanLocal carRentalCustomerSessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB(name = "OutletSessionBeanLocal")
    private OutletSessionBeanLocal outletSessionBeanLocal;

    @EJB
    private CarSessionBeanLocal carSessionBeanLocal; 
    
    
    
    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    
    
    public ReservationSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    

    @Override
    public Long createNewReservation(Long carCategoryId, Long pickupOutletId, Long returnOutletId, Long customerId, Reservation newReservation) throws CreateReservationException, CarCategoryNotFoundException, OutletNotFoundException, CustomerNotFoundException, InputDataValidationException
    {

        Set<ConstraintViolation<Reservation>>constraintViolations = validator.validate(newReservation);
        
        if(constraintViolations.isEmpty())
        {
            if(newReservation != null)
            {
                try
                {   
                    CarCategory carCategory = carCategorySessionBeanLocal.retrieveCategoryById(carCategoryId);
                    newReservation.setCarCategory(carCategory);

                    Outlet pickupOutlet = outletSessionBeanLocal.retrieveOutletById(pickupOutletId);
                    newReservation.setPickUpLocation(pickupOutlet);

                    Outlet returnOutlet = outletSessionBeanLocal.retrieveOutletById(returnOutletId);
                    newReservation.setReturnLocation(returnOutlet);

                    CarRentalCustomer customer = carRentalCustomerSessionBean.retrieveCarRentalCustomerById(customerId);
                    newReservation.setCustomer(customer);

                    em.persist(newReservation);

                    if(customer instanceof CarRentalCustomer)
                    {
                        CarRentalCustomer carRentalCustomer = (CarRentalCustomer)customer;
                        carRentalCustomer.getReservations().add(newReservation);
                    }

                    em.flush();

                    return newReservation.getReservationId();      

                }
                catch(CarCategoryNotFoundException | OutletNotFoundException | CustomerNotFoundException ex)
                {   
                    throw new CreateReservationException(ex.getMessage());
                }
            }
            else
            {
                throw new CreateReservationException("Reservation information not provided");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException
    {
        Reservation reservation = em.find(Reservation.class, reservationId);
        if (reservation != null)
        {
            return reservation;
        } else
        {
            throw new ReservationNotFoundException();
        }
    }
    
    public List<Reservation> retrieveAllReservations()
    {
        Query query = em.createQuery("SELECT r FROM Reservation r");
        return query.getResultList();
    }
    
    public void updateReservation(Reservation reservation)
    {
        em.merge(reservation);
    }
    
    @Override
    public void deleteReservation(Long reservationId) throws ReservationNotFoundException
    {
       Reservation reservation = retrieveReservationById(reservationId);
       em.remove(reservation);
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Reservation>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

}
