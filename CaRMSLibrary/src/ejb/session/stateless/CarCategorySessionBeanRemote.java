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
import javax.ejb.Remote;
import util.exception.CarCategoryNameExistException;
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.RentalRateNotAvailableException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author muhdm
 */
@Remote
public interface CarCategorySessionBeanRemote {

    public Long createNewCategory(CarCategory newCarCategory) throws CarCategoryNameExistException, UnknownPersistenceException, InputDataValidationException;

    public CarCategory retrieveCategoryById(Long carCategoryId) throws CarCategoryNotFoundException;
    
    public HashMap<CarCategory, Integer> retrieveQuantityOfCarsForEachCategory();
    
    public HashMap<CarCategory, BigDecimal> calculatePrevailingRentalFeeForEachCategories(List<CarCategory> list, Date pickupDate, Date returnDate) throws RentalRateNotAvailableException;
    
    public List<CarCategory> retrieveAllCategories();

    public CarCategory retrieveCarCategoryByName(String name);
}
