/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.PartnerNotFoundException;
import util.exception.PartnerUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author kathleen
 */
@Local
public interface PartnerSessionBeanLocal {

    public Long createNewPartner(Partner newPartner) throws UnknownPersistenceException, PartnerUsernameExistException, InputDataValidationException;

    public Partner retrievePartnerById(Long partnerId) throws PartnerNotFoundException;

    public void updatePartner(Partner partner);

    public void deletePartner(Long partnerId) throws PartnerNotFoundException;
    
}
