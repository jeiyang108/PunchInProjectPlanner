package ca.bcit.PunchIn.ArqullianTests;

import static org.junit.Assert.*;

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
public class CredentialDAOTests {

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
		credential.setPassword("99d2c681d92e73b0297259f8e1f057c18426670599cf50f1db9b11e02c3344aa83fd9ba4b3dd076ea618e480f7bd651858ecc27bb9e932f961cdf09a1e647c1f");
		credential.setUserName("johnny");
		credential.setSalt("j6MYqBbqDdB+HEzBxLM5Rw==");
		
		employee.setCredential(credential);
				
		return employee;
	}
	
	@Test
	@InSequence(1)
	public void testPersist() {
		Employee employee = createEmployee();
		
		employeeDAO.persist(employee);

		credentialDAO.persist(employee.getCredential());

		assertNotNull(credentialDAO.find("johnny"));
	}
	
	@Test
	@InSequence(2)
	public void testFind() {
		CredentialEntity credential = credentialDAO.find("johnny");
		
		assertEquals("johnny", credential.getUserName());
	}
	
	@Test
	@InSequence(3)
	public void testMerge() {
		CredentialEntity credential = credentialDAO.find("johnny");

		credential.setPassword("a");

		credentialDAO.merge(credential);
		
		CredentialEntity newCredential = credentialDAO.find("johnny");
		
		assertEquals("a", newCredential.getPassword());
		
	}
	
	@Test (expected = EJBTransactionRolledbackException.class)
	@InSequence(4)
	public void testPersistFail() {
		EmployeeEntity employeeEntity = employeeDAO.find((long) EMPLOYEE_ID);
		
		CredentialEntity credential = new CredentialEntity();
		credential.setEmployee(employeeEntity);
		credential.setRole("employee");
		credential.setPassword("99d2c681d92e73b0297259f8e1f057c18426670599cf50f1db9b11e02c3344aa83fd9ba4b3dd076ea618e480f7bd651858ecc27bb9e932f961cdf09a1e647c1f");
		credential.setUserName("johnny");
		credential.setSalt("j6MYqBbqDdB+HEzBxLM5Rw==");
		
		credentialDAO.persist(credential);
		
	}
	
	@Test
	@InSequence(5)
	public void testRemove() {
		credentialDAO.remove("johnny");
		employeeDAO.remove(EMPLOYEE_ID);
		
		assertNull(credentialDAO.find("johnny"));
		
	}
	
	@Test (expected = EJBException.class)
	@InSequence(6)
	public void testRemoveFail() {
		credentialDAO.remove("james");	
	}
	
	@Test
	@InSequence(7)
	public void testValidate1() {
		boolean isValid = credentialDAO.validate("doggo", "password");
		
		assertFalse(isValid);
	}
	
	@Test
	@InSequence(8)
	public void testValidate2() {
		boolean isValid = credentialDAO.validate("doggo", "test");
		
		assertFalse(isValid);
	}

}
