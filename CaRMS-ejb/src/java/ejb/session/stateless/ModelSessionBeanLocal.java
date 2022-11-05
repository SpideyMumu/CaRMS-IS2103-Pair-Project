/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author muhdm
 */
@Local
public interface ModelSessionBeanLocal {

    public Long createNewModel(Model newModel);

    public Model retrieveModelById(Long modelId);

    public List<Model> retrieveAllModels();

    public void updateCar(Model model);

    public void deleteModel(Long modelId);
    
}