/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarCategoryNotFoundException;
import util.exception.CreateNewRentalRateException;
import util.exception.InputDataValidationException;
import util.exception.RentalRateNotFoundException;
import util.exception.UpdateRentalRateException;

/**
 *
 * @author kathleen
 */
@Remote
public interface RentalRateSessionBeanRemote {
    
    public RentalRate createNewRentalRate(Long carCategoryId, RentalRate newRentalRate) throws CreateNewRentalRateException, CarCategoryNotFoundException, InputDataValidationException;
      
    public RentalRate retrieveRentalRateById(Long rentalRateId) throws RentalRateNotFoundException;
     
    public void updateRentalRate(RentalRate rentalRate) throws UpdateRentalRateException;

    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException;

    public List<RentalRate> retrieveAllRentalRates();
}
