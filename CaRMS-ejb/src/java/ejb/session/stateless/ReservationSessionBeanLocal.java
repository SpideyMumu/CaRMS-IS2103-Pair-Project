/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import entity.Reservation;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.CreateNewRentalRateException;
import util.exception.CreateReservationException;
import util.exception.CreateTransitDriverDispatchException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author kathleen
 */
@Local
public interface ReservationSessionBeanLocal {

    public Reservation createNewReservation(Long carId, Long pickupOutletId, Long returnOutletId, Long customerId, Reservation newReservation) throws CreateReservationException;

    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException;

    public void updateReservation(Reservation reservation);

    public void deleteReservation(Long reservationId) throws ReservationNotFoundException;

    public List<Reservation> retrieveAllReservations();

    public List<Reservation> retrieveReservationsByDates(Date startDate, Date endDate);

    public void allocateCarToReservation(Reservation reservation, Date date) throws CarNotFoundException, CreateTransitDriverDispatchException;

}
