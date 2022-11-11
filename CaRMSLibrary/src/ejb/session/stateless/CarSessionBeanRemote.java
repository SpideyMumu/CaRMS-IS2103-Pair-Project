/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarNotFoundException;
import util.exception.CarLicensePlateNumExistException;
import util.exception.CreateNewCarException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarException;

/**
 *
 * @author muhdm
 */
@Remote
public interface CarSessionBeanRemote {

    public void deleteCar(Long carId) throws CarNotFoundException;

    public void updateCar(Car car) throws UpdateCarException;

    public List<Car> retrieveAllCars();

    public Long createNewCar(Car newCar)throws CarLicensePlateNumExistException, UnknownPersistenceException;

    public Car retrieveCarById(Long carId) throws CarNotFoundException;
    
    public Car retrieveCarByLicensePlateNum(String licensePlateNum) throws CarNotFoundException;

    public Long createNewCar(Long modelId, Long outletId, Car newCar) throws CarLicensePlateNumExistException, UnknownPersistenceException, CreateNewCarException;
    
}
