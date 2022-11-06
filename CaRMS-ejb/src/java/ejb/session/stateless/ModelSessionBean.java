/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EntityNotFoundException;

/**
 *
 * @author muhdm
 */
@Stateless
public class ModelSessionBean implements ModelSessionBeanRemote, ModelSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewModel(Model newModel) {
        em.persist(newModel);
        em.flush();
        return newModel.getModelId();
    }
    
    @Override
    public Model retrieveModelById(Long modelId) throws EntityNotFoundException {
        Model model = em.find(Model.class, modelId);
        if (model != null) {
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

    @Override
    public void updateCar(Model model) {
        em.merge(model);
    }

    @Override
    public void deleteModel(Long modelId) throws EntityNotFoundException
    {
        Model modelToRemove = retrieveModelById(modelId);
        em.remove(modelToRemove);
    }
}
