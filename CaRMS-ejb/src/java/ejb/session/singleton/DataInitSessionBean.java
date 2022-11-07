/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.ModelSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import entity.Car;
import entity.CarCategory;
import entity.Employee;
import entity.Model;
import entity.Outlet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.CarStatus;
import util.enumeration.UserRole;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.EntityNotFoundException;

/**
 *
 * @author muhdm
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @EJB
    private ModelSessionBeanLocal modelSessionBean;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;

    @EJB
    private CarSessionBeanLocal carSessionBean;

    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBean;

    
    
    
    
    public DataInitSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        
        try {
            employeeSessionBean.retrieveEmployeeByUserName("employeeA1");
        } catch (EntityNotFoundException ex) {
            try {
                initializeData();
            } catch (ParseException ex2) {
                ex.printStackTrace();
            }
        }
    }
    
    private void initializeData() throws ParseException {
        
        //Outlets:
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
//        Date time = formatter.parse("10:00");
        
        
        Outlet outletA = new Outlet();
        outletA.setOutletName("Outlet A");
        
        Outlet outletB = new Outlet();
        outletB.setOutletName("Outlet B");
        
        Outlet outletC = new Outlet();
        outletC.setOutletName("Outlet C");
        outletC.setOpeningHour(formatter.parse("10:00"));
        outletC.setClosingHour(formatter.parse("22:00"));
        
        
        
        //Employee
        Employee employeeA1 = new Employee("Employee A1", "employeeA1", UserRole.SALES_MANAGER, outletA);
        Employee employeeA2 = new Employee("Employee A2", "employeeA2", UserRole.OPERATIONS_MANAGER, outletA);
        Employee employeeA3 = new Employee("Employee A3", "employeeA3", UserRole.CS_EXECUTIVE, outletA);
        Employee employeeA4 = new Employee("Employee A4", "employeeA4", UserRole.EMPLOYEE, outletA);
        Employee employeeA5 = new Employee("Employee A5", "employeeA5", UserRole.EMPLOYEE, outletA);
        
        //Associate employees into Outlet list of employees
        
        //Car Category
        CarCategory standardSedan = new CarCategory("Standard Sedan");
        CarCategory familySedan = new CarCategory("Family Sedan");
        CarCategory luxurySedan = new CarCategory("Luxury Sedan");
        CarCategory suvAndMinivan = new CarCategory("SUV and Minivan");
        
        //Model
        Model corolla = new Model("Toyota" , "Corolla", standardSedan);
        Model civic = new Model("Honda" , "Civic", standardSedan);
        Model sunny = new Model("Nissan" , "Sunny", standardSedan);
        Model mercs = new Model("Mercedes", "E Class", luxurySedan);
        Model bmw = new Model("BMW", "5 Series", luxurySedan);
        Model a6 = new Model("Audi", "A6", luxurySedan);
        
        //Associate models into car category list of models 
        
        //Car
        Car car1 = new Car("SS00A1TC", corolla, CarStatus.Available, outletA);
        Car car2 = new Car("SS00A2TC", corolla, CarStatus.Available, outletA);
        Car car3 = new Car("SS00A3TC", corolla, CarStatus.Available, outletA);
        Car car4 = new Car("SS00B1HC", civic, CarStatus.Available, outletB);
        Car car5 = new Car("SS00B2HC", civic, CarStatus.Available, outletB);
        Car car6 = new Car("SS00B3HC", civic, CarStatus.Available, outletB);
        Car car7 = new Car("SS00C1NS", sunny, CarStatus.Available, outletC);
        Car car8 = new Car("SS00C2NS", sunny, CarStatus.Available, outletC);
        Car car9 = new Car("SS00C3NS", sunny, CarStatus.Repair, outletC);
        Car car10 = new Car("LS00A4ME", mercs, CarStatus.Available, outletA);
        Car car11 = new Car("LS00B4B5", bmw, CarStatus.Available, outletB);
        Car car12 = new Car("LS00C4A6", a6, CarStatus.Available, outletC);
        
        //Associate Cars into model list of cars
        

        //RentalRate
        
    }
}
