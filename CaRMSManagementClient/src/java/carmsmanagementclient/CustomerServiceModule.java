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
import entity.RentalRate;

/**
 *
 * @author muhdm
 */
public class CustomerServiceModule {

    /* 
    This module is only accessible to Customer Service Executive
    Use Cases:
    1. Pickup Car
    2. Return Car
     */
    //Session Beans
    private CarCategorySessionBeanRemote carCategorySessionBean;
    private RentalRateSessionBeanRemote rentalRateSessionBean;
    
    //Current logged-in user
    private Employee currEmployee;

    public CustomerServiceModule() {
    }

    public CustomerServiceModule(CarCategorySessionBeanRemote carCategorySessionBean, RentalRateSessionBeanRemote rentalRateSessionBean, Employee currEmployee) {
        this.carCategorySessionBean = carCategorySessionBean;
        this.rentalRateSessionBean = rentalRateSessionBean;
        this.currEmployee = currEmployee;
    }
    
    public void customerServiceMenu() throws InvalidAccessRightException {
        if (currEmployee.getUserRole() != UserRole.CS_EXECUTIVE) {
            throw new InvalidAccessRightException("You don't have Customer Service Executive rights to access the customer service module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMS :: Customer Service ***\n");
            System.out.println("1: Pickup Car");
            System.out.println("2: Return Car");
            System.out.println("3: Back\n");

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {

                } else if (response == 2) {

                } else if (response == 3) {
                    break;
                }
            }

        }

    }
}
