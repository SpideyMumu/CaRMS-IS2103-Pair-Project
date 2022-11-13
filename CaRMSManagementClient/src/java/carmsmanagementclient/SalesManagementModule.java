/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.EJBtimerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.UserRole;
import util.exception.InvalidAccessRightException;
import ejb.session.stateless.EmployeeCaRMSSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchSessionBeanRemote;
import entity.Car;
import entity.CarCategory;
import entity.Model;
import entity.Outlet;
import entity.RentalRate;
import entity.TransitDriverDispatch;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import util.enumeration.CarStatus;
import util.enumeration.RentalRateType;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarLicensePlateNumExistException;
import util.exception.CarNotFoundException;
import util.exception.CreateNewCarException;
import util.exception.CreateNewModelException;
import util.exception.CreateNewRentalRateException;
import util.exception.CreateTransitDriverDispatchException;
import util.exception.EmployeeNotFoundException;
import util.exception.ModelNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.RentalRateNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.TransitDriverDispatchNotFound;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarException;
import util.exception.UpdateModelException;
import util.exception.UpdateRentalRateException;
import util.exception.UpdateTransitDriverDispatchException;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.ModelNameExistException;

/**
 *
 * @author muhdm
 */
/**
 *
 * @author muhdm
 */
public class SalesManagementModule {

    /* 
    This module is only accessible to Sales Manager and Operations Manager
     */
    //Bean Validation
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    //Remote Session Beans
    private CarSessionBeanRemote carSessionBean;
    private CarCategorySessionBeanRemote carCategorySessionBean;
    private EmployeeCaRMSSessionBeanRemote employeeSessionBean;
    private OutletSessionBeanRemote outletSessionBean;
    private ModelSessionBeanRemote modelSessionBean;
    private RentalRateSessionBeanRemote rentalRateSessionBean;
    private TransitDriverDispatchSessionBeanRemote transitDriverDispatchSessionBean;
    private EJBtimerSessionBeanRemote eJBtimerSessionBean;

    //Current logged-in user
    private Employee currEmployee;

    public SalesManagementModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public SalesManagementModule(CarSessionBeanRemote carSessionBean, CarCategorySessionBeanRemote carCategorySessionBean, EmployeeCaRMSSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean,
            ModelSessionBeanRemote modelSessionBean, RentalRateSessionBeanRemote rentalRateSessionBean, TransitDriverDispatchSessionBeanRemote transitDriverDispatchSessionBean, EJBtimerSessionBeanRemote eJBtimerSessionBean,
            Employee currEmployee) {
        this();
        this.carSessionBean = carSessionBean;
        this.carCategorySessionBean = carCategorySessionBean;
        this.employeeSessionBean = employeeSessionBean;
        this.outletSessionBean = outletSessionBean;
        this.modelSessionBean = modelSessionBean;
        this.rentalRateSessionBean = rentalRateSessionBean;
        this.transitDriverDispatchSessionBean = transitDriverDispatchSessionBean;
        this.eJBtimerSessionBean = eJBtimerSessionBean;

        this.currEmployee = currEmployee;
    }

    //This method serves to direct the logged in employee to the correct menu depending on their user role
    public void mainMenu() throws InvalidAccessRightException {
        if (currEmployee.getUserRole() == UserRole.OPERATIONS_MANAGER) {
            operationsManagerMenu();
        } else if (currEmployee.getUserRole() == UserRole.SALES_MANAGER) {
            salesManagerMenu();
        } else {
            throw new InvalidAccessRightException("You don't have Manager rights to access the Sales Management module.");
        }
    }

    //Operations Manager Use Cases below
    private void operationsManagerMenu() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMS :: Sales Management For Operations Manager ***\n");
            System.out.println("1: Create New Model");
            System.out.println("2: View All Model");
            System.out.println("3: Update Model");
            System.out.println("4: Delete Model");
            System.out.println("-----------------------");
            System.out.println("5: Create New Car");
            System.out.println("6: View All Cars");
            System.out.println("7: View Car Details");
            System.out.println("8: Update Car");
            System.out.println("9: Delete Car");
            System.out.println("-----------------------");
            System.out.println("10: View Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("11: Assign Transit Driver");
            System.out.println("12: Update Transit As Completed");
            System.out.println("13: Allocate Cars to Reservations");
            System.out.println("-----------------------");
            System.out.println("14: Back\n");
            response = 0;

            //OUTER:
            while (response < 1 || response > 13) {
                System.out.print("> ");
                response = scanner.nextInt();
                switch (response) {
                    case 1:
                        doCreateNewModel();
                        break;
                    case 2:
                        doViewAllModels();
                        break;
                    case 3:
                        doUpdateModels();
                        break;
                    case 4:
                        doDeleteModel();
                        break;
                    case 5:
                        doCreateNewCar();
                        break;
                    case 6:
                        doViewAllCars();
                        break;
                    case 7:
                        doViewCarDetails();
                        break;
                    case 8:
                        doUpdateCar();
                        break;
                    case 9:
                        doDeleteCar();
                        break;
                    case 10:
                        //System.out.println("Functionality Not Available right now.\n");
                        doViewTransitDriverDispatchRecords();
                        break;
                    case 11:
                        //System.out.println("Functionality Not Available right now.\n");
                        doAssignTransitDriver();
                        break;
                    case 12:
                        //System.out.println("Functionality Not Available right now.\n");
                        doUpdateTransitAsCompleted();
                        break;
                    case 13:
                        doAllocateCars();
                        break;
                    case 14:
                        return;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            }
        }
    }

    private void doCreateNewModel() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Create Model ***\n");
        Model newModel = new Model();

        //Ask make and model
        System.out.println("Type Make and Model name of this new Model:");
        System.out.print("Make Name> ");
        String makeName = sc.nextLine();
        newModel.setMakeName(makeName);
        System.out.print("Model Name> ");
        String modelName = sc.nextLine();
        newModel.setModelName(modelName);

        //Ask which car category it would be in
        List<CarCategory> carCategories = carCategorySessionBean.retrieveAllCategories();
        System.out.println("All Car Cateogries: \n");
        for (CarCategory carCategory : carCategories) {
            System.out.println("Name: " + carCategory.getCategoryName());
            System.out.println("ID: " + carCategory.getCategoryId());
            System.out.println("-----------------------");
        }

        System.out.print("Type the Car Category ID that this model is>");
        Long selection = sc.nextLong();

        try {
            CarCategory category = carCategorySessionBean.retrieveCategoryById(selection);
            newModel.setCarCategory(category);

            //Persist to DB
            Set<ConstraintViolation<Model>> constraintViolations = validator.validate(newModel);
            if (constraintViolations.isEmpty()) {
                Long modelId = modelSessionBean.createNewModel(selection, newModel);
                System.out.println("Succesully created new Model! ModelID is " + modelId + ". Model " + makeName + " " + modelName + ".");
            } else {
                showInputDataValidationErrorsForModel(constraintViolations);
            }
        } catch (CreateNewModelException | CarCategoryNotFoundException | ModelNameExistException | UnknownPersistenceException  ex) {
            System.out.println("Invalid input! " + ex.getMessage());
        }
    }

    private void doViewAllModels() {
        List<Model> listOfModels = modelSessionBean.retrieveAllModels();
        System.out.println("*** All Models below here***\n");

        for (Model model : listOfModels) {
            System.out.println("Name: " + model.getMakeName() + " " + model.getModelName());
            System.out.println("ID: " + model.getModelId());
            System.out.println("Car Category: " + model.getCarCategory().getCategoryName());
            if (model.isEnabled()) {
                System.out.println("Status: Enabled");
            } else {
                System.out.println("Status: Disabled");
            }
            System.out.println("-----------------------");
        }
    }

    private void doUpdateModels() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Update Model ***\n");
        int response;
        System.out.println("Which Model would you like to update?");
        System.out.print("Enter Model ID>");
        Long selection = sc.nextLong();

        try {
            Model model = modelSessionBean.retrieveModelById(selection);
            while (true) {
                System.out.println("You are currently updating " + model.getMakeName() + " " + model.getModelName());
                System.out.println("What would you like to update?");
                System.out.println("1: Make Name");
                System.out.println("2: Model Name");
                System.out.println("3: Car Category");
                System.out.println("4: Disable/Enable Model");
                System.out.println("5: Back \n");
                response = 0;

                while (response < 1 || response > 5) {
                    System.out.print("> ");

                    response = sc.nextInt();
                    sc.nextLine();

                    switch (response) {
                        case 1: // change Make name
                            System.out.print("Enter new Make name> ");
                            String makeName = sc.nextLine();
                            model.setMakeName(makeName);
                            Set<ConstraintViolation<Model>> constraintViolationsForMakeName = validator.validate(model);
                            if (constraintViolationsForMakeName.isEmpty()) {
                                try {
                                    modelSessionBean.updateModel(model);
                                    System.out.println("Successfully Updated Make name!");
                                } catch (UpdateModelException ex) {
                                    System.out.println("Update Failed! " + ex.getMessage());
                                }
                            } else {
                                showInputDataValidationErrorsForModel(constraintViolationsForMakeName);
                            }
                            break;
                        case 2: //change Model name
                            System.out.print("Enter new Model name> ");
                            String modelName = sc.nextLine();
                            model.setModelName(modelName);
                            Set<ConstraintViolation<Model>> constraintViolationsForModelName = validator.validate(model);
                            if (constraintViolationsForModelName.isEmpty()) {
                                try {
                                    modelSessionBean.updateModel(model);
                                    System.out.println("Successfully Updated Model name!");
                                } catch (UpdateModelException ex) {
                                    System.out.println("Update Failed! " + ex.getMessage());
                                }
                            } else {
                                showInputDataValidationErrorsForModel(constraintViolationsForModelName);
                            }
                            break;
                        case 3: //change Car Category
                            System.out.print("Enter new Car Category ID> ");
                            Long carCategoryID = sc.nextLong();
                            Set<ConstraintViolation<Model>> constraintViolationsForCategory = validator.validate(model);
                            if (constraintViolationsForCategory.isEmpty()) {
                                try {
                                    CarCategory newCarCategory = carCategorySessionBean.retrieveCategoryById(carCategoryID);
                                    model.setCarCategory(newCarCategory);
                                    modelSessionBean.updateModel(model);
                                    System.out.println("Successfully Updated Car Category!");
                                } catch (UpdateModelException | CarCategoryNotFoundException ex) {
                                    System.out.println("Update Failed! " + ex.getMessage());
                                }
                            } else {
                                showInputDataValidationErrorsForModel(constraintViolationsForCategory);
                            }
                            break;
                        case 4: //Disable/Enable - no need for bean validation since attribute changed is boolean
                            if (model.isEnabled()) {
                                System.out.print("Model is currently Enabled. ");
                                System.out.print("Would you like to disable it? (y/n) >");
                                if (sc.nextLine().equalsIgnoreCase("y")) {
                                    model.setEnabled(false);
                                    try {
                                        modelSessionBean.updateModel(model);
                                    } catch (UpdateModelException ex) {
                                        System.out.println("Update Failed! " + ex.getMessage());
                                    }
                                }
                            } else {
                                System.out.print("Model is currently Disabled. ");
                                System.out.print("Would you like to enable it? (y/n) >");
                                if (sc.nextLine().equalsIgnoreCase("y")) {
                                    model.setEnabled(true);
                                    try {
                                        modelSessionBean.updateModel(model);
                                    } catch (UpdateModelException ex) {
                                        System.out.println("Update Failed! " + ex.getMessage());
                                    }
                                }
                            }
                            break;
                        case 5:
                            return;
                        default:
                            System.out.println("Invalid option, please try again!\n");
                            break;
                    }
                }
            }
        } catch (ModelNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void doDeleteModel() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Delete Model ***\n");
        System.out.print("Model ID>");
        Long modelId = sc.nextLong();
        sc.nextLine();
        try {
            System.out.print("Are you sure you want to delete this model? (Y/N) >");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                modelSessionBean.deleteModel(modelId);
                System.out.println("Model " + modelId + " is succesfully Deleted!");
            } else {
                System.out.println("Model Deletion Aborted!");
            }
        } catch (ModelNotFoundException ex) {
            System.out.println("Invalid Input! " + ex.getMessage());
        }
    }

    private void showInputDataValidationErrorsForModel(Set<ConstraintViolation<Model>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

    private void doCreateNewCar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Create Car ***\n");
        Car newCar = new Car();
        //Assume new car is always available
        newCar.setStatus(CarStatus.Available);

        //License Plate, colour, model, outlet (currEmployee outlet)
        // Assume that when creating a new car it always starts off at the current logged in employee's outlet
        System.out.print("License Plate Number> ");
        String licensePlateNumber = sc.nextLine();
        newCar.setLicensePlateNum(licensePlateNumber);

        System.out.print("Color> ");
        String color = sc.nextLine();
        newCar.setColor(color);

        //Ask which Model it is
        this.doViewAllModels();
        System.out.print("Type the Model ID that this car is>");
        Long selection = sc.nextLong();

        try {
            Model model = modelSessionBean.retrieveModelById(selection);
            newCar.setModel(model);

            Outlet outlet = outletSessionBean.retrieveOutletById(currEmployee.getOutlet().getOutletId());
            newCar.setOutlet(outlet);
            Set<ConstraintViolation<Car>> constraintViolations = validator.validate(newCar);
            if (constraintViolations.isEmpty()) {
                //assume new car belongs to the outlet of the current employee
                Long carId = carSessionBean.createNewCar(selection, currEmployee.getOutlet().getOutletId(), newCar);
                System.out.println("Car Succesfully created! CarID is " + carId + ".");
            } else {
                showInputDataValidationErrorsForCar(constraintViolations);
            }
        } catch (ModelNotFoundException | CarLicensePlateNumExistException | UnknownPersistenceException | CreateNewCarException | OutletNotFoundException ex) {
            System.out.println("Invalid input! " + ex.getMessage());
        }
    }

    private void doViewAllCars() {
        List<Car> listOfCars = carSessionBean.retrieveAllCars();
        System.out.println("*** All Cars below here***\n");

        for (Car car : listOfCars) {
            System.out.println("License Plate: " + car.getLicensePlateNum());
            System.out.println("CarID : " + car.getCarId());
            System.out.println("Model: " + car.getModel().getMakeName() + " " + car.getModel().getModelName());
            System.out.println("Origin Outlet: " + car.getOutlet().getOutletName());
            System.out.println("Colour: " + car.getColor());
            System.out.println("Status: " + car.getStatus().toString());
            System.out.println("-----------------------");
        }
    }

    private void doUpdateCar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Update Car ***\n");
        int response;
        System.out.println("Which Car would you like to update?");
        System.out.print("Enter Car license plate number>");
        String licensePlateNum = sc.nextLine();

        try {
            Car car = carSessionBean.retrieveCarByLicensePlateNum(licensePlateNum);
            while (true) {
                System.out.println("You are currently updating: ");
                System.out.println("License Plate: " + car.getLicensePlateNum());
                System.out.println("CarID : " + car.getCarId());
                System.out.println("Model: " + car.getModel().getMakeName() + " " + car.getModel().getModelName());
                System.out.println("Origin Outlet: " + car.getOutlet().getOutletName());
                System.out.println("Colour: " + car.getColor());
                System.out.println("Status: " + car.getStatus().toString());
                System.out.println("-----------------------");
                System.out.println("What would you like to update?");
                System.out.println("0: License Plate Number");
                System.out.println("1: Color");
                System.out.println("2: Model");
                System.out.println("3: Original Outlet");
                System.out.println("4: Status");
                System.out.println("5: Back \n");
                response = 0;

                while (response < 1 || response > 5) {
                    System.out.print("> ");

                    response = sc.nextInt();
                    sc.nextLine();

                    switch (response) {
                        case 0:
                            System.out.print("Enter new license plate number> ");
                            String newLicensePlateNum = sc.nextLine();
                            //car.setColor(color);
                            car.setLicensePlateNum(newLicensePlateNum);
                            Set<ConstraintViolation<Car>> constraintViolationsForLicensePlate = validator.validate(car);
                            if (constraintViolationsForLicensePlate.isEmpty()) {
                                try {
                                    carSessionBean.updateCar(car);
                                    System.out.println("Successfully Updated License Plate Number!");
                                } catch (UpdateCarException ex) {
                                    System.out.println("Update Failed! " + ex.getMessage());
                                }
                            } else {
                                showInputDataValidationErrorsForCar(constraintViolationsForLicensePlate);
                            }
                        case 1: // change color
                            System.out.print("Enter new color> ");
                            String color = sc.nextLine();
                            car.setColor(color);
                            Set<ConstraintViolation<Car>> constraintViolationsForColor = validator.validate(car);
                            if (constraintViolationsForColor.isEmpty()) {
                                try {
                                    carSessionBean.updateCar(car);
                                    System.out.println("Successfully Updated Color!");
                                } catch (UpdateCarException ex) {
                                    System.out.println("Update Failed! " + ex.getMessage());
                                }
                            } else {
                                showInputDataValidationErrorsForCar(constraintViolationsForColor);
                            }
                            break;
                        case 2: //change model
                            System.out.print("Enter new Model ID> ");
                            Long modelId = sc.nextLong();
                            Set<ConstraintViolation<Car>> constraintViolationsForModel = validator.validate(car);
                            if (constraintViolationsForModel.isEmpty()) {
                                try {
                                    Model model = modelSessionBean.retrieveModelById(modelId);
                                    car.setModel(model);
                                    carSessionBean.updateCar(car);
                                    System.out.println("Successfully Updated Car Model!");
                                } catch (UpdateCarException | ModelNotFoundException ex) {
                                    System.out.println("Update Failed! " + ex.getMessage());
                                }
                            } else {
                                showInputDataValidationErrorsForCar(constraintViolationsForModel);
                            }
                            break;
                        case 3: //change outlet
                            System.out.print("Enter new Outlet ID> ");
                            Long outletID = sc.nextLong();
                            Set<ConstraintViolation<Car>> constraintViolationsForOutlet = validator.validate(car);
                            if (constraintViolationsForOutlet.isEmpty()) {
                                try {
                                    Outlet newOutlet = outletSessionBean.retrieveOutletById(outletID);
                                    car.setOutlet(newOutlet);
                                    carSessionBean.updateCar(car);
                                    System.out.println("Successfully Updated Car Outlet!");
                                } catch (UpdateCarException | OutletNotFoundException ex) {
                                    System.out.println("Update Failed! " + ex.getMessage());
                                }
                            } else {
                                showInputDataValidationErrorsForCar(constraintViolationsForOutlet);
                            }
                            break;
                        case 4: //change status - default status is always available - no need for bean validation
                            System.out.println("Status is currently: " + car.getStatus().toString());
                            System.out.println("Select new status: ");
                            System.out.print(
                                    "1: Available\n"
                                    + "2: Disabled\n"
                                    + "3: In Outlet\n"
                                    + "4: On Rental\n"
                                    + "5: Repair\n"
                                    + "6: In Transit\n"
                                    + ">");
                            int selection = sc.nextInt();
                            switch (selection) {
                                case 1:
                                    car.setStatus(CarStatus.Available);
                                    break;
                                case 2:
                                    car.setStatus(CarStatus.Disabled);
                                    break;
                                case 3:
                                    car.setStatus(CarStatus.InOutlet);
                                    break;
                                case 4:
                                    car.setStatus(CarStatus.OnRental);
                                    break;
                                case 5:
                                    car.setStatus(CarStatus.Repair);
                                    break;
                                case 6:
                                    car.setStatus(CarStatus.InTransit);
                                    break;
                                default:
                                    System.out.println("Invalid Option!");
                                    break;
                            }
                            try {
                                carSessionBean.updateCar(car);
                                System.out.println("Successfully Updated Car Status to " + car.getStatus());
                            } catch (UpdateCarException ex) {
                                System.out.println("Update Failed! " + ex.getMessage());
                            }
                            break;
                        case 5:
                            return;
                        default:
                            System.out.println("Invalid option, please try again!\n");
                            break;
                    }

                }
            }
        } catch (CarNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doDeleteCar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Delete Car ***\n");
        System.out.print("License Plate Nummber>");
        String licensePlateNum = sc.nextLine();
        try {
            Car carToDelete = carSessionBean.retrieveCarByLicensePlateNum(licensePlateNum);
            // Display Car details here
            System.out.println("License Plate: " + carToDelete.getLicensePlateNum());
            System.out.println("CarID : " + carToDelete.getCarId());
            System.out.println("Model: " + carToDelete.getModel().getMakeName() + " " + carToDelete.getModel().getModelName());
            System.out.println("Origin Outlet: " + carToDelete.getOutlet().getOutletName());
            System.out.println("Colour: " + carToDelete.getColor());
            System.out.println("Status: " + carToDelete.getStatus().toString());
            System.out.println("-----------------------");
            System.out.print("Are you sure you want to delete this car? (Y/N) >");

            if (sc.nextLine().equalsIgnoreCase("y")) {
                carSessionBean.deleteCar(carToDelete.getCarId());
                System.out.println("Car Succesfully Deleted!");
            } else {
                System.out.println("Car Deletion Aborted!");
            }

        } catch (CarNotFoundException ex) {
            System.out.println("Invalid Input! " + ex.getMessage());
        }
    }

    private void doViewCarDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** View Car Details ***");
        System.out.print("Enter License Plate Number>");
        String licensePlateNum = sc.nextLine();
        try {
            Car car = carSessionBean.retrieveCarByLicensePlateNum(licensePlateNum);
            System.out.println("License Plate: " + car.getLicensePlateNum());
            System.out.println("CarID : " + car.getCarId());
            System.out.println("Model: " + car.getModel().getMakeName() + " " + car.getModel().getModelName());
            System.out.println("Origin Outlet: " + car.getOutlet().getOutletName());
            System.out.println("Colour: " + car.getColor());
            System.out.println("Status: " + car.getStatus().toString());
            System.out.println("-----------------------");
        } catch (CarNotFoundException ex) {
            System.out.println("Please type the correct license plate number! " + ex.getMessage());
        }

    }

    private void showInputDataValidationErrorsForCar(Set<ConstraintViolation<Car>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

    private void doViewTransitDriverDispatchRecords() {
        System.out.println("*** View Transit Driver Dispatch Records For Today ***");
        //retrieve records here
        List<TransitDriverDispatch> fullListOfRecords = transitDriverDispatchSessionBean.retrieveAllDispatch();

        List<TransitDriverDispatch> listOfRecordsToday = new LinkedList<>();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // reset hour, minutes, seconds and millis
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // next day
        Calendar calendarTmr = Calendar.getInstance();
        calendarTmr.setTime(date);
        calendarTmr.set(Calendar.HOUR_OF_DAY, 0);
        calendarTmr.set(Calendar.MINUTE, 0);
        calendarTmr.set(Calendar.SECOND, 0);
        calendarTmr.set(Calendar.MILLISECOND, 0);
        calendarTmr.add(Calendar.DAY_OF_MONTH, 1);

        for (TransitDriverDispatch record : fullListOfRecords) {
            Date recordDate = record.getTransitStartDate();
            Calendar recordCalendar = Calendar.getInstance();
            recordCalendar.setTime(recordDate);

            if ((recordCalendar.before(calendarTmr) && recordCalendar.after(calendar)) //in between midnight and tmr midnight
                    || recordCalendar.equals(calendar)) { //transit on midnight
                listOfRecordsToday.add(record);
            }
        }

        for (TransitDriverDispatch record : listOfRecordsToday) {
            System.out.println("ID : " + record.getTransitId());
            System.out.println("Origin Outlet : " + record.getOriginOutlet().getOutletName());
            System.out.println("Return Outlet : " + record.getReturnOutlet().getOutletName());
            System.out.println("Driver name : " + record.getDriver().getName());
            System.out.println("Start : " + record.getTransitStartDate());
            System.out.println("End : " + record.getTransitEndDate());
            System.out.println("Car license plate number: " + record.getTransitCar().getLicensePlateNum());
            System.out.println("-----------------------");
        }
    }

    private void doAssignTransitDriver() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Assign driver to Transit ***\n");
        System.out.println("Enter transit driver dispatch Id > ");
        Long id = sc.nextLong();
        sc.nextLine();

        try {
            TransitDriverDispatch dispatch = transitDriverDispatchSessionBean.retrieveDispatchById(id);
            System.out.println("Transit Driver Dispatch Details:");
            System.out.println("ID : " + dispatch.getTransitId());
            System.out.println("Origin Outlet : " + dispatch.getOriginOutlet().getOutletName());
            System.out.println("Return Outlet : " + dispatch.getReturnOutlet().getOutletName());
            System.out.println("Driver employee name : " + dispatch.getDriver().getName());
            System.out.println("Start : " + dispatch.getTransitStartDate());
            System.out.println("End : " + dispatch.getTransitEndDate());
            System.out.println("Car license plate number: " + dispatch.getTransitCar().getLicensePlateNum());
            System.out.println("-----------------------");

            System.out.println("Are you sure you want to assign a different driver to this transit? (y/n) >");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                System.out.println("Do you want to assign yourself? (y/n) > ");
                if (sc.nextLine().equalsIgnoreCase("y")) {
                    dispatch.setDriver(currEmployee);
                } else {
                    Long employeeID = sc.nextLong();
                    dispatch.setDriver(employeeSessionBean.retrieveEmployeeById(employeeID));
                }
                transitDriverDispatchSessionBean.updateTransitDriverDispatch(dispatch);
                System.out.println("Succesfully assigned new driver!");
            } else {
                System.out.println("Assign driver to Transit Aborted!");
            }
        } catch (TransitDriverDispatchNotFound | UpdateTransitDriverDispatchException | EmployeeNotFoundException ex) {
            System.out.println("Invalid Input!" + ex.getMessage());
        }
    }

    private void doUpdateTransitAsCompleted() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Update Transit as Complete ***\n");
        System.out.println("Enter transit driver dispatch Id > ");
        Long id = sc.nextLong();
        sc.nextLine();
        try {
            TransitDriverDispatch dispatch = transitDriverDispatchSessionBean.retrieveDispatchById(id);
            System.out.println("Transit Driver Dispatch Details:");
            System.out.println("ID : " + dispatch.getTransitId());
            System.out.println("Origin Outlet : " + dispatch.getOriginOutlet().getOutletName());
            System.out.println("Return Outlet : " + dispatch.getReturnOutlet().getOutletName());
            System.out.println("Driver employee name : " + dispatch.getDriver().getName());
            System.out.println("Start : " + dispatch.getTransitStartDate());
            System.out.println("End : " + dispatch.getTransitEndDate());
            System.out.println("Car license plate number: " + dispatch.getTransitCar().getLicensePlateNum());
            System.out.println("-----------------------");
            System.out.println("Are you sure you want to update this transit as complete? (y/n) >");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                dispatch.getTransitCar().setStatus(CarStatus.Available);
                dispatch.setTransitEndDate(new Date());
                carSessionBean.updateCar(dispatch.getTransitCar());
                transitDriverDispatchSessionBean.updateTransitDriverDispatch(dispatch);
                System.out.println("Successfully Update Transit!");
            } else {
                System.out.println("Update Transit Aborted!");
            }
        } catch (TransitDriverDispatchNotFound | UpdateCarException | UpdateTransitDriverDispatchException ex) {
            System.out.println("Invalid Input!" + ex.getMessage());
        }
    }

    private void doAllocateCars() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Car allocation for date ***\n");
        System.out.println("Enter Date for car allocation (Format: dd/MM/yyyy HH:mm) > ");
        String dateString = sc.nextLine();
        SimpleDateFormat rentalDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            Date date = rentalDateFormat.parse(dateString);
            eJBtimerSessionBean.allocateCarsByDate(date);
        } catch (ParseException | CarNotFoundException | ReservationNotFoundException | CreateTransitDriverDispatchException ex) {
            System.out.println("Invalid Input!" + ex.getMessage());
        }

    }

    // Sales Manager Use Cases below
    private void salesManagerMenu() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMS :: Sales Management For Sales Manager ***\n");
            System.out.println("1: Create New Rental Rate");
            System.out.println("2: View All Rental Rates");
            System.out.println("3: View Rental Rate Details");
            System.out.println("4: Update Rental Rate");
            System.out.println("5: Delete Rental Rate");
            System.out.println("-----------------------");
            System.out.println("6: Back\n");
            response = 0;

            OUTER:
            while (response < 1 || response > 7) {
                System.out.print("> ");
                response = scanner.nextInt();
                switch (response) {
                    case 1:
                        doCreateRentalRate();
                        break;
                    case 2:
                        doViewAllRentalRates();
                        break;
                    case 3:
                        doViewRentalRateDetails();
                        break;
                    case 4:
                        doUpdateRentalRate();
                        break;
                    case 5:
                        doDeleteRentalRate();
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            }
        }
    }

    private void doCreateRentalRate() { //Assume Rental rate is enabled by default when creating and new rental rate cannot be default
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Create Rental Rate ***\n");
        RentalRate newRentalRate = new RentalRate();

        System.out.print("Enter rental rate name>");
        newRentalRate.setName(sc.nextLine());

        System.out.print("Enter rate per day>");
        newRentalRate.setRatePerDay(new BigDecimal(sc.nextDouble()));
        sc.nextLine();

        System.out.println("Select Rental rate type: ");
        System.out.print(
                "1: Promotion\n"
                + "2: Peak\n"
                + ">");
        int typeSelection = sc.nextInt();
        if (typeSelection == 1) {
            newRentalRate.setType(RentalRateType.Promotion);
        } else if (typeSelection == 2) {
            newRentalRate.setType(RentalRateType.Peak);
        } else {
            System.out.println("Invalid input! Creating operation failed. Please try again");
            return;
        }

        System.out.println("Select new Car Category: ");
        System.out.print(
                "1: Standard Sedan\n"
                + "2: Family Sedan\n"
                + "3: Luxury Sedan\n"
                + "4: SUV and Minivan\n"
                + ">");
        Long selection = sc.nextLong();
        try {
            CarCategory category = carCategorySessionBean.retrieveCategoryById(selection);
            newRentalRate.setCarCategory(category);
        } catch (CarCategoryNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        sc.nextLine();

        SimpleDateFormat rentalDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            System.out.print("Enter start date (Format: dd/MM/yyyy HH:mm) >");
            String startDateString = sc.nextLine();
            Date startDate = rentalDateFormat.parse(startDateString);
            newRentalRate.setStartDate(startDate);

            //Probably should check if end date is less than start date
            System.out.print("Enter end date (Fomrat: dd/MM/yyyy HH:mm) >");
            String endDateString = sc.nextLine();
            Date endDate = rentalDateFormat.parse(endDateString);
            newRentalRate.setEndDate(endDate);
        } catch (ParseException ex) {
            System.out.println("Please input the dates in the correct format! Creating operation failed. Please try again");
            return;
        }
        Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(newRentalRate);

        if (constraintViolations.isEmpty()) {
            try {
                rentalRateSessionBean.createNewRentalRate(selection, newRentalRate);
                System.out.println("Successfully created new rental rate!");
            } catch (CreateNewRentalRateException | CarCategoryNotFoundException | InputDataValidationException ex) {
                System.out.println("Creating operation failed! " + ex.getMessage());
            }
        } else {
            showInputDataValidationErrorsForRentalRate(constraintViolations);
        }
    }

    private void doViewAllRentalRates() {
        //Scanner sc = new Scanner(System.in);
        List<RentalRate> allRentalRates = rentalRateSessionBean.retrieveAllRentalRates();
        System.out.println("*** All Rental Rates below here***\n");

        for (RentalRate rate : allRentalRates) {
            System.out.println("Name: " + rate.getName());
            System.out.println("ID: " + rate.getRentalRateId());
            System.out.println("Rate per day: " + rate.getRatePerDay());
            System.out.println("Car Category: " + rate.getCarCategory().getCategoryName());
            System.out.println("Type: " + rate.getType());
            if (rate.getStartDate() != null) {
                System.out.println("Start Date: " + rate.getStartDate());
                System.out.println("End Date: " + rate.getEndDate());
            } else {
                System.out.println("Rental Rate is valid forever");
            }
            System.out.println("-----------------------");
        }
    }

    private void doViewRentalRateDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Rental Rate ID> ");
        Long rentalRateId = sc.nextLong();

        try {
            RentalRate rate = rentalRateSessionBean.retrieveRentalRateById(rentalRateId);
            System.out.println("Name: " + rate.getName());
            System.out.println("ID: " + rate.getRentalRateId());
            System.out.println("Rate per day: " + rate.getRatePerDay());
            System.out.println("Car Category: " + rate.getCarCategory().getCategoryName());
            System.out.println("Type: " + rate.getType());
            if (rate.getStartDate() != null) {
                System.out.println("Start Date: " + rate.getStartDate());
                System.out.println("End Date: " + rate.getEndDate());
            } else {
                System.out.println("Rental Rate is valid forever");
            }
            System.out.println("-----------------------");
        } catch (RentalRateNotFoundException ex) {
            System.out.println("Please type the correct ID! " + ex.getMessage());
        }
    }

    private void doUpdateRentalRate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Update Rental Rate ***\n");
        int response;
        System.out.println("Which Rental Rate would you like to update?");
        System.out.print("Enter Rental Rate ID>");
        Long selection = sc.nextLong();
        SimpleDateFormat rentalDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            RentalRate rentalRate = rentalRateSessionBean.retrieveRentalRateById(selection);
            while (true) {
                System.out.println("You are currently updating:");
                //Rental Rate details here
                System.out.println("Name: " + rentalRate.getName());
                System.out.println("ID: " + rentalRate.getRentalRateId());
                System.out.println("Rate per day: " + rentalRate.getRatePerDay());
                System.out.println("Car Category: " + rentalRate.getCarCategory().getCategoryName());
                System.out.println("Type: " + rentalRate.getType());
                if (rentalRate.getStartDate() != null) {
                    System.out.println("Start Date: " + rentalRate.getStartDate());
                    System.out.println("End Date: " + rentalRate.getEndDate());
                } else {
                    System.out.println("Rental Rate is valid forever");
                }
                System.out.println("-----------------------");

                System.out.println("What would you like to update?");
                System.out.println("1: Name");
                System.out.println("2: Rate Per Day");
                System.out.println("3: Car Category");
                System.out.println("4: Start Date");
                System.out.println("5: End Date");
                System.out.println("6: Type");
                System.out.println("7: Back \n");
                response = 0;

                while (response < 1 || response > 7) {
                    System.out.print("> ");
                    response = sc.nextInt();
                    sc.nextLine();

                    switch (response) {
                        case 1:
                            System.out.print("Enter new name>");
                            String name = sc.nextLine();
                            rentalRate.setName(name);
                            Set<ConstraintViolation<RentalRate>> constraintViolationsForName = validator.validate(rentalRate);

                            if (constraintViolationsForName.isEmpty()) {
                                try {

                                    rentalRateSessionBean.updateRentalRate(rentalRate);
                                    System.out.println("Successfully Updated Rate per day!");
                                } catch (UpdateRentalRateException ex) {
                                    System.out.println("Update Failed! " + ex.getMessage());
                                }
                            } else {
                                showInputDataValidationErrorsForRentalRate(constraintViolationsForName);
                            }
                            break;
                        case 2:
                            System.out.print("Enter new rate per day>");
                            BigDecimal ratePerDay = sc.nextBigDecimal();
                            rentalRate.setRatePerDay(ratePerDay);
                            Set<ConstraintViolation<RentalRate>> constraintViolationsForRate = validator.validate(rentalRate);
                            if (constraintViolationsForRate.isEmpty()) {
                                try {
                                    rentalRateSessionBean.updateRentalRate(rentalRate);
                                    System.out.println("Successfully Updated Rate per day!");
                                } catch (UpdateRentalRateException ex) {
                                    System.out.println("Update Failed! " + ex.getMessage());
                                }
                            } else {
                                showInputDataValidationErrorsForRentalRate(constraintViolationsForRate);
                            }
                            break;
                        case 3:
                            System.out.print("Enter new car category ID>");
                            Long carCategoryId = sc.nextLong();
                            try {
                                CarCategory carCategory = carCategorySessionBean.retrieveCategoryById(carCategoryId);
                                rentalRate.setCarCategory(carCategory);
                                rentalRateSessionBean.updateRentalRate(rentalRate);
                                System.out.println("Successfully Updated Car Category!");
                            } catch (CarCategoryNotFoundException | UpdateRentalRateException ex) {
                                System.out.println("Update Failed! " + ex.getMessage());
                            }
                            break;
                        case 4:
                            System.out.print("Enter new start date (Format: dd/MM/yyyy HH:mm) >");
                            String newStartDateString = sc.nextLine();
                            try {
                                rentalRate.setStartDate(rentalDateFormat.parse(newStartDateString));
                                Set<ConstraintViolation<RentalRate>> constraintViolationsForStartDate = validator.validate(rentalRate);
                                if (constraintViolationsForStartDate.isEmpty()) {
                                    rentalRateSessionBean.updateRentalRate(rentalRate);
                                    System.out.println("Successfully Start Date!");
                                } else {
                                    showInputDataValidationErrorsForRentalRate(constraintViolationsForStartDate);
                                }
                            } catch (UpdateRentalRateException | ParseException ex) {
                                System.out.println("Update Failed! " + ex.getMessage());
                            }
                            break;
                        case 5:
                            System.out.print("Enter new end date (Format: dd/MM/yyyy HH:mm) >");
                            String newEndDateString = sc.nextLine();
                            try {
                                rentalRate.setEndDate(rentalDateFormat.parse(newEndDateString));
                                Set<ConstraintViolation<RentalRate>> constraintViolationsForEndDate = validator.validate(rentalRate);
                                if (constraintViolationsForEndDate.isEmpty()) {
                                    rentalRateSessionBean.updateRentalRate(rentalRate);
                                    System.out.println("Successfully Updated End Date!");
                                } else {
                                    showInputDataValidationErrorsForRentalRate(constraintViolationsForEndDate);
                                }
                            } catch (UpdateRentalRateException | ParseException ex) {
                                System.out.println("Update Failed! " + ex.getMessage());
                            }
                            break;
                        case 6:
                            if (rentalRate.getType() == RentalRateType.Default) { //assume that default type rental rates cannot have its rental rate type changed
                                System.out.println("Default Rental Rates cannot have it type changed!Please try again!\n");
                            } else {
                                System.out.println("Select new type: \n1: Promotion \n2:Peak");
                                int typeSelection = sc.nextInt();
                                RentalRateType selectedType = (typeSelection == 1) ? RentalRateType.Promotion : RentalRateType.Peak;
                                try {
                                    rentalRate.setType(selectedType);
                                    rentalRateSessionBean.updateRentalRate(rentalRate);
                                    System.out.println("Successfully Updated Rental Rate type!");
                                } catch (UpdateRentalRateException ex) {
                                    System.out.println("Update Failed! " + ex.getMessage());
                                }
                            }
                            break;
                        case 7:
                            return;
                        default:
                            System.out.println("Invalid option, please try again!\n");
                            break;
                    }
                }
            }
        } catch (RentalRateNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doDeleteRentalRate() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Delete Rental Rate ***\n");
        System.out.print("Rental Rate ID>");
        Long rentalRateId = sc.nextLong();
        sc.nextLine();

        try {
            RentalRate rentalRate = rentalRateSessionBean.retrieveRentalRateById(rentalRateId);
            // Display Rental Rate details here
            System.out.println("Name: " + rentalRate.getName());
            System.out.println("ID: " + rentalRate.getRentalRateId());
            System.out.println("Rate per day: " + rentalRate.getRatePerDay());
            System.out.println("Car Category: " + rentalRate.getCarCategory().getCategoryName());
            System.out.println("Type: " + rentalRate.getType());
            if (rentalRate.getStartDate() != null) {
                System.out.println("Start Date: " + rentalRate.getStartDate());
                System.out.println("End Date: " + rentalRate.getEndDate());
            } else {
                System.out.println("Rental Rate is valid forever");
            }
            System.out.print("Are you sure you want to delete this rental rate? (Y/N) >");

            if (sc.nextLine().equalsIgnoreCase("y")) {
                rentalRateSessionBean.deleteRentalRate(rentalRateId);
                System.out.println("Rental Rate Succesfully Deleted!");
            } else {
                System.out.println("Rental Rate Deletion Aborted!");
            }

        } catch (RentalRateNotFoundException ex) {
            System.out.println("Invalid Input! " + ex.getMessage());
        }
    }

    private void showInputDataValidationErrorsForRentalRate(Set<ConstraintViolation<RentalRate>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}
