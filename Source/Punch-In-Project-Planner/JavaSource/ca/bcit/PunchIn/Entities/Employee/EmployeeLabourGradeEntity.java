package ca.bcit.PunchIn.Entities.Employee;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author sophie lindgren
 *
 */

@Entity
@Table(name = "EmployeeLabourGrade")
@XmlRootElement(name = "employeelabourgrade")
public class EmployeeLabourGradeEntity implements Serializable {
    
    @EmbeddedId
    private EmployeeLabourGradeId id;

    @ManyToOne
    @MapsId("labourGrade")
    @JoinColumn(name="LabourGradeID", columnDefinition="char")
    private LabourGradeEntity empLabourGrade;
    
    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name="EmployeeID")
    private EmployeeEntity employee;
    
    @Column(name="PromotionDate")
    @Temporal(TemporalType.DATE)
    private Date promotionDate;
    
    /**
     * @return the id
     */
    public EmployeeLabourGradeId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(EmployeeLabourGradeId id) {
        this.id = id;
    }

    /**
     * @return the empLabourGrade
     */
    public LabourGradeEntity getEmpLabourGrade() {
        return empLabourGrade;
    }

    /**
     * @param empLabourGrade the empLabourGrade to set
     */
    public void setEmpLabourGrade(LabourGradeEntity empLabourGrade) {
        this.empLabourGrade = empLabourGrade;
    }

    /**
     * @return the employee
     */
    public EmployeeEntity getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }

    /**
     * Accessor.
     * @return
     *      Labour Grade
     */
    public LabourGradeEntity getLabourGrade() {
        return empLabourGrade;
    }
    
    /**
     * Mutator.
     * @param labourGrade
     *      Labour Grade
     */
    public void setLabourGrade(LabourGradeEntity labourGrade) {
        this.empLabourGrade = labourGrade;
    }

    
    /**
     * Accessor.
     * @return
     *      Promotion Date
     */
    public Date getPromotionDate() {
        return promotionDate;
    }
    
    /**
     * Mutator
     * @param promotionDate
     *      Promotion Date.
     */
    public void setPromotionDate(Date promotionDate) {
        this.promotionDate = promotionDate;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((empLabourGrade == null) ? 0 : empLabourGrade.hashCode());
        result = prime * result + ((employee == null) ? 0 : employee.hashCode());
        result = prime * result + ((promotionDate == null) ? 0 : promotionDate.hashCode());
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
        EmployeeLabourGradeEntity other = (EmployeeLabourGradeEntity) obj;
        if (empLabourGrade == null) {
            if (other.empLabourGrade != null)
                return false;
        } else if (!empLabourGrade.equals(other.empLabourGrade))
            return false;
        if (employee == null) {
            if (other.employee != null)
                return false;
        } else if (!employee.equals(other.employee))
            return false;
        if (promotionDate == null) {
            if (other.promotionDate != null)
                return false;
        } else if (!promotionDate.equals(other.promotionDate))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return empLabourGrade.toString();
    }

    
    
}
