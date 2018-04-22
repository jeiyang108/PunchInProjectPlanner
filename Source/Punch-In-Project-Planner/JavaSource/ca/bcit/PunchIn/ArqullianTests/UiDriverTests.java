package ca.bcit.PunchIn.ArqullianTests;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.primefaces.model.DualListModel;

import ca.bcit.PunchIn.PasswordHash;
import ca.bcit.PunchIn.Beans.LoginBean;
import ca.bcit.PunchIn.Beans.ProjectDriver;
import ca.bcit.PunchIn.Beans.ReportBean;
import ca.bcit.PunchIn.Beans.UiDriver;
import ca.bcit.PunchIn.DAOs.BaseDAO;
import ca.bcit.PunchIn.DAOs.EmployeeDAO;
import ca.bcit.PunchIn.DAOs.ProjectDAO;
import ca.bcit.PunchIn.Entities.Credential.CredentialEntity;
import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;
import ca.bcit.PunchIn.Entities.Project.ProjectEntity;
import ca.bcit.PunchIn.Entities.Project.WorkPackageEntity;
import ca.bcit.PunchIn.Entities.Timesheet.Timesheet;
import ca.bcit.PunchIn.Enum.ProjectStatus;
import ca.bcit.PunchIn.Enum.WorkPackageStatus;
import ca.bcit.PunchIn.Models.Employee.Employee;

@RunWith(Arquillian.class)
public class UiDriverTests {

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
    private UiDriver uiDriver;

	@Test
	@InSequence(1)
	public void testGetSetContentRightProject() {
		uiDriver.setContentRightProject(true);
		assertTrue(uiDriver.getContentRightProject());
		uiDriver.setContentRightProject(false);
		assertFalse(uiDriver.getContentRightProject());
		
	}
	
	@Test
	@InSequence(2)
	public void testGetSetContentRightWp() {
		uiDriver.setContentRightWp(true);
		assertTrue(uiDriver.getContentRightWp());
		uiDriver.setContentRightWp(false);
		assertFalse(uiDriver.getContentRightWp());
	}

	@Test
	@InSequence(3)
	public void testGetSetViewProject() {
		ProjectEntity project = new ProjectEntity();
		uiDriver.setViewProject(project);
		assertEquals(project, uiDriver.getViewProject());
	}
	
	@Test
	@InSequence(4)
	public void testGetSetViewWp() {
		WorkPackageEntity workpackage = new WorkPackageEntity();
		uiDriver.setViewWp(workpackage);
		assertEquals(workpackage, uiDriver.getViewWp());
	}
	
	@Test
	@InSequence(5)
	public void testGetTotalComplete() {
		WorkPackageEntity wp1 = new WorkPackageEntity();
		WorkPackageEntity wp2 = new WorkPackageEntity();
		wp1.setStatus("closed");
		wp2.setStatus("complete");
		List<WorkPackageEntity> list = new ArrayList<WorkPackageEntity>();
		list.add(wp1);
		list.add(wp2);
		assertEquals(2, uiDriver.getTotalComplete(list));
		
	}
	
	@Test
	@InSequence(6)
	public void testGetTotalInProgress() {
		WorkPackageEntity wp1 = new WorkPackageEntity();
		WorkPackageEntity wp2 = new WorkPackageEntity();
		wp1.setStatus("open");
		wp2.setStatus("complete");
		List<WorkPackageEntity> list = new ArrayList<WorkPackageEntity>();
		list.add(wp1);
		list.add(wp2);
		assertEquals(1, uiDriver.getTotalInProgress(list));
		
	}
	
	@Test
	@InSequence(7)
	public void testGetTotalNotStarted() {
		WorkPackageEntity wp1 = new WorkPackageEntity();
		WorkPackageEntity wp2 = new WorkPackageEntity();
		wp1.setStatus("created");
		wp2.setStatus("planned");
		List<WorkPackageEntity> list = new ArrayList<WorkPackageEntity>();
		list.add(wp1);
		list.add(wp2);
		assertEquals(2, uiDriver.getTotalNotStarted(list));
		
	}
	
	@Test
	@InSequence(8)
	public void testGetProjectStatusClass() {
		ProjectEntity project = new ProjectEntity();
		project.setStatus("closed");
		assertEquals("statuscomplete", uiDriver.getProjectStatusClass(project));
		
		project.setStatus("complete");
		assertEquals("statuscomplete", uiDriver.getProjectStatusClass(project));
		
		project.setStatus("open");
		assertEquals("statusinprogress", uiDriver.getProjectStatusClass(project));
	
		project.setStatus("created");
		assertEquals("statusnotstarted", uiDriver.getProjectStatusClass(project));
	}
	
	@Test
	@InSequence(9)
	public void testGetWpStatusClass() {
		WorkPackageEntity workpackage = new WorkPackageEntity();
		workpackage.setStatus("closed");
		assertEquals("statuscomplete", uiDriver.getWpStatusClass(workpackage));
		
		workpackage.setStatus("complete");
		assertEquals("statuscomplete", uiDriver.getWpStatusClass(workpackage));
		
		workpackage.setStatus("open");
		assertEquals("statusinprogress", uiDriver.getWpStatusClass(workpackage));
	
		workpackage.setStatus("created");
		assertEquals("statusnotstarted", uiDriver.getWpStatusClass(workpackage));
	}
	
	@Test
	@InSequence(10)
	public void testFormatDate() {
		assertEquals("No Date", uiDriver.formatDate(null, "full"));
		assertEquals("No Date", uiDriver.formatDate(null, "else"));
		Date date = new Date(System.currentTimeMillis());
		String expected = new SimpleDateFormat("dd-MM-YYYY").format(date);
		assertEquals(expected, uiDriver.formatDate(date, "full"));
		expected = new SimpleDateFormat("dd-MM").format(date);
		assertEquals(expected, uiDriver.formatDate(date, "else"));
	}
	
	@Test
	@InSequence(11)
	public void testDisplayName() {
		EmployeeEntity emp = new EmployeeEntity();
		assertEquals("None", uiDriver.displayName(null));
		
		emp.setFirstName("Dangus");
		emp.setLastName("Changus");
		assertEquals("Dangus Changus", uiDriver.displayName(emp));
	}
	
	@Test
	@InSequence(12)
	public void testDisplayApprovalIcon() {
		Timesheet ts = new Timesheet();
		ts.setApprovalStatus(Timesheet.ApprovalStatus.SentForApproval);
		assertEquals("sent_icon.png", uiDriver.displayApprovalIcon(ts));
		
		ts.setApprovalStatus(Timesheet.ApprovalStatus.Returned);
		assertEquals("x_icon.png", uiDriver.displayApprovalIcon(ts));
		
		ts.setApprovalStatus(Timesheet.ApprovalStatus.Approved);
		assertEquals("check_icon.png", uiDriver.displayApprovalIcon(ts));
		
		ts.setApprovalStatus(Timesheet.ApprovalStatus.Unapproved);
		assertEquals("draft_icon.png", uiDriver.displayApprovalIcon(ts));
	}
	
	@Test
	@InSequence(13)
	public void testReset() {
		uiDriver.reset();
		assertFalse(uiDriver.getContentRightProject());
		assertFalse(uiDriver.getContentRightWp());
		assertNull(uiDriver.getViewProject());
		assertNull(uiDriver.getViewWp());
	}
	
}
