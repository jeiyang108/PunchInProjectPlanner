package ca.bcit.PunchIn.ArqullianTests;

import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.bcit.PunchIn.PasswordHash;
import ca.bcit.PunchIn.Beans.EmployeeDriver;
import ca.bcit.PunchIn.Beans.HrManagementDriver;
import ca.bcit.PunchIn.Beans.LoginBean;
import ca.bcit.PunchIn.DAOs.BaseDAO;
import ca.bcit.PunchIn.DAOs.CredentialDAO;
import ca.bcit.PunchIn.DAOs.EmployeeDAO;
import ca.bcit.PunchIn.Entities.Credential.CredentialEntity;
import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;
import ca.bcit.PunchIn.Entities.Project.ProjectEntity;
import ca.bcit.PunchIn.Entities.Timesheet.Timesheet;
import ca.bcit.PunchIn.Models.Employee.Employee;

@RunWith(Arquillian.class)
public class HrManagementDriverTests {
	
	@Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
        		.addPackage(LoginBean.class.getPackage())
        		.addPackage(BaseDAO.class.getPackage())
        		.addPackage(CredentialEntity.class.getPackage())
        		.addPackage(EmployeeEntity.class.getPackage())
        		.addPackage(ProjectEntity.class.getPackage())
        		.addPackage(Timesheet.class.getPackage())
        		.addPackage(Employee.class.getPackage())
        		.addPackage(PasswordHash.class.getPackage())
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsResource("META-INF/persistence.xml");
    }

	@Inject
	EmployeeDAO employeeDAO;
	
	@Inject
	CredentialDAO credentialDAO;
	
	@Inject
	EmployeeDriver empDriver;
	
	@Inject
	HrManagementDriver hrDriver;
	
	private static final long EMPLOYEE_ID = 501;
	
	private Employee createNewEmployee() {
		Employee employee = new Employee();
		EmployeeEntity employeeEntity = new EmployeeEntity();
		EmployeeEntity emp = employeeDAO.find((long) 1);
		
		employeeEntity.setEmployeeID(EMPLOYEE_ID);
		employeeEntity.setFirstName("Bruce");
		employeeEntity.setLastName("Wayne");
		employeeEntity.setSickLeaves(15);
		employeeEntity.setVacation((double) 9);
		employeeEntity.setTimesheetApprover(emp);
		employeeEntity.setSupervisor(emp);
		
		employee.setBasicInfo(employeeEntity);
		
		CredentialEntity credential = new CredentialEntity();
		credential.setEmployee(employeeEntity);
		credential.setRole("employee");
		credential.setPassword("99d2c681d92e73b0297259f8e1f057c18426670599cf50f1db9b11e02c3344aa83fd9ba4b3dd076ea618e480f7bd651858ecc27bb9e932f961cdf09a1e647c1f");
		credential.setUserName("bwayne");
		credential.setSalt("j6MYqBbqDdB+HEzBxLM5Rw==");
		
		employee.setCredential(credential);
		
		employee.getBasicInfo().setCredentials(credential);
		
		return employee;
	}
	
	private Employee createEmployee() {
		Employee employee = new Employee();
		EmployeeEntity employeeEntity = new EmployeeEntity();
		EmployeeEntity emp = employeeDAO.find((long) 1);
		
		employeeEntity.setEmployeeID(EMPLOYEE_ID);
		employeeEntity.setFirstName("Bruce");
		employeeEntity.setLastName("Wayne");
		employeeEntity.setSickLeaves(15);
		employeeEntity.setVacation((double) 9);
		employeeEntity.setTimesheetApprover(emp);
		employeeEntity.setSupervisor(emp);
		
		employee.setBasicInfo(employeeEntity);
		
		CredentialEntity credential = new CredentialEntity();
		credential.setEmployee(employeeEntity);
		credential.setRole("employee");
		credential.setPassword("");
		credential.setUserName("");
		
		employee.setCredential(credential);
		
		employee.getBasicInfo().setCredentials(credential);
		
		return employee;
	}
	
	
	@Test
	@InSequence(1)
	public void testCreateEmployee() {
		Employee emp = createNewEmployee();
		
		hrDriver.createEmployee(emp);
		
		EmployeeEntity employee = employeeDAO.find(EMPLOYEE_ID);
		
		assertEquals(emp.getBasicInfo(), employee);
		
	}
	
	@Test
	@InSequence(2)
	public void testUpdateEmployee() {
		EmployeeEntity employee = employeeDAO.find(EMPLOYEE_ID);
		Employee emp = new Employee();
		
		employee.setFirstName("Barry");
		employee.setLastName("Allen");
		
		emp.setBasicInfo(employee);
		
		hrDriver.updateEmployee(emp);
		
		EmployeeEntity newEmployee = employeeDAO.find(EMPLOYEE_ID);
		
		assertEquals("Barry", newEmployee.getFirstName());
	}
	
	@Test
	@InSequence(3)
	public void testDeleteEmployee() {
		EmployeeEntity employee = employeeDAO.find(EMPLOYEE_ID);
		Employee emp = new Employee();
		
		emp.setBasicInfo(employee);
		
		hrDriver.deleteEmployee(emp);
		
		assertNull(employeeDAO.find(EMPLOYEE_ID));
	}
	
	@Test
	@InSequence(4)
	public void testSubmitEmployee1() {
		hrDriver.setEmployees(hrDriver.getEmployees());
		
		Employee emp = createEmployee();
		
		hrDriver.setNewUserName("bwayne");
		hrDriver.setNewPassword("password");
		
		hrDriver.submitEmployee(emp);
		
		hrDriver.setEmployees(hrDriver.getEmployees());
		
		assertNotNull(employeeDAO.find(EMPLOYEE_ID));
		
	}
	
	@Test
	@InSequence(5)
	public void testSubmitEmployee2() {
		assertNotNull(credentialDAO.find("bwayne"));
	}
	
	@Test
	@InSequence(6)
	public void testAddEmployee() {
		hrDriver.addEmployee();
		
		Employee currEmp = hrDriver.getCurrEmp();
		
		assertEquals("password", currEmp.getCredential().getPassword());
	}
	
	@Test
	@InSequence(7)
	public void testEditEmployee() {
		EmployeeEntity employee = employeeDAO.find(EMPLOYEE_ID);
		Employee emp = new Employee();
		emp.setBasicInfo(employee);
		emp.setCredential(employee.getCredentials());
		
		hrDriver.editEmployee(emp);
		
		Employee currEmp = hrDriver.getCurrEmp();

		assertEquals("Bruce", currEmp.getBasicInfo().getFirstName());
	}
	
	@Test
	@InSequence(8)
	public void testGetEmployees() {
		List<Employee> list = hrDriver.getEmployees();
		
		assertEquals(501, list.size());
	}
	
	@Test
	@InSequence(9)
	public void testGetEmployeeEntities() {
		List<EmployeeEntity> list = hrDriver.getEmployeeEntities();
		
		assertEquals(501, list.size());
	}
	
	@Test
	@InSequence(10)
	public void testDelete() {
		EmployeeEntity employee = employeeDAO.find(EMPLOYEE_ID);
		Employee emp = new Employee();
		emp.setBasicInfo(employee);
		emp.setCredential(employee.getCredentials());
		
		hrDriver.deleteEmployee(emp);
		
		assertNull(employeeDAO.find(EMPLOYEE_ID));
	}
	
	
	/*@Test
	@InSequence(5)
	public void testSubmitEmployee2() {
		EmployeeEntity employeeEntity = employeeDAO.find(EMPLOYEE_ID);
		Employee emp = new Employee();
		
		emp.setBasicInfo(employeeEntity);
		
		CredentialEntity credential = new CredentialEntity();
		credential.setEmployee(employeeEntity);
		credential.setRole("employee");
		credential.setPassword("99d2c681d92e73b0297259f8e1f057c18426670599cf50f1db9b11e02c3344aa83fd9ba4b3dd076ea618e480f7bd651858ecc27bb9e932f961cdf09a1e647c1f");
		credential.setUserName("johnny");
		credential.setSalt("j6MYqBbqDdB+HEzBxLM5Rw==");
		
		emp.setCredential(credential);
		
		hrDriver.submitEmployee(emp);
	}*/

}
