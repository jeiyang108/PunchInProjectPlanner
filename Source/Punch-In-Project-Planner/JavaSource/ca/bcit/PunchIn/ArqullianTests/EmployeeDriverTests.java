package ca.bcit.PunchIn.ArqullianTests;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.bcit.PunchIn.PasswordHash;
import ca.bcit.PunchIn.Beans.EmployeeDriver;
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
public class EmployeeDriverTests {

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
	
	private static final long EMPLOYEE_ID = 1;
	private static final String USERNAME = "admin";
	
	@Inject 
	private LoginBean loginInfo;
	
	@Inject
	private EmployeeDriver empDriver;
	
	@Inject 
	private EmployeeDAO empDAO;
	
	@Test
	public void testSetCurrentEmp() {
		loginInfo.setEmployeeId(EMPLOYEE_ID);
		loginInfo.setUsername(USERNAME);
		
		EmployeeEntity currentEmp = empDriver.setCurrentEmp().getBasicInfo();
		
		assertEquals(currentEmp, empDAO.find(EMPLOYEE_ID));
	}

}
