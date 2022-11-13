/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Employee;
import entity.Outlet;
import entity.TransitDriverDispatch;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarNotFoundException;
import util.exception.CreateTransitDriverDispatchException;
import util.exception.CustomerNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.TransitDriverDispatchNotFound;
import util.exception.UpdateTransitDriverDispatchException;

/**
 *
 * @author muhdm
 */
@Stateless
public class TransitDriverDispatchSessionBean implements TransitDriverDispatchSessionBeanRemote, TransitDriverDispatchSessionBeanLocal {

    @EJB(name = "CarSessionBeanLocal")
    private CarSessionBeanLocal carSessionBeanLocal;

    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeCaRMSSessionBeanLocal employeeSessionBeanLocal;

    @EJB(name = "OutletSessionBeanLocal")
    private OutletSessionBeanLocal outletSessionBeanLocal;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public TransitDriverDispatchSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public TransitDriverDispatch createNewTransitDriverDispatch(Long carId, Long pickupOutletId, Long returnOutletId, Long employeeId, TransitDriverDispatch newTransitDriverDispatch) throws CreateTransitDriverDispatchException, InputDataValidationException {

        Set<ConstraintViolation<TransitDriverDispatch>> constraintViolations = validator.validate(newTransitDriverDispatch);

        if (constraintViolations.isEmpty()) {
            if (newTransitDriverDispatch != null) {
                try {
                    Car car = carSessionBeanLocal.retrieveCarById(carId);
                    newTransitDriverDispatch.setTransitCar(car);

                    Outlet pickupOutlet = outletSessionBeanLocal.retrieveOutletById(pickupOutletId);
                    newTransitDriverDispatch.setOriginOutlet(pickupOutlet);

                    Outlet returnOutlet = outletSessionBeanLocal.retrieveOutletById(returnOutletId);
                    newTransitDriverDispatch.setReturnOutlet(returnOutlet);

                    Employee employee = employeeSessionBeanLocal.retrieveEmployeeById(employeeId);
                    newTransitDriverDispatch.setDriver(employee);

                    em.persist(newTransitDriverDispatch);

                    em.flush();

                    return newTransitDriverDispatch;

                } catch (CarNotFoundException | OutletNotFoundException | EmployeeNotFoundException ex) {
                    throw new CreateTransitDriverDispatchException(ex.getMessage());
                }
            } else {
                throw new CreateTransitDriverDispatchException("Transit Driver Dispatch information not provided");
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<TransitDriverDispatch> retrieveAllDispatch() {
        Query query = em.createQuery("SELECT d FROM TransitDriverDispatch d");
        return query.getResultList();
    }

    @Override
    public TransitDriverDispatch retrieveDispatchById(Long id) throws TransitDriverDispatchNotFound {
        TransitDriverDispatch dispatch = em.find(TransitDriverDispatch.class, id);

        if (dispatch != null) {
            return dispatch;
        } else {
            throw new TransitDriverDispatchNotFound("Transit Driver Dispatch ID " + id + " does not exist!");
        }
    }

    @Override
    public void updateTransitDriverDispatch(TransitDriverDispatch dispatch) throws UpdateTransitDriverDispatchException {
        try {
            TransitDriverDispatch dispatchToUpdate = retrieveDispatchById(dispatch.getTransitId());
            dispatchToUpdate.setTransitCar(dispatch.getTransitCar());
            dispatchToUpdate.setTransitEndDate(dispatch.getTransitEndDate());
        } catch (TransitDriverDispatchNotFound ex) {
            throw new UpdateTransitDriverDispatchException(ex.getMessage());
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<TransitDriverDispatch>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
