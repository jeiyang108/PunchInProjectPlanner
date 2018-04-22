/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.bcit.PunchIn.DAOs;

import ca.bcit.PunchIn.Entities.Timesheet.Timesheet;

import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.persistence.TypedQuery;

/**
 *
 * @author yasharnesvaderani
 */
@Stateless
@Dependent
@Named
public class TimesheetDAO extends BaseDAO {
    
    public void persist(Timesheet timesheet) {
        em.persist(timesheet);
    }
    
    public Timesheet merge(Timesheet timesheet) {
        return em.merge(timesheet);
    }

    public Timesheet find(int timesheetID) {
        return em.find(Timesheet.class, timesheetID);
    }

    
    public List<Timesheet> findEmployeeSheets(Long employeeID) {   
        TypedQuery<Timesheet> query = em.createQuery("SELECT t from Timesheet t "
                + "WHERE t.employee.employeeID = :id", Timesheet.class);
        query.setParameter("id", employeeID);
        
        for(Timesheet t : query.getResultList()) {
        	System.out.println(t);
        }
        return query.getResultList();

    }
    
    /**
     * Gets all timesheets ready for approval for this approver.
     * Timesheets must have SentForApproval status and be by an employee
     * who has this approver designated.
     * @param approverID
     *      EmployeeID of approver
     * @return
     *      List of timesheets 
     */
    public List<Timesheet> findApproverSheets(long approverID) {

        TypedQuery<Timesheet> query = em.createQuery("SELECT t from Timesheet t "
                + "WHERE t.approvalStatus = :approval "
                + "AND t.employee.timesheetApprover.employeeID = :empId", Timesheet.class);
        query.setParameter("approval", Timesheet.ApprovalStatus.SentForApproval);
        query.setParameter("empId", approverID);
        
        return query.getResultList();
    }
     
    
    public void remove(int timesheetID) {
        em.remove(find(timesheetID));
    }

}
