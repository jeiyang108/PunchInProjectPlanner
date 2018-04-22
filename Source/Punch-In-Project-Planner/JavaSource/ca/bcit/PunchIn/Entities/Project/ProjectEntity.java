/**
 * 
 */
package ca.bcit.PunchIn.Entities.Project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;

/**
 * @author eboow
 *
 */
@Entity
@Table(name = "Project")
public class ProjectEntity implements Serializable {
    
    /**
	 * Default UID.
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name="ProjectNum") 
    private int projectNum;
    
    @Column(name="ProjectName", length=30, nullable=false) 
    private String projectName;
    
    @ManyToOne
    @JoinColumn(name="ProjectManagerID")
    private EmployeeEntity projectManager;
    
    @Column(name="StartDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    
    @Column(name="EndDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    
    @Column(name="Status", length=30, nullable=false)
    private String status;
    
    @Column(name="Summary", length=255)
    private String summary;
    
    @OneToOne(mappedBy="project", cascade= {CascadeType.ALL})
    private BudgetEntity budget;
    

    @OneToMany(mappedBy="project", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private List<WorkPackageEntity> workPackages;
    
    @Transient
    private boolean isNewlyCreated;
    /**
     * @return the projectNum
     */
    public int getProjectNum() {
        return projectNum;
    }

    /**
     * @param projectNum the projectNum to set
     */
    public void setProjectNum(int projectNum) {
        this.projectNum = projectNum;
    }

    public List<WorkPackageEntity> getWorkPackages() {
        return workPackages;
    }

    public void setWorkPackages(List<WorkPackageEntity> workPackages) {
        this.workPackages = workPackages;
    }
    
    public List<EmployeeEntity> getEmployees() {
        Set<EmployeeEntity> employeeSet = new HashSet<EmployeeEntity>();
        
        if (workPackages != null) {
            for (WorkPackageEntity workPackage : workPackages) {
                employeeSet.addAll(workPackage.getEmployees());
            }
        }
        
        
        List<EmployeeEntity> employees = new ArrayList<EmployeeEntity>();
        employees.addAll(employeeSet);
        
        return employees;
    }
    
    /**
     * Gets the total work done in labour days on the project for the given labour grade.
     * @param grade The labour grade to find the total for.
     * @return The total work done on the project by the specified labour grade.
     */
    public double getTotalForLabourGradeDays(String grade) {
        double total = 0.0;

        if (grade.equals("total")) {
            for (WorkPackageEntity workPackage : workPackages) {
                total += workPackage.calculateTotalCurrentLabourDays();
            }
        } else {
            for (WorkPackageEntity workPackage : workPackages) {
            	if (workPackage.getCurrentLabourDays() !=null ) {
            		total += workPackage.getCurrentLabourDays().get(grade);
            	}
            }
        }

        return total;
    }

    /**
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
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
     * @return the projectManager
     */
    public EmployeeEntity getProjectManager() {
        return projectManager;
    }

    /**
     * @param projectManager the projectManager to set
     */
    public void setProjectManager(EmployeeEntity projectManager) {
        this.projectManager = projectManager;
    }

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
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return the budget
     */
    public BudgetEntity getBudget() {
        return budget;
    }

    /**
     * @param budget the budget to set
     */
    public void setBudget(BudgetEntity budget) {
        this.budget = budget;
    }

    

    /**
	 * @return the idNewlyCreated
	 */
	public boolean getIsNewlyCreated() {
		return isNewlyCreated;
	}

	/**
	 * @param idNewlyCreated the idNewlyCreated to set
	 */
	public void setIsNewlyCreated(boolean isNewlyCreated) {
		this.isNewlyCreated = isNewlyCreated;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProjectEntity [projectNum=" + projectNum + ", projectName=" + projectName + ", projectManager="
				+ projectManager + ", startDate=" + startDate + ", endDate=" + endDate + ", status=" + status
				+ ", summary=" + summary + ", budget=" + budget + ", workPackages=" + workPackages + "]";
	}

	/* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + ((projectManager == null) ? 0 : projectManager.hashCode());
        result = prime * result + ((projectName == null) ? 0 : projectName.hashCode());
        result = prime * result + projectNum;
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((summary == null) ? 0 : summary.hashCode());
        result = prime * result + ((workPackages == null) ? 0 : workPackages.hashCode());
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
        ProjectEntity other = (ProjectEntity) obj;
        if (endDate == null) {
            if (other.endDate != null)
                return false;
        } else if (!endDate.equals(other.endDate))
            return false;
        if (projectManager == null) {
            if (other.projectManager != null)
                return false;
        } else if (!projectManager.equals(other.projectManager))
            return false;
        if (projectName == null) {
            if (other.projectName != null)
                return false;
        } else if (!projectName.equals(other.projectName))
            return false;
        if (projectNum != other.projectNum)
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (summary == null) {
            if (other.summary != null)
                return false;
        } else if (!summary.equals(other.summary))
            return false;
        if (workPackages == null) {
            if (other.workPackages != null)
                return false;
        } else if (!workPackages.equals(other.workPackages))
            return false;
        return true;
    }
    
}