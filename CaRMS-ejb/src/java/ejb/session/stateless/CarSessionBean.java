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
import java.util.Set;
import javax.ejb.EJB;
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
import util.enumeration.CarStatus;
import util.exception.CarNotFoundException;
import util.exception.CarLicensePlateNumExistException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.InvalidSearchCarConditionException;
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
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
   
    
    public CarSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    

    @Override
    public Long createNewCar(Car newCar) throws CarLicensePlateNumExistException, UnknownPersistenceException, InputDataValidationException
    {

        Set<ConstraintViolation<Car>>constraintViolations = validator.validate(newCar);
        
        if(constraintViolations.isEmpty())
        {
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
        else
        {
             throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
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
     
    public HashMap<Model, Integer> searchCar(Date pickupDate, String pickupOutlet, Date returnDate, String returnOutlet) throws OutletNotFoundException, InvalidSearchCarConditionException
    {
        
        //HashMap<CarCategory, Integer> categoryQuantity = carCategorySessionBeanLocal.retrieveQuantityOfCarsForEachCategory();

        List<Reservation> reservations = reservationSessionBeanLocal.retrieveAllReservations();
        
        Outlet searchPickupOutlet = outletSessionBeanLocal.retrieveOutletByOutletName(pickupOutlet);
        
        Outlet searchReturnOutlet = outletSessionBeanLocal.retrieveOutletByOutletName(returnOutlet);
        
        List<Car> availableCars = retrieveAvailableCars();
        
        HashMap<CarCategory, Integer> availableQuantity = new HashMap<CarCategory, Integer>();
        
        HashMap<Model, Integer> modelQuantity = new HashMap<Model, Integer>();
        
        for (Car car : availableCars)
        {
            Model model = car.getModel();
            CarCategory carCategory = model.getCarCategory();
            if (!availableQuantity.containsKey(carCategory))
            {
                availableQuantity.put(carCategory, 1);
            } 
            else 
            {
                int quantity = availableQuantity.get(carCategory);
                availableQuantity.replace(carCategory, quantity + 1);
            }
            if (!modelQuantity.containsKey(model))
            {
                modelQuantity.put(model, 1);
            }
            else
            {
                int numOfCars = modelQuantity.get(model);
                modelQuantity.replace(model, numOfCars + 1);
            }
        }
        
        if (searchPickupOutlet.getOpeningHour() != null && searchPickupOutlet.getClosingHour() !=null && searchReturnOutlet.getClosingHour() != null && searchReturnOutlet.getOpeningHour() != null)
        {
            if (pickupDate.before(searchPickupOutlet.getOpeningHour()) || searchPickupOutlet.getClosingHour().before(pickupDate) ||
                    returnDate.before(searchReturnOutlet.getOpeningHour()) || searchReturnOutlet.getClosingHour().before(returnDate))
            {
                throw new InvalidSearchCarConditionException("Pick up date or return date selected is out of outlet opening or closing hours!");
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
            if (!reservation.getIsCancelled())
            {
                Date startDate = reservation.getStartDate();
                Date endDate = reservation.getEndDate();
                CarCategory carCategory = reservation.getCarCategory();
                Outlet startOutlet = reservation.getPickUpLocation();
                Outlet endOutlet = reservation.getReturnLocation();
                Model carModel = reservation.getCarModel();

                int quantityOfCategory;
                int quantityOfModel;

                if ((startDate.before(pickupDateMinusTwoHours) && endDate.after(pickupDateMinusTwoHours) ||
                        (startDate.before(returnDatePlusTwoHours) && endDate.after(returnDatePlusTwoHours))))
                {
                    if (availableQuantity.containsKey(carCategory))
                    {
                        quantityOfCategory = availableQuantity.get(carCategory);
                        if (quantityOfCategory > 0)
                        {
                            availableQuantity.replace(carCategory, quantityOfCategory - 1);
                        }
                    }
                    if (carModel != null)
                    {
                        if (modelQuantity.containsKey(carModel))
                        {
                            quantityOfModel = modelQuantity.get(carModel);
                            if (quantityOfModel > 0)
                            {
                                modelQuantity.replace(carModel, quantityOfModel - 1);
                            }
                        }
                    }
                } 
                else if (searchReturnOutlet != startOutlet && (returnDatePlusTwoHours.after(startDate) || returnDatePlusTwoHours.equals(startDate))) 
                {
                    if (availableQuantity.containsKey(carCategory))
                    {
                        quantityOfCategory = availableQuantity.get(carCategory);
                        if (quantityOfCategory > 0)
                        {
                            availableQuantity.replace(carCategory, quantityOfCategory - 1);
                        }
                    }
                    if (carModel != null)
                    {
                        if (modelQuantity.containsKey(carModel))
                        {
                            quantityOfModel = modelQuantity.get(carModel);
                            if (quantityOfModel > 0)
                            {
                                modelQuantity.replace(carModel, quantityOfModel - 1);
                            }
                        }
                    }
                } else if (searchPickupOutlet != endOutlet && (pickupDateMinusTwoHours.before(endDate) || pickupDateMinusTwoHours.equals(endDate))) {

                    if (availableQuantity.containsKey(carCategory))
                    {
                        quantityOfCategory = availableQuantity.get(carCategory);
                        if (quantityOfCategory > 0)
                        {
                            availableQuantity.replace(carCategory, quantityOfCategory - 1);
                        }
                    }
                    if (carModel != null)
                    {
                        if (modelQuantity.containsKey(carModel))
                        {
                            quantityOfModel = modelQuantity.get(carModel);
                            if (quantityOfModel > 0)
                            {
                                modelQuantity.replace(carModel, quantityOfModel - 1);
                            }
                        }
                    }
                }
            }  
        }
        
        return modelQuantity;
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Car>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
