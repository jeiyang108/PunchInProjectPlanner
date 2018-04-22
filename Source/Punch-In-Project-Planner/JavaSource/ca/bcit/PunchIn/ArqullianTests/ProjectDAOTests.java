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
import ca.bcit.PunchIn.DAOs.EmployeeDAO;
import ca.bcit.PunchIn.DAOs.ProjectDAO;
import ca.bcit.PunchIn.Entities.Credential.CredentialEntity;
import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;
import ca.bcit.PunchIn.Entities.Project.ProjectEntity;
import ca.bcit.PunchIn.Entities.Project.ResponsibleEngineerEstimate;
import ca.bcit.PunchIn.Entities.Project.WPEstimateList;
import ca.bcit.PunchIn.Entities.Project.WorkPackageEntity;
import ca.bcit.PunchIn.Entities.Project.WorkPackageId;
import ca.bcit.PunchIn.Entities.Timesheet.Timesheet;
import ca.bcit.PunchIn.Models.Employee.Employee;

@RunWith(Arquillian.class)
public class ProjectDAOTests {

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
	ProjectDAO projectDAO;
	
	@Inject
	EmployeeDAO employeeDAO;
	
	private static final int PROJECT_NUM = 2018;
	private static final String WP_ID = "ERPS";
	private static final long PM_ID = 2;
	private static final long EMP_ID = 1;
	
	private WorkPackageEntity getWPEntity() {
		WorkPackageId wpId = new WorkPackageId();
		wpId.setProjectNum(PROJECT_NUM);
		wpId.setWorkpackageID(WP_ID);
		
		return projectDAO.findWP(wpId);
	}
	
	@Test
	@InSequence(1)
	public void testPersistProject() {
		ProjectEntity project = new ProjectEntity();
		EmployeeEntity projectManager = employeeDAO.find(PM_ID);

		project.setProjectNum(PROJECT_NUM);
		project.setProjectName("Enterprise Development");
		project.setProjectManager(projectManager);
		project.setStatus("Started");
		project.setSummary("Development Started");
		
		projectDAO.persistProject(project);
		
	}
	
	@Test
	@InSequence(2)
	public void testFindProject() {
		ProjectEntity project = projectDAO.findProject(PROJECT_NUM);
		EmployeeEntity projectManager = employeeDAO.find((long) 2);
		
		assertEquals(projectManager, project.getProjectManager());
		
	}
	
	@Test
	@InSequence(3)
	public void testMergeProject() {
		ProjectEntity project = projectDAO.findProject(PROJECT_NUM);

		project.setProjectName("General Development");

		projectDAO.mergeProject(project);
		
		ProjectEntity newProject = projectDAO.findProject(PROJECT_NUM);
		
		assertEquals("General Development", newProject.getProjectName());
		
	}
	
	@Test (expected = EJBTransactionRolledbackException.class)
	@InSequence(4)
	public void testPersistProjectFail() {
		ProjectEntity project = projectDAO.findProject(PROJECT_NUM);
		
		projectDAO.persistProject(project);
		
	}
	
	@Test
	@InSequence(5)
	public void testPersistWP() {
		WorkPackageEntity wpEntity = new WorkPackageEntity();
		WorkPackageId wpId = new WorkPackageId();
		ProjectEntity project = projectDAO.findProject(PROJECT_NUM);
		EmployeeEntity responsibleEngineer = employeeDAO.find(PM_ID);
		
		wpId.setProjectNum(PROJECT_NUM);
		wpId.setWorkpackageID(WP_ID);
		
		wpEntity.setId(wpId);
		wpEntity.setProjectNum(project);
		wpEntity.setTitle("Development");
		wpEntity.setResponsibleEngineer(responsibleEngineer);
		wpEntity.setStatus("Started");
		wpEntity.setPurpose("Test");
		wpEntity.setActivities("Testing activities");
		wpEntity.setInputs("Technolgy");
		wpEntity.setOutputs("Test Plan");
		
		projectDAO.persistWP(wpEntity);
		
	}
	
	@Test
	@InSequence(6)
	public void testFindWP() {		
		WorkPackageEntity wpEntity = getWPEntity();
		
		assertEquals("ERPS", wpEntity.getId().getWorkpackageID());
		
	}
	
	@Test
	@InSequence(7)
	public void testMergeWP() {
		WorkPackageEntity wpEntity = getWPEntity();
		WorkPackageId wpId = wpEntity.getId(); 

		wpEntity.setTitle("Testing");

		projectDAO.mergeWP(wpEntity);
		
		WorkPackageEntity newWPEntity = projectDAO.findWP(wpId);
		
		assertEquals("Testing", newWPEntity.getTitle());
		
	}
	
	@Test (expected = EJBTransactionRolledbackException.class)
	@InSequence(8)
	public void testPersistWPFail() {
		
		WorkPackageEntity wpEntity = getWPEntity();
		
		projectDAO.persistWP(wpEntity);
		
	}
	
	@Test
	@InSequence(9)
	public void testPersistEngineerEstimate() {
		ResponsibleEngineerEstimate estimate = new ResponsibleEngineerEstimate();
		estimate.setWorkPackage(getWPEntity());
		estimate.setJSPersonDays(1000.2);
		projectDAO.persistEngineerEstimate(estimate);
		
		List<WPEstimateList> list = projectDAO.findEngineerPackages(PM_ID);
		WorkPackageEntity ep = projectDAO.findWorkPackageWithEstimates(list.get(0));
		
		Set<ResponsibleEngineerEstimate> set = ep.getResponsibleEngineerEstimates();
		
		ResponsibleEngineerEstimate ree = set.iterator().next();
		
		assertEquals((Double) 1000.2, ree.getJSPersonDays());
	}
	
	@Test
	@InSequence(10)
	public void testRemoveWP() {
		projectDAO.removeEngineerEstimate(PM_ID);
		
		WorkPackageEntity wpEntity = getWPEntity();
		WorkPackageId wpId = wpEntity.getId(); 
		
		projectDAO.removeWP(wpId);
		
		assertNull(projectDAO.findWP(wpId));
		
	}
	
	@Test
	@InSequence(11)
	public void testRemoveProject() {
		
		projectDAO.removeProject(PROJECT_NUM);
		
		assertNull(projectDAO.findProject(PROJECT_NUM));
		
	}
	
	@Test (expected = EJBException.class)
	@InSequence(12)
	public void testRemoveProjectFail() {
		
		projectDAO.removeProject(PROJECT_NUM);
		
	}
	
	@Test
	@InSequence(13)
	public void testFindProjects1() {
		 List<ProjectEntity> list = projectDAO.findProjects();
		 
		 assertEquals(3, list.get(2).getProjectNum());
	}
	
	@Test
	@InSequence(14)
	public void testFindWorkPackages1() {
		List<WorkPackageEntity> list = projectDAO.findWorkpackages();
		WorkPackageId wpId = list.get(2).getId();
		
		assertEquals("DDPL", wpId.getWorkpackageID());
		
	}
	
	@Test
	@InSequence(15)
	public void testFindEmployeeProjects() {
		 List<ProjectEntity> list = projectDAO.findEmployeeProjects(EMP_ID);
		 
		 assertEquals(1, list.size());
	}
	
	@Test
	@InSequence(16)
	public void testFindEmployeePackages() {
		List<WorkPackageEntity> list = projectDAO.findEmployeePackages(EMP_ID);
		
		assertEquals(3, list.size());
	}
	
	@Test
	@InSequence(17)
	public void testFindProjectWithWorkPackages() {
		ProjectEntity project = projectDAO.findProject(5);
		ProjectEntity pro = projectDAO.findProjectWithWorkPackages(project);
		
		List<WorkPackageEntity> list = pro.getWorkPackages();
		//WorkPackageEntity wp = list.get(0);
		
		assertEquals(3, list.size());
	}
	
	@Test
	@InSequence(18)
	public void testFindProjectWithWorkPackagesAndEmployees() {
		ProjectEntity project = projectDAO.findProject(5);
		ProjectEntity pro = projectDAO.findProjectWithWorkPackagesAndEmployees(project);
		
		List<WorkPackageEntity> list = pro.getWorkPackages();
		WorkPackageEntity wp = list.get(0);
		Set<EmployeeEntity> emp = wp.getEmployees();
		
		EmployeeEntity employee = emp.iterator().next();
		
		assertEquals("Olga", employee.getFirstName());
	}
	
	@Test
	@InSequence(19)
	public void testFindProjectWithWorkPackagesAndEmployeesAndEstimates1() {
		ProjectEntity project = projectDAO.findProject(5);
		ProjectEntity pro = projectDAO.findProjectWithWorkPackagesAndEmployeesAndEstimates(project);
		
		List<WorkPackageEntity> list = pro.getWorkPackages();
		WorkPackageEntity wp = list.get(0);
		Set<EmployeeEntity> emp = wp.getEmployees();
		
		EmployeeEntity employee = emp.iterator().next();
		
		assertEquals("Olga", employee.getFirstName());
	}
	
	@Test
	@InSequence(20)
	public void testFindProjectWithWorkPackagesAndEmployeesAndEstimates2() {
		ProjectEntity project = projectDAO.findProject(5);
		ProjectEntity pro = projectDAO.findProjectWithWorkPackagesAndEmployeesAndEstimates(project);
		
		List<WorkPackageEntity> list = pro.getWorkPackages();
		WorkPackageEntity wp = list.get(0);
		Set<ResponsibleEngineerEstimate> set = wp.getResponsibleEngineerEstimates();
		
		ResponsibleEngineerEstimate ree = set.iterator().next();
		
		assertEquals((Double) 26.0, ree.getP2PersonDays());
	}
	
	@Test (expected = EJBException.class)
	@InSequence(21)
	public void testFindProjectsWithWorkPackages() {
		List<ProjectEntity> list = projectDAO.findProjectsWithWorkPackages();
		
		assertEquals(3, list.size());
	}
	
	@Test
	@InSequence(22)
	public void testFindWorkPackages2() {
		List<WorkPackageEntity> list = projectDAO.findWorkpackages(5);
		WorkPackageId wpId = list.get(0).getId();
		
		assertEquals("DDPL", wpId.getWorkpackageID());
	}
	
	@Test
	@InSequence(23)
	public void testFindWorkpackagesWithEmployees() {
		List<WorkPackageEntity> list = projectDAO.findWorkpackagesWithEmployees(5);
		WorkPackageEntity wp = list.get(0);
		Set<EmployeeEntity> emp = wp.getEmployees();
		
		EmployeeEntity employee = emp.iterator().next();

		assertEquals("Olga", employee.getFirstName());
	}
	
	@Test
	@InSequence(24)
	public void testFindEngineerPackages() {
		List<WPEstimateList> list = projectDAO.findEngineerPackages((long) 5);

		assertEquals(1500, list.size());
	}
	
	@Test
	@InSequence(25)
	public void testWorkpackageInUse() {
		List<WorkPackageEntity> list = projectDAO.findWorkpackages(5);
		WorkPackageEntity wp = list.get(0);
		
		boolean isInUse = projectDAO.workpackageInUse(wp);

		assertTrue(isInUse);
	}

	@Test
	@InSequence(26)
	public void testFindOpenEmployeeProjects1() {
		List<ProjectEntity> list = projectDAO.findOpenEmployeeProjects((long) 147);
		ProjectEntity project = list.get(0);

		assertEquals("Renner-Dare", project.getProjectName());
	}
	
	@Test
	@InSequence(27)
	public void testFindOpenEmployeeProjects2() {
		List<ProjectEntity> list = projectDAO.findOpenEmployeeProjects((long) 3);

		assertEquals(0, list.size());
	}
	
	@Test
	@InSequence(28)
	public void testFindProjects2() {
		EmployeeEntity pm = employeeDAO.find((long) 4);
		
		List<ProjectEntity> list = projectDAO.findProjects(pm);
		 
		 assertEquals(500, list.size());
	}
	
	@Test
	@InSequence(29)
	public void testIsProjectManager1() {
		EmployeeEntity pm = employeeDAO.find((long) 4);
		
		boolean isPM = projectDAO.isProjectManager(pm);
		 
		 assertTrue(isPM);
	}
	
	@Test
	@InSequence(30)
	public void testIsProjectManager2() {
		EmployeeEntity pm = employeeDAO.find((long) 5);
		
		boolean isPM = projectDAO.isProjectManager(pm);
		 
		 assertFalse(isPM);
	}
	
	@Test
	@InSequence(31)
	public void testIsResponsibleEngineer1() {
		EmployeeEntity re = employeeDAO.find((long) 4);
		
		boolean isRE = projectDAO.isResponsibleEngineer(re);
		 
		 assertFalse(isRE);
	}
	
	@Test
	@InSequence(32)
	public void testIsResponsibleEngineer2() {
		EmployeeEntity re = employeeDAO.find((long) 4);
		
		boolean isRE = projectDAO.isResponsibleEngineer(re);
		 
		 assertFalse(isRE);
	}
}
