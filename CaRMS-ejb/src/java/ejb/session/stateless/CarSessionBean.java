/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.Model;
import entity.Outlet;
import entity.Reservation;
import entity.TransitDriverDispatch;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author muhdm
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @EJB(name = "OutletSessionBeanLocal")
    private OutletSessionBeanLocal outletSessionBeanLocal;

    @EJB(name = "ModelSessionBeanLocal")
    private ModelSessionBeanLocal modelSessionBeanLocal;

    @EJB(name = "CarCategorySessionBeanLocal")
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;

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
     
    public HashMap<CarCategory, Integer> searchCar(Date pickupDate, String pickupOutlet, Date returnDate, String returnOutlet) throws OutletNotFoundException
    {
        
        HashMap<CarCategory, Integer> categoryQuantity = carCategorySessionBeanLocal.retrieveQuantityOfCarsForEachCategory();

        List<Reservation> reservations = reservationSessionBeanLocal.retrieveAllReservations();
        
        Outlet searchPickupOutlet = outletSessionBeanLocal.retrieveOutletByOutletName(pickupOutlet);
        
        Outlet searchReturnOutlet = outletSessionBeanLocal.retrieveOutletByOutletName(returnOutlet);
        
        List<Car> availableCars = retrieveAvailableCars();
        
        for (Car car : availableCars)
        {
            Model model = car.getModel();
            CarCategory carCategory = model.getCarCategory();
            if (!categoryQuantity.containsKey(carCategory))
            {
                categoryQuantity.put(carCategory, 1);
            } 
            else 
            {
                int quantity = categoryQuantity.get(carCategory);
                categoryQuantity.replace(carCategory, quantity + 1);
            }
        }
        
        Calendar pickupDateCalendarMinusTwoHours = Calendar.getInstance();
        pickupDateCalendarMinusTwoHours.setTime(pickupDate);
        pickupDateCalendarMinusTwoHours.add(pickupDateCalendarMinusTwoHours.HOUR_OF_DAY, -2);
        Date pickupDateMinusTwoHours = pickupDateCalendarMinusTwoHours.getTime();
        
        Calendar returnDateCalendarPlusTwoHours = Calendar.getInstance();
        returnDateCalendarPlusTwoHours.setTime(returnDate);
        returnDateCalendarPlusTwoHours.add(returnDateCalendarPlusTwoHours.HOUR_OF_DAY, 2);
        Date returnDatePlusTwoHours = returnDateCalendarPlusTwoHours.getTime();
        
        for(Reservation reservation : reservations)
        {
            Date startDate = reservation.getStartDate();
            Date endDate = reservation.getEndDate();
            CarCategory carCategory = reservation.getCarCategory();
            Outlet startOutlet = reservation.getPickUpLocation();
            Outlet endOutlet = reservation.getReturnLocation();
            
            int quantity;
            
            if ((startDate.before(pickupDateMinusTwoHours) && endDate.after(pickupDateMinusTwoHours) ||
                    (startDate.before(returnDatePlusTwoHours) && endDate.after(returnDatePlusTwoHours))))
            {
                if (categoryQuantity.containsKey(carCategory))
                {
                    quantity = categoryQuantity.get(carCategory);
                    if (quantity > 0)
                    {
                        categoryQuantity.replace(carCategory, quantity - 1);
                    }
                } 
            } 
            else if (searchReturnOutlet != startOutlet && (returnDatePlusTwoHours.after(startDate) || returnDatePlusTwoHours.equals(startDate))) 
            {
                if (categoryQuantity.containsKey(carCategory))
                {
                    quantity = categoryQuantity.get(carCategory);
                    if (quantity > 0)
                    {
                        categoryQuantity.replace(carCategory, quantity - 1);
                    }
                }
            } else if (searchPickupOutlet != endOutlet && (pickupDateMinusTwoHours.before(endDate) || pickupDateMinusTwoHours.equals(endDate))) {
               
                if (categoryQuantity.containsKey(carCategory))
                {
                    quantity = categoryQuantity.get(carCategory);
                    if (quantity > 0)
                    {
                        categoryQuantity.replace(carCategory, quantity - 1);
                    }
                }
            }
        }
        
        return categoryQuantity;
    }
}
