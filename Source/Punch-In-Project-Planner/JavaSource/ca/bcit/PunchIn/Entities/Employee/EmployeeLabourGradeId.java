package ca.bcit.PunchIn.Entities.Employee;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Class for combined primary key for EmployeeLabourGradeEntity.
 * @author sophie
 *
 */
@Embeddable
public class EmployeeLabourGradeId implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

    @Column(name="labourGradeID", columnDefinition="char", length=3)
	private String labourGrade;

    @Column(name="EmployeeID")
    private Long employeeId;
	
	/**
     * @return the labourGrade
     */
    public String getLabourGrade() {
        return labourGrade;
    }

    /**
     * @param labourGrade the labourGrade to set
     */
    public void setLabourGrade(String labourGrade) {
        this.labourGrade = labourGrade;
    }

    /**
     * @return the employeeId
     */
    public Long getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((employeeId == null) ? 0 : employeeId.hashCode());
        result = prime * result + ((labourGrade == null) ? 0 : labourGrade.hashCode());
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
        EmployeeLabourGradeId other = (EmployeeLabourGradeId) obj;
        if (employeeId == null) {
            if (other.employeeId != null)
                return false;
        } else if (!employeeId.equals(other.employeeId))
            return false;
        if (labourGrade == null) {
            if (other.labourGrade != null)
                return false;
        } else if (!labourGrade.equals(other.labourGrade))
            return false;
        return true;
    }
    
    
}
