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
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author muhdm
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewEmployee(Employee newEmployee) {
        em.persist(newEmployee);
        em.flush();
        return newEmployee.getEmployeeId();
    }
    
    @Override
    public Employee retrieveEmployeeById(Long employeeId) {
        return em.find(Employee.class, employeeId);
    }
    
    @Override
    public Employee retrieveEmployeeByUserName(String username) {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :inUsername");
        query.setParameter("inUsername", username);
        
        return (Employee) query.getSingleResult(); //implement try catch EntityNotFound
    } 
    
    //login function here
//    @Override
//    public Employee staffLogin(String username, String password) //throws InvalidLoginCredentialException
//    {
//        try
//        {
//            Employee staffEntity = retrieveStaffByUsername(username);
//            
//            if(staffEntity.getPassword().equals(password))
//            {
//                staffEntity.getSaleTransactionEntities().size();                
//                return staffEntity;
//            }
//            else
//            {
//                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
//            }
//        }
//        catch(StaffNotFoundException ex)
//        {
//            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
//        }
     
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
    public void deleteEmployee(Long employeeId) //throws StaffNotFoundException
    {
        Employee e = retrieveEmployeeById(employeeId);
        em.remove(e);
    }

}
