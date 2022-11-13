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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarNotFoundException;
import util.exception.CarLicensePlateNumExistException;
import util.exception.CreateNewCarException;
import util.exception.InputDataValidationException;
import util.exception.InvalidSearchCarConditionException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarException;

/**
 *
 * @author muhdm
 */
@Local
public interface CarSessionBeanLocal {

    public void deleteCar(Long carId) throws CarNotFoundException;

    public void updateCar(Car car) throws UpdateCarException;

    public List<Car> retrieveAllCars();

    public Long createNewCar(Car newCar)throws CarLicensePlateNumExistException, UnknownPersistenceException, InputDataValidationException;

    public Car retrieveCarById(Long carId) throws CarNotFoundException;

    public Car retrieveCarByLicensePlateNum(String licensePlateNum) throws CarNotFoundException;

    public Long createNewCar(Long modelId, Long outletId, Car newCar) throws CarLicensePlateNumExistException, UnknownPersistenceException, CreateNewCarException;

    public List<Car> retrieveAvailableCars();
    
    public List<Car> retrieveDisabledCars();

    public List<Car> retrieveInTransitCars();

    public List<Car> retrieveInRepairCars();

    public List<Car> retrieveCarsByOutletName(String outletName);

    public List<Car> retrieveAvailableCarsByModelAndOutlet(Model model, Outlet outlet);

    public List<Car> getCarsForReservationForPickupOutlet(Reservation reservation);

    public List<Car> retrieveAvailableCarsByCategoryAndOutlet(CarCategory category, Outlet outlet);

    public List<Car> getCarsForReservationFromOtherOutlets(Reservation reservation);

    public HashMap<Model, Integer> searchCar(Date pickupDate, String pickupOutlet, Date returnDate, String returnOutlet) throws OutletNotFoundException, InvalidSearchCarConditionException;
    
}
