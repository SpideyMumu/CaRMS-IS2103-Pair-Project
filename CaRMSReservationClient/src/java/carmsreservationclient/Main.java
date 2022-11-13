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
import java.text.ParseException;
import javax.ejb.EJB;

/**
 *
 * @author muhdm
 */
public class Main {

    @EJB
    private static OutletSessionBeanRemote outletSessionBean;

    @EJB
    private static ModelSessionBeanRemote modelSessionBean;

    @EJB
    private static ReservationSessionBeanRemote reservationSessionBean;

    @EJB
    private static CarRentalCustomerSessionBeanRemote carRentalCustomerSessionBean;

    @EJB
    private static CarSessionBeanRemote carSessionBean;

    @EJB
    private static CarCategorySessionBeanRemote carCategorySessionBean;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(carRentalCustomerSessionBean, carSessionBean, reservationSessionBean, carCategorySessionBean, modelSessionBean, outletSessionBean);
        try {
            mainApp.runApp();
        } catch(ParseException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
