/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import javax.ejb.Remote;

/**
 *
 * @author muhdm
 */
@Remote
public interface OutletSessionBeanRemote {
    
    public Outlet retrieveOutletById(Long outletId);

    public Long createNewOutlet(Outlet newOutlet);

    public void updateOutlet(Outlet outlet);

    public void deleteOutlet(Long outletId);
    
}
