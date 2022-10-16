/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.ModelSessionBeanLocal;
import entity.Car;
import entity.Model;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author muhdm
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private CarSessionBeanLocal carSessionBean;

    @EJB
    private ModelSessionBeanLocal modelSessionBean;

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct() {
        if(em.find(Model.class, 1l) == null && em.find(Car.class, "ABC1234D") == null) {
            Model firstModel = new Model("Toyota", "Corolla"); //already have empty list of cars
            Car firstCar = new Car("ABC1234D");
            firstCar.setModel(firstModel);
            firstModel.getListOfCars().add(firstCar);
            modelSessionBean.createNewModel(firstModel);
            carSessionBean.createNewCar(firstCar);
        } 
    }
    
    
}
