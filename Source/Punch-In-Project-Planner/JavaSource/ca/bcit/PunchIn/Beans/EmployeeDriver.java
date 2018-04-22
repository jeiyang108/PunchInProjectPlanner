package ca.bcit.PunchIn.Beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.PunchIn.DAOs.CredentialDAO;
import ca.bcit.PunchIn.DAOs.EmployeeDAO;
import ca.bcit.PunchIn.Models.Employee.Employee;
import java.io.Serializable;

/**
 * @author eboow
 *
 */
@SessionScoped
@Named("employeeDriver")
public class EmployeeDriver implements Serializable {
    
    /**
     * Default UID.
     */
    private static final long serialVersionUID = 1L;

	private Employee currentEmp;
	
	private String driverName = "Employee Driver";
	
	
	@Inject 
	private LoginBean loginInfo;
	
	@Inject 
	private EmployeeDAO empDAO;
	
	@Inject
    private CredentialDAO credentialDAO;

	
	@PostConstruct
	public void init() {
      //currentEmp.setBasicInfo(empDAO.find(loginInfo.getEmployeeId()));
      //currentEmp.setRoles(empDAO.getRoles(loginInfo.getEmployeeId()));
	}
	
	public void updateEmployee(Employee emp) {
		//empDAO.updateSelf(emp);
	}
	
	/**
     * @return the driverName
     */
    public String getDriverName() {
        return driverName;
    }
	
	/**
	 * @return the currentEmp
	 */
	public Employee getCurrentEmp() {
		return currentEmp;
	}

	/**
	 * @param currentEmp the currentEmp to set
	 */
	public Employee setCurrentEmp() {
	    currentEmp = new Employee();
        currentEmp.setBasicInfo(empDAO.find(loginInfo.getEmployeeId()));
        currentEmp.setCredential(credentialDAO.find(loginInfo.getUsername()));
        currentEmp.setTimesheetApprover(empDAO.employeeIsTimesheetApprover(loginInfo.getEmployeeId()));
        return currentEmp;
	}

}
