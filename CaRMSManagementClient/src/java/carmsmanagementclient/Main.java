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
import javax.ejb.EJB;
import ejb.session.stateless.EmployeeCaRMSSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.TransitDriveDispatchSessionBeanRemote;

/**
 *
 * @author muhdm
 */
public class Main {

    //All Remote Session Bean here
    @EJB
    private static CarSessionBeanRemote carSessionBean;
    @EJB
    private static CarCategorySessionBeanRemote carCategorySessionBean;
    @EJB
    private static EmployeeCaRMSSessionBeanRemote employeeSessionBean;
    @EJB
    private static OutletSessionBeanRemote outletSessionBean;
    @EJB
    private static ModelSessionBeanRemote modelSessionBean;
     @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBean;
    @EJB
    private static TransitDriveDispatchSessionBeanRemote transitDriveDispatchSessionBean;

    
    
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(carSessionBean, carCategorySessionBean, employeeSessionBean, outletSessionBean, modelSessionBean, rentalRateSessionBean, transitDriveDispatchSessionBean);
        mainApp.runApp();
    }
    
}
