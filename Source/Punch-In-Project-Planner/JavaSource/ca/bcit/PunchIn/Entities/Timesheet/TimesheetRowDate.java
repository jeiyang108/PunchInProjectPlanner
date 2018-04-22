/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.bcit.PunchIn.Entities.Timesheet;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Class representing a day of week for a timesheet entry.
 * @author Sophie Lindgren
 * @version 1.0
 *
 */
@Entity
@Table(name="Day")
public class TimesheetRowDate implements Serializable {
    
    @Id
    @Column(name="DayID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int timesheetRowDateId;
    
    /**
     * The day of week.
     * Number between 1 (Sunday) and 7 (Saturday). 
     */
    @Column(name="DayOfWeek", columnDefinition="tinyint")
    private int dayOfWeek;
    
    @ManyToOne
    @JoinColumn(name="timesheetRowId")
    private TimesheetRow timesheetRow;
    /**
     * The hours worked for the given date.
     */
    @Column(name="Hour", precision=3, scale=1)
    private BigDecimal hoursWorked;
    
    public TimesheetRowDate() {
    }
    
    /**
     * Constructor.
     * @param dayOfWeek
     *      Day of week.
     * @param twRow
     *      The row this date belongs to 
     */
    public TimesheetRowDate(int dayOfWeek, TimesheetRow tsRow) {
        this.dayOfWeek = dayOfWeek;
        this.timesheetRow = tsRow;
    }
    
    /**
     * Accessor.
     * @return
     *      Day of week
     */
    public int getDayOfWeek() {
        return dayOfWeek;
    }
    
    /**
     * Mutator.
     * @param dayOfWeek
     *      Day of week
     */
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    } 

    /**
     * Accessor.
     * @return
     *      Hours worked
     */
    public BigDecimal getHoursWorked() {
        return hoursWorked;
    }
    
    /**
     * Mutator.
     * @param hoursWorked
     *      Hours worked
     */
    public void setHoursWorked(BigDecimal hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
}
