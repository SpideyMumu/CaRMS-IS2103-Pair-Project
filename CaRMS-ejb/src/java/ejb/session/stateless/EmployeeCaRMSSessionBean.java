/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import java.util.List;
import javax.ejb.EJB;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CreateNewEmployeeException;
import util.exception.EmployeeNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OutletNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author muhdm
 */
/**
 *
 * @author muhdm
 */
@Stateless
public class EmployeeCaRMSSessionBean implements EmployeeCaRMSSessionBeanRemote, EmployeeCaRMSSessionBeanLocal {

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public EmployeeCaRMSSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewEmployee(Long outletId, Employee employee) throws CreateNewEmployeeException, EmployeeUsernameExistException, UnknownPersistenceException {

        try {
            Outlet outlet = outletSessionBean.retrieveOutletById(outletId);
            employee.setOutlet(outlet);
            outlet.getEmployees().add(employee);

            try {
                em.persist(employee);
                em.flush();
                return employee.getEmployeeId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new EmployeeUsernameExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }

        } catch (OutletNotFoundException ex) {
            throw new CreateNewEmployeeException(ex.getMessage());
        }
    }

    @Override
    public Long createNewEmployee(Employee newEmployee) throws EmployeeUsernameExistException, UnknownPersistenceException, InputDataValidationException {

        Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(newEmployee);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newEmployee);
                em.flush();
                return newEmployee.getEmployeeId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new EmployeeUsernameExistException();
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
    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException {
        Employee employee = em.find(Employee.class, employeeId);
        if (employee != null) {
            return employee;
        } else {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " does not exist!");
        }
    }

    @Override
    public Employee retrieveEmployeeByUserName(String username) throws EmployeeNotFoundException {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (Employee) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EmployeeNotFoundException("Employee with username " + username + " does not exist!");
        }
    }

    //login function here
    @Override
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            Employee employee = retrieveEmployeeByUserName(username);

            if (employee.getPassword().equals(password)) {
                //staffEntity.getSaleTransactionEntities().size();                
                return employee;
            } else {
                throw new InvalidLoginCredentialException("Invalid password!");
            }
        } catch (EmployeeNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist!");
        }
    }

    @Override
    public List<Employee> retrieveAllEmployees() {
        Query query = em.createQuery("SELECT e FROM Employee e");

        return query.getResultList();
    }

    @Override
    public List<Employee> retrieveEmployeesFromOutlet(Outlet outlet) {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.outlet = :outlet");
        query.setParameter("outlet", outlet);

        return query.getResultList();
    }

    @Override
    public void updateEmployee(Employee employee) {
        em.merge(employee);
    }

    @Override
    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException {
        Employee e = retrieveEmployeeById(employeeId);
        em.remove(e);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Employee>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
