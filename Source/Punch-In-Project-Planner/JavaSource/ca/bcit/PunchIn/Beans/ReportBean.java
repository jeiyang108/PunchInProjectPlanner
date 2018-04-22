package ca.bcit.PunchIn.Beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.PunchIn.DAOs.ProjectDAO;
import ca.bcit.PunchIn.DAOs.TimesheetDAO;
import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;
import ca.bcit.PunchIn.Entities.Project.ProjectEntity;
import ca.bcit.PunchIn.Entities.Project.WorkPackageEntity;
import ca.bcit.PunchIn.Entities.Timesheet.Timesheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Named
@SessionScoped
public class ReportBean implements Serializable {

    /**
     * Data access object for projects.
     */
    @Inject private ProjectDAO projectDAO;

    /**
     * DAO for timesheet.
     */
    @Inject
    private TimesheetDAO tsDAO;
    
    /**
     * Current list of projects.
     */
    private List<ProjectEntity> projectList;

    /**
     * Current project being viewed.
     */
    private ProjectEntity currentProject;

    /**
     * List of the last 12 weeks to view employee work for.
     */
    private List<Calendar> projectWeeks;
    
    private Set<WorkPackageEntity> earnedValuePackages;
 
    /**
     * Sets the project list that can be viewed for reports.
     */
    @PostConstruct
    public void setProjectList() {
        projectList = projectDAO.findProjects();
    }

    /**
     * Load timesheets for Employee.
     * @param proj project.
     */
    private void  lazyLoadTimesheetsForEmployee(ProjectEntity proj) {
    	 for (WorkPackageEntity wp : proj.getWorkPackages()) {
         	for (EmployeeEntity emp: wp.getEmployees()) {
         		Set<Timesheet> set = new HashSet<Timesheet>();
         		set.addAll(tsDAO.findEmployeeSheets(emp.getEmployeeID()));
         		emp.setTimesheets(set);
         	}
             wp.calculateCurrentLabour();
         }
    	return ;
    }
    /**
     * Sets the current project to view summary report for 
     * and returns navigation string to summary page.
     * @param project The project to view.
     * @return Navigation string to project report summary page.
     */
    public String getProjectSummary(ProjectEntity project) {
        currentProject = 
                projectDAO.findProjectWithWorkPackagesAndEmployees(project);
        lazyLoadTimesheetsForEmployee(currentProject);
        getLabourSpent();
        return "reports_summary";
    }

    /**
     * Sets the current project to view breakdown report for 
     * and returns navigation string to summary page.
     * @param project The project to view.
     * @return Navigation string to project report summary page.
     */
    public String getProjectBreakdown(ProjectEntity project) {
        currentProject =
                projectDAO.findProjectWithWorkPackagesAndEmployees(project);
        lazyLoadTimesheetsForEmployee(currentProject);
        getLabourSpent();
        getListOfProjectWeeks();
        return "reports_employee_summary";
    }
    
    /**
     * Sets the current project to view the earned value report for
     * and returns navigation string to page.
     * @param project The project to view the report for.
     * @return Navigation string to earned value report.
     */
    public String getProjectEarnedValue(ProjectEntity project) {
        currentProject =
                projectDAO.findProjectWithWorkPackagesAndEmployeesAndEstimates(
                        project);
        lazyLoadTimesheetsForEmployee(currentProject);
        getLabourSpent();
        
        earnedValuePackages = new HashSet<WorkPackageEntity>();
        earnedValuePackages.addAll(currentProject.getWorkPackages());
        return "reports_earnedvalue";
    }

    /**
     * Calculates the labour spent on each work package.
     */
    private void getLabourSpent() {
        for (WorkPackageEntity workPackage : currentProject.getWorkPackages()) {
            workPackage.calculateCurrentLabour();
        }
    }

    /**
     * Gets the total work done in dollars on the project for the given labour grade.
     * @param grade The labour grade to find the total for.
     * @return The total work done on the project by the specified labour grade.
     */
    public double getTotalDollarsForLabourGrade(String grade) {
        double total = 0.0;

        if (grade.equals("total")) {
            for (WorkPackageEntity workPackage : currentProject.getWorkPackages()) {
                total += workPackage.calculateTotalCurrentLabourDollars();
            }
        } else {
            for (WorkPackageEntity workPackage : currentProject.getWorkPackages()) {
                total += workPackage.getCurrentLabourDollars().get(grade);
            }
        }

        return total;
    }

    /**
     * Gets the sum of the work done by the employee for the given project weeks.
     * @param emp The employee to find the work for.
     * @param wp The work package to find the employee's work for.
     * @return The sume of the work done by the employee on the work package.
     */
    public double getSumOfWeeklyLabour(EmployeeEntity emp, WorkPackageEntity wp) {
        double sum = 0;
        for (Calendar week : projectWeeks) {
            sum += emp.getWeeklyLabourForWP(wp, week);
        }
        
        return sum;
    }

    /**
     * Gets the date of the Friday of the current week.
     * @return
     *      Date of Friday.
     */
    private Date getCurrentEndWeek() { 
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        return c.getTime();
    }

    /**
     * Get the last 12 weeks of the project.
     */
    private void getListOfProjectWeeks() {
        Calendar start = new GregorianCalendar();
        Calendar end = new GregorianCalendar();

        projectWeeks = new ArrayList<Calendar>();

        if (currentProject.getStartDate() != null) { 
            start.setTime(currentProject.getStartDate()); 
            end.setTime(getCurrentEndWeek()); 
 
            for (int i = 0; i < 12; i++) { 
                if (!end.before(start)) { 
                    projectWeeks.add((Calendar) end.clone()); 
                } 
                end.add(Calendar.WEEK_OF_YEAR, -1); 
            }
            Collections.reverse(projectWeeks); 
        }
    }

    /**
     * @return the currentProject
     */
    public ProjectEntity getCurrentProject() {
        return currentProject;
    }

    /**
     * @return the projectList
     */
    public List<ProjectEntity> getProjectList() {
        return projectList;
    }

    /**
     * @return the selectedWeek
     */
    public List<Calendar> getProjectWeeks() {
        return projectWeeks;
    }

    /**
     * @param selectedWeek the selectedWeek to set
     */
    public void setProjectWeeks(List<Calendar> projectWeeks) {
        this.projectWeeks = projectWeeks;
    }
    
    public Set<WorkPackageEntity> getEarnedValuePackages() {
        return earnedValuePackages;
    }

}
