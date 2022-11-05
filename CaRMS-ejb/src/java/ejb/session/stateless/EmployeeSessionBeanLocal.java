/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author muhdm
 */
@Local
public interface EmployeeSessionBeanLocal {

    public Employee retrieveEmployeeById(Long employeeId);

    public Long createNewEmployee(Employee newEmployee);

    public void updateEmployee(Employee employee);

    public void deleteEmployee(Long employeeId);

    public Employee retrieveEmployeeByUserName(String username);

    public List<Employee> retrieveAllEmployees();
    
}
