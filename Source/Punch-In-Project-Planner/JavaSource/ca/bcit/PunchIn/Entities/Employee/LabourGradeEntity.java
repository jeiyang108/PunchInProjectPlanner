package ca.bcit.PunchIn.Entities.Employee;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author sophie lindgren
 *
// */
@Entity
@Table(name = "LabourGrade")
@XmlRootElement(name = "labourgrade")
public class LabourGradeEntity implements Serializable {


    @Id
    @Column(name="labourGradeID", columnDefinition="char", length=3)
    private String labourGrade;    

    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="labourGradeID")
    private Set<RateEntity> rate;

    /**
     * Accessor.
     * @return
     *      labourGrade
     */
    public String getLabourGrade() {
        return labourGrade;
    }

    /**
     * Mutator.
     * @param labourGrade
     *      labourGrade
     */
    public void setLabourGrade(String labourGrade) {
        this.labourGrade = labourGrade;
    }

    /**
     * @return the rate
     */
    public Set<RateEntity> getRate() {
        return rate;
    }

    /**
     * Gets the Rate for the specified date.
     * @param date The date to find the rate for.
     * @return The rate at the given date.
     */
    public RateEntity getRateForDate(Date date) {
        RateEntity rateForDate = null;
        for (RateEntity currentRate : rate) {

            if (rateForDate == null) {
                rateForDate = currentRate;
            }

            // If the given date is after the start date for the rate.
            if (date.after(currentRate.getStartDate())) {

                // Check if the rate in the loop is after the current rate chosen.
                if (currentRate.getStartDate().after(rateForDate.getStartDate())) {
                    rateForDate = currentRate;
                }
            }
        }
        
        return rateForDate;
    }


    /**
     * @param rate the rate to set
     */
    public void setRate(Set<RateEntity> rate) {
        this.rate = rate;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return labourGrade;
    }


}
