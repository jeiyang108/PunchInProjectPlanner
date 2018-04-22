package ca.bcit.PunchIn.ArqullianTests;

import static org.junit.Assert.*;

import java.util.Calendar;
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
public class ReportBeanTests {

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
    private ProjectDAO projectDAO;
	
	@Inject
	private EmployeeDAO empDAO;
	
	@Inject 
	private ReportBean reportBean;

	@Test
	@InSequence(1)
	public void testGetSetProjectList() {
		reportBean.setProjectList();
		List<ProjectEntity> projects = projectDAO.findProjects();
		assertEquals(projects, reportBean.getProjectList());
	}

	@Test
	@InSequence(2)
	public void testGetProjectSummary() {
		ProjectEntity project = new ProjectEntity();
		String actual = reportBean.getProjectSummary(project);
		assertEquals("reports_summary", actual);
	}
	
	@Test
	@InSequence(3)
	public void testGetProjectBreakdown() {
		ProjectEntity project = new ProjectEntity();
		String actual = reportBean.getProjectBreakdown(project);
		assertEquals("reports_employee_summary", actual);
	}
	
	
}
