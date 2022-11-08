/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EntityNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author muhdm
 */
@Stateless
public class EmployeeCaRMSSessionBean implements EmployeeCaRMSSessionBeanRemote, EmployeeCaRMSSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewEmployee(Employee newEmployee) {
        em.persist(newEmployee);
        em.flush();
        return newEmployee.getEmployeeId();
    }
    
    @Override
    public Employee retrieveEmployeeById(Long employeeId) throws EntityNotFoundException {
        Employee employee = em.find(Employee.class, employeeId);
        if (employee != null) {
            return employee;
        } else {
            throw new EntityNotFoundException("Employee with this ID does not exist!");
        }
    }
    
    @Override
    public Employee retrieveEmployeeByUserName(String username) throws EntityNotFoundException {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (Employee) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EntityNotFoundException("Employee with username " + username + " does not exist!");
        }
    }
    
    //login function here
    @Override
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException
    {
        try
        {
            Employee employee = retrieveEmployeeByUserName(username);
            
            if(employee.getPassword().equals(password))
            {
                //staffEntity.getSaleTransactionEntities().size();                
                return employee;
            }
            else
            {
                throw new InvalidLoginCredentialException("Invalid password!");
            }
        }
        catch(EntityNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist!");
        }
    }
     
    @Override
    public List<Employee> retrieveAllEmployees()
    {
        Query query = em.createQuery("SELECT e FROM Employee e");
        
        return query.getResultList();
    }
    
    @Override
    public void updateEmployee(Employee employee)
    {
        em.merge(employee);
    }
    
    @Override
    public void deleteEmployee(Long employeeId) throws EntityNotFoundException
    {
        Employee e = retrieveEmployeeById(employeeId);
        em.remove(e);
    }

}
