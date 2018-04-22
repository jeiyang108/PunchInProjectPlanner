/**
 * 
 */
package ca.bcit.PunchIn.Entities.Employee;

import ca.bcit.PunchIn.Entities.Credential.CredentialEntity;
import ca.bcit.PunchIn.Entities.Project.WorkPackageEntity;
import ca.bcit.PunchIn.Entities.Timesheet.Timesheet;
import ca.bcit.PunchIn.Entities.Timesheet.TimesheetRow;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author eboow
 *
 */
@Entity
@Table(name = "Employee")
public class EmployeeEntity implements Serializable {

    /**
     * Default UID.
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name="EmployeeID")
    private Long employeeID;

    @Column(name="FirstName", length=25, nullable=false)
    private String firstName;

    @Column(name="LastName", length=25, nullable=false)
    private String lastName;
    
    @Transient
    private String shortName;

    @Column(name="SickLeaves")
    private Integer sickLeaves = 0;

    @Column(name="Vacation", columnDefinition="float")
    private Double vacation = 0.0;

    @Column(name="FlexTime", columnDefinition="decimal", precision=4, scale=2)
    private Double flexTime = 0.0;

    @Column(name="Active")
    private boolean active;
    
	@ManyToOne
    @JoinColumn(name="TimesheetApprover")
    private EmployeeEntity timesheetApprover;
    
    @ManyToOne
    @JoinColumn(name="Supervisor")
    private EmployeeEntity supervisor;
    
    @OneToOne(mappedBy="employee", cascade=CascadeType.ALL, orphanRemoval = true)
    private CredentialEntity credentials;
    
    @OneToMany
    @JoinColumn(name="EmployeeID")
    private Set<EmployeeLabourGradeEntity> labourGrade;

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="employeeID")
    @OrderBy("WeekEnd DESC")
    private Set<Timesheet> timesheets;
    
    @ManyToMany(mappedBy="employees")
    private List<WorkPackageEntity> workPackages;
    

    public Set<Timesheet> getTimesheets() {
        return timesheets;
    }

    public void setTimesheets(Set<Timesheet> timesheets) {
        this.timesheets = timesheets;
    }
    
    /**
     * Takes a take and removes the time comparison so
     * two dates can be compared.
     * @param date
     * @return
     *      The date with time portions set to 0. 
     */
    private static Date removeTimeFromDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    
    /**
     * Checks if an employee already has a timesheet
     * for the current week.
     * @return
     *      True if timesheet exists; otherwise false
     */
    public boolean hasTimesheetForWeek() {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        for (Timesheet ts : timesheets) {
            if (removeTimeFromDate(ts.getDate())
                    .compareTo(removeTimeFromDate(c.getTime())) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the employeeID
     */

    public Long getEmployeeID() {
        return employeeID;
    }
    
    /**
     * @param employeeID the employeeID to set
     */
    public void setEmployeeID(Long employeeID) {
        this.employeeID = employeeID;
    }
    
    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * @return the sickLeaves
     */
    public Integer getSickLeaves() {
        return sickLeaves;
    }
    /**
     * @param sickLeaves the sickLeaves to set
     */
    public void setSickLeaves(int sickLeaves) {
        this.sickLeaves = sickLeaves;
    }

    /**
     * Determines if an employee is active in the company.
     * @return true if active, otherwise false;
     */
    public boolean isActive() {
		return active;
	}
    
    /**
     * Sets the active status of the employee.
     * @param active 
     */
	public void setActive(boolean active) {
		this.active = active;
	}
    /**
     * @return the vacation
     */
    public Double getVacation() {
        return vacation;
    }

    /**
     * @param vacation the vacation to set
     */
    public void setVacation(Double vacation) {
        this.vacation = vacation;
    }
    
    /**
     * @return the flexTime
     */
    public Double getFlexTime() {
        return flexTime;
    }
    /**
     * @param flexTime the flexTime to set
     */
    public void setFlexTime(double flexTime) {
        this.flexTime = flexTime;
    }

    public EmployeeEntity getTimesheetApprover() {
        return timesheetApprover;
    }

    public void setTimesheetApprover(EmployeeEntity timesheetApprover) {
        this.timesheetApprover = timesheetApprover;
    }

    public EmployeeEntity getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(EmployeeEntity supervisor) {
        this.supervisor = supervisor;
    }

    /**
     * @return the credentials
     */
    public CredentialEntity getCredentials() {
        return credentials;
    }

    /**
     * @param credentials the credentials to set
     */
    public void setCredentials(CredentialEntity credentials) {
        this.credentials = credentials;
    }

    /**
     * @return the labourGrade
     */
    public Set<EmployeeLabourGradeEntity> getLabourGrade() {
        return labourGrade;
    }

	/**
     * @param labourGrade the labourGrade to set
     */
    public void setLabourGrade(Set<EmployeeLabourGradeEntity> labourGrade) {
        this.labourGrade = labourGrade;
    }
    
    /**
     * Gets the labour grade for the employee at the specified date.
     * @param date The date that the employee had the labour grade for.
     * @return The Labour Grade for that employee at the time.
     */
    public EmployeeLabourGradeEntity getLabourGradeForDate(Date date) {
        
        EmployeeLabourGradeEntity currentGrade = null;
        
        for (EmployeeLabourGradeEntity grade : labourGrade) {
            
            // If employee only has no promotions
            if (grade.getPromotionDate() == null) {
                break;
            }
            
            if (date.after(grade.getPromotionDate())) {
                
                // If this is the first labour grade that exists before the specified date
                if (currentGrade == null ) {
                    currentGrade = grade;
                    continue;
                }
                
                // Checks if this grade found is newer than the currentGrade stored.
                if (grade.getPromotionDate().after(currentGrade.getPromotionDate())) {
                    currentGrade = grade;
                }
            }
        }
        
        // If employee only has one grade with no promotions
        if (currentGrade == null) {
            currentGrade = labourGrade.iterator().next();
        }
        
        return currentGrade;
    }
    
    /**
     * Gets the employees total work done on a work package in a given week.
     * @param wp The work package to find the work done for.
     * @param week The week in which to find the work done for.
     * @return The amount of labour days spent on the work package in the given week.
     */
    public double getWeeklyLabourForWP(WorkPackageEntity wp, Calendar week) {
        Calendar calWeek = week;
        Calendar timesheetWeek = Calendar.getInstance();
     
        
        for (Timesheet timesheet : timesheets) {
            timesheetWeek.setTime(timesheet.getDate());
            
            if (calWeek.get(Calendar.YEAR) == timesheetWeek.get(Calendar.YEAR)) {
                if (calWeek.get(Calendar.WEEK_OF_YEAR) == timesheetWeek.get(Calendar.WEEK_OF_YEAR)) {
                    for (TimesheetRow row : timesheet.getEntries()) { 
                        if (row.getWorkPackage().equals(wp)) {
                           return row.getTotal().doubleValue() / WorkPackageEntity.HOURS_TO_DAY;
                        }
                    } 
                }
            } 
        }
        
        // No work done
        return 0.0;
    }

    public List<WorkPackageEntity> getWorkPackages() {
        return workPackages;
    }

    public void setWorkPackages(List<WorkPackageEntity> workPackages) {
        this.workPackages = workPackages;
    }

    public void setSickLeaves(Integer sickLeaves) {
        this.sickLeaves = sickLeaves;
    }

    public void setFlexTime(Double flexTime) {
        this.flexTime = flexTime;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EmployeeEntity [employeeID=" + employeeID + ", firstName=" + firstName + ", lastName=" + lastName
                + "]";
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((employeeID == null) ? 0 : employeeID.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((flexTime == null) ? 0 : flexTime.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((shortName == null) ? 0 : shortName.hashCode());
		result = prime * result + ((sickLeaves == null) ? 0 : sickLeaves.hashCode());
		result = prime * result + ((vacation == null) ? 0 : vacation.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeEntity other = (EmployeeEntity) obj;
		if (employeeID == null) {
			if (other.employeeID != null)
				return false;
		} else if (!employeeID.equals(other.employeeID))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (flexTime == null) {
			if (other.flexTime != null)
				return false;
		} else if (!flexTime.equals(other.flexTime))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (shortName == null) {
			if (other.shortName != null)
				return false;
		} else if (!shortName.equals(other.shortName))
			return false;
		if (sickLeaves == null) {
			if (other.sickLeaves != null)
				return false;
		} else if (!sickLeaves.equals(other.sickLeaves))
			return false;
		if (vacation == null) {
			if (other.vacation != null)
				return false;
		} else if (!vacation.equals(other.vacation))
			return false;
		return true;
	}
	
}
