/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import javax.ejb.Remote;
import util.exception.EntityNotFoundException;

/**
 *
 * @author muhdm
 */
@Remote
public interface CarCategorySessionBeanRemote {

    public Long createNewCategory(CarCategory newCarCategory);

    public CarCategory retrieveCategoryById(Long carCategoryId) throws EntityNotFoundException;
    
}
