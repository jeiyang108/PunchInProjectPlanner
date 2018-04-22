package ca.bcit.PunchIn.ArqullianTests;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import javax.ejb.EJBException;
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
import ca.bcit.PunchIn.DAOs.EmployeeDAO;
import ca.bcit.PunchIn.DAOs.TimesheetDAO;
import ca.bcit.PunchIn.Entities.Credential.CredentialEntity;
import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;
import ca.bcit.PunchIn.Entities.Project.ProjectEntity;
import ca.bcit.PunchIn.Entities.Timesheet.Timesheet;
import ca.bcit.PunchIn.Models.Employee.Employee;

@RunWith(Arquillian.class)
public class TimesheetDAOTests {

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
	TimesheetDAO timesheetDAO;
	
	@Inject
	EmployeeDAO employeeDAO;
	
	private static final long EMPLOYEE_ID = 2;
	
	public int getId() {
		List<Timesheet> list = timesheetDAO.findEmployeeSheets(EMPLOYEE_ID);
		Timesheet ts = list.get(list.size() - 1);
		return ts.getTimesheetId();
	}
	
	@Test
	@InSequence(1)
	public void testPersist() {
		EmployeeEntity employee = employeeDAO.find(EMPLOYEE_ID);
		Date date = new Date();
		Timesheet ts = new Timesheet(date, employee);
		
		timesheetDAO.persist(ts);
		
		List<Timesheet> list = timesheetDAO.findEmployeeSheets(EMPLOYEE_ID);
		
		assertEquals(2, list.size());
		
	}
	
	@Test
	@InSequence(2)
	public void testFindEmployeeSheets() {
		List<Timesheet> list = timesheetDAO.findEmployeeSheets(EMPLOYEE_ID);
		EmployeeEntity employee = employeeDAO.find(EMPLOYEE_ID);
		
		Timesheet ts = list.get(list.size() - 1);
				
		assertEquals(employee, ts.getEmployee());
	}
	
	@Test
	@InSequence(3)
	public void testFind() {
		Timesheet ts  = timesheetDAO.find(getId());
		EmployeeEntity employee = ts.getEmployee();
		EmployeeEntity e = employeeDAO.find(EMPLOYEE_ID);
		
		assertEquals(e, employee);
		
	}
	
	@Test
	@InSequence(4)
	public void testMerge() {
		Timesheet ts  = timesheetDAO.find(getId());

		ts.setFlextime();
		
		Timesheet newTS = timesheetDAO.find(getId());
		
		assertNull(newTS.getFlextime());
		
	}
	
	@Test (expected = EJBException.class)
	@InSequence(5)
	public void testPersistFail() {
		EmployeeEntity employee = employeeDAO.find(EMPLOYEE_ID);
		Date date = new Date();
		Timesheet ts = new Timesheet(date, employee);
		
		ts.setTimesheetId(getId());
		
		timesheetDAO.persist(ts);
	}
	
	@Test
	@InSequence(6)
	public void testRemove() {
		int oldId = getId();
		
		timesheetDAO.remove(oldId);
		
		assertNull(timesheetDAO.find(oldId));
	}
	
	@Test
	@InSequence(7)
	public void testFindApproverSheets1() {
		List<Timesheet> ts = timesheetDAO.findApproverSheets(EMPLOYEE_ID);
		
		assertEquals(266, ts.size());
	}

	@Test
	@InSequence(7)
	public void testFindApproverSheets2() {
		List<Timesheet> ts = timesheetDAO.findApproverSheets(5);
		
		assertEquals(0, ts.size());
	}
}
