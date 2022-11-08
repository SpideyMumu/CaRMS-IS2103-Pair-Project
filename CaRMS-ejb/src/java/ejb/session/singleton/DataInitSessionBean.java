/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.ModelSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import entity.Car;
import entity.CarCategory;
import entity.Employee;
import entity.Model;
import entity.Outlet;
import entity.RentalRate;
import java.math.BigDecimal;
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
import util.enumeration.RentalRateType;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarLicensePlateNumExistException;
import util.exception.CreateNewRentalRateException;
import util.exception.EntityNotFoundException;
import util.exception.UnknownPersistenceException;
import ejb.session.stateless.EmployeeCaRMSSessionBeanLocal;
import util.exception.OutletNotFoundException;

/**
 *
 * @author muhdm
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBean;

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @EJB
    private ModelSessionBeanLocal modelSessionBean;

    @EJB
    private EmployeeCaRMSSessionBeanLocal employeeSessionBean;

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
            } catch (OutletNotFoundException | ParseException | CarCategoryNotFoundException | CarLicensePlateNumExistException | UnknownPersistenceException | EntityNotFoundException | CreateNewRentalRateException ex2) {
                ex.printStackTrace();
            }
        }
    }
    
    private void initializeData() throws ParseException, OutletNotFoundException, CarCategoryNotFoundException, CarLicensePlateNumExistException, UnknownPersistenceException, EntityNotFoundException, CreateNewRentalRateException {
        
        //Outlets:
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        
        Outlet outletA = new Outlet();
        outletA.setOutletName("Outlet A");
        
        Outlet outletB = new Outlet();
        outletB.setOutletName("Outlet B");
        
        Outlet outletC = new Outlet();
        outletC.setOutletName("Outlet C");
        outletC.setOpeningHour(formatter.parse("10:00"));
        outletC.setClosingHour(formatter.parse("22:00"));
        
        Long outletAId = outletSessionBean.createNewOutlet(outletA);
        Long outletBId = outletSessionBean.createNewOutlet(outletB);
        Long outletCId = outletSessionBean.createNewOutlet(outletC);
        
        //Employee
        Employee employeeA1 = new Employee("Employee A1", "employeeA1", UserRole.SALES_MANAGER, outletA);
        Employee employeeA2 = new Employee("Employee A2", "employeeA2", UserRole.OPERATIONS_MANAGER, outletA);
        Employee employeeA3 = new Employee("Employee A3", "employeeA3", UserRole.CS_EXECUTIVE, outletA);
        Employee employeeA4 = new Employee("Employee A4", "employeeA4", UserRole.EMPLOYEE, outletA);
        Employee employeeA5 = new Employee("Employee A5", "employeeA5", UserRole.EMPLOYEE, outletA);
        Employee employeeB1 = new Employee("Employee B1", "employeeB1", UserRole.SALES_MANAGER, outletB);
        Employee employeeB2 = new Employee("Employee B2", "employeeB2", UserRole.OPERATIONS_MANAGER, outletB);
        Employee employeeB3 = new Employee("Employee B3", "employeeB3", UserRole.CS_EXECUTIVE, outletB);
        Employee employeeC1 = new Employee("Employee C1", "employeeC1", UserRole.SALES_MANAGER, outletC);
        Employee employeeC2 = new Employee("Employee C2", "employeeC2", UserRole.OPERATIONS_MANAGER, outletC);
        Employee employeeC3 = new Employee("Employee C3", "employeeC3", UserRole.CS_EXECUTIVE, outletC);
        
        employeeSessionBean.createNewEmployee(employeeA1);
        employeeSessionBean.createNewEmployee(employeeA2);
        employeeSessionBean.createNewEmployee(employeeA3);
        employeeSessionBean.createNewEmployee(employeeA4);
        employeeSessionBean.createNewEmployee(employeeA5);
        employeeSessionBean.createNewEmployee(employeeB1);
        employeeSessionBean.createNewEmployee(employeeB2);
        employeeSessionBean.createNewEmployee(employeeB3);
        employeeSessionBean.createNewEmployee(employeeC1);
        employeeSessionBean.createNewEmployee(employeeC2);
        employeeSessionBean.createNewEmployee(employeeC3);
        
        //Associate employees into Outlet list of employees
        //outlet A employees
        Outlet managedOutletA = outletSessionBean.retrieveOutletById(outletAId);
        managedOutletA.getEmployees().add(employeeA1);
        managedOutletA.getEmployees().add(employeeA2);
        managedOutletA.getEmployees().add(employeeA3);
        managedOutletA.getEmployees().add(employeeA4);
        managedOutletA.getEmployees().add(employeeA5);
        
        //outlet B employees
        Outlet managedOutletB = outletSessionBean.retrieveOutletById(outletBId);
        managedOutletB.getEmployees().add(employeeB1);
        managedOutletB.getEmployees().add(employeeB2);
        managedOutletB.getEmployees().add(employeeB3);
        
        //outlet C employees
        Outlet managedOutletC = outletSessionBean.retrieveOutletById(outletCId);
        managedOutletC.getEmployees().add(employeeC1);
        managedOutletC.getEmployees().add(employeeC2);
        managedOutletC.getEmployees().add(employeeC3);
        
        //Car Category
        CarCategory standardSedan = new CarCategory("Standard Sedan");
        CarCategory familySedan = new CarCategory("Family Sedan");
        CarCategory luxurySedan = new CarCategory("Luxury Sedan");
        CarCategory suvAndMinivan = new CarCategory("SUV and Minivan");
        
        Long standardSedanId = carCategorySessionBean.createNewCategory(standardSedan);
        Long familySedanId = carCategorySessionBean.createNewCategory(familySedan);
        Long luxurySedanId = carCategorySessionBean.createNewCategory(luxurySedan);
        Long suvAndMinivanId = carCategorySessionBean.createNewCategory(suvAndMinivan);
        
        //Model
        Model corolla = new Model("Toyota" , "Corolla", standardSedan);
        Model civic = new Model("Honda" , "Civic", standardSedan);
        Model sunny = new Model("Nissan" , "Sunny", standardSedan);
        Model mercs = new Model("Mercedes", "E Class", luxurySedan);
        Model bmw = new Model("BMW", "5 Series", luxurySedan);
        Model a6 = new Model("Audi", "A6", luxurySedan);

        Long corollaId = modelSessionBean.createNewModel(corolla);
        Long civicId = modelSessionBean.createNewModel(civic);
        Long sunnyId = modelSessionBean.createNewModel(sunny);
        Long bmwId = modelSessionBean.createNewModel(bmw);
        Long mercsId = modelSessionBean.createNewModel(mercs);
        Long a6Id = modelSessionBean.createNewModel(a6);
        
        //Associate models into car category list of models 
        CarCategory managedStandardSedan = carCategorySessionBean.retrieveCategoryById(standardSedanId);
        managedStandardSedan.getModels().add(corolla);
        managedStandardSedan.getModels().add(civic);
        managedStandardSedan.getModels().add(sunny);
        
        CarCategory managedluxurySedan = carCategorySessionBean.retrieveCategoryById(luxurySedanId);
        managedluxurySedan.getModels().add(a6);
        managedluxurySedan.getModels().add(bmw);
        managedluxurySedan.getModels().add(mercs);
        
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
        
        carSessionBean.createNewCar(car1);
        carSessionBean.createNewCar(car2);
        carSessionBean.createNewCar(car3);
        carSessionBean.createNewCar(car4);
        carSessionBean.createNewCar(car5);
        carSessionBean.createNewCar(car6);
        carSessionBean.createNewCar(car7);
        carSessionBean.createNewCar(car8);
        carSessionBean.createNewCar(car9);
        carSessionBean.createNewCar(car10);
        carSessionBean.createNewCar(car11);
        carSessionBean.createNewCar(car12);
        
        //Associate Cars into model list of cars
        
        Model managedCorolla = modelSessionBean.retrieveModelById(corollaId);
        managedCorolla.getCars().add(car1);
        managedCorolla.getCars().add(car2);
        managedCorolla.getCars().add(car3);
        
        Model managedCivic = modelSessionBean.retrieveModelById(civicId);
        managedCivic.getCars().add(car4);
        managedCivic.getCars().add(car5);
        managedCivic.getCars().add(car6);
        
        Model managedSunny = modelSessionBean.retrieveModelById(sunnyId);
        managedSunny.getCars().add(car7);
        managedSunny.getCars().add(car8);
        managedSunny.getCars().add(car9);
        
        Model managedMercs = modelSessionBean.retrieveModelById(mercsId);
        managedMercs.getCars().add(car10);
        Model managedBmw = modelSessionBean.retrieveModelById(bmwId);
        managedBmw.getCars().add(car11);
        Model managedA6 = modelSessionBean.retrieveModelById(a6Id);
        managedA6.getCars().add(car12);
        
        //Associate Outlet to car
        managedOutletA.getCars().add(car1);
        managedOutletA.getCars().add(car2);
        managedOutletA.getCars().add(car3);
        
        managedOutletB.getCars().add(car4);
        managedOutletB.getCars().add(car5);
        managedOutletB.getCars().add(car6);
        
        managedOutletC.getCars().add(car7);
        managedOutletC.getCars().add(car8);
        managedOutletC.getCars().add(car9);
        
        managedOutletA.getCars().add(car10);
        managedOutletB.getCars().add(car11);
        managedOutletC.getCars().add(car12);
        
        //RentalRate
        SimpleDateFormat rentalDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        RentalRate rentalRate1 = new RentalRate("Standard Sedan - Default", RentalRateType.Default,  standardSedan, new BigDecimal(100), null, null);
        RentalRate rentalRate2 = new RentalRate("Standard Sedan - Weekend Promo", RentalRateType.Promotion, standardSedan, new BigDecimal(80), rentalDateFormat.parse("09/12/2022 12:00"),  rentalDateFormat.parse("11/12/2022 00:00"));
        RentalRate rentalRate3 = new RentalRate("Family Sedan - Default", RentalRateType.Default, familySedan, new BigDecimal(200), null, null);
        RentalRate rentalRate4 = new RentalRate("Luxury Sedan - Default", RentalRateType.Default, luxurySedan, new BigDecimal(300), null, null);
        RentalRate rentalRate5 = new RentalRate("Luxury Sedan - Monday", RentalRateType.Peak, luxurySedan, new BigDecimal(310), rentalDateFormat.parse("05/12/2022 00:00"), rentalDateFormat.parse("05/12/2022 23:59"));
        RentalRate rentalRate6 = new RentalRate("Luxury Sedan - Tuesday", RentalRateType.Peak, luxurySedan, new BigDecimal(320), rentalDateFormat.parse("06/12/2022 00:00"), rentalDateFormat.parse("06/12/2022 23:59"));
        RentalRate rentalRate7 = new RentalRate("Luxury Sedan - Wednesday", RentalRateType.Peak, luxurySedan, new BigDecimal(320), rentalDateFormat.parse("07/12/2022 00:00"), rentalDateFormat.parse("07/12/2022 23:59"));
        RentalRate rentalRate8 = new RentalRate("Luxury Sedan - Weekday Promo", RentalRateType.Promotion, luxurySedan, new BigDecimal(250), rentalDateFormat.parse("07/12/2022 12:00"), rentalDateFormat.parse("08/12/2022 12:00"));
        RentalRate rentalRate9 = new RentalRate("SUV and Minivan - Default", RentalRateType.Default, suvAndMinivan, new BigDecimal(400), null, null);
        
        rentalRateSessionBean.createNewRentalRate(standardSedanId, rentalRate1);
        rentalRateSessionBean.createNewRentalRate(standardSedanId, rentalRate2);
        rentalRateSessionBean.createNewRentalRate(familySedanId, rentalRate3);
        rentalRateSessionBean.createNewRentalRate(luxurySedanId, rentalRate4);
        rentalRateSessionBean.createNewRentalRate(luxurySedanId, rentalRate5);
        rentalRateSessionBean.createNewRentalRate(luxurySedanId, rentalRate6);
        rentalRateSessionBean.createNewRentalRate(luxurySedanId, rentalRate7);
        rentalRateSessionBean.createNewRentalRate(luxurySedanId, rentalRate8);
        rentalRateSessionBean.createNewRentalRate(suvAndMinivanId, rentalRate9);
        
    }
}
