/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CreateNewModelException;
import util.exception.ModelNotFoundException;
import util.exception.UpdateModelException;

/**
 *
 * @author muhdm
 */
@Remote
public interface ModelSessionBeanRemote {
    
    public Long createNewModel(Model newModel);

    public Long createNewModel(Long carCategoryId, Model model) throws CreateNewModelException;
    
    public Model retrieveModelById(Long modelId) throws ModelNotFoundException;

    public List<Model> retrieveAllModels();

    public void updateCar(Model model) throws UpdateModelException;

    public void deleteModel(Long modelId) throws ModelNotFoundException;

}
