package ca.bcit.PunchIn.ArqullianTests;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import ca.bcit.PunchIn.Beans.TimesheetDriver;
import ca.bcit.PunchIn.DAOs.BaseDAO;
import ca.bcit.PunchIn.DAOs.CredentialDAO;
import ca.bcit.PunchIn.DAOs.EmployeeDAO;
import ca.bcit.PunchIn.DAOs.ProjectDAO;
import ca.bcit.PunchIn.DAOs.TimesheetDAO;
import ca.bcit.PunchIn.Entities.Credential.CredentialEntity;
import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;
import ca.bcit.PunchIn.Entities.Project.ProjectEntity;
import ca.bcit.PunchIn.Entities.Project.WorkPackageEntity;
import ca.bcit.PunchIn.Entities.Project.WorkPackageId;
import ca.bcit.PunchIn.Entities.Timesheet.Timesheet;
import ca.bcit.PunchIn.Entities.Timesheet.TimesheetRow;
import ca.bcit.PunchIn.Models.Employee.Employee;
import sun.security.timestamp.TSRequest;

@RunWith(Arquillian.class)
public class TimesheetDriverTests {

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
	
	private static final long EMPLOYEE_ID = 5;
	private static final int PROJECT_ID = 10;
	private int timesheetId = 0;
	
	@Inject
    private TimesheetDAO timesheetDAO;
	
	@Inject
	private TimesheetDriver timesheetDriver;
	
	@Inject
	private LoginBean loginBean;
	
	@Inject 
	private CredentialDAO credentialDAO;
	
	@Inject
	private EmployeeDAO employeeDAO;
	
	private Timesheet ts;
	
	
	public int getId() {
		List<Timesheet> list = timesheetDAO.findEmployeeSheets(EMPLOYEE_ID);
		Timesheet ts = list.get(list.size() - 1);
		return ts.getTimesheetId();
	}
	
	private void createTimesheet() {
		EmployeeEntity employee = employeeDAO.find(EMPLOYEE_ID);
		timesheetDriver.addTimesheet(employee);
		ts = timesheetDriver.getTs();
	}
	
	private void createLoginBean() {
		EmployeeEntity employeeEntity = employeeDAO.find(EMPLOYEE_ID);
		Employee employee = new Employee();
		employee.setBasicInfo(employeeEntity);
		
		CredentialEntity credential = credentialDAO.find("ghost");
		loginBean.setUsername(credential.getUserName());
		loginBean.setPassword(credential.getPassword());
		loginBean.setCurrEmp(employee);
	}
	
	@Test
	@InSequence(1)
	public void testAddTimesheet() {
		createTimesheet();
		
		assertEquals("Unapproved", ts.getApprovalStatus().toString());
	}
	
	@Test
	@InSequence(2)
	public void testAddTimesheetRow() {		
		createTimesheet();
		timesheetDriver.addTimesheetRow();
		timesheetDriver.addTimesheetRow();
		timesheetDriver.addTimesheetRow();
		
		assertEquals(3, ts.getEntries().size());
	}
	
	@Test
	@InSequence(3)
	public void testDeleteTimesheetRow() {		
		createTimesheet();
		timesheetDriver.addTimesheetRow();
		timesheetDriver.addTimesheetRow();
		timesheetDriver.addTimesheetRow();
		
		List<TimesheetRow> rows = ts.getEntries();
		
		TimesheetRow row1 = rows.get(0);
		
		timesheetDriver.deleteTimesheetRow(row1);
		
		assertEquals(2, ts.getEntries().size());
	}
	
	@Test
	@InSequence(4)
	public void testSaveTimesheetPersist() {		
		createTimesheet();
		
		// Mocks a login bean needed for saveTimesheet()
		createLoginBean();
		
		timesheetDriver.saveTimesheet();
		
		timesheetId = getId();
		
		Timesheet timesheet = timesheetDAO.find(timesheetId);
		
		assertEquals("Olga", timesheet.getEmployee().getFirstName());
	}
	
	/*@Test
	@InSequence(5)
	public void testSaveTimesheetRow() {		
		List<Timesheet> list = timesheetDAO.findEmployeeSheets(EMPLOYEE_ID);
		ts = list.get(list.size() - 1);
		timesheetDriver.viewTimesheet(ts);

		timesheetDriver.addTimesheetRow();
		Set<TimesheetRow> rows = ts.getEntries();
		
		TimesheetRow row1 = rows.iterator().next();
		
		WorkPackageId wpId = new WorkPackageId();
		wpId.setProjectNum(501);
		wpId.setWorkpackageID("ERPS");
		
		WorkPackageEntity wp = projectDAO.findWP(wpId);
		
		row1.setWorkPackage(wp);
		
		//timesheetDriver.addTimesheetRow();
		//timesheetDriver.addTimesheetRow();
		
		timesheetDriver.saveTimesheet();
		
		Timesheet timesheet = timesheetDAO.find(ts.getTimesheetId());
		
		assertEquals("Grissel", timesheet.getEmployee().getFirstName());
	}*/
	
	
	@Test
	@InSequence(5)
	public void testSaveTimesheetMerge() {
		List<Timesheet> list = timesheetDAO.findEmployeeSheets(EMPLOYEE_ID);
		ts = list.get(list.size() - 1);
		ts.setApprovalNotes("needs approval");
		
		// Mocks a login bean needed for saveTimesheet()
		createLoginBean();
		
		// use this to set current ts in timesheetdriver class
		timesheetDriver.viewTimesheet(ts);
		
		timesheetDriver.saveTimesheet();
		
		Timesheet timesheet = timesheetDAO.find(ts.getTimesheetId());
		
		assertEquals ("needs approval", timesheet.getApprovalNotes());
		
	}
	
	@Test
	@InSequence(5)
	public void testGetAllProjects() {
		List<ProjectEntity> projects = timesheetDriver.getAllProjects(EMPLOYEE_ID);
		
		assertEquals(1, projects.size());
	}
	
	@Test
	@InSequence(6)
	public void testGetWorkpackagesForProject() {
		List<WorkPackageEntity> workPackages = timesheetDriver.getWorkpackagesForProject(PROJECT_ID);
		
		assertEquals(3, workPackages.size());
	}
	
	@Test
	@InSequence(7)
	public void testSendTimesheetForApproval() {
		List<Timesheet> list = timesheetDAO.findEmployeeSheets(EMPLOYEE_ID);
		ts = list.get(list.size() - 1);
		timesheetDriver.viewTimesheet(ts);
		
		// Mocks a login bean needed for saveTimesheet()
		createLoginBean();
		
		timesheetDriver.sendTimesheetForApproval();
		
		assertEquals("SentForApproval", ts.getApprovalStatus().toString());
	}
	
	@Test
	@InSequence(8)
	public void testApproveTimesheet() {
		List<Timesheet> list = timesheetDAO.findEmployeeSheets(EMPLOYEE_ID);
		ts = list.get(list.size() - 1);
		timesheetDriver.viewTimesheet(ts);
		
		// Mocks a login bean needed for saveTimesheet()
		createLoginBean();
		
		timesheetDriver.approveTimesheet();
		
		assertEquals("Approved", ts.getApprovalStatus().toString());
	}
	
	@Test
	@InSequence(9)
	public void testReturnTimesheet() {
		List<Timesheet> list = timesheetDAO.findEmployeeSheets(EMPLOYEE_ID);
		ts = list.get(list.size() - 1);
		timesheetDriver.viewTimesheet(ts);
		
		// Mocks a login bean needed for saveTimesheet()
		createLoginBean();
		
		timesheetDriver.returnTimesheet();
		
		assertEquals("Returned", ts.getApprovalStatus().toString());
	}
	
	@Test
	@InSequence(10)
	public void testIsReadOnly() {
		List<Timesheet> list = timesheetDAO.findEmployeeSheets(EMPLOYEE_ID);
		ts = list.get(list.size() - 1);
		timesheetDriver.viewTimesheet(ts);
		
		boolean isReadOnly = timesheetDriver.isReadOnly();
		
		assertFalse(isReadOnly);
	}
	
	@Test
	@InSequence(10)
	public void testShowApprovalNotes() {
		List<Timesheet> list = timesheetDAO.findEmployeeSheets(EMPLOYEE_ID);
		ts = list.get(list.size() - 1);
		timesheetDriver.viewTimesheet(ts);
		
		boolean val = timesheetDriver.showApprovalNotes();
		
		assertTrue(val);
	}
	
	@Test
	@InSequence(11)
	public void testGetTimesheetsForApprover() {
		List<Timesheet> list = timesheetDriver.getTimesheetsForApprover(2);
		
		assertEquals(266, list.size());
	}
	
	@Test
	@InSequence(12)
	public void testViewTimesheets() {
		List<Timesheet> list = timesheetDAO.findEmployeeSheets(EMPLOYEE_ID);
		ts = list.get(list.size() - 1);
		timesheetDriver.viewTimesheet(ts);

		assertEquals("Returned", ts.getApprovalStatus().toString());
	}

}
