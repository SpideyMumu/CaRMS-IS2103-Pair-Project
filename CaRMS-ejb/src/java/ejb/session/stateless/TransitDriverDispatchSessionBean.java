/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Employee;
import entity.Outlet;
import entity.TransitDriverDispatch;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CarNotFoundException;
import util.exception.CreateTransitDriverDispatchException;
import util.exception.CustomerNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.OutletNotFoundException;

/**
 *
 * @author muhdm
 */
@Stateless
public class TransitDriverDispatchSessionBean implements TransitDriverDispatchSessionBeanRemote, TransitDriverDispatchSessionBeanLocal {

    @EJB(name = "CarSessionBeanLocal")
    private CarSessionBeanLocal carSessionBeanLocal;

    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
 
    @EJB(name = "OutletSessionBeanLocal")
    private OutletSessionBeanLocal outletSessionBeanLocal;
    
    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    
    @Override
    public TransitDriverDispatch createNewTransitDriverDispatch(Long carId, Long pickupOutletId, Long returnOutletId, Long employeeId, TransitDriverDispatch newTransitDriverDispatch) throws CreateTransitDriverDispatchException
    {
        if(newTransitDriverDispatch != null)
        {
            try
            {   
                Car car = carSessionBeanLocal.retrieveCarById(carId);
                newTransitDriverDispatch.setTransitCar(car);

                Outlet pickupOutlet = outletSessionBeanLocal.retrieveOutletById(pickupOutletId);
                newTransitDriverDispatch.setOriginOutlet(pickupOutlet);
                
                Outlet returnOutlet = outletSessionBeanLocal.retrieveOutletById(returnOutletId);
                newTransitDriverDispatch.setReturnOutlet(returnOutlet);
                
                Employee employee = employeeSessionBeanLocal.retrieveEmployeeById(employeeId);
                newTransitDriverDispatch.setDriver(employee);
                
                em.persist(newTransitDriverDispatch);

                em.flush();
                
                return newTransitDriverDispatch;      
          
            }
            catch(CarNotFoundException | OutletNotFoundException | EmployeeNotFoundException ex)
            {   
                throw new CreateTransitDriverDispatchException(ex.getMessage());
            }
        }
        else
        {
            throw new CreateTransitDriverDispatchException("Transit Driver Dispatch information not provided");
        }
    }
    
    @Override
    public List<TransitDriverDispatch> retrieveAllDispatch()
    {
        Query query = em.createQuery("SELECT d FROM TransitDriverDispatch d");
        return query.getResultList();
    }
   
}
