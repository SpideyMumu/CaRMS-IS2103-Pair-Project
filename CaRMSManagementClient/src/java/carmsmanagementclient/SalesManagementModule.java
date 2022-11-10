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
import ejb.session.stateless.EmployeeSessionBeanRemote;

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
    private EmployeeSessionBeanRemote employeeSessionBean;
    private OutletSessionBeanRemote outletSessionBean;
    private ModelSessionBeanRemote modelSessionBean;
    
    //Current logged-in user
    private Employee currEmployee;

    public SalesManagementModule() {
    }

    public SalesManagementModule(CarSessionBeanRemote carSessionBean, CarCategorySessionBeanRemote carCategorySessionBean, EmployeeSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean, ModelSessionBeanRemote modelSessionBean, Employee currEmployee) {
        this.carSessionBean = carSessionBean;
        this.carCategorySessionBean = carCategorySessionBean;
        this.employeeSessionBean = employeeSessionBean;
        this.outletSessionBean = outletSessionBean;
        this.modelSessionBean = modelSessionBean;
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
            
            while(response < 1 || response > 9)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    
                }
                else if(response == 2)
                {
                    
                }
                else if(response == 3)
                {
                    
                }
                else if(response == 4)
                {
                   
                }
                else if(response == 5)
                {
                    
                }
                else if(response == 6)
                {
                    
                }
                else if (response == 9)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 9)
            {
                break;
            }
        }
    }

    
    
    
    private void salesManagerMenu() //throws InvalidAccessRightException
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
            
            while(response < 1 || response > 7)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    
                }
                else if(response == 2)
                {
                    
                }
                else if(response == 3)
                {
                    
                }
                else if(response == 4)
                {
                    
                }
                else if(response == 5)
                {
                    
                }
                else if(response == 6)
                {
                    
                }
                else if (response == 7)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 7)
            {
                break;
            }
        }
    }
    
    
}
