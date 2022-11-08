/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Remote;
import util.exception.EntityNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author muhdm
 */
@Remote
public interface EmployeeSessionBeanRemote {

    public Employee retrieveEmployeeById(Long employeeId) throws EntityNotFoundException;

    public Long createNewEmployee(Employee newEmployee);

    public void updateEmployee(Employee employee);

    public void deleteEmployee(Long employeeId) throws EntityNotFoundException;

    public Employee retrieveEmployeeByUserName(String username) throws EntityNotFoundException;

    public List<Employee> retrieveAllEmployees();
    
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;

}
