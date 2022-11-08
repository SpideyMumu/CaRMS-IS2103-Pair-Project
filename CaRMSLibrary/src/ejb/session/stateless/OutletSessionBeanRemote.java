/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import javax.ejb.Remote;
import util.exception.OutletNotFoundException;

/**
 *
 * @author muhdm
 */
@Remote
public interface OutletSessionBeanRemote {
    
    public Outlet retrieveOutletById(Long outletId) throws OutletNotFoundException;

    public Long createNewOutlet(Outlet newOutlet);

    public void updateOutlet(Outlet outlet);

    public void deleteOutlet(Long outletId) throws OutletNotFoundException;
    
}
