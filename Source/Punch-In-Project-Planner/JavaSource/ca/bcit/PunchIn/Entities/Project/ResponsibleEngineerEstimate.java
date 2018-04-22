package ca.bcit.PunchIn.Entities.Project;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents the estimates the responsible engineer makes on 
 * the remaining amount of work left in the work package.
 * @author yasharnesvaderani
 * @version 1.0
 */
@Entity
@Table(name = "ResponsibleEngineerEstimate")
public class ResponsibleEngineerEstimate implements Serializable {
    
    /**
     * Auto-generated primary key.
     */
    @Id
    @Column(name = "ResponsibleEngineerEstimateID")
    private long estimateId;
    
    /**
     * The work package that the estimate is for.
     */
    @ManyToOne
    @JoinColumns({ 
        @JoinColumn(name = "WorkPackageID", 
                referencedColumnName = "WorkPackageID"),
        @JoinColumn(name = "ProjectNum",
                referencedColumnName = "ProjectNum")})
    private WorkPackageEntity workPackage;
    
    /**
     * The date for which the estimate was made.
     */
    @Column(name = "EstimateDate")
    @Temporal(TemporalType.DATE)
    private Calendar estimateDate;
    
    /** 
     * The amount of work in JS Person Days estimated to be left in the project.
     */
    @Column(name = "JSPersonDays", columnDefinition = "float")
    private Double JSPersonDays;
    
    /** 
     * The amount of work in DS Person Days estimated to be left in the project.
     */
    @Column(name = "DSPersonDays", columnDefinition = "float")
    private Double DSPersonDays;
    
    /** 
     * The amount of work in P1 Person Days estimated to be left in the project.
     */
    @Column(name = "P1PersonDays", columnDefinition = "float")
    private Double P1PersonDays;
    
    /** 
     * The amount of work in P2 Person Days estimated to be left in the project.
     */
    @Column(name = "P2PersonDays", columnDefinition = "float")
    private Double P2PersonDays;
    
    /** 
     * The amount of work in P3 Person Days estimated to be left in the project.
     */
    @Column(name = "P3PersonDays", columnDefinition = "float")
    private Double P3PersonDays;
    
    /** 
     * The amount of work in P4 Person Days estimated to be left in the project.
     */
    @Column(name = "P4PersonDays", columnDefinition = "float")
    private Double P4PersonDays;
    
    /** 
     * The amount of work in P5 Person Days estimated to be left in the project.
     */
    @Column(name = "P5PersonDays", columnDefinition = "float")
    private Double P5PersonDays;
    
    /** 
     * The amount of work in P6 Person Days estimated to be left in the project.
     */
    @Column(name = "P6PersonDays", columnDefinition = "float")
    private Double P6PersonDays;
    
    public double getSum() {
        double sum = 0;
        if (JSPersonDays != null) {
            sum += JSPersonDays;
        }
        
        if (DSPersonDays != null) {
            sum += JSPersonDays;
        }
        
        if (P1PersonDays != null) {
            sum += JSPersonDays;
        }
        
        if (P2PersonDays != null) {
            sum += JSPersonDays;
        }
        
        if (P3PersonDays != null) {
            sum += JSPersonDays;
        }
        
        if (P4PersonDays != null) {
            sum += JSPersonDays;
        }
        
        if (P5PersonDays != null) {
            sum += JSPersonDays;
        }
        if (P6PersonDays != null) {
            sum += JSPersonDays;
        }
        
        return sum;
    }

    /**
     * Returns the estimate id.
     * @return the estimateId
     */
    public long getEstimateId() {
        return estimateId;
    }

    /**
     * Sets the estimate id.
     * @param estimateId the estimateId to set
     */
    public void setEstimateId(long estimateId) {
        this.estimateId = estimateId;
    }
    
    /**
     * Returns the work package for the estimate.
     * @return The work package for the estimate.
     */
    public WorkPackageEntity getWorkPackage() {
        return workPackage;
    }
    
    /**
     * Sets the work package for the responsible engineer estimate.
     * @param wp The work package to set.
     */
    public void setWorkPackage(WorkPackageEntity wp) {
        workPackage = wp;
    }

    /**
     * Returns the date for the estimate.
     * @return the estimateDate
     */
    public Calendar getEstimateDate() {
        return estimateDate;
    }

    /**
     * Sets the date for the estimate.
     * @param estimateDate the estimateDate to set
     */
    public void setEstimateDate(Calendar estimateDate) {
        this.estimateDate = estimateDate;
    }

    /**
     * Returns the jSPersonDays.
     * @return the jSPersonDays
     */
    public Double getJSPersonDays() {
        return JSPersonDays;
    }

    /**
     * Sets the jSPersonDays.
     * @param jSPersonDays the jSPersonDays to set
     */
    public void setJSPersonDays(Double jSPersonDays) {
        JSPersonDays = jSPersonDays;
    }

    /**
     * Returns the dSPerson Days.
     * @return the dSPersonDays
     */
    public Double getDSPersonDays() {
        return DSPersonDays;
    }

    /**
     * Sets the dSPersonDays.
     * @param dSPersonDays the dSPersonDays to set
     */
    public void setDSPersonDays(Double dSPersonDays) {
        DSPersonDays = dSPersonDays;
    }

    /**
     * Returns the p1PersonDays.
     * @return the p1PersonDays
     */
    public Double getP1PersonDays() {
        return P1PersonDays;
    }

    /**
     * Sets the p1PersonDays.
     * @param p1PersonDays the p1PersonDays to set
     */
    public void setP1PersonDays(Double p1PersonDays) {
        P1PersonDays = p1PersonDays;
    }

    /**
     * Returns the p2PersonDays.
     * @return the p2PersonDays
     */
    public Double getP2PersonDays() {
        return P2PersonDays;
    }

    /**
     * Sets the p2PersonDays.
     * @param p2PersonDays the p2PersonDays to set
     */
    public void setP2PersonDays(Double p2PersonDays) {
        P2PersonDays = p2PersonDays;
    }

    /**
     * Returns the p3PersonDays.
     * @return the p3PersonDays
     */
    public Double getP3PersonDays() {
        return P3PersonDays;
    }

    /**
     * Sets the p3PersonDays.
     * @param p3PersonDays the p3PersonDays to set
     */
    public void setP3PersonDays(Double p3PersonDays) {
        P3PersonDays = p3PersonDays;
    }

    /**
     * Returns the p4PersonDays.
     * @return the p4PersonDays
     */
    public Double getP4PersonDays() {
        return P4PersonDays;
    }

    /**
     * Sets the p4PersonDays.
     * @param p4PersonDays the p4PersonDays to set
     */
    public void setP4PersonDays(Double p4PersonDays) {
        P4PersonDays = p4PersonDays;
    }

    /**
     * Returns the p5PersonDays.
     * @return the p5PersonDays
     */
    public Double getP5PersonDays() {
        return P5PersonDays;
    }

    /**
     * Sets the p5PersonDays.
     * @param p5PersonDays the p5PersonDays to set
     */
    public void setP5PersonDays(Double p5PersonDays) {
        P5PersonDays = p5PersonDays;
    }

    /**
     * Returns the p6PersonDays.
     * @return the p6PersonDays
     */
    public Double getP6PersonDays() {
        return P6PersonDays;
    }

    /**
     * Sets the p6PersonDays.
     * @param p6PersonDays the p6PersonDays to set
     */
    public void setP6PersonDays(Double p6PersonDays) {
        P6PersonDays = p6PersonDays;
    }

}
