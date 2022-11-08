/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.OutletNotFoundException;

/**
 *
 * @author muhdm
 */
@Stateless
public class OutletSessionBean implements OutletSessionBeanRemote, OutletSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createNewOutlet(Outlet newOutlet) {
        em.persist(newOutlet);
        em.flush();
        return newOutlet.getOutletId();
    }

    @Override
    public Outlet retrieveOutletById(Long outletId) throws OutletNotFoundException {
        Outlet outlet = em.find(Outlet.class, outletId);
        if (outlet != null)
        {
            return outlet;
        } else 
        {
            throw new OutletNotFoundException();
        }
    }
    
    @Override
    public void updateOutlet(Outlet outlet) {
        em.merge(outlet);
    }

    @Override
    public void deleteOutlet(Long outletId) throws OutletNotFoundException//throws StaffNotFoundException
    {
        Outlet outletToRemove = retrieveOutletById(outletId);
        em.remove(outletToRemove);
    }
}
