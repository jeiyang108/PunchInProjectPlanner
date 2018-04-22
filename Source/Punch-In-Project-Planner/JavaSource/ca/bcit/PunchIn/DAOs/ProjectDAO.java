package ca.bcit.PunchIn.DAOs;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;
import ca.bcit.PunchIn.Entities.Project.ProjectEntity;
import ca.bcit.PunchIn.Entities.Project.ResponsibleEngineerEstimate;
import ca.bcit.PunchIn.Entities.Project.WPEstimateList;
import ca.bcit.PunchIn.Entities.Project.WorkPackageEntity;
import ca.bcit.PunchIn.Entities.Project.WorkPackageId;
import ca.bcit.PunchIn.Entities.Timesheet.TimesheetRow;

@Stateless
@Dependent
@Named
public class ProjectDAO extends BaseDAO {
    
    /**
     * Default UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Save Project data to database.
     * 
     * @param prj
     *            Project Entity
     */
    public void persistProject(ProjectEntity prj) {
        em.persist(prj);
    }
    
    /**
     * Save Work Package data to database.
     * 
     * @param wp
     *            WorkPackage Entity
     */
    public void persistWP(WorkPackageEntity wp) {
        em.persist(wp);
    }

    /**
     * Update Project data to database.
     * 
     * @param prj
     *            Project Entity
     */
    public void mergeProject(ProjectEntity prj) {
        em.merge(prj);
    }
    
    /**
     * Update Work Package data to database.
     * 
     * @param wp
     *            WorkPackage Entity
     */
    public void mergeWP(WorkPackageEntity wp) {
        em.merge(wp);
    }
    
    
    /**
     * Find Project entity in database.
     * 
     * @param id
     *            Project Num.
     * @return the Project Entity.
     */
    public ProjectEntity findProject(int id) {
        return em.find(ProjectEntity.class, id);
    }
    
    /**
     * Finds the project entity with its work packages loaded.
     * @param project The project to find.
     * @return The project with its work packages.
     */
    public ProjectEntity findProjectWithWorkPackages(ProjectEntity project) {
        TypedQuery<ProjectEntity> query = em.createQuery("SELECT p FROM ProjectEntity p JOIN FETCH p.workPackages wp"
                + " WHERE p.projectNum=:id", ProjectEntity.class);
        query.setParameter("id", project.getProjectNum());
        
        return query.getSingleResult();
    }
    
    public ProjectEntity findProjectWithWorkPackagesAndEmployees(ProjectEntity project) {
        TypedQuery<ProjectEntity> query = em.createQuery("SELECT p FROM ProjectEntity p "
        			+ "LEFT JOIN FETCH p.workPackages wp " 
        			+ "LEFT JOIN FETCH wp.employees emp " 
        			+ "LEFT JOIN FETCH emp.labourGrade lg "
        			+ "WHERE p.projectNum=:id", ProjectEntity.class);
        query.setParameter("id", project.getProjectNum());
        
        
        return query.getSingleResult();
    }
    
    /**
     * Finds the project with the work packages, 
     * employees, and estimates loaded.
     * @param project The project to search for.
     * @return The project with all the work packages, 
     * employees, and estimates loaded.
     */
    public ProjectEntity findProjectWithWorkPackagesAndEmployeesAndEstimates(
            ProjectEntity project) {
        TypedQuery<ProjectEntity> query = 
                em.createQuery("SELECT p FROM ProjectEntity p "
                + "LEFT JOIN FETCH p.workPackages wp "
                + "LEFT JOIN FETCH wp.responsibleEngineerEstimates "
                + "LEFT JOIN FETCH wp.employees emp " 
                + "LEFT JOIN FETCH emp.labourGrade lg "
                + "WHERE p.projectNum=:id", ProjectEntity.class);
        query.setParameter("id", project.getProjectNum());
        
        
        return query.getSingleResult();
    }
    
    /**
     * Find all projects
     * @return list of projects.
     */
    public List<ProjectEntity> findProjects() {
        List<ProjectEntity> list = null;
        TypedQuery<ProjectEntity> query = em.createQuery("SELECT p FROM ProjectEntity p", ProjectEntity.class);
        list = query.getResultList();
//        for(ProjectEntity project: list) {
//        	project.getEmployees().size();
//        }
        return list;
    }
    
    /**
     * Find all projects are managed by  current employee.
     * @return list of projects.
     */
    public List<ProjectEntity> findProjects(EmployeeEntity emp) {
        List<ProjectEntity> list = null;
        TypedQuery<ProjectEntity> query = em.createQuery("SELECT p FROM ProjectEntity p where p.projectManager = :user", ProjectEntity.class);
        query.setParameter("user", emp);
        try {
        	list = query.getResultList(); 
        } catch (Exception ex) {
        	list = null;
        }
        return list;
    }
    /**
     * Check if the employee is a project manager.
     * @param emp current employee
     * @return true if employee is project manager; otherwise, false.
     */
    public boolean isProjectManager(EmployeeEntity emp) {
    	List<ProjectEntity> list =  findProjects(emp);
    	if (list != null && list.size() > 0) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * Check if the employee is a responsible engineer.
     * @param emp current employee
     * @return true if employee is responsible engineer; otherwise, false.
     */
    public boolean isResponsibleEngineer(EmployeeEntity emp) {
        
        Query query = em.createQuery("SELECT COUNT (wp.startDate) FROM WorkPackageEntity wp "
                + "WHERE wp.responsibleEngineer.employeeID = :id");
        
        query.setParameter("id", emp.getEmployeeID());
        long count = (long) query.getSingleResult();
        
        if (count > 0) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Finds all the projects with their workpackages loaded.
     * @return list of project with work packages
     */
    public List<ProjectEntity> findProjectsWithWorkPackages() {
        TypedQuery<ProjectEntity> query = em.createQuery("SELECT p FROM ProjectEntity p "
                + "JOIN FETCH p.wp WorkPackageEntity", ProjectEntity.class);
        
        return query.getResultList();
    }
    
    
    /**
     * Find all workpackages
     * @return list of workpackages.
     */
    public List<WorkPackageEntity> findWorkpackages() {
        List<WorkPackageEntity> list = null;
        TypedQuery<WorkPackageEntity> query = em.createQuery("SELECT w FROM WorkPackageEntity w", WorkPackageEntity.class);
        list = query.getResultList();
        
        return list;
    }  
    
    /**
     * Find  workpackages of a project
     * @param projectNum current project num (key).
     * @return list of workpackages.
     */
    public List<WorkPackageEntity> findWorkpackages(int projectNum) {
        List<WorkPackageEntity> list = null;
        TypedQuery<WorkPackageEntity> query = 	em.createQuery("SELECT w FROM WorkPackageEntity w where w.id.projectNum = :projNum", WorkPackageEntity.class);
        query.setParameter("projNum", projectNum);
        list = query.getResultList();
        
        return list;
    }  
    
    /**
     * Find  workpackages of a project with their employees loaded.
     * @param projectNum current project num (key).
     * @return list of workpackages with employees
     */
    public List<WorkPackageEntity> findWorkpackagesWithEmployees(int projectNum) {
        List<WorkPackageEntity> list = null;
        
        //TypedQuery<WorkPackageEntity> query =   em.createQuery("SELECT distinct w FROM WorkPackageEntity w "
        //        + "JOIN FETCH w.employees emp JOIN FETCH emp.labourGrade where w.id.projectNum = :projNum", WorkPackageEntity.class);
        
        TypedQuery<WorkPackageEntity> query =   em.createQuery("SELECT distinct w FROM WorkPackageEntity w "
                + "where w.id.projectNum = :projNum", WorkPackageEntity.class);
        
        query.setParameter("projNum", projectNum);
        list = query.getResultList();
        
        for(WorkPackageEntity w : list) {
        	w.getEmployees().size();
        	for (EmployeeEntity e: w.getEmployees()) {
        		e.getLabourGrade().size();
        	}
        }
        
        return list;
    }  
    
    
    /**
     * Find all projects for specified employee
     * 
     * @param id
     *            Employee ID
     * @return list of projects for employee.
     */
    public List<ProjectEntity> findEmployeeProjects(Long id) {
        
        TypedQuery<ProjectEntity> employeeProjects = em.createQuery("SELECT DISTINCT p "
                + "FROM EmployeeEntity e JOIN e.workPackages wp "
                + "JOIN wp.project p JOIN FETCH p.workPackages pwp JOIN FETCH pwp.employees WHERE e.employeeID = :employeeID"
                + " ORDER BY p.startDate", ProjectEntity.class);
        
        employeeProjects.setParameter("employeeID", id);
        
        return employeeProjects.getResultList();
    }
    
    /** 
     * Find all open projects for specified employee 
     *  
     * @param id 
     *            Employee ID 
     * @return list of projects for employee. 
     */ 
    public List<ProjectEntity> findOpenEmployeeProjects(Long id) { 
         
        TypedQuery<ProjectEntity> employeeProjects = em.createQuery("SELECT DISTINCT p " 
                + "FROM EmployeeEntity e JOIN e.workPackages wp " 
                + "JOIN wp.project p JOIN FETCH p.workPackages pwp " 
                + "JOIN FETCH pwp.employees WHERE e.employeeID = :employeeID " 
                + "AND p.status = 'Open' AND pwp.status = 'Open' " 
                + "ORDER BY p.startDate", ProjectEntity.class); 
         
        employeeProjects.setParameter("employeeID", id); 
         
        return employeeProjects.getResultList(); 
    }
    
    /**
     * Find WorkPackage entity in database.
     * 
     * @param workPackageId
     *            WorkPackage ID.
     * @return the WorkPackage Entity.
     */
    public WorkPackageEntity findWP(WorkPackageId workPackageId) {
        return em.find(WorkPackageEntity.class, workPackageId);
    }
    
    /**
     * Find all workpackages for specified employee
     * 
     * @param id
     *            Employee ID
     * @return list of projects for employee.
     */
    public List<WorkPackageEntity> findEmployeePackages(Long id) {
        EmployeeEntity emp = em.find(EmployeeEntity.class, id);
        emp.getWorkPackages().size();
        return emp.getWorkPackages();
    }
    
    /**
     * Find all workpackages for specified responsible engineer
     * 
     * @param id
     *            Employee ID
     * @return list of work packages with the specified engineer
     */
    public List<WPEstimateList> findEngineerPackages(Long id) {
        TypedQuery<WPEstimateList> query = em.createQuery("SELECT"
                + " NEW ca.bcit.PunchIn.Entities.Project.WPEstimateList("
                + "wp.startDate,"
                + " wp.title,"
                + " wp.id.workpackageID, wp.project.projectName, wp.id.projectNum) "
                + "FROM WorkPackageEntity wp "
                + "WHERE wp.responsibleEngineer.employeeID = :id", WPEstimateList.class);
        
        query.setParameter("id", id);
        
        return query.getResultList();
    }
    
    public WorkPackageEntity findWorkPackageWithEstimates(WPEstimateList estimate) {
        TypedQuery<WorkPackageEntity> query = em.createQuery(
                "SELECT wp FROM WorkPackageEntity wp "
                + "LEFT JOIN FETCH wp.responsibleEngineerEstimates "
                + "WHERE wp.id.projectNum = :projectNum "
                + "AND wp.id.workpackageID = :wpID", WorkPackageEntity.class);
        
        query.setParameter("projectNum", estimate.getProjectNum());
        query.setParameter("wpID", estimate.getWpNum());
              
        
        return query.getSingleResult();
    }

    /**
     * Remove Project from database.
     * 
     * @param id
     *            Project Num.
     */
    public void removeProject(int id) {

        em.remove(findProject(id));
    }
    
    /**
     * Remove WorkPackage from database.
     * 
     * @param id
     *            WorkPackage Num.
     */
    public void removeWP(WorkPackageId workPackageId) {
    	WorkPackageEntity wp = findWP(workPackageId);
    	if (wp != null) {		
    		em.remove(wp);
    	}
    	return;
    }
    
    /** 
     * Check if the work package is referenced. 
     * @param wp work package object.
     * @return
     */
    public boolean workpackageInUse(WorkPackageEntity wp) {
    	 
    	TypedQuery<TimesheetRow> tsr = em.createQuery("SELECT r " + 
    			"FROM TimesheetRow r where r.workPackage.id = :id"  , TimesheetRow.class);
        
        tsr.setParameter("id", wp.getId());
        List<TimesheetRow> results = tsr.getResultList();
        if (results.isEmpty()) 
        	return false;
        return true;
    }
    
    /**
     * Persist the responsible engineer estimate into the database.
     * @param estimate The estimate to be persisted.
     */
    public void persistEngineerEstimate(ResponsibleEngineerEstimate estimate) {
        em.persist(estimate);
    }
    
    /**
     * Remove the responsible engineer estimate into the database.
     * Using this for testing purposes
     * @param estimate The estimate to be persisted.
     */
    public void removeEngineerEstimate(Long id) {
    		List<WPEstimateList> list = findEngineerPackages(id);
    		WorkPackageEntity ep = findWorkPackageWithEstimates(list.get(0));
    		
    		Set<ResponsibleEngineerEstimate> set = ep.getResponsibleEngineerEstimates();
    		
		ResponsibleEngineerEstimate estimate = set.iterator().next();
    	
        em.remove(estimate);
    }
    
}