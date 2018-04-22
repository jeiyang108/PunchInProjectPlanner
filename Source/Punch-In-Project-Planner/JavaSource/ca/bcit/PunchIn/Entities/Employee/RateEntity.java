package ca.bcit.PunchIn.Entities.Employee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * 
 * @author sophie lindgren
 *
 */
@Entity
@Table(name = "Rate")
public class RateEntity implements Serializable {
 
    @Id
    @Column(name="RateID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rateId;
    
    @Column(name="StartDate", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date startDate;
    
    @Column(name="DailyRate", nullable=false, precision=6, scale=2)
    private BigDecimal dailyRate;
    
    /**
     * Accessor.
     * @return
     *      rateId
     */
    public Long getRateId() {
        return rateId;
    }
    /**
     * Mutator.
     * @param rateId
     *      Rate ID
     *      
     */
    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }
    
    /**
     * Accessor.
     * @return
     *      Start Date
     */
    public Date getStartDate() {
        return startDate;
    }
    
    /**
     * Mutator.
     * @param startDate
     *      Start Date
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;        
    }
 
    /**
     * Accessor.
     * @return
     *      Daily Rate
     */
    public BigDecimal getDailyRate() {
        return dailyRate;
    }
    
    /**
     * Mutator.
     * @param dailyRate
     *      Daily Rate
     */
    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }
}
