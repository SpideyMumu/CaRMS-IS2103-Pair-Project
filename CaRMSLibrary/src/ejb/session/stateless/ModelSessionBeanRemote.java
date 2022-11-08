/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import java.util.List;
import javax.ejb.Remote;
import util.exception.ModelNotFoundException;

/**
 *
 * @author muhdm
 */
@Remote
public interface ModelSessionBeanRemote {
    
    public Long createNewModel(Model newModel);

    public Model retrieveModelById(Long modelId) throws ModelNotFoundException;

    public List<Model> retrieveAllModels();

    public void updateCar(Model model);

    public void deleteModel(Long modelId) throws ModelNotFoundException;
    
}
