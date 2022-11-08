/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CarNotFoundException;

/**
 *
 * @author muhdm
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewCar(Car newCar) {
        em.persist(newCar);
        em.flush();
        return newCar.getCarId();
    }

    @Override
    public Car retrieveCarById(Long carId) throws CarNotFoundException{
        Car car = em.find(Car.class, carId);
        if (car != null)
        {
            return car;
        } else
        {
            throw new CarNotFoundException();
        }
    }
    
    @Override
    public Car retrieveCarByLicensePlateNum(String licensePlateNum) {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.licensePlateNum = :currLicensePlateNum");
        query.setParameter("currLicensePlateNum", licensePlateNum);
        
        return (Car) query.getSingleResult(); // implement exception of Car not found
    }

    @Override
    public List<Car> retrieveAllCars() {
        Query query = em.createQuery("SELECT c FROM Car c");

        return query.getResultList();
    }

    @Override
    public void updateCar(Car car) {
        em.merge(car);
    }

    @Override
    public void deleteCar(Long carId) throws CarNotFoundException//throws StaffNotFoundException
    {
        Car carToRemove = retrieveCarById(carId);
        em.remove(carToRemove);
    }

}
