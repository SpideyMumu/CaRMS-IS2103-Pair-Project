/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RentalRateType;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarCategoryNotFoundException;
import util.exception.CreateNewRentalRateException;
import util.exception.InputDataValidationException;
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
    
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    
    
    public RentalRateSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
   
    @Override
    public RentalRate createNewRentalRate(Long carCategoryId, RentalRate newRentalRate) throws CreateNewRentalRateException, CarCategoryNotFoundException, InputDataValidationException
    {
        
        Set<ConstraintViolation<RentalRate>>constraintViolations = validator.validate(newRentalRate);
        
        
        if(constraintViolations.isEmpty())
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
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public RentalRate retrieveRentalRateById(Long rentalRateId) throws RentalRateNotFoundException {
        RentalRate rentalRate = em.find(RentalRate.class, rentalRateId);

        if (rentalRate != null) {
            return rentalRate;
        } else {
            throw new RentalRateNotFoundException("Rental rate ID " + rentalRateId + " does not exist!");
        }
    }

    @Override
    public List<RentalRate> retrieveAllRentalRates() {
        Query query = em.createQuery("SELECT r FROM RentalRate r");
        List<RentalRate> list = query.getResultList();

        list.sort((o1, o2) -> {
            if (o1.getCarCategory().getCategoryId().equals(o2.getCarCategory().getCategoryId())) {
                if (o1.getStartDate() == null && o2.getStartDate() != null) {
                    return -1;
                } else if (o1.getStartDate() != null && o2.getStartDate() == null) {
                    return 1;
                } else if (o1.getStartDate() == null && o2.getStartDate() == null) {
                    return 0;
                } else {
                    return o1.getStartDate().compareTo(o2.getStartDate());
                }
            } else {
                if (o1.getCarCategory().getCategoryId() > o2.getCarCategory().getCategoryId()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return list;
    }

    @Override
    public void updateRentalRate(RentalRate rentalRate) throws UpdateRentalRateException {
        //em.merge(rentalRate);
        
        if (rentalRate.getStartDate().compareTo(rentalRate.getEndDate()) > 0) { //end date cannot be earlier than start date
            throw new UpdateRentalRateException("End Date is earlier than start date after updating! Please enter the correct dates");
        }

        try {
            RentalRate rentalRateToUpdate = retrieveRentalRateById(rentalRate.getRentalRateId());
            rentalRateToUpdate.setEnabled(rentalRate.isEnabled());
            rentalRateToUpdate.setName(rentalRate.getName());
            rentalRateToUpdate.setRatePerDay(rentalRate.getRatePerDay());
            rentalRateToUpdate.setType(rentalRate.getType());
            rentalRateToUpdate.setEndDate(rentalRate.getEndDate());
            rentalRateToUpdate.setStartDate(rentalRate.getStartDate());
            
            //Associate new car category and disassociate old car category
            rentalRateToUpdate.getCarCategory().getRentalRates().size();
            rentalRateToUpdate.getCarCategory().getRentalRates().remove(rentalRateToUpdate);
            
            CarCategory newCarCategory = carCategorySessionBeanLocal.retrieveCategoryById(rentalRate.getCarCategory().getCategoryId());
            rentalRateToUpdate.setCarCategory(newCarCategory);
            
            if(!newCarCategory.getRentalRates().contains(rentalRateToUpdate))
            {
               newCarCategory.getRentalRates().add(rentalRateToUpdate);
            }
       
            
        } catch (RentalRateNotFoundException | CarCategoryNotFoundException ex) {
            throw new UpdateRentalRateException(ex.getMessage());
        }

    }

    @Override
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException {
        RentalRate rentalRate = retrieveRentalRateById(rentalRateId);

        if (rentalRate.getReservations().isEmpty()) {
            rentalRate.getCarCategory().getRentalRates().remove(rentalRate);
            em.remove(rentalRate);
        } else {
            //disable rental rate here
            rentalRate.setEnabled(false);
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalRate>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    } 
    
}
