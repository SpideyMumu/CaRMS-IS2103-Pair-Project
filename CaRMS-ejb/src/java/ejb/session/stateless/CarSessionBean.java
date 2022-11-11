/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
import entity.Outlet;
import java.util.List;
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
import util.exception.CreateNewCarException;
import util.exception.ModelNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarException;

/**
 *
 * @author muhdm
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @EJB
    private ModelSessionBeanLocal modelSessionBean;

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
    public Long createNewCar(Long modelId, Long outletId, Car newCar) throws CarLicensePlateNumExistException, UnknownPersistenceException, CreateNewCarException {

        try {
            Model model = modelSessionBean.retrieveModelById(modelId);
            if (!model.isEnabled()) {
                throw new CreateNewCarException("Car cannot be created as Model selected is disabled!");
            }

            Outlet outlet = outletSessionBean.retrieveOutletById(outletId);
            newCar.setModel(model);
            newCar.setOutlet(outlet);

            model.getCars().add(newCar);
            outlet.getCars().add(newCar);

            em.persist(newCar);
            em.flush();

            return newCar.getCarId();
        } catch (OutletNotFoundException | ModelNotFoundException | PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new CarLicensePlateNumExistException("License Plate " + newCar.getLicensePlateNum() + " already exists in Database!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public Car retrieveCarById(Long carId) throws CarNotFoundException {
        Car car = em.find(Car.class, carId);
        if (car != null) {
            return car;
        } else {
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
            throw new CarNotFoundException("Car with license plate number " + licensePlateNum + " does not exist!");
        }
    }

    @Override
    public List<Car> retrieveAllCars() {
        Query query = em.createQuery("SELECT c FROM Car c");

        List<Car> cars = query.getResultList();

        cars.sort(((o1, o2) -> {
            if (o1.getModel().getCarCategory().getCategoryId().equals(o2.getModel().getCarCategory().getCategoryId())) {
                if (o1.getModel().getMakeName().equals(o2.getModel().getMakeName())) {
                    if (o1.getModel().getModelName().equals(o2.getModel().getModelName())) {
                        return o1.getLicensePlateNum().compareTo(o2.getLicensePlateNum());
                    }
                    return o1.getModel().getModelName().compareTo(o2.getModel().getModelName());
                }
                return o1.getModel().getMakeName().compareTo(o2.getModel().getMakeName());
            }

            return o1.getModel().getCarCategory().getCategoryId().compareTo(o2.getModel().getCarCategory().getCategoryId());
        }));

        return cars;
    }

    @Override
    public void updateCar(Car newCar) throws UpdateCarException {
        //em.merge(car);

        try {
            Car carToUpdate = retrieveCarById(newCar.getCarId());
            if (carToUpdate.getLicensePlateNum().equals(newCar.getLicensePlateNum())) {
                carToUpdate.setColor(newCar.getColor());
                carToUpdate.setStatus(newCar.getStatus());
                
                //Disassociate Model and Outlet
                carToUpdate.getModel().getCars().size();
                carToUpdate.getModel().getCars().remove(carToUpdate);
                carToUpdate.getOutlet().getCars().size();
                carToUpdate.getOutlet().getCars().remove(carToUpdate);
                
                //Associate model and outlet
                Model newModel = modelSessionBean.retrieveModelById(newCar.getModel().getModelId());
                Outlet newOutlet = outletSessionBean.retrieveOutletById(newCar.getOutlet().getOutletId());
                carToUpdate.setModel(newModel);
                carToUpdate.setOutlet(newOutlet);
                newModel.getCars().add(carToUpdate);
                newOutlet.getCars().add(carToUpdate);
                
                
                
            } else {
                throw new UpdateCarException("Editing License Plate is not allowed! Update Car Operation aborted");
            }
        } catch (CarNotFoundException | ModelNotFoundException | OutletNotFoundException ex) {
            throw new UpdateCarException(ex.getMessage());
        }
    }

    @Override
    public void deleteCar(Long carId) throws CarNotFoundException {
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
