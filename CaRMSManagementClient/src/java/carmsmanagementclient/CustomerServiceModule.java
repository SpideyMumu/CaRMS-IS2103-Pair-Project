/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSesionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.UserRole;
import util.exception.InvalidAccessRightException;
import ejb.session.stateless.EmployeeCaRMSSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.Car;
import entity.RentalRate;
import util.enumeration.CarStatus;
import util.exception.CarNotFoundException;
import util.exception.UpdateCarException;

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
    //private CustomerSesionBeanRemote customerSesionBean;
    private CarSessionBeanRemote carSessionBean;

    //Current logged-in user
    private Employee currEmployee;

    public CustomerServiceModule() {
    }

    public CustomerServiceModule(CarSessionBeanRemote carSessionBean, CarCategorySessionBeanRemote carCategorySessionBean, RentalRateSessionBeanRemote rentalRateSessionBean, Employee currEmployee) {
        this.carSessionBean = carSessionBean;
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
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doPickUpCar();
                } else if (response == 2) {
                    doReturnCar();
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 3) {
                break;
            }

        }

    }

    private void doPickUpCar() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** CaRMS :: Pickup Car ***\n");
        System.out.print("Enter License Plate Number>");
        String licensePlateNumber = sc.nextLine();

        try {
            Car car = carSessionBean.retrieveCarByLicensePlateNum(licensePlateNumber);
            System.out.print("Are you sure this car is getting picked up? (y/n)");
            String response = sc.nextLine();
            if (response.equalsIgnoreCase("y")) {
                //update car here
                car.setStatus(CarStatus.OnRental);
                carSessionBean.updateCar(car);
                System.out.println("Car pickedup successfully!");
            } else {
                System.out.println("Pickup Car Canceled! Exiting now...");
            }
        } catch (CarNotFoundException | UpdateCarException ex) {
            System.out.println("Pickup Car Failed! " + ex.getMessage());
        }

    }

    private void doReturnCar() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** CaRMS :: Return Car ***\n");
        System.out.print("Enter License Plate Number>");
        String licensePlateNumber = sc.nextLine();

        try {
            Car car = carSessionBean.retrieveCarByLicensePlateNum(licensePlateNumber);
            System.out.print("Are you sure this car is returned? (y/n)");
            String response = sc.nextLine();
            if (response.equalsIgnoreCase("y")) {
                //update car here - status and location of outlet changed (assume that location will change to logged in employees outlet)
                car.setOutlet(currEmployee.getOutlet());
                car.setStatus(CarStatus.Available);
                carSessionBean.updateCar(car);
                System.out.println("Car returned successfully!");
            } else {
                System.out.println("Return Car Canceled! Exiting now...");
            }
        } catch (CarNotFoundException | UpdateCarException ex) {
            System.out.println("Return Car Failed! " + ex.getMessage());
        }
    }
}
