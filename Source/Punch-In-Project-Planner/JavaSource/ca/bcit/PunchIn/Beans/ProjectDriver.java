/**
 * 
 */
package ca.bcit.PunchIn.Beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

import ca.bcit.PunchIn.DAOs.EmployeeDAO;
import ca.bcit.PunchIn.DAOs.ProjectDAO;
import ca.bcit.PunchIn.DAOs.TimesheetDAO;
import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;
import ca.bcit.PunchIn.Entities.Project.BudgetEntity;
import ca.bcit.PunchIn.Entities.Project.ProjectEntity;
import ca.bcit.PunchIn.Entities.Project.ResponsibleEngineerBudgetEntity;
import ca.bcit.PunchIn.Entities.Project.WorkPackageEntity;
import ca.bcit.PunchIn.Entities.Project.WorkPackageId;
import ca.bcit.PunchIn.Entities.Timesheet.Timesheet;
import ca.bcit.PunchIn.Enum.ProjectStatus;
import ca.bcit.PunchIn.Enum.WorkPackageStatus;
import ca.bcit.PunchIn.Models.Employee.Employee;

/**
 * Driver bean for the projects.
 * @author eboow
 * @version 1.0 
 */
@SessionScoped
@Named
public class ProjectDriver implements Serializable {

    /**
     * Default UID.
     **/
    private static final long serialVersionUID = 1L;

    /**
     * List of projects being viewed.
     */
    private List<ProjectEntity> projects;

    /**
     * List of filtered projects.
     */
    private List<ProjectEntity> filteredProjects;

    /**
     * List of work packages in the project.
     */
    private List<WorkPackageEntity> workpackages;

    /**
     * The current project being viewed.
     */
    private ProjectEntity currProj;

    /**
     * The current work package being viewed.
     */
    private WorkPackageEntity currWorkpackage;

    /**
     * List of project managers.
     */
    private List<EmployeeEntity> projectManagers;
    
    /**
     * Inject Loginbean.
     */
    @Inject 
    private LoginBean loginBean;

    /**
     * DAO for project.
     */
    @Inject 
    private ProjectDAO projectDAO;

    /**
     * DAO for employee.
     */
    @Inject
    private EmployeeDAO empDAO;
    
    /**
     * DAO for timesheet.
     */
    @Inject
    private TimesheetDAO tsDAO;

    /**
     * Dual List for adding and removing employees from work packages.
     */
    private DualListModel<EmployeeEntity> pickEmps;

    /**
     * Returns the current project selected.
     * @return the currProj
     */
    public ProjectEntity getCurrProj() {
        return currProj;
    }

    /**
     * Sets the current project selected.
     * @param currProj the currProj to set
     */
    public void setCurrProj(ProjectEntity currProj) {
        this.currProj = currProj;
    }


    /**
     * Returns the dual list model of employees.
     * @return the pickEmps
     */
    public DualListModel<EmployeeEntity> getPickEmps() {
        return pickEmps;
    }

    /**
     * Sets the dual list model of employees.
     * @param pickEmps the pickEmps to set
     */
    public void setPickEmps(DualListModel<EmployeeEntity> pickEmps) {
        this.pickEmps = pickEmps;
    }



    /**
     * Returns the current work package.
     * @return the currWorkpackage
     */
    public WorkPackageEntity getCurrWorkpackage() {
        return currWorkpackage;
    }

    /**
     * Sets the current work package.
     * @param currWorkpackage the currWorkpackage to set
     */
    public void setCurrWorkpackage(WorkPackageEntity currWorkpackage) {
        this.currWorkpackage = currWorkpackage;

        List<EmployeeEntity> empsSource = new ArrayList<EmployeeEntity>();
        List<EmployeeEntity> empsTarget = new ArrayList<>();
        empsTarget.addAll(currWorkpackage.getEmployees());
        
        Set<Employee> empList = empDAO.getEmployees(currProj.getProjectManager());
        
        for (Employee emp : empList) {
            if (!empsTarget.contains(emp.getBasicInfo())) {
                empsSource.add(emp.getBasicInfo());
            }
        }

        pickEmps = new DualListModel<EmployeeEntity>(empsSource, empsTarget);
    }

    /**
     * Gets the list of all projects.
     * @return the projects
     */
    public List<ProjectEntity> getProjects() {
        if (projects == null) {
        	if (loginBean.getCurrEmp().isAdmin()) {
        		projects = projectDAO.findProjects();
        	} else {
        		projects = projectDAO.findProjects(loginBean.getCurrEmp().getBasicInfo());
        	}
            
        }
        return projects;
    }


    /**
     * Gets the list of filtered projects.
     * @return the filteredProjects
     */
    public List<ProjectEntity> getFilteredProjects() {
        return filteredProjects;
    }

    /**
     * Sets the list of filtered projects.
     * @param filteredProjects the filteredProjects to set
     */
    public void setFilteredProjects(List<ProjectEntity> filteredProjects) {
        this.filteredProjects = filteredProjects;
    }

    /**
     * Gets the list of work packages.
     * @return the workpackages
     */
    public List<WorkPackageEntity> getWorkpackages() {
        if (workpackages == null) {
            workpackages = projectDAO.findWorkpackages();
        }
        return workpackages;
    }

    /**
     * Return the work package names.
     * @param projectEntity the project to return work package names for.
     * @return the Work Packages' name.
     */                 
    public List<String> getWorkPackagesNameOfProject(
            ProjectEntity projectEntity) {
        List<String> list = new ArrayList<String>();
        for (WorkPackageEntity wp: projectEntity.getWorkPackages()) {
            list.add(wp.getId().getWorkpackageID());
        }
        return list;
    }

    /**
     * Returns the list of work package names for the current project.
     * @return the Work Packages' name.
     */
    public List<String> getWorkPackagesNameOfProject() {
        List<String> list = new ArrayList<String>();
        if (currProj != null && currProj.getWorkPackages() != null) {
            for (WorkPackageEntity wp: currProj.getWorkPackages()) {
                list.add(wp.getId().getWorkpackageID());
            }
        }
        return list;
    }

    /**
     * Returns the employee names for the specified project.
     * @param projectEntity the project to find the employee names for.
     * @return the Employees name of project.
     */
    public List<String> getEmployeeNamesOfProject(ProjectEntity projectEntity) {
        List<String> list = new ArrayList<String>();
        for (EmployeeEntity emp : projectEntity.getEmployees()) {
            list.add(emp.getFirstName() + " " + emp.getLastName());
        }
        return list;
    }

    /**
     * Returns the employee names for the current project.
     * @return the Employees name of project.
     */
    public List<String> getEmployeeNamesOfProject() {
        List<String> list = new ArrayList<String>();
        if (currProj != null) {
            for (EmployeeEntity emp : currProj.getEmployees()) {
                list.add(emp.getFirstName() + " " + emp.getLastName());
            }
        }
        return list;
    }


    /**
     * Returns a list of project managers.
     * @return the projectManagers
     */
    public List<EmployeeEntity> getProjectManagers() {
        
        projectManagers = empDAO.getProjectManagers();
        

        return projectManagers;
    }




    /**
     * Show Project Details.
     * @param projectEntity project.
     * @return action page.
     */
    public String viewProject(ProjectEntity projectEntity) {
        currProj = projectEntity;
        
        currProj.setWorkPackages(projectDAO.findWorkpackagesWithEmployees(
                currProj.getProjectNum()));

        for (WorkPackageEntity wp : currProj.getWorkPackages()) {
        	for (EmployeeEntity emp: wp.getEmployees()) {
        		Set<Timesheet> set = new HashSet<Timesheet>();
        		set.addAll(tsDAO.findEmployeeSheets(emp.getEmployeeID()));
        		emp.setTimesheets(set);
        	}
            wp.calculateCurrentLabour();
        }

        return "viewProject";
    }


    /**
     * Show Workpackage Details.
     * @param wp WorkpackageEntity.
     * @return action page.
     */
    public String viewWorkpackage(WorkPackageEntity wp) {
        currWorkpackage = wp;
        return "viewWorkpackage";
    }

    /**
     * Create a Project.
     * @return action page.
     */
    public String createProject() {
        currProj = new ProjectEntity();
        currProj.setBudget(new BudgetEntity());
        currProj.setWorkPackages(new ArrayList<WorkPackageEntity>());
        currProj.setBudget(new BudgetEntity());
        currProj.setIsNewlyCreated(true);
        return editProject();
    }

    /**
     * Edit a Project.
     * @return action page.
     */
    public String editProject() {
        return "editProject";
    }

    /**
     * Submit a Project to DB.
     * @return action page.
     */
    public String submitEditProject() {
        //currProj.setEmployees(pickEmps.getSource());
        //System.out.println("enter submit edit project");
        FacesContext facesContext = FacesContext.getCurrentInstance();
        boolean isNewProj = currProj.getIsNewlyCreated();

        if (isNewProj
                && projectDAO.findProject(currProj.getProjectNum()) != null) {
            facesContext.addMessage(
                    "project_create_edit", new FacesMessage(
                            "Select Project Number already exists."));
            facesContext.validationFailed();
            return null;
        }

        if (currProj.getStartDate() != null && currProj.getEndDate() != null 
                && currProj.getStartDate().compareTo(
                        currProj.getEndDate()) > 0) {
            facesContext.addMessage(
                    "project_create_edit", new FacesMessage(
                            "Project end date should not prior start date."));
            facesContext.validationFailed();
            return null;
        }

        for (WorkPackageEntity wp: currProj.getWorkPackages()) {
            wp.getId().setProjectNum(currProj.getProjectNum());
            //System.out.println(wp);
            if (!isNewProj) {
                if (projectDAO.findWP(wp.getId()) == null) {
                    if (wp.getBudget() != null) {
                        wp.getBudget().setId(wp.getId());
                    }
                    projectDAO.persistWP(wp);
                }
            }
        }
        currProj.getBudget().setProject(currProj);

        //System.out.println(currProj);
        currProj.setIsNewlyCreated(false);

        if (isNewProj) {
            projectDAO.persistProject(currProj);
            projects.add(currProj);
        } else {
            projectDAO.mergeProject(currProj);
        }


        return "viewProject";
    }

    /**
     * Create a new WorkPackage to current project.
     * @return edit project page.
     */
    public String newWP() {
        // System.out.println("Enter new Wp");
        WorkPackageEntity wp = new WorkPackageEntity();
        wp.setId(new WorkPackageId());
        wp.setProject(currProj);
        wp.setEmployees(new HashSet<EmployeeEntity>());
        List<WorkPackageEntity> wplist = currProj.getWorkPackages();
        if (wplist == null) {
            wplist = new ArrayList<WorkPackageEntity>();
            currProj.setWorkPackages(wplist);
        }
        wp.setBudget(new ResponsibleEngineerBudgetEntity());
        setCurrWorkpackage(wp);

        return "project_wp_edit";
    }

    /**
     * Returns the list of all status.
     * @return the list of all status;
     */
    public List<String> getProjectStatusList() {
        return Stream.of(ProjectStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    /**
     * Returns the list of all work package statuses.
     * @return the list of all workpackage status;
     */
    public List<String> getWorkPackageStatusList() {
        return Stream.of(WorkPackageStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    /**
     * Save current workpackage.
     * @return Navigation string to editProject if successfull,
     * redisplay page, otherwise.
     */
    public String currWorkpackageSave() {
        boolean success = true;
        //System.out.println("Enter Save current wp");
        Set<EmployeeEntity> set = new HashSet<EmployeeEntity>();
        set.addAll(pickEmps.getTarget());
        currWorkpackage.setEmployees(set);

        List<WorkPackageEntity> wps = currProj.getWorkPackages();
        if (wps.indexOf(currWorkpackage) < 0) {
            wps.add(currWorkpackage);
        }
        if (success) {
            return "editProject";
        } 
        return null;
    }

    /**
     * Edit a workpackage.
     * @param wp the work package to edit.
     * @return Navigation string to edit work package.
     */
    public String editWorkpackage(WorkPackageEntity wp) {
        setCurrWorkpackage(wp);
        return "project_wp_edit";
    }

    /**
     * Cancel current workpackage.
     * @return Navigation string to editProject page.
     */
    public String currWorkpackageCancel() {
        return "editProject";
    }

    /**
     * Check the wp if it is referenced by a timesheetrow.
     * @param wp work package object
     * @return if wp is referenced by one timesheetrow, 
     * return false; otherwise, return true.
     */
    public boolean isWPDeletable(WorkPackageEntity wp) {
        return !projectDAO.workpackageInUse(wp);
    }

    /**
     * Deletes specified work package.
     * @param wp The work package to delete.
     * @return Navigation string to redisplay page.
     */
    public String delWorkpackage(WorkPackageEntity wp) {
        List<WorkPackageEntity> wps = currProj.getWorkPackages();
        int index = wps.indexOf(wp);
        if (index >= 0) {
            projectDAO.removeWP(wp.getId());
            wps.remove(index);
            if (workpackages!=null) {
            	workpackages.remove(wp);
            }
            RequestContext.getCurrentInstance().update("project_create_edit");
        }
        return null;
    }
    
    /**
     * Check current project if it is editable.
     * @return true if editable. otherwise, return false.
     */
    public boolean isCurrProjEditable() {
    	if (currProj.getStatus().equals(ProjectStatus.Closed.toString())) {
    		return false;
    	}
    	return true;
    }

}
