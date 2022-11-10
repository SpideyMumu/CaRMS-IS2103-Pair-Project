/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CarCategoryNotFoundException;
import util.exception.CreateNewRentalRateException;
import util.exception.RentalRateNotFoundException;
import util.exception.UpdateRentalRateException;

/**
 *
 * @author kathleen
 */
@Stateless
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @EJB(name = "CarCategorySessionBeanLocal")
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    

    @Override
    public RentalRate createNewRentalRate(Long carCategoryId, RentalRate newRentalRate) throws CreateNewRentalRateException, CarCategoryNotFoundException
    {
        if(newRentalRate != null)
        {
            try
            {
                CarCategory carCategory = carCategorySessionBeanLocal.retrieveCategoryById(carCategoryId);
                newRentalRate.setCarCategory(carCategory);
                carCategory.getRentalRates().add(newRentalRate);

                em.persist(newRentalRate);
                
                em.flush();

                return newRentalRate;
            }
            catch(CarCategoryNotFoundException ex)
            {          
                throw new CreateNewRentalRateException(ex.getMessage());
            }
        }
        else
        {
            throw new CreateNewRentalRateException("Rental rate information not provided");
        }
    }
    
    @Override
    public RentalRate retrieveRentalRateById(Long rentalRateId) throws RentalRateNotFoundException
    {
        RentalRate rentalRate = em.find(RentalRate.class, rentalRateId);
        
        if(rentalRate != null)
        {       
            return rentalRate;
        }
        else
        {
            throw new RentalRateNotFoundException("Rental rate ID " + rentalRateId + " does not exist!");
        }                
    }
    
    @Override
    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, UpdateRentalRateException
    {
        //em.merge(rentalRate);
        
        if(rentalRate != null && rentalRate.getRentalRateId()!= null)
        {
        
              RentalRate rentalRateToUpdate = retrieveRentalRateById(rentalRate.getRentalRateId());

              if(rentalRateToUpdate.getName().equals(rentalRate.getName()))
              {
                  rentalRateToUpdate.setType(rentalRate.getType());
                  rentalRateToUpdate.setRatePerDay(rentalRate.getRatePerDay());
                  rentalRateToUpdate.setStartDate(rentalRate.getStartDate());
                  rentalRateToUpdate.setEndDate(rentalRate.getEndDate());
                  rentalRateToUpdate.setEnabled(rentalRate.getEnabled());
                  rentalRateToUpdate.setCarCategory(rentalRate.getCarCategory());
              }
              else
              {
                  throw new UpdateRentalRateException("Name of rental rate record to be updated does not match the existing record");
              }
        }
        else
        {
            throw new RentalRateNotFoundException("Product ID not provided for product to be updated");
        }
    }
    
    @Override
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException
    {
        RentalRate rentalRate = retrieveRentalRateById(rentalRateId);
        
        if (rentalRate.getReservations().isEmpty()) {
            rentalRate.getCarCategory().getRentalRates().remove(rentalRate);
            em.remove(rentalRate);
        } else {
            //disable rental rate here
            rentalRate.setEnabled(false);
        }
    }
    
    
    
    
}
