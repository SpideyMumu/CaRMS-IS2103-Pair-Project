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
    public Outlet retrieveOutletById(Long outletId) {
        return em.find(Outlet.class, outletId);
    }
    
    @Override
    public void updateOutlet(Outlet outlet) {
        em.merge(outlet);
    }

    @Override
    public void deleteOutlet(Long outletId) //throws StaffNotFoundException
    {
        Outlet outletToRemove = retrieveOutletById(outletId);
        em.remove(outletToRemove);
    }
}
