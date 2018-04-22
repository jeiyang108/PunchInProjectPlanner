package ca.bcit.PunchIn.ArqullianTests;

import static org.junit.Assert.*;

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
public class LoginBeanTests {

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
	LoginBean loginBean;
	
	@Inject
	EmployeeDAO employeeDAO;
	
	@Inject
	CredentialDAO credentialDAO;
	
	@Test
	@InSequence(1)
	public void testIsPM1() {
		EmployeeEntity pm = employeeDAO.find((long) 4);
		CredentialEntity credential = credentialDAO.find("koi");
		
		Employee employee = new Employee();
		employee.setBasicInfo(pm);
		employee.setCredential(credential);
		
		loginBean.setCurrEmp(employee);
		boolean isPM = loginBean.isProjectManager();
		
		assertTrue(isPM);
	}
	
	@Test
	@InSequence(2)
	public void testIsPM2() {
		EmployeeEntity pm = employeeDAO.find((long) 1);
		CredentialEntity credential = credentialDAO.find("admin");
		
		Employee employee = new Employee();
		employee.setBasicInfo(pm);
		employee.setCredential(credential);
		
		loginBean.setCurrEmp(employee);
		boolean isPM = loginBean.isProjectManager();
		
		assertTrue(isPM);
	}
	
	@Test
	@InSequence(3)
	public void testIsPM3() {
		EmployeeEntity pm = employeeDAO.find((long) 5);
		CredentialEntity credential = credentialDAO.find("ghost");
		
		Employee employee = new Employee();
		employee.setBasicInfo(pm);
		employee.setCredential(credential);
		
		loginBean.setCurrEmp(employee);
		boolean isPM = loginBean.isProjectManager();
		
		assertFalse(isPM);
	}
	
	@Test
	@InSequence(4)
	public void testIsRE1() {
		EmployeeEntity re = employeeDAO.find((long) 4);
		CredentialEntity credential = credentialDAO.find("koi");
		
		Employee employee = new Employee();
		employee.setBasicInfo(re);
		employee.setCredential(credential);
		
		loginBean.setCurrEmp(employee);
		boolean isRE = loginBean.isResponsibleEngineer();
		
		assertFalse(isRE);
	}
	
	@Test
	@InSequence(5)
	public void testIsRE2() {
		EmployeeEntity re = employeeDAO.find((long) 1);
		CredentialEntity credential = credentialDAO.find("admin");
		
		Employee employee = new Employee();
		employee.setBasicInfo(re);
		employee.setCredential(credential);
		
		loginBean.setCurrEmp(employee);
		boolean isRE = loginBean.isResponsibleEngineer();
		
		assertTrue(isRE);
	}
	
	@Test
	@InSequence(6)
	public void testIsRE3() {
		EmployeeEntity re = employeeDAO.find((long) 5);
		CredentialEntity credential = credentialDAO.find("ghost");
		
		Employee employee = new Employee();
		employee.setBasicInfo(re);
		employee.setCredential(credential);
		
		loginBean.setCurrEmp(employee);
		boolean isRE = loginBean.isResponsibleEngineer();
		
		assertTrue(isRE);
	}

}
