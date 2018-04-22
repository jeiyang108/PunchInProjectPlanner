package ca.bcit.PunchIn.Entities.Timesheet;

import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Class representing a timesheet for a given
 * week and employee.
 * @author Sophie Lindgren
 * @version 1.0
 */
@Entity
@Table(name="Timesheet")
//@NamedEntityGraph(name = "graph.Timesheet.entries", 
//               attributeNodes = @NamedAttributeNode(value = "entries", subgraph = "entries"), 
//               subgraphs = @NamedSubgraph(name = "entries", attributeNodes = @NamedAttributeNode("daysOfWeek")))
public class Timesheet implements Serializable {
    /**
     * Enum for different values of the Approval status.
     * @author sophie
     *
     */
    public enum ApprovalStatus {
        Unapproved,
        SentForApproval,
        Returned,
        Approved
    }
    /**
     * ID for timesheet (primary key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TimesheetID")
    private int timesheetId;
    
    @ManyToOne(optional=false)
    @JoinColumn(name="EmployeeID")
    private EmployeeEntity employee;
    
    /**
     * The week of the year for the timesheet.
     */
    @Column(name="WeekEnd", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date endWeek;
    
    /**
     * Total overtime for the week.
     */
    @Column(name="OverTime", precision=4, scale=2)
    private BigDecimal overtime;
    
    /**
     * Total flextime for the week.
     */
    @Column(name="FlexTime", precision=4, scale=2)
    private BigDecimal flextime;
    
    /**
     * List of row entries for the timesheet.
     */
    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="timesheet")
    private List<TimesheetRow> entries;
    
    /**
     * Approval status of the timesheet.
     */
    @Column(name="Approval", nullable=false)
    private ApprovalStatus approvalStatus;
    
    /**
     * Notes written by the timesheet approver.
     */
    @Column(name="Notes")
    private String approvalNotes;

    @Transient
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    /**
     * Empty constructor for bean injection.
     */
    public Timesheet() {}
    
    /**
     * Constructor
     * @param endWeek
     * @param employee
     */
    public Timesheet(Date endWeek, EmployeeEntity employee) {
        this.endWeek = endWeek;
        this.employee = employee;
        this.approvalStatus = ApprovalStatus.Unapproved;
        entries = new ArrayList<TimesheetRow>();      
    }
    
    /**
     * Accessor.
     * @return
     *      EmployeeEntity for timesheet employee
     */
    public EmployeeEntity getEmployee() {
        return employee;
    }

    
    /**
     * Accessor.
     * @return
     *      Timesheet Id
     */
    public int getTimesheetId() {
        return timesheetId;
    }
    
    /**
     * Mutator.
     * @param timesheetId
     *      Timesheet Id
     */
    public void setTimesheetId(int timesheetId) {
        this.timesheetId = timesheetId;
    }
    
    /**
     * Calculates the grand total of hours worked.
     * @return
     *      Total hours worked.
     */
    public BigDecimal getTotalHours() {
        BigDecimal total = new BigDecimal(0);
          
        for (TimesheetRow entry : entries) {
            total = total.add(entry.getTotal());
        }        
        
        return total;
    }
    
    /**
     * Accessor.
     * @return
     *      overtime
     */
    public BigDecimal getOvertime() {
       return overtime;
    }
    
    /**
     * Accessor.
     * @return
     *      flextime
     */
    public BigDecimal getFlextime() {
        return flextime;
    }
    
    /**
     * TODO: implement overtime calculations
     */
    public void setOvertime() {}
    
    /**
     * TODO: implement flextime calculations
     */
    public void setFlextime() {}
    
    /**
     * Gets the total hours for a weekday.
     * @param dayOfWeek
     *      The day of week.
     * @return
     *      total hours for weekday.
     */
    public BigDecimal getTotalHoursByWeekday(int dayOfWeek) {
        BigDecimal total = new BigDecimal(0);
        for (TimesheetRow entry : entries) {
            total = total.add(entry.getDayOfWeek(dayOfWeek).getHoursWorked());
        }
        return total;
    }
    
    /**
     * Adds an entry to the timesheet.
     */
    public void addEntry() {
        entries.add(new TimesheetRow(this));
    }
    
    /**
     * Deletes an entry from the timesheet.
     * @param entryId
     *      TimesheetRowId of row to delete.
     * @return
     *      True if entry is deleted successfully;
     *      False if entry with that Id is not found.
     */
    public boolean deleteEntry(int entryId) {
        TimesheetRow entryToDelete = null;
        for (TimesheetRow entry : entries) {
            if (entry.getTimesheetRowId() == entryId) {
                entryToDelete = entry;
                break;
            }
        }
        
        if (entryToDelete != null) {
            entries.remove(entryToDelete);
            return true;
        }
        
        return false;
    }
    
    /**
     * Accessor for all rows.
     * @return
     *      entries
     */
    public List<TimesheetRow> getEntries() {
        return entries;
    }
    
    /**
     * Accessor for approval status.
     * @return
     *      Approval status of timesheet.
     */
    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }
    
    /**
     * Mutator for approval status.
     * @param value
     *      New approval status of timesheet.
     */
    public void setApprovalStatus(ApprovalStatus value) {
        approvalStatus = value;
    }
    
    /**
     * Accessor.
     * @return
     *      End week.
     */
    public String getEndWeek() {
        return dateFormat.format(endWeek);
    }
    
    public Date getDate() {
        return endWeek;
    }
    
    /**
     * Gets the weeknumber of the timesheet's week.
     * @return
     *      Week number as formatted string.
     */
    public int getWeekNumber() {
        Calendar c = Calendar.getInstance();
        c.setTime(endWeek);
        return c.get(Calendar.WEEK_OF_YEAR);
    }
    
    /**
     * Mutator for approval notes.
     * @param value
     *      Notes from timesheet approver.
     */
    public void setApprovalNotes(String value) {
        this.approvalNotes = value;
    }
    
    /**
     * Accessor for approval notes.
     * @return
     *     Notes from timesheet approval
     */
    public String getApprovalNotes() {
        return approvalNotes;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Timesheet [timesheetId=" + timesheetId + ", employee=" + employee + ", endWeek=" + endWeek
				+ ", overtime=" + overtime + ", flextime=" + flextime + ", entries=" + entries + ", approvalStatus="
				+ approvalStatus.toString() + "]";
	}
}
