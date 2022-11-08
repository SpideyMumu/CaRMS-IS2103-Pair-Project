/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author muhdm
 */
@Local
public interface EmployeeCaRMSSessionBeanLocal {

    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException;

    public Long createNewEmployee(Employee newEmployee);

    public void updateEmployee(Employee employee);

    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException;

    public Employee retrieveEmployeeByUserName(String username) throws EmployeeNotFoundException;

    public List<Employee> retrieveAllEmployees();
    
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;
}
