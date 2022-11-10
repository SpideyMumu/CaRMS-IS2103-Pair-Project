/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Outlet;
import entity.Reservation;
import entity.TransitDriverDispatch;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.enumeration.CarStatus;
import util.exception.CarNotFoundException;
import util.exception.CarLicensePlateNumExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author muhdm
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @EJB(name = "TransitDriverDispatchSessionBeanLocal")
    private TransitDriverDispatchSessionBeanLocal transitDriverDispatchSessionBeanLocal;

    @EJB(name = "ReservationSessionBeanLocal")
    private ReservationSessionBeanLocal reservationSessionBeanLocal;
    
    

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    

    @Override
    public Long createNewCar(Car newCar) throws CarLicensePlateNumExistException, UnknownPersistenceException {

        try {
            em.persist(newCar);
            em.flush();

            return newCar.getCarId();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new CarLicensePlateNumExistException(ex.getMessage());
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public Car retrieveCarById(Long carId) throws CarNotFoundException{
        Car car = em.find(Car.class, carId);
        if (car != null)
        {
            return car;
        } else
        {
            throw new CarNotFoundException("Car with ID " + carId + " does not exist!");
        }
    }
     
    @Override
    public Car retrieveCarByLicensePlateNum(String licensePlateNum) throws CarNotFoundException {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.licensePlateNum = :currLicensePlateNum");
        query.setParameter("currLicensePlateNum", licensePlateNum);
        
        try {
            return (Car) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CarNotFoundException("Car with license plate number " +  licensePlateNum + " does not exist!");
        }
    }

    @Override
    public List<Car> retrieveAllCars() {
        Query query = em.createQuery("SELECT c FROM Car c");

        return query.getResultList();
    }
    
    public List<Car> retrieveAvailableCars() {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.status = :inStatus");
        query.setParameter("inStatus", CarStatus.Available);
        return query.getResultList();
    }
    
    public List<Car> retrieveDisabledCars() {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.status = :inStatus");
        query.setParameter("inStatus", CarStatus.Disabled);
        return query.getResultList();
    }
    
    public List<Car> retrieveInTransitCars() {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.status = :inStatus");
        query.setParameter("inStatus", CarStatus.InTransit);
        return query.getResultList();
    }
    
    public List<Car> retrieveInOutletCars() {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.status = :inStatus");
        query.setParameter("inStatus", CarStatus.InOutlet);
        return query.getResultList();
    }
    
    public List<Car> retrieveInRepairCars() {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.status = :inStatus");
        query.setParameter("inStatus", CarStatus.Repair);
        return query.getResultList();
    }
    
    public List<Car> retrieveCarsByOutletName(String outletName) {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.outlet.outletName = :inOutletName");
        query.setParameter("inOutletName", outletName);
        return query.getResultList();
    }

    @Override
    public void updateCar(Car car) {
        em.merge(car);
    }

    @Override
    public void deleteCar(Long carId) throws CarNotFoundException
    {
        Car carToRemove = retrieveCarById(carId);
        
        if (carToRemove.getStatus() != CarStatus.OnRental || carToRemove.getStatus() != CarStatus.InTransit || carToRemove.getReservations().isEmpty()) {
            carToRemove.getModel().getCars().remove(carToRemove);
            carToRemove.getOutlet().getCars().remove(carToRemove);
            em.remove(carToRemove);
        } else {
            carToRemove.setStatus(CarStatus.Disabled);
        }
    }
     
    public List<Car> searchCar(Date pickupDate, String pickupOutlet, Date returnDate, String returnOutlet)
    {
        List<Car> allCars = retrieveAllCars();
        List<Car> inRepairCars = retrieveInRepairCars();
        List<Reservation> reservations = reservationSessionBeanLocal.retrieveAllReservations();
        List<Car> inTransitCars = retrieveInTransitCars();
        List<TransitDriverDispatch> transitDriverDispatches = transitDriverDispatchSessionBeanLocal.retrieveAllDispatch();
        List<Car> disabledCars = retrieveDisabledCars();
        
        for (Car car : allCars)
        {
            if (inRepairCars.contains(car)) //remove cars that are under repair
            {
                allCars.remove(car);
            }
        }

        for (Reservation reservation : reservations)
        {
            Date startDate = reservation.getStartDate();
            Date endDate = reservation.getEndDate();
            Calendar startDateCalendar = Calendar.getInstance();
            startDateCalendar.setTime(startDate);
            Calendar endDateCalendar = Calendar.getInstance();
            endDateCalendar.setTime(endDate);

            Calendar pickupDateCalendar = Calendar.getInstance();
            pickupDateCalendar.setTime(pickupDate);
            Calendar returnDateCalendar = Calendar.getInstance();
            returnDateCalendar.setTime(returnDate);
            
            Car car = reservation.getCar();
            
            if ((startDate.before(pickupDate) && pickupDate.before(returnDate)) ||  //check if during that period got reservations
                    ((startDate.before(returnDate)) && (returnDate.before(endDate))))
            {
                if (allCars.contains(car))
                {
                    allCars.remove(car);
                }
            }
            if (startDate.equals(pickupDate) || startDate.equals(returnDate) || endDate.equals(pickupDate) || endDate.equals(returnDate))
            {
                if (allCars.contains(car))
                {
                    allCars.remove(car);
                }
            }
        }
        
        for (TransitDriverDispatch transitDriverDispatch : transitDriverDispatches) //check if during that period got transiting cars
        {
            Date transitStartDate = transitDriverDispatch.getTransitStartDate();
            Date transitEndDate = transitDriverDispatch.getTransitEndDate();
            Calendar transitStartDateCalendar = Calendar.getInstance();
            transitStartDateCalendar.setTime(transitStartDate);
            Calendar transitEndDateCalendar = Calendar.getInstance();
            transitEndDateCalendar.setTime(transitEndDate);
            
            Calendar pickupDateCalendar = Calendar.getInstance();
            pickupDateCalendar.setTime(pickupDate);
            Calendar returnDateCalendar = Calendar.getInstance();
            returnDateCalendar.setTime(returnDate);
            
            Car carInTransit = transitDriverDispatch.getTransitCar();
            
            if (((transitStartDate.before(pickupDate) && pickupDate.before(transitEndDate)) ||
                    (transitStartDate.before(returnDate) && returnDate.before(transitEndDate))))
            {
                if (allCars.contains(carInTransit))
                {
                    allCars.remove(carInTransit);
                }
            }
            if (transitStartDate.equals(pickupDate) || transitStartDate.equals(returnDate) || transitEndDate.equals(pickupDate) || transitEndDate.equals(returnDate))
            {
                if (allCars.contains(carInTransit))
                {
                    allCars.remove(carInTransit);
                }
            }
        }
        
        
        for (Car car : disabledCars) //check for disabled cars
        {
            if (allCars.contains(car))
            {
                allCars.remove(car);
            }
        }
        
        for (Car car : allCars)
        {
            if (!car.getOutlet().equals(pickupOutlet))
            {
                
            }
        }
        return allCars;
    }
}
