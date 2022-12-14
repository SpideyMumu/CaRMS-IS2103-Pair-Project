/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CreateNewEmployeeException;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author muhdm
 */


@Remote
public interface EmployeeCaRMSSessionBeanRemote {

    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException;

    public Long createNewEmployee(Long outletId, Employee employee) throws CreateNewEmployeeException, EmployeeUsernameExistException, UnknownPersistenceException;

    public Long createNewEmployee(Employee newEmployee) throws EmployeeUsernameExistException, UnknownPersistenceException, InputDataValidationException;

    public void updateEmployee(Employee employee);

    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException;

    public Employee retrieveEmployeeByUserName(String username) throws EmployeeNotFoundException;

    public List<Employee> retrieveAllEmployees();

    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;

    public List<Employee> retrieveEmployeesFromOutlet(Outlet outlet);

}
