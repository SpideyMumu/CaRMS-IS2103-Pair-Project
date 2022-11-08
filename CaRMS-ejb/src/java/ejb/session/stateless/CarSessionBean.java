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

}
