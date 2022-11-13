/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.OutletNameExistException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author muhdm
 */
@Stateless
public class OutletSessionBean implements OutletSessionBeanRemote, OutletSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public OutletSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewOutlet(Outlet newOutlet) throws OutletNameExistException, UnknownPersistenceException, InputDataValidationException {

        Set<ConstraintViolation<Outlet>> constraintViolations = validator.validate(newOutlet);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newOutlet);
                em.flush();

                return newOutlet.getOutletId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new OutletNameExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public Outlet retrieveOutletById(Long outletId) throws OutletNotFoundException {
        Outlet outlet = em.find(Outlet.class, outletId);
        if (outlet != null) {
            outlet.getEmployees().size();
            outlet.getCars().size();
            return outlet;
        } else {
            throw new OutletNotFoundException("Outlet with ID " + outletId + " does not exist!");
        }
    }

    public List<Outlet> retrieveAllOutlets() {
        Query query = em.createQuery("SELECT o FROM Outlet o");
        return query.getResultList();
    }

    public Outlet retrieveOutletByOutletName(String name) throws OutletNotFoundException {
        Query query = em.createQuery("SELECT o FROM Outlet o WHERE o.outletName = :inName");
        query.setParameter("inName", name);

        try {
            return (Outlet) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new OutletNotFoundException("Outlet name " + name + " does not exist!");
        }

    }

    @Override
    public void updateOutlet(Outlet outlet) {
        em.merge(outlet);
    }

    @Override
    public void deleteOutlet(Long outletId) throws OutletNotFoundException {
        Outlet outletToRemove = retrieveOutletById(outletId);
        em.remove(outletToRemove);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Outlet>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
