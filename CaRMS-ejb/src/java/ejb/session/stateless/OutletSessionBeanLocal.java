/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.OutletNameExistException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;


/**
 *
 * @author muhdm
 */
@Local
public interface OutletSessionBeanLocal {

    public Outlet retrieveOutletById(Long outletId) throws OutletNotFoundException;

    public Long createNewOutlet(Outlet newOutlet) throws OutletNameExistException, UnknownPersistenceException, InputDataValidationException;

    public void updateOutlet(Outlet outlet);

    public void deleteOutlet(Long outletId) throws OutletNotFoundException;

    public Outlet retrieveOutletByOutletName(String name) throws OutletNotFoundException;
    
}
