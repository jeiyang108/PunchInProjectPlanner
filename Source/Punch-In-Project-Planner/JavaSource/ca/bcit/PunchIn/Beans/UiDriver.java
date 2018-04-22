package ca.bcit.PunchIn.Beans;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;
import ca.bcit.PunchIn.Entities.Project.ProjectEntity;
import ca.bcit.PunchIn.Entities.Project.WorkPackageEntity;
import ca.bcit.PunchIn.Entities.Timesheet.Timesheet;

/**
 * UiDriver.
 * Class controls methods that control UI display properties.
 * 
 * @author mtius
 * @version 1.0
 */
@ApplicationScoped
@Named("uiDriver")
public class UiDriver {
    /** Visibility property for right project pane. */
    private boolean contentRightProject;
    /** Visibility property for right work package pane. */
    private boolean contentRightWp;
    /** Stores the currently selected project. */
    private ProjectEntity viewProject;
    /** Stores the currently selected work package. */
    private WorkPackageEntity viewWp;
    
    /**
     * Empty constructor.
     */
    public UiDriver() { }
    
    /**
     * Getter for right project pane visibility.
     * 
     * @return true if right project pane should be visible
     */
    public boolean getContentRightProject() {
        return contentRightProject;
    }
    
    /**
     * Setter for right project pane visibility.
     * 
     * @param value
     *              new value for right project pane visibility
     */
    public void setContentRightProject(boolean value) {
        contentRightProject = value;
    }
    
    /**
     * Getter for right work package pane visibility.
     * 
     * @return true if right work package pane should be visible
     */
    public boolean getContentRightWp() {
        return contentRightWp; 
    }
    
    /**
     * Setter for right work package pane visibility.
     * 
     * @param value
     *              new value for right work package pane visibility
     */
    public void setContentRightWp(boolean value) {
        contentRightWp = value;
    }
    
    /**
     * Getter for currently selected project.
     * 
     * @return currently selected project
     */
    public ProjectEntity getViewProject() { 
        return viewProject; 
    }
    
    /**
     * Setter for currently selected project.
     * 
     * @param value
     *              project currently selected
     */
    public void setViewProject(ProjectEntity value) { 
        viewProject = value; 
    }
    
    /**
     * Getter for currently selected work package.
     * 
     * @return currently selected workpackage
     */
    public WorkPackageEntity getViewWp() { 
        return viewWp; 
    }
    
    /**
     * Setter for currently selected work package.
     * 
     * @param value
     *              work package currently selected
     */
    public void setViewWp(WorkPackageEntity value) { 
        viewWp = value; 
    }
    
    /**
     * Calculates the total completed work packages assigned to the current
     * user.
     * 
     * @param wps
     *              work packages assigned to current user
     * @return number of completed work packages assigned to current user
     */
    public int getTotalComplete(List<WorkPackageEntity> wps) {
        int total = 0;
        
        for (int i = 0; i < wps.size(); i++) {
            if (wps.get(i).getStatus().equalsIgnoreCase("Closed")
                    || wps.get(i).getStatus().equalsIgnoreCase("Complete")) {
                total++;
            }
        }
        
        return total;
    }
    
    /**
     * Calculates the total in progress work packages assigned to the current
     * user.
     * 
     * @param wps
     *              work packages assigned to current user
     * @return number of in progress work packages assigned to current user
     */
    public int getTotalInProgress(List<WorkPackageEntity> wps) {
        int total = 0;
        
        for (int i = 0; i < wps.size(); i++) {
            if (wps.get(i).getStatus().equalsIgnoreCase("Open")) {
                total++;
            }
        }
        
        return total;
    }
    
    /**
     * Calculates the total not started work packages assigned to the current
     * user.
     * 
     * @param wps
     *              work packages assigned to current user
     * @return number of not started work packages assigned to current user
     */
    public int getTotalNotStarted(List<WorkPackageEntity> wps) {
        int total = 0;
        
        for (int i = 0; i < wps.size(); i++) {
            if (wps.get(i).getStatus().equalsIgnoreCase("Created") 
                    || wps.get(i).getStatus().equalsIgnoreCase("Planned")) {
                total++;
            }
        }
        
        return total;
    }
    
    /**
     * Determines if an input box requires the disabled CSS class.
     * 
     * @param dis
     *              true if the input box is set to disabled
     * @return the disabled class if required
     */
    public String getDisabledClass(boolean dis) {
        String cssClass = "";
        
        if (dis) {
            cssClass = "disabled"; 
        }
        
        return cssClass;
    }
    
    /**
     * Determines the CSS class used to display project statuses.
     * 
     * @param project
     *              the project listed
     * @return the CSS class to be used
     */
    public String getProjectStatusClass(ProjectEntity project) {
        String status = project.getStatus();
        String statusClass;
        
        if (status.equalsIgnoreCase("Closed") 
                || status.equalsIgnoreCase("Complete")) {
            statusClass = "statuscomplete";
        } else if (status.equalsIgnoreCase("Open")) {
            statusClass = "statusinprogress";
        } else {
            statusClass = "statusnotstarted";
        }
        
        return statusClass;
    }
    
    /**
     * Determines the CSS class used to display work package statuses.
     * 
     * @param wp
     *              the work package listed
     * @return the CSS class to be used
     */
    public String getWpStatusClass(WorkPackageEntity wp) {
        String status = wp.getStatus();
        String statusClass;
        
        if (status.equalsIgnoreCase("Closed") 
                || status.equalsIgnoreCase("Complete")) {
            statusClass = "statuscomplete";
        } else if (status.equalsIgnoreCase("Open")) {
            statusClass = "statusinprogress";
        } else {
            statusClass = "statusnotstarted";
        }
        
        return statusClass;
    }
    
    /**
     * Formats date as either long or short.
     * 
     * @param date
     *              the date to be formatted
     * @param type
     *              how the date should be formatted
     * @return the formatted date as a string
     */
    public String formatDate(Date date, String type) {
        
        if (date == null) {
            return "No Date";
        }
        
        String fmtDate;
        
        if (type.equals("full")) {
            fmtDate = new SimpleDateFormat("dd-MM-YYYY").format(date);
        } else {
            fmtDate = new SimpleDateFormat("dd-MM").format(date);
        }
        
        return fmtDate;
    }
    
    /**
     * Formats decimal numbers rounded to the hundredth place.
     * 
     * @param value
     *              the decimal number to be formatted
     * @return the formatted decimal number as a string
     */
    public String roundValue(double value) {
        NumberFormat fmt = new DecimalFormat("#0.00");
        String rounded = fmt.format(value);
        
        return rounded;
    }
    
    /**
     * Formats decimal number as a currency.
     * 
     * @param value
     *              the decimal number to be formatted
     * @return the formatted decimal number as a string
     */
    public String currencyValue(double value) {
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        String currency = fmt.format(value);
        
        return currency;
    }
    
    /**
     * Combines first and last name parameters from the employee class and 
     * formats them as one complete name.
     * 
     * @param emp
     *              the employee entity to retrieve the name parameters from
     * @return the full name of the employee as a string
     */
    public String displayName(EmployeeEntity emp) {
        if (emp == null) {
            return "None";
        }
        
        return emp.getFirstName() + " " + emp.getLastName();
    }
    
    /**
     * Determines the icon displayed in the timesheet approval column.
     * 
     * @param timesheet
     *              the timesheet entity to retrieve the status from
     * @return the icon to be used in the timesheet approval column
     */
    public String displayApprovalIcon(Timesheet timesheet) {
        String imgFile;
        
        switch (timesheet.getApprovalStatus()) {
        case SentForApproval :
            imgFile = "sent_icon.png";
            break;
        case Returned :
            imgFile = "x_icon.png";
            break;
        case Approved :
            imgFile = "check_icon.png";
            break;
        default : //Unapproved
            imgFile = "draft_icon.png";
            break;
        }
        
        return imgFile;
    }
    
    /**
     * Resets the display when the user navigates from the projects or 
     * work packages dashboard pages.
     */
    public void reset() {
        contentRightProject = false;
        contentRightWp = false;
        viewProject = null;
        viewWp = null;
    }
}
