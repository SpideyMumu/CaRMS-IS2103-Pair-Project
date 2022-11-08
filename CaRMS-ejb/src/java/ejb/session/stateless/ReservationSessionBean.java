/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarRentalCustomer;
import entity.Customer;
import entity.Outlet;
import entity.Reservation;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CarNotFoundException;
import util.exception.CreateReservationException;
import util.exception.CustomerNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author kathleen
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private CarRentalCustomerSessionBeanLocal carRentalCustomerSessionBean;

    @EJB
    private CustomerSesionBeanLocal customerSessionBeanLocal;

    @EJB(name = "OutletSessionBeanLocal")
    private OutletSessionBeanLocal outletSessionBeanLocal;

    @EJB
    private CarSessionBeanLocal carSessionBeanLocal; 
    
    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    

    @Override
    public Reservation createNewReservation(Long carId, Long pickupOutletId, Long returnOutletId, Long customerId, Reservation newReservation) throws CreateReservationException
    {
        if(newReservation != null)
        {
            try
            {   
                Car car = carSessionBeanLocal.retrieveCarById(carId);
                newReservation.setCar(car);
                car.getReservations().add(newReservation);
                
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
                
                return newReservation;      
          
            }
            catch(CarNotFoundException | OutletNotFoundException | CustomerNotFoundException ex)
            {   
                throw new CreateReservationException(ex.getMessage());
            }
        }
        else
        {
            throw new CreateReservationException("Reservation information not provided");
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
    
    public void updateReservation(Reservation reservation)
    {
        /*if(productEntity != null && productEntity.getProductId()!= null)
        {
            ProductEntity productEntityToUpdate = retrieveProductByProductId(productEntity.getProductId());
            
            if(productEntityToUpdate.getSkuCode().equals(productEntity.getSkuCode()))
            {
                productEntityToUpdate.setName(productEntity.getName());
                productEntityToUpdate.setDescription(productEntity.getDescription());
                productEntityToUpdate.setQuantityOnHand(productEntity.getQuantityOnHand());
                productEntityToUpdate.setUnitPrice(productEntity.getUnitPrice());
                productEntityToUpdate.setCategory(productEntity.getCategory());
            }
            else
            {
                throw new UpdateProductException("SKU Code of product record to be updated does not match the existing record");
            }
        }
        else
        {
            throw new ProductNotFoundException("Product ID not provided for product to be updated");
        }*/
        em.merge(reservation);
    }
    
    @Override
    public void deleteReservation(Long reservationId) throws ReservationNotFoundException
    {
       Reservation reservation = retrieveReservationById(reservationId);
       em.remove(reservation);
    }

}
