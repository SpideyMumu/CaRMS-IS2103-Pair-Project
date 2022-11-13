/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import util.exception.CarNotFoundException;
import util.exception.CreateTransitDriverDispatchException;
import util.exception.InputDataValidationException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author muhdm
 */
@Stateless
public class EJBtimerSessionBean implements EJBtimerSessionBeanRemote, EJBtimerSessionBeanLocal {

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @Schedule(dayOfMonth = "*", dayOfWeek = "*", hour = "2", info = "Allocate Cars For Today's Reservations at 2am")
    @Override
    public void allocateCarsForToday() throws CarNotFoundException, ReservationNotFoundException, CreateTransitDriverDispatchException {
        //today
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // reset hour, minutes, seconds and millis
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date todayDate = calendar.getTime();

        // next day
        Calendar calendarTmr = Calendar.getInstance();
        calendarTmr.setTime(date);
        calendarTmr.set(Calendar.HOUR_OF_DAY, 0);
        calendarTmr.set(Calendar.MINUTE, 0);
        calendarTmr.set(Calendar.SECOND, 0);
        calendarTmr.set(Calendar.MILLISECOND, 0);
        calendarTmr.add(Calendar.DAY_OF_MONTH, 1);
        Date tmrDate = calendarTmr.getTime();

        List<Reservation> reservationsForTdy = reservationSessionBean.retrieveReservationsByDates(todayDate, tmrDate);

        if (!reservationsForTdy.isEmpty()) {
            for (Reservation reservation : reservationsForTdy) {
                if (reservation.getCar() == null && (!reservation.isCancelled())) {
                    //allocate car here
                    try {
                        reservationSessionBean.allocateCarToReservation(reservation,date);
                    } catch (CarNotFoundException ex) {
                        throw new CarNotFoundException(ex.getMessage() + "for Reservation ID: " + reservation.getReservationId());
                    } catch (CreateTransitDriverDispatchException | InputDataValidationException ex) {
                        throw new CreateTransitDriverDispatchException(ex.getMessage());
                    }
                }
            }
        } else {
            throw new ReservationNotFoundException("No reservations for today");
        }
    }

    @Override
    public void allocateCarsByDate(Date date) throws CarNotFoundException, ReservationNotFoundException, CreateTransitDriverDispatchException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date todayDate = calendar.getTime();

        Calendar calendarTmr = Calendar.getInstance();
        calendarTmr.setTime(date);
        calendarTmr.set(Calendar.HOUR_OF_DAY, 0);
        calendarTmr.set(Calendar.MINUTE, 0);
        calendarTmr.set(Calendar.SECOND, 0);
        calendarTmr.set(Calendar.MILLISECOND, 0);
        calendarTmr.add(Calendar.DAY_OF_MONTH, 1);
        Date tmrDate = calendarTmr.getTime();

        List<Reservation> reservationsForTdy = reservationSessionBean.retrieveReservationsByDates(todayDate, tmrDate);
        if (!reservationsForTdy.isEmpty()) {
            for (Reservation reservation : reservationsForTdy) {
                if (reservation.getCar() == null && (!reservation.isCancelled())) {
                    //allocate car here
                    try {
                        reservationSessionBean.allocateCarToReservation(reservation, date);
                    } catch (CarNotFoundException ex) {
                        throw new CarNotFoundException(ex.getMessage() + "for Reservation ID: " + reservation.getReservationId());
                    } catch (CreateTransitDriverDispatchException | InputDataValidationException ex) {
                        throw new CreateTransitDriverDispatchException(ex.getMessage());
                    }
                }
            }
        } else {
            throw new ReservationNotFoundException("No reservations for today");
        }

    }
}
