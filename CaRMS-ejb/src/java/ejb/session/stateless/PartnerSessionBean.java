/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Partner;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.CustomerMobilePhoneExistException;
import util.exception.PartnerNotFoundException;
import util.exception.PartnerUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author kathleen
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    
    @Override
    public Long createNewPartner(Partner newPartner) throws UnknownPersistenceException, PartnerUsernameExistException
    {
        try
        {
            em.persist(newPartner);
            em.flush();

            return newPartner.getPartnerId();
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new PartnerUsernameExistException();
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else
            {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public Partner retrievePartnerById(Long partnerId) throws PartnerNotFoundException
    {
        
        Partner partner = em.find(Partner.class, partnerId);
        if (partner != null)
        {
            return partner;
        } else {
            throw new PartnerNotFoundException();
        }
    }
    
    @Override
    public void updatePartner(Partner partner)
    {
        em.merge(partner);
    }
    
    @Override
    public void deletePartner(Long partnerId) throws PartnerNotFoundException
    {
        Partner partner = retrievePartnerById(partnerId);
        em.remove(partner);
    }
}
