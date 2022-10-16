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
    
    public Long createNewModel(Model model);
    
    public List<Model> viewAllModels();
}
