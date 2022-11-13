/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarRentalCustomerSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.Car;
import entity.CarCategory;
import entity.CarRentalCustomer;
import entity.Customer;
import entity.Model;
import entity.Outlet;
import entity.RentalRate;
import entity.Reservation;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CarStatus;
import util.exception.CarCategoryNotFoundException;
import util.exception.CreateReservationException;
import util.exception.CustomerMobilePhoneExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.InvalidSearchCarConditionException;
import util.exception.OutletNotFoundException;
import util.exception.RentalRateNotAvailableException;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author kathleen
 */
public class MainApp {

    private CarRentalCustomerSessionBeanRemote carRentalCustomerSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;

    private CarRentalCustomer carRentalCustomer;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public MainApp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public MainApp(CarRentalCustomerSessionBeanRemote carRentalCustomerSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CarCategorySessionBeanRemote carCategorySessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote) {
        this();
        this.carRentalCustomerSessionBeanRemote = carRentalCustomerSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
    }

    public void runApp() throws ParseException, InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to CaRMS Reservation System ***\n");
            System.out.println("1: Login");
            System.out.println("2: Register as customer");
            System.out.println("3: Search car");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");

                        menuMain();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    doRegisterCustomer();
                    System.out.println("Register successful!\n");
                    System.out.println("Log in to the system? y/n");
                    if (scanner.nextLine().trim().equals("y")) {
                        doLogin();
                    }
                } else if (response == 3) {
                    doSearchCar();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 2) {
                break;
            }
        }
    }

    private void doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String mobileNumber = "";
        String password = "";

        System.out.println("*** CaRMS Reservation System :: Login ***\n");
        System.out.print("Enter mobile number> ");
        mobileNumber = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (mobileNumber.length() > 0 && password.length() > 0) {
            carRentalCustomer = carRentalCustomerSessionBeanRemote.carRentalCustomerLogin(mobileNumber, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void doRegisterCustomer() {
        Scanner scanner = new Scanner(System.in);

        CarRentalCustomer customer = new CarRentalCustomer();

        System.out.println("*** CaRMS Reservation System :: Register as customer *** \n");

        System.out.println("Enter mobile number>");
        customer.setMobileNumber(scanner.nextLine().trim());
        System.out.println("Enter full name>");
        customer.setFullName(scanner.nextLine().trim());
        System.out.println("Enter password for account> ");
        customer.setPassword(scanner.nextLine().trim());
        Set<ConstraintViolation<CarRentalCustomer>> constraintViolations = validator.validate(customer);
        if (constraintViolations.isEmpty()) {
            try {
                Long newCustomerId = carRentalCustomerSessionBeanRemote.createNewCarRentalCustomer(customer);
                System.out.println("Registered successfully!: Your customer Id is " + newCustomerId + "\n");
            } catch (CustomerMobilePhoneExistException ex) {
                System.out.println("An error has occurred while registering!: The mobile number already exist\n");
            } catch (UnknownPersistenceException | InputDataValidationException ex) {
                System.out.println("An unknown error has occurred while registering!: " + ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForCustomer(constraintViolations);
        }
    }

    private void menuMain() throws InvalidLoginCredentialException, ParseException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMS Reservation System ***\n");
            System.out.println("You are login as " + carRentalCustomer.getFullName());
            System.out.println("1: Search car");
            System.out.println("2: Reserve car");
            System.out.println("3: Cancel reservation");
            System.out.println("4: View reservation details");
            System.out.println("5: View all my reservations");
            System.out.println("6: Logout\n");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doSearchCar();
                } else if (response == 2) {
                    doReserveCar();
                } else if (response == 3) {
                    doCancelReservation();
                } else if (response == 4) {
                    doViewReservationDetails();
                } else if (response == 5) {
                    doViewAllMyReservations();
                } else if (response == 6) {
                    System.out.println("Log out successfully!");
                    runApp();
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 6) {
                break;
            }
        }
    }

    private void doCancelReservation() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CaRMS Reservation System :: Cancel reservation ***\n");
        System.out.println();
        doViewAllMyReservations();

        while (true) {
            System.out.println("Enter Id of reservation that you would like to cancel>");
            Long id = Long.parseLong(scanner.nextLine().trim());

            try {
                Reservation reservation = reservationSessionBeanRemote.retrieveReservationById(id);
                if (reservation.isCancelled()) {
                    System.out.println("Reservation with Id " + id + " was already cancelled!");
                } else {
                    if (reservation.getCar() != null) {
                        Car car = reservation.getCar();
                        car.setStatus(CarStatus.Available);
                    }
                    reservation.setIsCancelled(true);

                    Date pickupDate = reservation.getStartDate();
                    Calendar pickupCalendar = Calendar.getInstance();
                    pickupCalendar.setTime(pickupDate);
                    int pickupDay = pickupCalendar.get(Calendar.DAY_OF_MONTH);

                    Date today = new Date();
                    Calendar cancelCalendar = Calendar.getInstance();
                    cancelCalendar.setTime(today);
                    int cancelDay = pickupCalendar.get(Calendar.DAY_OF_MONTH);

                    BigDecimal amount = reservation.getTotalAmountChargeable();
                    BigDecimal refund;
                    BigDecimal penalty;

                    if (!reservation.getPayOnPickup()) {
                        if (pickupDay - cancelDay >= 7 && pickupDay - cancelDay < 14) {
                            refund = amount.multiply(new BigDecimal(0.80));
                            penalty = amount.multiply(new BigDecimal(0.20));
                            System.out.println("$" + refund.toString() + " is refunded to your credit card, with a 20% penalty of $" + penalty.toString());
                        } else if (pickupDay - cancelDay >= 3 && pickupDay - cancelDay < 7) {
                            refund = amount.multiply(new BigDecimal(0.50));
                            System.out.println("$" + refund.toString() + " is refunded to your credit card, with a 50% penalty of $" + refund.toString());
                        } else if (pickupDay - cancelDay < 3) {
                            refund = amount.multiply(new BigDecimal(0.30));
                            penalty = amount.multiply(new BigDecimal(0.70));
                            System.out.println("$" + refund.toString() + " is refunded to your credit card, with a 70% penalty of $" + penalty.toString());
                        }
                    } else if (reservation.getPayOnPickup()) {
                        if (pickupDay - cancelDay >= 7 && pickupDay - cancelDay < 14) {
                            penalty = amount.multiply(new BigDecimal(0.20));
                            System.out.println("20% penalty applied, $" + penalty.toString() + " will be deducted from your credit card");
                        } else if (pickupDay - cancelDay >= 3 && pickupDay - cancelDay < 7) {
                            penalty = amount.multiply(new BigDecimal(0.50));
                            System.out.println("50% penalty applied, $" + penalty.toString() + " will be deducted from your credit card");
                        } else if (pickupDay - cancelDay < 3) {
                            penalty = amount.multiply(new BigDecimal(0.70));
                            System.out.println("70% penalty applied, $" + penalty.toString() + " will be deducted from your credit card");
                        }
                    }
                }

            } catch (ReservationNotFoundException ex) {
                System.out.println("An error has occurred while cancelling reservation: " + ex.getMessage() + "!\n");
            }
        }
    }

    private void doReserveCar() throws ParseException {
        Scanner scanner = new Scanner(System.in);
        Reservation reservation = new Reservation();

        try {
            System.out.println("*** CaRMS Reservation System :: Reserve Car ***\n");
            System.out.println();
            System.out.println("Enter your preferred start date in the format dd/MM/yyyy, e.g. 25/12/2021>");
            String startDate = scanner.nextLine().trim();
            System.out.println("Enter your preferred start time in the format HH:mm, e.g. 12:05>");
            String startTime = scanner.nextLine().trim();
            String startDateTime = startDate + " " + startTime;

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date searchStartDate = formatter.parse(startDateTime);

            reservation.setStartDate(searchStartDate);

            System.out.println("Enter your preferred end date in the format dd/MM/yyyy, e.g. 25/12/2021>");
            String endDate = scanner.nextLine().trim();
            System.out.println("Enter your preferred end time in the format HH:mm, e.g. 12:05>");
            String endTime = scanner.nextLine().trim();
            String endDateTime = endDate + " " + endTime;

            Date searchEndDate = formatter.parse(endDateTime);

            reservation.setEndDate(searchEndDate);

            System.out.println("Enter your preferred pick up outlet name>");
            String pickupOutletName = scanner.nextLine().trim();

            Outlet pickupOutlet = outletSessionBeanRemote.retrieveOutletByOutletName(pickupOutletName);
            Long pickupOutletId = pickupOutlet.getOutletId();

            System.out.println("Enter your preferred return outlet name>");
            String returnOutletName = scanner.nextLine().trim();

            Outlet returnOutlet = outletSessionBeanRemote.retrieveOutletByOutletName(returnOutletName);
            Long returnOutletId = returnOutlet.getOutletId();

            HashMap<Model, Integer> searchResult = carSessionBeanRemote.searchCar(searchStartDate, pickupOutletName, searchEndDate, returnOutletName);

            List<CarCategory> listOfCategoriesAvailable = new ArrayList<CarCategory>();

            for (Map.Entry<Model, Integer> map : searchResult.entrySet()) {
                if (map.getValue() > 0) {
                    CarCategory carCat = map.getKey().getCarCategory();
                    if (!listOfCategoriesAvailable.contains(carCat)) {
                        listOfCategoriesAvailable.add(carCat);
                    }
                }
            }

            String carCategory;
            String carModel;
            Model model;
            CarCategory category;
            Long modelId;
            Long categoryId;

            while (true) {
                System.out.println("Enter 1 if you have a preference for car category, or 2 if you have a preference for car model>");
                if (scanner.nextLine().trim().equals("1")) {
                    System.out.println("Enter your preferred car category>");
                    carCategory = scanner.nextLine().trim();
                    category = carCategorySessionBeanRemote.retrieveCarCategoryByName(carCategory);
                    categoryId = category.getCategoryId();
                    if (!listOfCategoriesAvailable.contains(category)) {
                        System.out.println("Car Category " + carCategory + " is not available for reservation!");
                    } else {
                        break;
                    }
                } else if (scanner.nextLine().trim().equals("2")) {
                    System.out.println("Enter your preferred car model>");
                    carModel = scanner.nextLine().trim();
                    model = modelSessionBeanRemote.retrieveModelByName(carModel);

                    if (!searchResult.containsKey(model) || searchResult.get(model) <= 0) {
                        System.out.println("Car model " + carModel + " is not available for reservation!");
                    } else {
                        reservation.setCarModel(model);
                        category = model.getCarCategory();
                        categoryId = category.getCategoryId();
                        break;
                    }
                } else {
                    System.out.println("Invalid input, please enter again>");
                }
            }

            HashMap<CarCategory, BigDecimal> rates = carCategorySessionBeanRemote.calculatePrevailingRentalFeeForEachCategories(listOfCategoriesAvailable, searchStartDate, searchEndDate);

            BigDecimal amount = rates.get(category);

            reservation.setTotalAmountChargeable(amount);

            System.out.println("The total amount chargeable for the reservation period and car category / car model is : $" + amount.toString());

            String creditCardNumber;
            String cvv;

            System.out.println("Default payment is done now, do you want to switch to payment on time of pickup? y/n>");

            if (scanner.nextLine().trim().equals("y")) {
                reservation.setPayOnPickup(true);
            }
            System.out.println("Enter your credit card number>");
            creditCardNumber = scanner.nextLine().trim();

            reservation.setCreditCardNumber(creditCardNumber);

            System.out.println("Enter cvv number>");
            cvv = scanner.nextLine().trim();

            reservation.setCvv(cvv);

            Long reservationId = reservationSessionBeanRemote.createNewReservation(categoryId, pickupOutletId, returnOutletId, this.carRentalCustomer.getCustomerId(), reservation);
            System.out.println("Reservation made successfully! Reservation Id : " + reservationId);
        } catch (OutletNotFoundException | CreateReservationException | CarCategoryNotFoundException | CustomerNotFoundException | InvalidSearchCarConditionException | RentalRateNotAvailableException | InputDataValidationException ex) {
            System.out.println("An error has occurred while performing reservation: " + ex.getMessage() + "!\n");
        }

    }

    private void doSearchCar() throws ParseException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CaRMS Reservation System :: Search Car ***\n");
        System.out.println();
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

        try {
            HashMap<Model, Integer> searchResult = carSessionBeanRemote.searchCar(searchStartDate, pickupOutletName, searchEndDate, returnOutletName);
            List<CarCategory> listOfCategoriesAvailable = new ArrayList<CarCategory>();

            for (Map.Entry<Model, Integer> map : searchResult.entrySet()) {
                if (map.getValue() > 0) {
                    CarCategory carCat = map.getKey().getCarCategory();
                    if (!listOfCategoriesAvailable.contains(carCat)) {
                        listOfCategoriesAvailable.add(carCat);
                    }
                }
            }

            HashMap<CarCategory, BigDecimal> rates = carCategorySessionBeanRemote.calculatePrevailingRentalFeeForEachCategories(listOfCategoriesAvailable, searchStartDate, searchEndDate);

            if (rates.size() <= 0) {
                System.out.println("No available cars for the period that you have searched");
            }

            for (Map.Entry<CarCategory, BigDecimal> rentalFee : rates.entrySet()) {
                CarCategory carCategory = rentalFee.getKey();
                BigDecimal rate = rentalFee.getValue();
                System.out.println("Car Category " + carCategory.getCategoryName() + " is available at $" + rate.toString());
                System.out.println("Models available for Car Category " + carCategory.getCategoryName() + " are : ");

                for (Map.Entry<Model, Integer> map : searchResult.entrySet()) {
                    Model model = map.getKey();
                    int num = map.getValue();
                    if (model.getCarCategory().equals(carCategory) && num > 0) {
                        System.out.println(model.getModelName());
                    }
                }
            }
        } catch (OutletNotFoundException | InvalidSearchCarConditionException | RentalRateNotAvailableException ex) {
            System.out.println("An error has occurred while searching car: " + ex.getMessage() + "!\n");
        }
    }

    private void doViewReservationDetails() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CaRMS Reservation System :: View Reservation Details");
        System.out.println();

        List<Reservation> myReservations = carRentalCustomer.getReservations();

        for (Reservation reservation : myReservations) {
            System.out.println("Reservation Id: " + reservation.getReservationId() + ": start date - " + reservation.getStartDate().toString() + ", end date - " + reservation.getEndDate().toString());
        }

        System.out.println("Enter the Id of the reservation that you would like to enquire about> ");
        Long id = Long.parseLong(scanner.nextLine().trim());

        try {
            Reservation reservation = reservationSessionBeanRemote.retrieveReservationById(id);

            Date startDate = reservation.getStartDate();
            Date endDate = reservation.getEndDate();
            BigDecimal totalAmountChargeable = reservation.getTotalAmountChargeable();
            Car car = reservation.getCar();
            Outlet pickupLocation = reservation.getPickUpLocation();
            Outlet returnLocation = reservation.getReturnLocation();

            System.out.println("Reservation Id : " + reservation.getReservationId());
            System.out.println("Start Date and Time : " + startDate.toString());
            System.out.println("End Date and Time : " + endDate.toString());
            System.out.println("Total amount chargeable : $" + totalAmountChargeable.toString());
            System.out.println("Pick up outlet : " + pickupLocation.getOutletName());
            System.out.println("Return outlet : " + returnLocation.getOutletName());
            if (reservation.getCarModel() != null) {
                System.out.println("Car Model : " + reservation.getCarModel());
            }
            System.out.println("Car Category : " + reservation.getCarCategory());
            System.out.println("Cancelled : " + reservation.isCancelled().toString());
        } catch (ReservationNotFoundException ex) {
            System.out.println("An error has occurred while viewing reservation details: " + ex.getMessage() + "!\n");
        }

    }

    private void doViewAllMyReservations() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CaRMS Reservation System :: View all my Reservation Details");
        System.out.println();
        List<Reservation> myReservations = carRentalCustomer.getReservations();

        for (Reservation reservation : myReservations) {
            Date startDate = reservation.getStartDate();
            Date endDate = reservation.getEndDate();
            BigDecimal totalAmountChargeable = reservation.getTotalAmountChargeable();
            Car car = reservation.getCar();
            Outlet pickupLocation = reservation.getPickUpLocation();
            Outlet returnLocation = reservation.getReturnLocation();

            System.out.println("Reservation Id : " + reservation.getReservationId());
            System.out.println("Start Date and Time : " + startDate.toString());
            System.out.println("End Date and Time : " + endDate.toString());
            System.out.println("Total amount chargeable : $" + totalAmountChargeable.toString());
            System.out.println("Pick up outlet : " + pickupLocation.getOutletName());
            System.out.println("Return outlet : " + returnLocation.getOutletName());
            if (reservation.getCarModel() != null) {
                System.out.println("Car Model : " + reservation.getCarModel());
            }
            System.out.println("Car Category : " + reservation.getCarCategory());
            System.out.println();

        }
    }

    private void showInputDataValidationErrorsForCustomer(Set<ConstraintViolation<CarRentalCustomer>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}
