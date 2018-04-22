package ca.bcit.PunchIn.DAOs;

import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;
import ca.bcit.PunchIn.Models.Employee.Employee;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

/**
 * @author eboow
 *
 */
@Stateless
public class EmployeeDAO extends BaseDAO {
		
	/**
	 * Default UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Save user data to database.
	 * 
	 * @param emp
	 *            employee
	 */
	public void persist(Employee emp) {
        EmployeeEntity empEntity = emp.getBasicInfo();
		em.persist(empEntity);
	}

	/**
	 * Update user data to database.
	 * 
	 * @param emp
	 *            employee
	 */
	public void merge(Employee emp) {
        EmployeeEntity empEntity = emp.getBasicInfo();
		em.merge(empEntity);
	}

	
	/**
	 * Employee update self's data to database.
	 * 
	 * @param emp
	 *            employee
	 */
	public void updateSelf(Employee emp) {
        EmployeeEntity empEntity = emp.getBasicInfo();
		em.merge(empEntity);
	}

	/**
	 * Find employee entity in database.
	 * 
	 * @param id
	 *            employee id
	 * @return the Employee.
	 */
	public EmployeeEntity find(Long id) {
		EmployeeEntity emp = em.find(EmployeeEntity.class, id);
		if (emp != null) {
			emp.getTimesheets().size();
		}
		return emp;
	}

	/**
	 * Remove employee from database.
	 * 
	 * @param id
	 *            employee id.
	 */
	public void remove(Long id) {
		em.remove(find(id));
	}
	
	
	/**
	 * Delete employee from database.
	 * 
	 * @param emp
	 *            employee.
	 */
	public void delete(Employee emp) {
		em.remove(find(emp.getBasicInfo().getEmployeeID()));
	}
	
	/**
	 * Get all employees.
	 * @return set of employees.
	 */
	
	public Set<Employee> getEmployees(){
		Set<Employee> set = new HashSet<Employee>();
		TypedQuery<EmployeeEntity> query = em.createQuery("select e from EmployeeEntity e", EmployeeEntity.class);
		List<EmployeeEntity> entityList = null;
		try {
			entityList = query.getResultList();
		} catch (Exception ex) {
			entityList = new ArrayList<EmployeeEntity>();
		}
		
		for (EmployeeEntity entity : entityList) {
			Employee emp = new Employee();
			emp.setBasicInfo(entity);
			set.add(emp);
		}
		return set;
	}
	
	/**
	 * Get all employees who are supervised by current employee.
	 * @return set of employees.
	 */
	
	public Set<Employee> getEmployees(EmployeeEntity supervisor){
		Set<Employee> set = new HashSet<Employee>();
		TypedQuery<EmployeeEntity> query = em.createQuery("select e from EmployeeEntity e where e.supervisor = :manager", EmployeeEntity.class);
		query.setParameter("manager", supervisor);
		List<EmployeeEntity> entityList = null;
		try {
			entityList = query.getResultList();
		} catch (Exception ex) {
			entityList = new ArrayList<EmployeeEntity>();
		}
		
		for (EmployeeEntity entity : entityList) {
			Employee emp = new Employee();
			emp.setBasicInfo(entity);
			set.add(emp);
		}
		return set;
	}

	public List<EmployeeEntity> getProjectManagers() {
		// TODO Auto-generated method stub
		
		List<EmployeeEntity> list = new ArrayList<EmployeeEntity>();
		//How to determine an employee is a project manager is TBD.
		TypedQuery<EmployeeEntity> query = em.createQuery("select e from EmployeeEntity e ", EmployeeEntity.class);
		list = query.getResultList();
		return list;
	}
	
	/**
	 * Checks if an employee is a timesheet approver.
	 * @param employeeId
	 * @return
	 */
	public boolean employeeIsTimesheetApprover(long employeeId){
	    TypedQuery<EmployeeEntity> query = em.createQuery("select e from EmployeeEntity e "
	            + "WHERE e.timesheetApprover.employeeID = :empId"
	            , EmployeeEntity.class);
	    query.setParameter("empId", employeeId);
	    List<EmployeeEntity> entityList = query.getResultList();
	    
	    return !entityList.isEmpty();
	}
}
