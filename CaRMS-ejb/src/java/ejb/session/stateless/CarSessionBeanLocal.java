/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarNotFoundException;

/**
 *
 * @author muhdm
 */
@Local
public interface CarSessionBeanLocal {

    public void deleteCar(Long carId) throws CarNotFoundException;

    public void updateCar(Car car);

    public List<Car> retrieveAllCars();

    public Long createNewCar(Car newCar);

    public Car retrieveCarById(Long carId) throws CarNotFoundException;

    public Car retrieveCarByLicensePlateNum(String licensePlateNum);
    
}
