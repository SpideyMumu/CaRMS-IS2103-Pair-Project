/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kathleen
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB(name = "OutletSessionBeanLocal")
    private OutletSessionBeanLocal outletSessionBeanLocal;

    @EJB
    private CarSessionBeanLocal carSessionBeanLocal; 
    

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    

     public SaleTransactionEntity createNewSaleTransaction(Long staffId, SaleTransactionEntity newSaleTransactionEntity) throws StaffNotFoundException, CreateNewSaleTransactionException
    {
        if(newSaleTransactionEntity != null)
        {
            try
            {
                StaffEntity staffEntity = staffEntitySessionBeanLocal.retrieveStaffByStaffId(staffId);
                newSaleTransactionEntity.setStaffEntity(staffEntity);
                staffEntity.getSaleTransactionEntities().add(newSaleTransactionEntity);

                entityManager.persist(newSaleTransactionEntity);

                for(SaleTransactionLineItemEntity saleTransactionLineItemEntity:newSaleTransactionEntity.getSaleTransactionLineItemEntities())
                {
                    productEntitySessionBeanLocal.debitQuantityOnHand(saleTransactionLineItemEntity.getProductEntity().getProductId(), saleTransactionLineItemEntity.getQuantity());
                    entityManager.persist(saleTransactionLineItemEntity);
                }

                entityManager.flush();

                return newSaleTransactionEntity;
            }
            catch(ProductNotFoundException | ProductInsufficientQuantityOnHandException ex)
            {
                // The line below rolls back all changes made to the database.
                eJBContext.setRollbackOnly();
                
                throw new CreateNewSaleTransactionException(ex.getMessage());
            }
        }
        else
        {
            throw new CreateNewSaleTransactionException("Sale transaction information not provided");
        }
    }

    public void persist(Object object) {
        em.persist(object);
    }
    
}
