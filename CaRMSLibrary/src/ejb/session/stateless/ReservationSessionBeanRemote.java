/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import entity.Reservation;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarCategoryNotFoundException;
import util.exception.CreateNewRentalRateException;
import util.exception.CreateReservationException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author kathleen
 */
@Remote
public interface ReservationSessionBeanRemote {
    
       public Long createNewReservation(Long carCategoryId, Long pickupOutletId, Long returnOutletId, Long customerId, Reservation newReservation) throws CreateReservationException, CarCategoryNotFoundException, OutletNotFoundException, CustomerNotFoundException, InputDataValidationException;

       public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException;

       public void updateReservation(Reservation reservation);

       public void deleteReservation(Long reservationId) throws ReservationNotFoundException;
       
       public List<Reservation> retrieveAllReservations();
}
