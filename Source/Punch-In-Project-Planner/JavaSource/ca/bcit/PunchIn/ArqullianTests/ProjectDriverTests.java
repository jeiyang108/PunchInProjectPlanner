package ca.bcit.PunchIn.ArqullianTests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.primefaces.model.DualListModel;

import ca.bcit.PunchIn.PasswordHash;
import ca.bcit.PunchIn.Beans.LoginBean;
import ca.bcit.PunchIn.Beans.ProjectDriver;
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
public class ProjectDriverTests {

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
        		.addAsManifestResource(new File("WebContent/WEB-INF/faces-config.xml"), "faces-config.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsResource("META-INF/persistence.xml");
    }
	
	@Inject 
    private ProjectDAO projectDAO;
	
	@Inject
	private EmployeeDAO empDAO;
	
	@Inject 
	private ProjectDriver projectDriver;

	@Test
	@InSequence(1)
	public void testGetSetCurrProj() {
		ProjectEntity project = new ProjectEntity();
		projectDriver.setCurrProj(project);
		assertEquals(project ,projectDriver.getCurrProj());
	}
	
	@Test
	@InSequence(2)
	public void testGetSetPickEmps() {
		DualListModel<EmployeeEntity> pickemps = new DualListModel<EmployeeEntity>();
		projectDriver.setPickEmps(pickemps);
		assertEquals(pickemps, projectDriver.getPickEmps());
	}

	@Test
	@InSequence(3)
	public void testGetSetCurrWorkpackage() {
		WorkPackageEntity workpackage = new WorkPackageEntity();
		projectDriver.setCurrWorkpackage(workpackage);
		assertEquals(workpackage, projectDriver.getCurrWorkpackage());
	}
	
	@Test
	@InSequence(4)
	public void testGetProjects() {
		List<ProjectEntity> projects = projectDAO.findProjects();
		assertEquals(projects, projectDriver.getProjects());
	}
	
	@Test
	@InSequence(5)
	public void testSetGetFilteredProjects() {
		List<ProjectEntity> filteredprojects = projectDAO.findProjects();
		projectDriver.setFilteredProjects(filteredprojects);
		assertEquals(filteredprojects, projectDriver.getFilteredProjects());
	}
	
	@Test
	@InSequence(6)
	public void testGetWorkPackages() {
		List<WorkPackageEntity> workpackage = projectDAO.findWorkpackages();
		assertEquals(workpackage, projectDriver.getWorkpackages());
	}
	
	@Test
	@InSequence(7)
	public void testGetWorkPackageNameOfProjectPara() {
		
	}
	
	@Test
	@InSequence(8)
	public void testGetWorkPackageNameOfProject() {
		
	}
	
	@Test
	@InSequence(9)
	public void testGetEmployeeNamesOfProjectPara() {
		
	}
	
	@Test
	@InSequence(10)
	public void testGetEmployeeNamesOfProject() {
		
	}
	
	@Test
	@InSequence(11)
	public void testGetProjectManagers() {
		List<EmployeeEntity> projectmanagers = empDAO.getProjectManagers();
		
		assertEquals(projectmanagers, projectDriver.getProjectManagers());
	}
	
	@Test
	@InSequence(12)
	public void testViewProject() {
		ProjectEntity project = new ProjectEntity();
		projectDriver.viewProject(project);
		assertEquals(project, projectDriver.getCurrProj());
		assertEquals("viewProject", projectDriver.viewProject(project));
	}
	
	@Test
	@InSequence(13)
	public void testViewWorkPackage() {
		WorkPackageEntity workpackage = new WorkPackageEntity();
		projectDriver.viewWorkpackage(workpackage);
		assertEquals(workpackage, projectDriver.getCurrWorkpackage());
		assertEquals("viewWorkpackage", projectDriver.viewWorkpackage(workpackage));
	}
	
	@Test
	@InSequence(14)
	public void testCreateProject() {
		projectDriver.createProject();
		assertNotNull(projectDriver.getCurrProj());
	}
	
	@Test
	@InSequence(15)
	public void testEditProject() {
		assertEquals("editProject", projectDriver.editProject());
	}
	
	@Test
	@InSequence(16)
	public void testSubmitEditProject() {
		//TO DO
		assertEquals("viewProject", projectDriver.submitEditProject());
	}
	
	@Test
	@InSequence(17)
	public void testNewWp() {
		//To Do
	}
	
	@Test
	@InSequence(18)
	public void testGetProjectStatusList() {
		assertEquals(Stream.of(ProjectStatus.values())
        .map(Enum::name)
        .collect(Collectors.toList()), 
        projectDriver.getProjectStatusList());
	}
	
	@Test
	@InSequence(19)
	public void testGetWorkPackageStatusList() {
		assertEquals(Stream.of(WorkPackageStatus.values())
		        .map(Enum::name)
		        .collect(Collectors.toList()),
		        projectDriver.getWorkPackageStatusList());
	}
	
	@Test
	@InSequence(20)
	public void testCurrWorkpackageSave() {
		//To Do
	}
}
