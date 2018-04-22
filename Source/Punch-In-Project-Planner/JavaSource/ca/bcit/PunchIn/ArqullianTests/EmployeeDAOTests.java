package ca.bcit.PunchIn.ArqullianTests;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRolledbackException;
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
public class EmployeeDAOTests {

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
	
	private static final long EMPLOYEE_ID = 501;
	
	@Inject
	CredentialDAO credentialDAO;
	
	@Inject
	EmployeeDAO employeeDAO;
	
	@Test
	@InSequence(1)
	public void testPersist() {
		EmployeeEntity employeeEntity = new EmployeeEntity();
		Employee employee = new Employee();
		
		EmployeeEntity emp = employeeDAO.find((long) 1);
		
		employeeEntity.setEmployeeID(EMPLOYEE_ID);
		employeeEntity.setFirstName("Joe");
		employeeEntity.setLastName("Dirt");
		employeeEntity.setTimesheetApprover(emp);
		employeeEntity.setSupervisor(emp);
		employeeEntity.setActive(true);
		
		employee.setBasicInfo(employeeEntity);
		
		employeeDAO.persist(employee);
	}
	
	@Test
	@InSequence(2)
	public void testFind() {
		EmployeeEntity employeeEntity = employeeDAO.find(EMPLOYEE_ID);
		
		assertEquals("Joe", employeeEntity.getFirstName());
		
	}
	
	@Test
	@InSequence(3)
	public void testMerge() {
		EmployeeEntity employeeEntity = employeeDAO.find(EMPLOYEE_ID);
		Employee employee = new Employee();

		employeeEntity.setFirstName("James");
		
		employee.setBasicInfo(employeeEntity);

		employeeDAO.merge(employee);
		
		EmployeeEntity newEmployeeEntity = employeeDAO.find(EMPLOYEE_ID);
		
		assertEquals("James", newEmployeeEntity.getFirstName());
		
	}
	
	@Test
	@InSequence(4)
	public void testUpdateSelf() {
		EmployeeEntity employeeEntity = employeeDAO.find(EMPLOYEE_ID);
		Employee employee = new Employee();

		employeeEntity.setLastName("Cantwell");
		
		employee.setBasicInfo(employeeEntity);

		employeeDAO.merge(employee);
		
		EmployeeEntity newEmployeeEntity = employeeDAO.find(EMPLOYEE_ID);
		
		assertEquals("Cantwell", newEmployeeEntity.getLastName());
		
	}
	
	@Test (expected = EJBTransactionRolledbackException.class)
	@InSequence(5)
	public void testPersistFail() {
		EmployeeEntity employeeEntity = employeeDAO.find(EMPLOYEE_ID);
		Employee employee = new Employee();

		employee.setBasicInfo(employeeEntity);
		
		employeeDAO.persist(employee);		
	}
	
	@Test
	@InSequence(6)
	public void testRemove() {
		employeeDAO.remove(EMPLOYEE_ID);
		
		assertNull(employeeDAO.find(EMPLOYEE_ID));
		
	}
	
	@Test (expected = EJBException.class)
	@InSequence(7)
	public void testRemoveFail() {
		employeeDAO.remove(EMPLOYEE_ID);
		
	}
	
	@Test
	@InSequence(8)
	public void testGetEmployees1() {
		Set<Employee> employees = employeeDAO.getEmployees();
		
		int setSize = employees.size();
		
		assertEquals(500, setSize);
	}
	
	@Test
	@InSequence(9)
	public void testGetEmployees2() {
		EmployeeEntity supervisor = employeeDAO.find((long) 3);
		
		Set<Employee> employees = employeeDAO.getEmployees(supervisor);
		
		int setSize = employees.size();
		
		assertEquals(497, setSize);
	}

	@Test
	@InSequence(10)
	public void testDelete() {
		EmployeeEntity employeeEntity = new EmployeeEntity();
		Employee employee = new Employee();
		
		EmployeeEntity emp = employeeDAO.find((long) 1);
		
		employeeEntity.setEmployeeID(EMPLOYEE_ID);
		employeeEntity.setFirstName("Joe");
		employeeEntity.setLastName("Dirt");
		employeeEntity.setTimesheetApprover(emp);
		employeeEntity.setSupervisor(emp);
		
		employee.setBasicInfo(employeeEntity);
		
		employeeDAO.persist(employee);
		
		employeeDAO.delete(employee);
	}
	
	@Test
	@InSequence(11)
	public void testGetProjectManagers() {
		List<EmployeeEntity> pms = employeeDAO.getProjectManagers();
		
		assertEquals(500, pms.size());
	}
	
	@Test
	@InSequence(11)
	public void testEmployeeIsTimesheetApprover1() {
		boolean isApprover = employeeDAO.employeeIsTimesheetApprover((long) 2);
		
		assertTrue(isApprover);
	}
	
	@Test
	@InSequence(11)
	public void testEmployeeIsTimesheetApprover2() {
		boolean isApprover = employeeDAO.employeeIsTimesheetApprover((long) 500);
		
		assertFalse(isApprover);
	}
	
}
