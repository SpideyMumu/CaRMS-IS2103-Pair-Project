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
import entity.Model;
import entity.RentalRate;
import java.util.List;

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
    
    public void mainMenu() throws InvalidAccessRightException
    { 
        if(currEmployee.getUserRole()== UserRole.OPERATIONS_MANAGER)
        {
            operationsManagerMenu();
        } 
        else if (currEmployee.getUserRole() == UserRole.SALES_MANAGER) 
        {
            salesManagerMenu();
        } else {
            throw new InvalidAccessRightException("You don't have Manager rights to access the Sales Management module.");
        }
    }
    
    
    private void operationsManagerMenu()
    {
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
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
            
            if(response == 9)
            {
                break;
            }
        }
    }

    private void doCreateNewModel() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Create Model ***\n");
    }
    
    private void doViewAllModels() {
        List<Model> listOfModels = modelSessionBean.retrieveAllModels();
        System.out.println("*** All Models below here***\n");

        for (Model model : listOfModels) {
            System.out.println("Name: " + model.getMakeName() + " " + model.getModelName());
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
        
    }
    
    private void doCreateNewCar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Create Car ***\n");
    }    
    
    private void doViewAllCars() {
         List<Car> listOfCars = carSessionBean.retrieveAllCars();
        System.out.println("*** All Cars below here***\n");

        for (Car car : listOfCars) {
            System.out.println("License Plate: " + car.getLicensePlateNum());
            System.out.println("Model: " + car.getModel().getMakeName() + " " + car.getModel().getModelName());
            System.out.println("Origin Outlet: " + car.getOutlet().getOutletName());
            System.out.println("Colour: " + car.getColor());
            System.out.println("Status: "+ car.getStatus().toString());
            System.out.println("-----------------------");
        }
    }
    
    private void doUpdateCar() {
        
    }
    
    private void doDeleteCar() {
        
    }
    
    private void doViewCarDetails() {
        
    }
    
    // Sales Manager Use Cases Below
    private void salesManagerMenu()
    {
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
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
                        break OUTER;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;                
                }
            }
            
            if(response == 6)
            {
                break;
            }
        }
    }
    
    private void doCreateRentalRate() {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** Create Rental Rate ***\n");
        
        
    }
    
    private void doViewAllRentalRates() {
        //Scanner sc = new Scanner(System.in);
        List<RentalRate> allRentalRates= rentalRateSessionBean.retrieveAllRentalRates();
        System.out.println("*** All Rental Rates below here***\n");
        
        for (RentalRate rate : allRentalRates) {
            System.out.println("Name: " + rate.getName());
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
    }
    
    private void doUpdateRentalRate() {
        Scanner sc = new Scanner(System.in);
    }
    
    private void doDeleteRentalRate() {
        Scanner sc = new Scanner(System.in);
    }
    
}
