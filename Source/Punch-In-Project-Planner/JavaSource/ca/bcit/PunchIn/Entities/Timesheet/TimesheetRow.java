package ca.bcit.PunchIn.Entities.Timesheet;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

import ca.bcit.PunchIn.Entities.Project.ProjectEntity;
import ca.bcit.PunchIn.Entities.Project.WorkPackageEntity;
import ca.bcit.PunchIn.Entities.Project.WorkPackageId;

/**
 * Represents an entry row in a timesheet.
 * @author Sophie Lindgren
 * @version 1.0
 *
 */
@Entity
@Table(name="TimesheetRow")
public class TimesheetRow implements Serializable {
    /**
     * ID for timesheet row (primary key).
     */
    @Id
    @Column(name="TimesheetRowID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int timesheetRowId;

    /**
     * Link the timesheet;
     */
    @ManyToOne
    @JoinColumn(name="TimesheetID")
    private Timesheet timesheet;

    /**
     * Number of days in a workweek.
     */
    public static final int DAYS_IN_WEEK = 7;



    /**
     * Entries for each workday in the week.
     */
    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="timesheetRow")
    private List<TimesheetRowDate> daysOfWeek;

    /**
     * Notes related to this entry.
     */
    @Column(name="NOTES", length=100)
    private String notes;

    @ManyToOne()
    @JoinColumns( {
        @JoinColumn(name="WorkpackageID", referencedColumnName="WorkpackageID"),
        @JoinColumn(name="ProjectNum", referencedColumnName="ProjectNum")
    } )
    private WorkPackageEntity workPackage;
    
    /**
     * Zero-parameter constructor.
     */
    public TimesheetRow() {
    }

    /**
     * Constructor.
     * Initializes empty values for each day of week.
     */
    public TimesheetRow(Timesheet ts) {
        this.timesheet = ts;
        daysOfWeek = new ArrayList<TimesheetRowDate>();
        for (int x = 1; x <= DAYS_IN_WEEK; x++) {
            TimesheetRowDate rowDate = new TimesheetRowDate(x, this);
            rowDate.setHoursWorked(new BigDecimal(0));
            daysOfWeek.add(rowDate);
        }
        workPackage = new WorkPackageEntity();
        workPackage.setId(new WorkPackageId());
    }

    public TimesheetRowDate getDayOfWeek(int dayOfWeek) {
        for (TimesheetRowDate day : daysOfWeek) {
            if (day.getDayOfWeek() == dayOfWeek)
                return day;
        }

        //create new day if not extant
        TimesheetRowDate date = new TimesheetRowDate(dayOfWeek, this);
        date.setHoursWorked(new BigDecimal(0));
        daysOfWeek.add(date);
        return date;
    }


    /**
     * Gets the total hours for the week.
     * @return
     *      Total for all days of week.
     */
    public BigDecimal getTotal() {
        BigDecimal total = new BigDecimal(0);
        for (TimesheetRowDate rowDate : daysOfWeek) {
            total = total.add(rowDate.getHoursWorked());   

        }
        return total;
    } 


    /**
     * Accessor.
     * @return
     *      Notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Mutator.
     * @param notes
     *      Notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Accessor.
     * @return
     *      Timesheet Row Id
     */
    public int getTimesheetRowId() {
        return timesheetRowId;
    }

    /**
     * Mutator.
     * @param timesheetRowId
     *      Timesheet Row Id
     */
    public void setTimesheetRowId(int timesheetRowId) {
        this.timesheetRowId = timesheetRowId;
    }

    public WorkPackageEntity getWorkPackage() {
        return workPackage;
    }

    public void setWorkPackage(WorkPackageEntity workPackage) {
        this.workPackage = workPackage;
    }

    
    public void selectProject(AjaxBehaviorEvent event) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

}
