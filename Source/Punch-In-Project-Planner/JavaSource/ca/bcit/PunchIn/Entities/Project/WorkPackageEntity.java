/**
 * 
 */
package ca.bcit.PunchIn.Entities.Project;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;
import ca.bcit.PunchIn.Entities.Employee.EmployeeLabourGradeEntity;
import ca.bcit.PunchIn.Entities.Employee.RateEntity;
import ca.bcit.PunchIn.Entities.Timesheet.Timesheet;
import ca.bcit.PunchIn.Entities.Timesheet.TimesheetRow;

/**
 * @author eboow
 *
 */

@Entity
@Table(name = "WorkPackage")
public class WorkPackageEntity {

    /**
     * Represents the ratio of hours in a labour day. 
     */
    public static int HOURS_TO_DAY = 8;

    @EmbeddedId
    private WorkPackageId id;

    @ManyToOne
    @JoinColumn(name="ProjectNum", insertable=false, updatable=false)
    private ProjectEntity project;

    @ManyToMany
    @JoinTable(name="EmployeeWorkPackage",
            joinColumns= {@JoinColumn(name="WorkPackageID", referencedColumnName="WorkPackageID", columnDefinition="varchar"), 
                    @JoinColumn(name="ProjectNum", referencedColumnName="ProjectNum", columnDefinition="int")},
            inverseJoinColumns = {@JoinColumn(name="EmployeeID")}
    )
    private Set<EmployeeEntity> employees;

    @Column(name="title", length=30, nullable=false)
    private String title;

    @ManyToOne
    @JoinColumn(name="ResponsibleEngineerID")
    private EmployeeEntity responsibleEngineer;
    
    @OneToMany
    @JoinColumns({ 
        @JoinColumn(name = "WorkPackageID", 
                referencedColumnName = "WorkPackageID"),
        @JoinColumn(name = "ProjectNum",
                referencedColumnName = "ProjectNum")})
    @OrderBy("estimateDate DESC")
    private Set<ResponsibleEngineerEstimate> responsibleEngineerEstimates;

    @Column(name="StartDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name="EndDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @ManyToOne
    @JoinColumns({@JoinColumn(name="ParentProjectNum", referencedColumnName="ProjectNum"), 
        @JoinColumn(name="ParentWPID", referencedColumnName="WorkpackageID")})
    private WorkPackageEntity parentPackage;


    @Column(name="Status", length=30, nullable=false)
    private String status;

    @Column(name="Purpose", length=255, nullable=false)
    private String purpose;

    @Column(name="Activities", length=255, nullable=false)
    private String activities;

    @Column(name="Inputs", length=255, nullable=false)
    private String inputs;

    @Column(name="Outputs", length=255, nullable=false)
    private String outputs;

    @OneToOne(mappedBy="workPackage", cascade = CascadeType.ALL)
    private ResponsibleEngineerBudgetEntity budget;

    /**
     * Map of current labour days spent on the work package by grade.
     */
    @Transient
    private Map<String, Double> currentLabourDays;

    /**
     * Map of current dollar amount spent on project by grade.
     */
    @Transient
    private Map<String, Double> currentLabourDollars;


    /**
     * Calculates the current labour spent on the work package to date.
     */
    public void calculateCurrentLabour() {
        // Sets up the maps with their labour grade keys
        initializeLabourMaps();

        // Set to true if the row for the work package is found in the timesheet
        boolean found;
        // Total hours worked on the work package in the timesheet row.
        double rowTotal;
        // The labour grade for the employee
        EmployeeLabourGradeEntity labourGrade;
        // The current rate for the hours worked on the timesheet
        RateEntity currentRate;
        Date timesheetDate;

        for (EmployeeEntity employee : employees) {
            for (Timesheet timesheet : employee.getTimesheets()) {
                // Reset found for each timesheet
                found = false;

                for (TimesheetRow row : timesheet.getEntries()) {

                    // Break out of loop if row was found
                    if (found) {
                        break;
                    }

                    if (row.getWorkPackage().equals(this)) {
                        found = true;
                        timesheetDate = timesheet.getDate();
                        // Gets the labourGrade and rate for the employee at the timesheet's date
                        labourGrade = employee.getLabourGradeForDate(timesheetDate);
                        currentRate = labourGrade.getLabourGrade().getRateForDate(timesheetDate);
                        // Gets the string value of the labour grade to put it in the map.
                        String grade = labourGrade.getLabourGrade().getLabourGrade();


                        rowTotal = row.getTotal().doubleValue();
                        // Adds the amount of work to the current total.
                        currentLabourDays.put(grade, currentLabourDays.get(grade) + (rowTotal / HOURS_TO_DAY));
                        currentLabourDollars.put(grade, currentLabourDollars.get(grade) + 
                                (rowTotal * currentRate.getDailyRate().doubleValue()));
                    }
                }
            }
        }

    }

    /**
     * Helper function to set up labour maps.
     */
    private void initializeLabourMaps() {
        currentLabourDays = new HashMap<String, Double>();
        currentLabourDollars = new HashMap<String, Double>();

        currentLabourDays.put("P1", 0.0);
        currentLabourDays.put("P2", 0.0);
        currentLabourDays.put("P3", 0.0);
        currentLabourDays.put("P4", 0.0);
        currentLabourDays.put("P5", 0.0);
        currentLabourDays.put("P6", 0.0);
        currentLabourDays.put("JS", 0.0);
        currentLabourDays.put("DS", 0.0);

        currentLabourDollars.put("P1", 0.0);
        currentLabourDollars.put("P2", 0.0);
        currentLabourDollars.put("P3", 0.0);
        currentLabourDollars.put("P4", 0.0);
        currentLabourDollars.put("P5", 0.0);
        currentLabourDollars.put("P6", 0.0);
        currentLabourDollars.put("JS", 0.0);
        currentLabourDollars.put("DS", 0.0);
    }

    /**
     * Calculates the total number of labours days
     *  spent on the work package to date.
     * @return The total number of labour days.
     */
    public double calculateTotalCurrentLabourDays() {

        double sum = 0;

        for (double value : currentLabourDays.values()) {
            sum += value;
        }

        return sum;
    }
    
    /**
     * Calculates the total dollar amount spent on the work package to date.
     * @return The total dollar amount
     */
    public double calculateTotalCurrentLabourDollars() {

        double sum = 0;

        for (double value : currentLabourDollars.values()) {
            sum += value;
        }

        return sum;
    }
 
    
    /**
     * Gets the variance from the current actual work done and estimated budget.
     * @return the variance.
     */
    public String getVariance() {
        NumberFormat fmt = DecimalFormat.getPercentInstance();
        double variance = 0;
        double current = calculateTotalCurrentLabourDays();
        double estimated = budget.getSum();
        
        variance = (estimated - current) / estimated;
        
        return fmt.format(variance);
    }
    
    /**
     * Gets the estimated budget at completion for the work package.
     * @return the EAC.
     */
    public double getEAC() {
        ResponsibleEngineerEstimate estimate = getMostRecentEstimate();
        double estimateSum = 0;
        if (estimate != null) {
            estimateSum = estimate.getSum();
        }
        
        if (status.equals("Created")) {
            return budget.getSum();
        } else if (status.equals("Open")) {
            return calculateTotalCurrentLabourDays() 
                    + estimateSum;
        } else {
            return calculateTotalCurrentLabourDays();
        }
    }
    
    /**
     * Gets the most recent estimate for work left on the work package.
     * @return The most recent estimate.
     */
    public ResponsibleEngineerEstimate getMostRecentEstimate() {
        ResponsibleEngineerEstimate current = null;
        
        for (ResponsibleEngineerEstimate estimate 
                : responsibleEngineerEstimates) {
            if (current == null) {
                current = estimate;
                continue;
            }
            
            if (estimate.getEstimateDate().after(current.getEstimateDate())) {
                current = estimate;
            }
            
        }
        
        return current;
    }
    
    /**
     * @return the projectNum
     */
    public ProjectEntity getProjectNum() {
        return project;
    }

    /**
     * @param projectNum the projectNum to set
     */
    public void setProjectNum(ProjectEntity project) {
        this.project = project;
    }

    public Set<EmployeeEntity> getEmployees() {
        if (employees == null) {
            return new HashSet<EmployeeEntity>();
        }
        return employees;
    }

    public void setEmployees(Set<EmployeeEntity> employees) {
        this.employees = employees;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the responsibleEngineerID
     */
    public EmployeeEntity getResponsibleEngineer() {
        return responsibleEngineer;
    }

    /**
     * @param responsibleEngineerID the responsibleEngineerID to set
     */
    public void setResponsibleEngineer(EmployeeEntity responsibleEngineer) {
        this.responsibleEngineer = responsibleEngineer;
    }

    /**
     * @return the responsibleEngineerEstimates
     */
    public Set<ResponsibleEngineerEstimate> getResponsibleEngineerEstimates() {
        return responsibleEngineerEstimates;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the id
     */
    public WorkPackageId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(WorkPackageId id) {
        this.id = id;
    }

    /**
     * @return the project
     */

   
	/**
	 * @return the project
	 */
	public ProjectEntity getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(ProjectEntity project) {
		this.project = project;
	}

    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WorkPackageEntity [id=" + id + ", employees=" + employees + ", title=" + title
				+ ", responsibleEngineer=" + responsibleEngineer + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", status=" + status + ", purpose=" + purpose + ", activities="
				+ activities + ", inputs=" + inputs + ", outputs=" + outputs + ", budget=" + budget + "]";
	}

	/**
     * @return the parentPackage
     */
//    public WorkPackageEntity getParentPackage() {
//        return parentPackage;
//    }

    /**
     * @param parentPackage the parentPackage to set
     */
//    public void setParentPackage(WorkPackageEntity parentPackage) {
//        this.parentPackage = parentPackage;
//    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the purpose
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * @param purpose the purpose to set
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     * @return the activities
     */
    public String getActivities() {
        return activities;
    }

    /**
     * @param activities the activities to set
     */
    public void setActivities(String activities) {
        this.activities = activities;
    }

    /**
     * @return the inputs
     */
    public String getInputs() {
        return inputs;
    }

    /**
     * @param inputs the inputs to set
     */
    public void setInputs(String inputs) {
        this.inputs = inputs;
    }

    /**
     * @return the outputs
     */
    public String getOutputs() {
        return outputs;
    }

    /**
     * @param outputs the outputs to set
     */
    public void setOutputs(String outputs) {
        this.outputs = outputs;
    }

    /**
     * @return the budget
     */
    public ResponsibleEngineerBudgetEntity getBudget() {
        return budget;
    }

    /**
     * @param budget the budget to set
     */
    public void setBudget(ResponsibleEngineerBudgetEntity budget) {
        this.budget = budget;
    }

    /**
     * @return the currentLabourDays
     */
    public Map<String, Double> getCurrentLabourDays() {
        return currentLabourDays;
    }

    /**
     * @return the currentLabourDollars
     */
    public Map<String, Double> getCurrentLabourDollars() {
        return currentLabourDollars;
    }
    
    /* (non-Javadoc) 
     * @see java.lang.Object#hashCode() 
     */ 
    @Override 
    public int hashCode() { 
        final int prime = 31; 
        int result = 1; 
        result = prime * result + ((id == null) ? 0 : id.hashCode()); 
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
        WorkPackageEntity other = (WorkPackageEntity) obj; 
        if (id == null) { 
            if (other.id != null) 
                return false; 
        } else if (!id.equals(other.id)) 
            return false; 
        return true;
    }
}