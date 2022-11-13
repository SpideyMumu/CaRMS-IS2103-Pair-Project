/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.Model;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarNotFoundException;
import util.exception.CarLicensePlateNumExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidSearchCarConditionException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author muhdm
 */
@Remote
public interface CarSessionBeanRemote {

    public void deleteCar(Long carId) throws CarNotFoundException;

    public void updateCar(Car car);

    public List<Car> retrieveAllCars();

    public Long createNewCar(Car newCar)throws CarLicensePlateNumExistException, UnknownPersistenceException, InputDataValidationException;

    public Car retrieveCarById(Long carId) throws CarNotFoundException;
    
    public Car retrieveCarByLicensePlateNum(String licensePlateNum) throws CarNotFoundException;
    
    public List<Car> retrieveAvailableCars();
    
    public List<Car> retrieveDisabledCars();

    public List<Car> retrieveInTransitCars();

    public List<Car> retrieveInRepairCars();

    public List<Car> retrieveCarsByOutletName(String outletName);

    public HashMap<Model, Integer> searchCar(Date pickupDate, String pickupOutlet, Date returnDate, String returnOutlet) throws OutletNotFoundException, InvalidSearchCarConditionException;
    
}
