/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarNotFoundException;
import util.exception.CarLicensePlateNumExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author muhdm
 */
@Local
public interface CarSessionBeanLocal {

    public void deleteCar(Long carId) throws CarNotFoundException;

    public void updateCar(Car car);

    public List<Car> retrieveAllCars();

    public Long createNewCar(Car newCar)throws CarLicensePlateNumExistException, UnknownPersistenceException;

    public Car retrieveCarById(Long carId) throws CarNotFoundException;

    public Car retrieveCarByLicensePlateNum(String licensePlateNum) throws CarNotFoundException;

    public List<Car> retrieveAvailableCars();

    public List<Car> retrieveDisabledCars();

    public List<Car> retrieveInTransitCars();

    public List<Car> retrieveInOutletCars();

    public List<Car> retrieveInRepairCars();

    public List<Car> retrieveCarsByOutletName(String outletName);

    public List<Car> searchCar(Date pickupDate, String pickupOutlet, Date returnDate, String returnOutlet);
    
}
