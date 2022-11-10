/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatch;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CreateTransitDriverDispatchException;

/**
 *
 * @author muhdm
 */
@Remote
public interface TransitDriverDispatchSessionBeanRemote {
    
    public TransitDriverDispatch createNewTransitDriverDispatch(Long carId, Long pickupOutletId, Long returnOutletId, Long employeeId, TransitDriverDispatch newTransitDriverDispatch) throws CreateTransitDriverDispatchException;
    
    public List<TransitDriverDispatch> retrieveAllDispatch();
    
}
