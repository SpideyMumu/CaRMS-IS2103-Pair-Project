/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
import java.util.HashMap;
import java.util.List;
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
import util.exception.EntityNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ModelNameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author muhdm
 */
@Stateless
public class ModelSessionBean implements ModelSessionBeanRemote, ModelSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    
    public ModelSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    

    @Override
    public Long createNewModel(Model newModel) throws ModelNameExistException, UnknownPersistenceException, InputDataValidationException 
    {
        
        Set<ConstraintViolation<Model>>constraintViolations = validator.validate(newModel);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newModel);
                em.flush();

                return newModel.getModelId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new ModelNameExistException();
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
    public Model retrieveModelById(Long modelId) throws EntityNotFoundException {
        Model model = em.find(Model.class, modelId);
        if (model != null) {
            model.getCars().size();
            return model;
        } else {
            throw new EntityNotFoundException("Model with ID " + modelId + " does not exist!");
        }
    }
    
    @Override
    public List<Model> retrieveAllModels() {
        Query query = em.createQuery("SELECT m FROM Model m");

        return query.getResultList();
    }
    
    public Model retrieveModelByName(String name)
    {
        Query query = em.createQuery("SELECT m FROM Model m WHERE m.modelName = :inName");
        query.setParameter("inName", name);
        return (Model)query.getSingleResult();
    }
    
    public HashMap<Model, Integer> retrieveQuantityOfCarsForEachModel() 
    {
        List<Model> models = retrieveAllModels();
        HashMap<Model, Integer> hashmap = new HashMap<Model, Integer>();
        
        for (Model model : models)
        {
            List<Car> cars = model.getCars();
            int size = cars.size();
            hashmap.put(model, size);
        }
        
        return hashmap;
    }

    @Override
    public void updateCar(Model model) {
        em.merge(model);
    }

    @Override
    public void deleteModel(Long modelId) throws EntityNotFoundException
    {
        Model modelToRemove = retrieveModelById(modelId);
        
        if (modelToRemove.getCars().isEmpty()) {
            modelToRemove.getCarCategory().getModels().remove(modelToRemove);
            em.remove(modelToRemove);
        } else {
            modelToRemove.setEnabled(false);
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Model>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
