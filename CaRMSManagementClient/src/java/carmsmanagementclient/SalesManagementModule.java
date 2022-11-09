/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.UserRole;
import util.exception.InvalidAccessRightException;
import ejb.session.stateless.EmployeeCaRMSSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.Car;
import entity.CarCategory;
import entity.Model;
import entity.RentalRate;
import java.util.List;
import util.enumeration.CarStatus;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarLicensePlateNumExistException;
import util.exception.CarNotFoundException;
import util.exception.CreateNewCarException;
import util.exception.CreateNewModelException;
import util.exception.ModelNotFoundException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author muhdm
 */
public class SalesManagementModule {

    /* 
    This module is only accessible to Sales Manager and Operations Manager
     */

    //Remote Session Beans
    private CarSessionBeanRemote carSessionBean;
    private CarCategorySessionBeanRemote carCategorySessionBean;
    private EmployeeCaRMSSessionBeanRemote employeeSessionBean;
    private OutletSessionBeanRemote outletSessionBean;
    private ModelSessionBeanRemote modelSessionBean;
    private RentalRateSessionBeanRemote rentalRateSessionBean;

    //Current logged-in user
    private Employee currEmployee;

    public SalesManagementModule() {
    }

    public SalesManagementModule(CarSessionBeanRemote carSessionBean, CarCategorySessionBeanRemote carCategorySessionBean, EmployeeCaRMSSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean, ModelSessionBeanRemote modelSessionBean, RentalRateSessionBeanRemote rentalRateSessionBean, Employee currEmployee) {
        this.carSessionBean = carSessionBean;
        this.carCategorySessionBean = carCategorySessionBean;
        this.employeeSessionBean = employeeSessionBean;
        this.outletSessionBean = outletSessionBean;
        this.modelSessionBean = modelSessionBean;
        this.rentalRateSessionBean = rentalRateSessionBean;
        this.currEmployee = currEmployee;
    }

    public void mainMenu() throws InvalidAccessRightException {
        if (currEmployee.getUserRole() == UserRole.OPERATIONS_MANAGER) {
            operationsManagerMenu();
        } else if (currEmployee.getUserRole() == UserRole.SALES_MANAGER) {
            salesManagerMenu();
        } else {
            throw new InvalidAccessRightException("You don't have Manager rights to access the Sales Management module.");
        }
    }

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
            System.out.println("-----------------------");
            System.out.println("13: Back\n");
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
                        System.out.println("Functionality Not Available right now.\n");
                        break;
                    case 11:
                        System.out.println("Functionality Not Available right now.\n");
                        break;
                    case 12:
                        System.out.println("Functionality Not Available right now.\n");
                        break;
                    case 13:
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
        List<CarCategory> carCategories = carCategorySessionBean.retrieveAllCarCategories();
        System.out.println("All Car Cateogries: \n");
        for (CarCategory carCategory : carCategories) {
            System.out.println("Name: " + carCategory.getCategoryName());
            System.out.println("ID: " + carCategory.getCategoryId());
            System.out.println("-----------------------");
        }

        System.out.print("Type the Car Category ID that this model is>");
        Long selection = sc.nextLong();

        //Persist to DB
        try {
            Long modelId = modelSessionBean.createNewModel(selection, newModel);
            System.out.println("Succesully created new Model! ModelID is " + modelId + ". Model " + makeName + " " + modelName + ".");
        } catch (CreateNewModelException ex) {
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

    private void doCreateNewCar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Create Car ***\n");
        Car newCar = new Car();
        //assume new car is always available
        newCar.setStatus(CarStatus.Available);

        //License Plate, colour, model, outlet (currEmployee outlet)
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
        try { //assume new car belongs to the outlet of the current employee
            Long carId = carSessionBean.createNewCar(selection, currEmployee.getOutlet().getOutletId(), newCar);
            System.out.println("Car Succesfully created! CarID is " + carId + ".");
        } catch (CarLicensePlateNumExistException | UnknownPersistenceException | CreateNewCarException ex) {
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

    // Sales Manager Use Cases Below
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

    private void doCreateRentalRate() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Create Rental Rate ***\n");

    }

    private void doViewAllRentalRates() {
        //Scanner sc = new Scanner(System.in);
        List<RentalRate> allRentalRates = rentalRateSessionBean.retrieveAllRentalRates();
        System.out.println("*** All Rental Rates below here***\n");

        for (RentalRate rate : allRentalRates) {
            System.out.println("Name: " + rate.getName());
            System.out.println("ID: " + rate.getRentalRateId());
            System.out.println("Price per day: " + rate.getRatePerDay());
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
            System.out.println("Price per day: " + rate.getRatePerDay());
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
    }

    private void doDeleteRentalRate() {
        Scanner sc = new Scanner(System.in);
    }

}
