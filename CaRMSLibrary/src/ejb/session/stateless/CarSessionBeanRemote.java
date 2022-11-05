/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author muhdm
 */
@Remote
public interface CarSessionBeanRemote {

    public void deleteCar(Long carId);

    public void updateCar(Car car);

    public List<Car> retrieveAllCars();

    public Long createNewCar(Car newCar);

    public Car retrieveCarById(Long carId);
    
    public Car retrieveCarByLicensePlateNum(String licensePlateNum);
    
}
