/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    public CarCategory retrieveCategoryById (Long carCategoryId) {
        CarCategory carCategory = em.find(CarCategory.class, carCategoryId);
        return carCategory;
    }
    
}