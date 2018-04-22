package ca.bcit.PunchIn.Beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.PunchIn.DAOs.TimesheetDAO;
import ca.bcit.PunchIn.DAOs.ProjectDAO;
import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;
import ca.bcit.PunchIn.Entities.Timesheet.Timesheet;
import ca.bcit.PunchIn.Entities.Timesheet.TimesheetRow;
import ca.bcit.PunchIn.Entities.Project.ProjectEntity;
import ca.bcit.PunchIn.Entities.Project.WorkPackageEntity;

@SessionScoped
@Named
public class TimesheetDriver implements Serializable {
    /**
     *The current timesheet being viewed or edited.
     */
	private Timesheet ts;

	@Inject
    private TimesheetDAO timesheetDAO;
		
	@Inject
	private ProjectDAO projectDAO;
	
	@Inject
	private LoginBean loginBean;
	
	/**
	 * Projects loaded for display on timesheet row.
	 */
	private List<ProjectEntity> projects;
	
	private List<Timesheet> approvalSheets;
	
	
	
	/**
	 * Views the given timesheet.
	 * @param ts
	 *         The timesheet to view.
	 * @return
	 *         Navigation string to timesheet details page.
	 */
    public String viewTimesheet(Timesheet ts) {
    	this.ts = ts;
        return "timesheetRows";
    }
    
    /**
     * Adds a new timesheet.
     * @param employee
     *      The employee the timesheet is for.
     * @return
     *      Navigation string to timesheet details page.
     */
    public String addTimesheet(EmployeeEntity employee) {
        this.ts = new Timesheet(getCurrentEndWeek(), employee);
        return "timesheetRows";
    }
    
    /**
     * Deletes a row from the given timesheet.
     * @param row
     *      The row to delete.
     * @return
     *      Navigation string to the same page.
     */
    public String deleteTimesheetRow(TimesheetRow row) {
        ts.deleteEntry(row.getTimesheetRowId());
        
        return "timesheetRows";
    }
    
    /**
     * Adds a row to the current timesheet.
     * @return
     *      Navigation string to the same page. 
     */
    public String addTimesheetRow() {
        ts.addEntry();
        return "timesheetRows";
    }
    
    /**
     * Saves the current timesheet.
     * @return
     *      Navigation string to main timesheet page.
     */
    public String saveTimesheet() {
        boolean isNew = (ts.getTimesheetId() == 0);
        if (isNew) {
            timesheetDAO.persist(ts);
        } else {
            ts = timesheetDAO.merge(ts);
        }
        //reload timesheets for employee
        loginBean.getCurrEmp().getBasicInfo().setTimesheets(
                new HashSet<Timesheet>(
                        timesheetDAO.findEmployeeSheets(
                                ts.getEmployee().getEmployeeID())));
        return "timesheets";
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
	 * @return the ts
	 */
	public Timesheet getTs() {
		//System.out.println(ts);
		return ts;
	}
	
	/**
	 * Gets all projects for display in a timesheet row.
	 * @return
	 *      List of all projects.
	 */
	public List<ProjectEntity> getAllProjects(long id) {
	    if (projects == null) {
            projects = projectDAO.findEmployeeProjects(id);   
	    }
	    return projects;
	}
	
	/**
	 * Gets all work packages for a given project
	 * for display in a timesheet row.
	 * @param projectNum
	 *         ProjectNum for selected project.
	 * @return
	 *         List of all workpackages for project.
	 */
	public List<WorkPackageEntity> getWorkpackagesForProject(int projectId) {
	    if (projectId == 0) {
	        return new ArrayList<WorkPackageEntity>();
	    }
	    
	    ProjectEntity project = projectDAO.findProject(projectId);
        project.setWorkPackages(projectDAO.findWorkpackages(project.getProjectNum()));
	    return project.getWorkPackages();
	}

	/**
	 * Sends the currrent timesheet for approval.
	 * @return
	 *     Navigation string back to timesheets page.
	 */
	public String sendTimesheetForApproval() {
	    ts.setApprovalStatus(Timesheet.ApprovalStatus.SentForApproval);
        return saveTimesheet();	    
	}
	
	/**
	 * Approves the current timesheet.
	 * @return
	 *     Navigation string back to timesheets page.
	 */
	public String approveTimesheet() {
	    ts.setApprovalStatus(Timesheet.ApprovalStatus.Approved);
	    return saveTimesheet();
	}
	
	/**
	 * Returns the current timesheet.
	 * @return
	 *     Navigation string back to timesheets page.
	 */
	public String returnTimesheet() {
	    ts.setApprovalStatus(Timesheet.ApprovalStatus.Returned);
	    return saveTimesheet();
	}
	
	/**
	 * Checks if the current timesheet should be readonly.
	 * "Approved" and "Sent For Approval" timesheets
	 * cannot be edited.
	 * @return
	 *        True if timesheet read only.
	 */
	public boolean isReadOnly() {
	    return (ts.getApprovalStatus() == Timesheet.ApprovalStatus.SentForApproval 
	            || ts.getApprovalStatus() == Timesheet.ApprovalStatus.Approved);
 	}
	
	/**
	 * Checks whether to display approval notes on Employee
	 * timesheets page. These are displayed if the timesheet has been
	 * approved or returned by the timesheet approver.
	 * @return
	 *      True if approval notes are displayed.
	 */
	public boolean showApprovalNotes() {
        return (ts.getApprovalStatus() == Timesheet.ApprovalStatus.Returned 
                || ts.getApprovalStatus() == Timesheet.ApprovalStatus.Approved); 
	}
	
	/**
	 * Loads the list of unapproved timesheets
	 * designated for this approver.
	 * @param empId
	 *         EmployeeId of currently signed on employee
	 * @return
	 *         List of unapproved timesheets for this approver
	 */
	public List<Timesheet> getTimesheetsForApprover(long approverID) {
	    if (approvalSheets == null) {
	        approvalSheets = timesheetDAO.findApproverSheets(approverID);
	    }
	    return approvalSheets;
	}

}
