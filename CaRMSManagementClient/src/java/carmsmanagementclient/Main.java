/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import com.sun.org.apache.xpath.internal.operations.Mod;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import entity.Car;
import entity.Model;
import java.util.List;
import java.util.Scanner;
import javax.ejb.EJB;

/**
 *
 * @author muhdm
 */
public class Main {

    @EJB
    private static ModelSessionBeanRemote modelSessionBean;

    @EJB
    private static CarSessionBeanRemote carSessionBean;
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        List<Car> listOfCars = carSessionBean.viewAllCars();
        List<Model> listOfModels = modelSessionBean.viewAllModels();
        
        for(Car car : listOfCars) {
            System.out.println("Car number plate is: " + car.getLicensePlateNum());
            System.out.println("Car model is: " + car.getModel().getMakeName() + " " + car.getModel().getModelName());
        }
        System.out.println("All Available models are:");
        for(Model model: listOfModels) {
            System.out.println(model.getMakeName() + " " + model.getModelName());
        }
        
    }
    
}
