/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarCategoryNotFoundException;

/**
 *
 * @author muhdm
 */
@Local
public interface CarCategorySessionBeanLocal {
    
    public Long createNewCategory(CarCategory newCarCategory);

    public CarCategory retrieveCategoryById(Long carCategoryId) throws CarCategoryNotFoundException;

    public HashMap<CarCategory, Integer> retrieveQuantityOfCarsForEachCategory();

    public HashMap<CarCategory, BigDecimal> calculatePrevailingRentalFeeForEachCategories(List<CarCategory> list, Date pickupDate, Date returnDate);
    
}
