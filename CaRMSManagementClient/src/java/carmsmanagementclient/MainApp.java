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
import javax.ejb.EJB;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;
import ejb.session.stateless.EmployeeCaRMSSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;

/**
 *
 * @author muhdm
 */
public class MainApp {

    //Remote Session Beans
    private CarSessionBeanRemote carSessionBean;
    private CarCategorySessionBeanRemote carCategorySessionBean;
    private EmployeeCaRMSSessionBeanRemote employeeSessionBean;
    private OutletSessionBeanRemote outletSessionBean;
    private ModelSessionBeanRemote modelSessionBean;
    private RentalRateSessionBeanRemote rentalRateSessionBean;
    
    //Current user
    private Employee currEmployee;
    
    //Sub modules
    private SalesManagementModule salesManagementModule;
    private CustomerServiceModule customerServiceModule;
    
    public MainApp() 
    {        
    }

    public MainApp(CarSessionBeanRemote carSessionBean, CarCategorySessionBeanRemote carCategorySessionBean, EmployeeCaRMSSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean, ModelSessionBeanRemote modelSessionBean, RentalRateSessionBeanRemote rentalRateSessionBean) {
        this.carSessionBean = carSessionBean;
        this.carCategorySessionBean = carCategorySessionBean;
        this.employeeSessionBean = employeeSessionBean;
        this.outletSessionBean = outletSessionBean;
        this.modelSessionBean = modelSessionBean;
        this.rentalRateSessionBean = rentalRateSessionBean;
    }
    
    public void runApp()
    {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Merlion Car Rental Management System ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = sc.nextInt();

                if(response == 1) // implement login here
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        
                        //To update respective module with the currEmployee and all SBs
                        salesManagementModule = new SalesManagementModule(carSessionBean, carCategorySessionBean, employeeSessionBean, outletSessionBean, modelSessionBean, rentalRateSessionBean, currEmployee);
                        customerServiceModule = new CustomerServiceModule(carCategorySessionBean, rentalRateSessionBean, currEmployee);
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    System.out.println("Thank you for using Merlion Car Rental Management System!");
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 2)
            {
                break;
            }
        }
    }
    
    private void doLogin() throws InvalidLoginCredentialException
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** CaRMS :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {      
            currEmployee = employeeSessionBean.employeeLogin(username, password);
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    private void menuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Merlion Car Rental Management System ***\n");
            System.out.println("You are logged in as " + currEmployee.getName() + " with " + currEmployee.getUserRole().toString() + " rights\n");
            System.out.println("1: Sales Management");
            System.out.println("2: Customer Service");
            System.out.println("3: Logout\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    //Sales Management Here
                    try {
                        salesManagementModule.mainMenu();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 2)
                {
                    //Customer service module here
                    try {
                        customerServiceModule.customerServiceMenu();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 3)
                {
                    System.out.println("You have successfully logged out!");
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 3)
            {
                break;
            }
        }
    }
    
}
