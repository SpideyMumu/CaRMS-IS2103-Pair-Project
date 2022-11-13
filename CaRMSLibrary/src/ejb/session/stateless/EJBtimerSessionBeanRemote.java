/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.util.Date;
import javax.ejb.Remote;
import util.exception.CarNotFoundException;
import util.exception.CreateTransitDriverDispatchException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author muhdm
 */
@Remote
public interface EJBtimerSessionBeanRemote {

    public void allocateCarsForToday() throws CarNotFoundException, ReservationNotFoundException, CreateTransitDriverDispatchException;

    public void allocateCarsByDate(Date date) throws CarNotFoundException, ReservationNotFoundException, CreateTransitDriverDispatchException;
    
}
