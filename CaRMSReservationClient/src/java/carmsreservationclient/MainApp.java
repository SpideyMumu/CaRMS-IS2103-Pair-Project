/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarRentalCustomerSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.Car;
import entity.CarRentalCustomer;
import entity.Customer;
import entity.Outlet;
import entity.RentalRate;
import entity.Reservation;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.CustomerMobilePhoneExistException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author kathleen
 */
public class MainApp
{
    private CarRentalCustomerSessionBeanRemote carRentalCustomerSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    
    private CarRentalCustomer carRentalCustomer;
      
    public MainApp() 
    {        
    }

    
    
    public MainApp(CarRentalCustomerSessionBeanRemote carRentalCustomerSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote)
    {
        this.carRentalCustomerSessionBeanRemote = carRentalCustomerSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
    }
    
    
    
    public void runApp() throws ParseException
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to CaRMS Reservation System ***\n");
            System.out.println("1: Login");
            System.out.println("2: Register as customer");
            System.out.println("3: Exit\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                       
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    doRegisterCustomer();
                    System.out.println("Register successful!\n");
                    System.out.println("Log in to the system? y/n");
                    
                }
                else if (response == 3)
                {
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
        String mobileNumber = "";
        String password = "";
        
        System.out.println("*** CaRMS Reservation System :: Login ***\n");
        System.out.print("Enter mobile number> ");
        mobileNumber = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(mobileNumber.length() > 0 && password.length() > 0)
        {
            carRentalCustomer = carRentalCustomerSessionBeanRemote.carRentalCustomerLogin(mobileNumber, password);      
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    private void doRegisterCustomer()
    {
        Scanner scanner = new Scanner(System.in);
        
        CarRentalCustomer customer = new CarRentalCustomer();
        
        System.out.println("*** CaRMS Reservation System :: Register as customer *** \n");
        
        System.out.println("Enter mobile number>");
        customer.setMobileNumber(scanner.nextLine().trim());
        System.out.println("Enter full name>");
        customer.setFullName(scanner.nextLine().trim());
        System.out.println("Enter password for account> ");
        customer.setPassword(scanner.nextLine().trim());
        
        try
        {
            Long newCustomerId = carRentalCustomerSessionBeanRemote.createNewCarRentalCustomer(customer);
            System.out.println("Registered successfully!: Your customer Id is " + newCustomerId + "\n");
        }
        catch(CustomerMobilePhoneExistException ex)
        {
            System.out.println("An error has occurred while registering!: The mobile number already exist\n");
        }
        catch(UnknownPersistenceException ex)
        {
            System.out.println("An unknown error has occurred while registering!: " + ex.getMessage() + "\n");
        }
    }
    
    
    
    private void menuMain() throws ParseException
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CaRMS Reservation System ***\n");
            System.out.println("You are login as " + carRentalCustomer.getFullName());
            System.out.println("1: Search car");
            System.out.println("2: Reserve car");
            System.out.println("3: Cancel reservation");
            System.out.println("4: View reservation details");
            System.out.println("5: View all my reservations");
            System.out.println("6: Logout\n");
            response = 0;
            
            while(response < 1 || response > 6)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doSearchCar();
                }
                else if(response == 2)
                {
                    //doReserveCar();
                }
                else if(response == 3)
                {
                    //doCancelReservation();
                }
                else if(response == 4)
                {
                    //doViewReservationDetails();
                }
                else if(response == 5)
                {
                    doViewAllMyReservations();
                }
                else if (response == 6)
                {
                    System.out.println("Log out successfully!");
                    runApp();
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 6)
            {
                break;
            }
        }
    }
    
    private void doSearchCar() throws ParseException
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CaRMS Reservation System :: Search Car ***\n");
        System.out.println("Enter your preferred start date in the format dd/MM/yyyy, e.g. 25/12/2021>");
        String startDate = scanner.nextLine().trim();
        System.out.println("Enter your preferred start time in the format HH:mm, e.g. 12:05>");
        String startTime = scanner.nextLine().trim();   
        String startDateTime = startDate + " " + startTime;
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date searchStartDate = formatter.parse(startDateTime);
        
        System.out.println("Enter your preferred end date in the format dd/MM/yyyy, e.g. 25/12/2021>");
        String endDate = scanner.nextLine().trim();
        System.out.println("Enter your preferred end time in the format HH:mm, e.g. 12:05>");
        String endTime = scanner.nextLine().trim();
        String endDateTime = endDate + " " + endTime;
        
        Date searchEndDate = formatter.parse(endDateTime);
        
        System.out.println("Enter your preferred pick up outlet name>");
        String pickupOutletName = scanner.nextLine().trim();
        
        System.out.println("Enter your preferred return outlet name>");
        String returnOutletName = scanner.nextLine().trim();
        
        List<Car> searchResult = carSessionBeanRemote.searchCar(searchStartDate, pickupOutletName, searchEndDate, returnOutletName);
            
        
    }
    
    private void doViewReservationDetails() throws ReservationNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CaRMS Reservation System :: View Reservation Details");
        
        List<Reservation> myReservations = carRentalCustomer.getReservations();
        
        for (Reservation reservation : myReservations)
        {
            System.out.println("Reservation Id: " + reservation.getReservationId() + ": start date - " + reservation.getStartDate().toString() + ", end date - " + reservation.getEndDate().toString());
        }
        
        System.out.println("Enter the Id of the reservation that you would like to enquire about> ");
        Long id = Long.parseLong(scanner.nextLine().trim());
        
        try
        {
            Reservation reservation = reservationSessionBeanRemote.retrieveReservationById(id);
            
            Date startDate = reservation.getStartDate();
            Date endDate = reservation.getEndDate();
            BigDecimal totalAmountChargeable = reservation.getTotalAmountChargeable();
            Car car = reservation.getCar();
            Outlet pickupLocation = reservation.getPickUpLocation();
            Outlet returnLocation = reservation.getReturnLocation();
            
            System.out.println("start date: " + startDate.toString() + 
                    ", end date: " + endDate.toString() + ", total amount chargeable: $" + totalAmountChargeable.toString() + 
                    ", car license plate number: " + car.getLicensePlateNum() + ", car model: " + car.getModel() +
                    ", pick up outlet: " + pickupLocation.getOutletName() + ", return outlet: " + returnLocation.getOutletName());
        }
        catch(ReservationNotFoundException ex)
        {
            System.out.println("Failed to display reservation details: reservation Id does not exist!");
        }
        
    }
    
    private void doViewAllMyReservations()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CaRMS Reservation System :: View all my Reservation Details");
        List<Reservation> myReservations = carRentalCustomer.getReservations();
        
        int count = 1;
        
        for (Reservation reservation : myReservations)
        {
            Date startDate = reservation.getStartDate();
            Date endDate = reservation.getEndDate();
            BigDecimal totalAmountChargeable = reservation.getTotalAmountChargeable();
            Car car = reservation.getCar();
            Outlet pickupLocation = reservation.getPickUpLocation();
            Outlet returnLocation = reservation.getReturnLocation();
            
            System.out.println(count + ". Reservation Id: " + reservation.getReservationId() + ", start date: " + startDate.toString() + 
                    ", end date: " + endDate.toString() + ", total amount chargeable: $" + totalAmountChargeable.toString() + 
                    ", car license plate number: " + car.getLicensePlateNum() + ", car model: " + car.getModel() +
                    ", pick up outlet: " + pickupLocation.getOutletName() + ", return outlet: " + returnLocation.getOutletName());
            
            count += 1;
        }
    }
    

}