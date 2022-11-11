/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CarCategoryNotFoundException;

/**
 *
 * @author muhdm
 */
@Stateless
public class CarCategorySessionBean implements CarCategorySessionBeanRemote, CarCategorySessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createNewCategory(CarCategory newCarCategory) {
        em.persist(newCarCategory);
        em.flush();
        
        return newCarCategory.getCategoryId();
    }
    
    @Override
    public CarCategory retrieveCategoryById (Long carCategoryId) throws CarCategoryNotFoundException
    {
        CarCategory carCategory = em.find(CarCategory.class, carCategoryId);
            
        if(carCategory != null)
        {
            carCategory.getModels().size();
            carCategory.getRentalRates().size();
            return carCategory;
        }
        else
        {
            throw new CarCategoryNotFoundException("Car category ID " + carCategoryId + " does not exist!");
        }
    }
    
    public List<CarCategory> retrieveAllCarCategories() {
        Query query = em.createQuery("SELECT c FROM CarCategory c");
        return query.getResultList();    
    }
    
}
