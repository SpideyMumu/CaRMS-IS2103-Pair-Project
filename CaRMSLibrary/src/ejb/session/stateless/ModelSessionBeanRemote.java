/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author muhdm
 */
@Remote
public interface ModelSessionBeanRemote {

    public Long createNewModel(Model model);

    public List<Model> viewAllModels();
    
}
