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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RentalRateType;
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
    
    public List<CarCategory> retrieveAllCategories() 
    {
        Query query = em.createQuery("SELECT c FROM CarCategory c");
        return query.getResultList();
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
    
    public HashMap<CarCategory, BigDecimal> calculatePrevailingRentalFeeForEachCategories(List<CarCategory> list, Date pickupDate, Date returnDate)
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
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.setTime(startDate);
                
                Date endDate = rentalRate.getEndDate();
                Calendar endCalendar = Calendar.getInstance();
                endCalendar.setTime(endDate);
                
                int startDay = startCalendar.DAY_OF_MONTH;
                int endDay = startCalendar.DAY_OF_MONTH;
                
                for (int i = startDay; i <= endDay; i++)
                {
                    if(hashmap.containsKey(i))
                    {
                        List<RentalRate> listOfRentalRates = hashmap.get(i);
                        listOfRentalRates.add(rentalRate);
                        hashmap.replace(i, listOfRentalRates);
                    }
                    else
                    {
                        List<RentalRate> listOfRentalRates = new ArrayList<RentalRate>();
                        listOfRentalRates.add(rentalRate);
                        hashmap.put(i, listOfRentalRates);
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
                prevailingRentalFee = prevailingRentalFee.add(rentalRate.getRatePerDay());
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
}
