package ca.bcit.PunchIn.Entities.Project;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

@Entity
@Table(name = "ResponsibleEngineerBudget")
public class ResponsibleEngineerBudgetEntity {
    
    @EmbeddedId
    private WorkPackageId id;
    
    @OneToOne
    @PrimaryKeyJoinColumns( { 
        @PrimaryKeyJoinColumn(name="WorkPackageID", referencedColumnName="WorkPackageID"),
        @PrimaryKeyJoinColumn(name="ProjectNum", referencedColumnName="ProjectNum")
        } )
    
    private WorkPackageEntity workPackage;
    
    @Column(name="JSPersonDays", columnDefinition="float")
    private Double JSPersonDays;
    
    @Column(name="DSPersonDays", columnDefinition="float")
    private Double DSPersonDays;
    
    @Column(name="P1PersonDays", columnDefinition="float")
    private Double P1PersonDays;
    
    @Column(name="P2PersonDays", columnDefinition="float")
    private Double P2PersonDays;
    
    @Column(name="P3PersonDays", columnDefinition="float")
    private Double P3PersonDays;
    
    @Column(name="P4PersonDays", columnDefinition="float")
    private Double P4PersonDays;
    
    @Column(name="P5PersonDays", columnDefinition="float")
    private Double P5PersonDays;
    
    @Column(name="P6PersonDays", columnDefinition="float")
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
     * @return the workPackage
     */
    public WorkPackageEntity getWorkPackage() {
        return workPackage;
    }

    /**
     * @param workPackage the workPackage to set
     */
    public void setWorkPackage(WorkPackageEntity workPackage) {
        this.workPackage = workPackage;
    }

    /**
     * @return the jSPersonDays
     */
    public Double getJSPersonDays() {
        return JSPersonDays;
    }

    /**
     * @param jSPersonDays the jSPersonDays to set
     */
    public void setJSPersonDays(Double jSPersonDays) {
        JSPersonDays = jSPersonDays;
    }

    /**
     * @return the dSPersonDays
     */
    public Double getDSPersonDays() {
        return DSPersonDays;
    }

    /**
     * @param dSPersonDays the dSPersonDays to set
     */
    public void setDSPersonDays(Double dSPersonDays) {
        DSPersonDays = dSPersonDays;
    }

    /**
     * @return the p1PersonDays
     */
    public Double getP1PersonDays() {
        return P1PersonDays;
    }

    /**
     * @param p1PersonDays the p1PersonDays to set
     */
    public void setP1PersonDays(Double p1PersonDays) {
        P1PersonDays = p1PersonDays;
    }

    /**
     * @return the p2PersonDays
     */
    public Double getP2PersonDays() {
        return P2PersonDays;
    }

    /**
     * @param p2PersonDays the p2PersonDays to set
     */
    public void setP2PersonDays(Double p2PersonDays) {
        P2PersonDays = p2PersonDays;
    }

    /**
     * @return the p3PersonDays
     */
    public Double getP3PersonDays() {
        return P3PersonDays;
    }

    /**
     * @param p3PersonDays the p3PersonDays to set
     */
    public void setP3PersonDays(Double p3PersonDays) {
        P3PersonDays = p3PersonDays;
    }

    /**
     * @return the p4PersonDays
     */
    public Double getP4PersonDays() {
        return P4PersonDays;
    }

    /**
     * @param p4PersonDays the p4PersonDays to set
     */
    public void setP4PersonDays(Double p4PersonDays) {
        P4PersonDays = p4PersonDays;
    }

    /**
     * @return the p5PersonDays
     */
    public Double getP5PersonDays() {
        return P5PersonDays;
    }

    /**
     * @param p5PersonDays the p5PersonDays to set
     */
    public void setP5PersonDays(Double p5PersonDays) {
        P5PersonDays = p5PersonDays;
    }

    /**
     * @return the p6PersonDays
     */
    public Double getP6PersonDays() {
        return P6PersonDays;
    }

    /**
     * @param p6PersonDays the p6PersonDays to set
     */
    public void setP6PersonDays(Double p6PersonDays) {
        P6PersonDays = p6PersonDays;
    }

}
