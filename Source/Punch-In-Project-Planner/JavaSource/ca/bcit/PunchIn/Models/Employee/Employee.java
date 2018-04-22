package ca.bcit.PunchIn.Models.Employee;

import java.util.List;

import ca.bcit.PunchIn.Entities.Credential.CredentialEntity;
import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;
import ca.bcit.PunchIn.Entities.Employee.EmployeeLabourGradeEntity;

public class Employee {

    private EmployeeEntity basicInfo;
    private CredentialEntity credential;
    private List<EmployeeLabourGradeEntity> labourGrades;
    /**
     * True if the employee is a timesheet approver.
     */
    private boolean isTimesheetApprover;
    //private List<ProjectEntity> projects;
    //private List<EmployeeWorkPackageEntity> workPackages;

    /**
     * @return the basicInfo
     */
    public EmployeeEntity getBasicInfo() {
        return basicInfo;
    }
    /**
     * @param basicInfo the basicInfo to set
     */
    public void setBasicInfo(EmployeeEntity basicInfo) {
        this.basicInfo = basicInfo;
    }

    /**
     * @return the employee's labour grades
     */
    public List<EmployeeLabourGradeEntity> getLabourGrades() {
        return labourGrades;
    }

    /**
     * @param labourGrades
     *       the labour grades to set
     */
    public void setLabourGrades(List<EmployeeLabourGradeEntity> labourGrades) {
        this.labourGrades = labourGrades;
    }


    /**
     * @return the credential
     */
    public CredentialEntity getCredential() {
        return credential;
    }
    /**
     * @param credential the credential to set
     */
    public void setCredential(CredentialEntity credential) {
        this.credential = credential;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Employee [basicInfo=" + basicInfo + ", credential=" + credential + "]";
    }

    /**
     * Checks if employee is an admin.
     * @return
     *      True if admin
     */
    public boolean isAdmin() {
        return (credential.getRole().equalsIgnoreCase("admin"));
    }
    
    
    /**
     * Checks if employee is a timesheet approver.
     * @return
     *      True if timesheet approver.
     */
    public boolean isTimesheetApprover() {
        return isTimesheetApprover;
    }
    
    /**
     * Mutator for timesheet approver.
     * @param value
     *      True if timesheet approver.
     */
    public void setTimesheetApprover(boolean value) {
        isTimesheetApprover = value;
    }
}
