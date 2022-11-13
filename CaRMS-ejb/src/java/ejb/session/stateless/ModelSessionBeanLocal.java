/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewModelException;
import util.exception.ModelNotFoundException;
import util.exception.UpdateModelException;
import util.exception.InputDataValidationException;
import util.exception.ModelNameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author muhdm
 */
@Local
public interface ModelSessionBeanLocal {

    public Long createNewModel(Model newModel) throws ModelNameExistException, UnknownPersistenceException, InputDataValidationException;

    public Long createNewModel(Long carCategoryId, Model model) throws ModelNameExistException, CreateNewModelException, UnknownPersistenceException, InputDataValidationException;

    public Model retrieveModelById(Long modelId) throws ModelNotFoundException;

    public List<Model> retrieveAllModels();

    public void updateModel(Model model) throws UpdateModelException;

    public void deleteModel(Long modelId) throws ModelNotFoundException;

    public HashMap<Model, Integer> retrieveQuantityOfCarsForEachModel();

    public Model retrieveModelByName(String name);

}
