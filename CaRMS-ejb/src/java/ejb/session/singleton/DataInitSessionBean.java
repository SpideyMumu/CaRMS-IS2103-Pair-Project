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
import util.enumeration.RentalRateType;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarLicensePlateNumExistException;
import util.exception.CreateNewRentalRateException;
import util.exception.UnknownPersistenceException;
import ejb.session.stateless.EmployeeCaRMSSessionBeanLocal;
import util.exception.CarNotFoundException;
import util.exception.CreateNewCarException;
import util.exception.CreateNewEmployeeException;
import util.exception.CreateNewModelException;
import util.exception.EmployeeNotFoundException;
import util.exception.ModelNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import ejb.session.stateless.EmployeeCaRMSSessionBeanLocal;

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
        } catch (EmployeeNotFoundException ex) {
            try {
                initializeData();
            } catch (OutletNotFoundException | ParseException | CarCategoryNotFoundException | EmployeeNotFoundException | ModelNotFoundException
                    | CarNotFoundException | CarLicensePlateNumExistException | UnknownPersistenceException
                    | CreateNewRentalRateException | CreateNewEmployeeException | CreateNewModelException | CreateNewCarException ex2) {
                ex.printStackTrace();
            }
        }
    }

    private void initializeData()
            throws OutletNotFoundException, ParseException, CarCategoryNotFoundException, EmployeeNotFoundException, ModelNotFoundException,
            CarNotFoundException, CarLicensePlateNumExistException, UnknownPersistenceException, CreateNewRentalRateException,
            CreateNewEmployeeException, CreateNewModelException, CreateNewCarException {

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

        employeeSessionBean.createNewEmployee(outletAId, employeeA1);
        employeeSessionBean.createNewEmployee(outletAId, employeeA2);
        employeeSessionBean.createNewEmployee(outletAId, employeeA3);
        employeeSessionBean.createNewEmployee(outletAId, employeeA4);
        employeeSessionBean.createNewEmployee(outletAId, employeeA5);
        employeeSessionBean.createNewEmployee(outletBId, employeeB1);
        employeeSessionBean.createNewEmployee(outletBId, employeeB2);
        employeeSessionBean.createNewEmployee(outletBId, employeeB3);
        employeeSessionBean.createNewEmployee(outletCId, employeeC1);
        employeeSessionBean.createNewEmployee(outletCId, employeeC2);
        employeeSessionBean.createNewEmployee(outletCId, employeeC3);

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
        Model corolla = new Model("Toyota", "Corolla", standardSedan);
        Model civic = new Model("Honda", "Civic", standardSedan);
        Model sunny = new Model("Nissan", "Sunny", standardSedan);
        Model mercs = new Model("Mercedes", "E Class", luxurySedan);
        Model bmw = new Model("BMW", "5 Series", luxurySedan);
        Model a6 = new Model("Audi", "A6", luxurySedan);

        Long corollaId = modelSessionBean.createNewModel(standardSedanId, corolla);
        Long civicId = modelSessionBean.createNewModel(standardSedanId, civic);
        Long sunnyId = modelSessionBean.createNewModel(standardSedanId, sunny);
        Long bmwId = modelSessionBean.createNewModel(luxurySedanId, bmw);
        Long mercsId = modelSessionBean.createNewModel(luxurySedanId, mercs);
        Long a6Id = modelSessionBean.createNewModel(luxurySedanId, a6);

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

        carSessionBean.createNewCar(corollaId, outletAId, car1);
        carSessionBean.createNewCar(corollaId, outletAId, car2);
        carSessionBean.createNewCar(corollaId, outletAId, car3);
        carSessionBean.createNewCar(civicId, outletBId, car4);
        carSessionBean.createNewCar(civicId, outletBId, car5);
        carSessionBean.createNewCar(civicId, outletBId, car6);
        carSessionBean.createNewCar(sunnyId, outletCId, car7);
        carSessionBean.createNewCar(sunnyId, outletCId, car8);
        carSessionBean.createNewCar(sunnyId, outletCId, car9);
        carSessionBean.createNewCar(mercsId, outletAId, car10);
        carSessionBean.createNewCar(bmwId, outletBId, car11);
        carSessionBean.createNewCar(a6Id, outletCId, car12);

        //RentalRate
        SimpleDateFormat rentalDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        RentalRate rentalRate1 = new RentalRate("Standard Sedan - Default", RentalRateType.Default, standardSedan, new BigDecimal(100), null, null);
        RentalRate rentalRate2 = new RentalRate("Standard Sedan - Weekend Promo", RentalRateType.Promotion, standardSedan, new BigDecimal(80), rentalDateFormat.parse("09/12/2022 12:00"), rentalDateFormat.parse("11/12/2022 00:00"));
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
