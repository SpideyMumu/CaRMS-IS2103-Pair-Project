/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.Model;
import entity.RentalRate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.RentalRateType;
import util.exception.CarCategoryNameExistException;
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.RentalRateNotAvailableException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author muhdm
 */
@Stateless
public class CarCategorySessionBean implements CarCategorySessionBeanRemote, CarCategorySessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
        
    public CarCategorySessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    
    @Override
    public Long createNewCategory(CarCategory newCarCategory) throws CarCategoryNameExistException, UnknownPersistenceException, InputDataValidationException 
    {
        
        Set<ConstraintViolation<CarCategory>> constraintViolations = validator.validate(newCarCategory);
        /*em.persist(newCarCategory);
        em.flush();
        
        return newCarCategory.getCategoryId();*/
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newCarCategory);
                em.flush();

                return newCarCategory.getCategoryId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CarCategoryNameExistException();
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
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
    
    public List<CarCategory> retrieveAllCategories() 
    {
        Query query = em.createQuery("SELECT c FROM CarCategory c");
        return query.getResultList();
    }
    
    public CarCategory retrieveCarCategoryByName(String name)
    {
        Query query = em.createQuery("SELECT c FROM CarCategory c WHERE c.categoryName = :inName");
        query.setParameter("inName", name);
        return (CarCategory)query.getSingleResult();
    }
    
    public HashMap<CarCategory, Integer> retrieveQuantityOfCarsForEachCategory()
    {
        List<CarCategory> carCategories = retrieveAllCategories();
        HashMap<CarCategory, Integer> hashmap = new HashMap<CarCategory, Integer>();
        
        int numberOfCars = 0;
        for (CarCategory category : carCategories)
        {
            List<Model> models = category.getModels();
            
            for (Model model : models)
            {
                List<Car> cars = model.getCars();
                numberOfCars += cars.size();
              
            }
            hashmap.put(category, numberOfCars);
        }
        
        return hashmap;
    }
    
    public HashMap<CarCategory, BigDecimal> calculatePrevailingRentalFeeForEachCategories(List<CarCategory> list, Date pickupDate, Date returnDate) throws RentalRateNotAvailableException
    {
        Calendar pickupCalendar = Calendar.getInstance();
        pickupCalendar.setTime(pickupDate);
        
        int pickupDay = pickupCalendar.DAY_OF_MONTH;
        
        Calendar returnCalendar = Calendar.getInstance();
        returnCalendar.setTime(returnDate);
        
        int returnDay = returnCalendar.DAY_OF_MONTH;
        
        HashMap<CarCategory, BigDecimal> rates = new HashMap<CarCategory, BigDecimal>();
        
        BigDecimal prevailingRentalFee = new BigDecimal(0);
        
        
        for (CarCategory carCategory : list) //for each car category
        {
            List<RentalRate> rentalRates = carCategory.getRentalRates(); //find the rental rates for the car category
            
            HashMap<Integer, List<RentalRate>> hashmap = new HashMap<Integer, List<RentalRate>>();
            
            for (RentalRate rentalRate : rentalRates)
            {
                Date startDate = rentalRate.getStartDate();
                 
                Date endDate = rentalRate.getEndDate();
                
                if (startDate != null && endDate != null)
                {
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.setTime(startDate);

                    Calendar endCalendar = Calendar.getInstance();
                    endCalendar.setTime(endDate);

                    int startDay = startCalendar.DAY_OF_MONTH;
                    int endDay = startCalendar.DAY_OF_MONTH;
                    
                    List<RentalRate> listOfRentalRates;

                    for (int i = startDay; i <= endDay; i++)
                    {
                        if(hashmap.containsKey(i))
                        {
                            listOfRentalRates = hashmap.get(i);
                            listOfRentalRates.add(rentalRate);
                            hashmap.replace(i, listOfRentalRates);
                        }
                        else
                        {
                            listOfRentalRates = new ArrayList<RentalRate>();
                            listOfRentalRates.add(rentalRate);
                            hashmap.put(i, listOfRentalRates);
                        }
                    }
                }
                else
                {
                    for(Map.Entry<Integer, List<RentalRate>> rateMap : hashmap.entrySet())
                    {
                        int day = rateMap.getKey();
                        List<RentalRate> ratesList = rateMap.getValue();
                        ratesList.add(rentalRate);
                        hashmap.replace(day, ratesList);
                    }
                    for(int i = 1; i <= 31; i++)
                    {
                        if (!hashmap.containsKey(i))
                        {
                            List<RentalRate> rate = new ArrayList<RentalRate>();
                            rate.add(rentalRate);
                            hashmap.put(i, rate);
                        }
                    }
                }
            }
            
            for (int j = pickupDay; j <= returnDay; j++)
            {
                List<RentalRate> rentalRatesMap = hashmap.get(j);
                
                RentalRateType rentalRateType = RentalRateType.Default;
                
                RentalRate rentalRate = null;
                
                for (RentalRate rate : rentalRatesMap) //choose the correct rate to apply for each date
                {
                    if (rentalRate == null)
                    {
                        rentalRate = rate;
                    }
                    if (rate.getType() == RentalRateType.Default)
                    {
                        if(rentalRateType == RentalRateType.Default)
                        {
                            if (!rentalRate.equals(rate))
                            {
                                BigDecimal ratePerDay = rate.getRatePerDay();
                                BigDecimal rentalRatePerDay = rentalRate.getRatePerDay();
                                if (ratePerDay.compareTo(rentalRatePerDay) < 0)
                                {
                                    rentalRate = rate;
                                }
                            }
                        }
                    } 
                    else if (rate.getType() == RentalRateType.Peak)
                    {
                        if(rentalRateType == RentalRateType.Default)
                        {
                            rentalRateType = RentalRateType.Peak;
                        }
                        else if(rentalRateType == RentalRateType.Peak)
                        {
                            if (!rentalRate.equals(rate))
                            {
                                BigDecimal ratePerDay = rate.getRatePerDay();
                                BigDecimal rentalRatePerDay = rentalRate.getRatePerDay();
                                if (ratePerDay.compareTo(rentalRatePerDay) < 0)
                                {
                                    rentalRate = rate;
                                }
                            }
                        }
                    }
                    else if (rate.getType() == RentalRateType.Promotion)
                    {
                        if (rentalRateType != RentalRateType.Promotion)
                        {
                            rentalRateType = RentalRateType.Promotion;
                        }
                        else if(rentalRateType == RentalRateType.Promotion)
                        {
                            if (!rentalRate.equals(rate))
                            {
                                BigDecimal ratePerDay = rate.getRatePerDay();
                                BigDecimal rentalRatePerDay = rentalRate.getRatePerDay();
                                if (ratePerDay.compareTo(rentalRatePerDay) < 0)
                                {
                                    rentalRate = rate;
                                }
                            }
                        }
                    }
                }
                if (rentalRate == null)
                {
                    throw new RentalRateNotAvailableException("No rental rate available for the date " + j);
                }
                else
                {
                    prevailingRentalFee = prevailingRentalFee.add(rentalRate.getRatePerDay());
                }
            }
            if (rates.containsKey(carCategory))
            {
                rates.replace(carCategory, prevailingRentalFee);
            }
            else if (!rates.containsKey(carCategory))
            {
                rates.put(carCategory, prevailingRentalFee);
            }    
        }
        return rates;
    }
    
     
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CarCategory>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
