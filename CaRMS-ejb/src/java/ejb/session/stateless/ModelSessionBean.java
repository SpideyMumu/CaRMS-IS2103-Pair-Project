/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.Model;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CarCategoryNotFoundException;
import util.exception.CreateNewModelException;
import util.exception.ModelNotFoundException;
import util.exception.UpdateModelException;

/**
 *
 * @author muhdm
 */
@Stateless
public class ModelSessionBean implements ModelSessionBeanRemote, ModelSessionBeanLocal {

    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBean;

    
    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewModel(Model newModel) {
        em.persist(newModel);
        em.flush();
        return newModel.getModelId();
    }
    
    @Override
    public Long createNewModel(Long carCategoryId, Model model) throws CreateNewModelException {
        try {
            CarCategory carCategory = carCategorySessionBean.retrieveCategoryById(carCategoryId);
            model.setCarCategory(carCategory);
            carCategory.getModels().add(model);
            
            em.persist(model);
            em.flush();
            
            return model.getModelId();
            
        } catch (CarCategoryNotFoundException ex) {
            throw new CreateNewModelException(ex.getMessage());
        }
    }
    
    @Override
    public Model retrieveModelById(Long modelId) throws ModelNotFoundException {
        Model model = em.find(Model.class, modelId);
        if (model != null) {
            model.getCars().size();
            return model;
        } else {
            throw new ModelNotFoundException("Model with ID " + modelId + " does not exist!");
        }
    }
    
    @Override
    public List<Model> retrieveAllModels() {
        Query query = em.createQuery("SELECT m FROM Model m");

        List<Model> models = query.getResultList();
        models.sort(((o1, o2) -> {
            if (o1.getCarCategory().getCategoryId().equals(o2.getCarCategory().getCategoryId())) {
                if (o1.getMakeName().equals(o2.getMakeName())) {
                    return o1.getModelName().compareTo(o2.getModelName());
                }
                return o1.getMakeName().compareTo(o2.getMakeName());
            }     
            return o1.getCarCategory().getCategoryId().compareTo(o2.getCarCategory().getCategoryId());
        }));
        
        return models;
    }

    @Override
    public void updateModel(Model model) throws UpdateModelException {
        //em.merge(model);
        
        try {
            Model modelToUpdate = retrieveModelById(model.getModelId());
            modelToUpdate.setCarCategory(model.getCarCategory());
            modelToUpdate.setEnabled(model.isEnabled());
            modelToUpdate.setMakeName(model.getMakeName());
            modelToUpdate.setModelName(model.getModelName());
        } catch (ModelNotFoundException ex) {
            throw new UpdateModelException("Model you want to update does not exist in the Database!");
        }
    }

    @Override
    public void deleteModel(Long modelId) throws ModelNotFoundException
    {
        Model modelToRemove = retrieveModelById(modelId);
        
        if (modelToRemove.getCars().isEmpty()) {
            modelToRemove.getCarCategory().getModels().remove(modelToRemove);
            em.remove(modelToRemove);
        } else {
            modelToRemove.setEnabled(false);
        }
    }
}
