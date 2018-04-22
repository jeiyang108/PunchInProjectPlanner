package ca.bcit.PunchIn.Beans;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.PunchIn.PasswordHash;
import ca.bcit.PunchIn.DAOs.CredentialDAO;
import ca.bcit.PunchIn.DAOs.EmployeeDAO;
import ca.bcit.PunchIn.Entities.Credential.CredentialEntity;
import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;
import ca.bcit.PunchIn.Models.Employee.Employee;

@SessionScoped
@Named
public class HrManagementDriver implements Serializable {
	
	/**
	 * Default UID.
	 */
	private static final long serialVersionUID = 1L;
	
	private Employee currEmp; 
	private String newUserName;
	private String newPassword;
	private List<Employee> allEmployees;
	private List<EmployeeEntity> allEmployeeEntities;
	private List<Employee> filteredEmployees;

	@Inject 
	private EmployeeDAO empDAO;
	
	@Inject
	private CredentialDAO credDAO;
	
	@Inject 
	public HrManagementDriver(EmployeeDriver empDriver) {
		currEmp = empDriver.getCurrentEmp();
	}
	
	public HrManagementDriver() {
		
	}
	
	
	
	/**
	 * View an existing employee.
	 * @param emp The employee to view.
	 * @return String to redirect to the view employee page.
	 */
	public String viewEmployee(Employee emp) {
		currEmp = emp;
		return "viewEmployee";
	}

	/**
	 * Once an employee is edited or created, persist them by giving them a role and 
	 * @param emp
	 * @return
	 */
	public String submitEmployee(Employee emp) {
		//If setting new username.
		if(!newUserName.isEmpty())
		{
			//If new username is not in database and employee already has existing username.
			if(credDAO.find(newUserName) == null && !emp.getBasicInfo().getCredentials().getUserName().isEmpty())
			{
				//Remove old one.
				credDAO.remove(emp.getBasicInfo().getCredentials().getUserName());
			}
			//Set new username.
			emp.getBasicInfo().getCredentials().setUserName(newUserName);
			newUserName="";
		}
		
		if(!newPassword.isEmpty())
		{
			try {
				byte[] salt = PasswordHash.getSalt();
				String hashedPassword = PasswordHash.getSecurePassword(newPassword, salt);
				String saltString = Base64.getEncoder().encodeToString(salt);
				emp.getBasicInfo().getCredentials().setSalt(saltString);
				emp.getBasicInfo().getCredentials().setPassword(hashedPassword);
			} catch (NoSuchAlgorithmException e) {
				System.err.println("Failed to salt.");
			}
			newPassword="";
		}
		//Check if employee already exists.
		if (empDAO.find(emp.getBasicInfo().getEmployeeID()) == null)
		{
			createEmployee(emp);
		} else {
			updateEmployee(emp);
		}
		if(allEmployees.indexOf(emp) == -1)
		{
			allEmployees.add(emp);
		}
		//If credentials aren't found then it is a new employee. 
		if(credDAO.find(emp.getBasicInfo().getCredentials().getUserName()) == null)
		{
			//Create credentials for new employee.
			credDAO.persist(emp.getBasicInfo().getCredentials());
		} else {
			//Update credentials for existing employee.
			credDAO.merge(emp.getBasicInfo().getCredentials());
		}
		
		return "employees";
	}
	
	/**
	 * Edit an existing employee.
	 * @return String to redirect to the edit employee page with an already existing employee.
	 */
	public String editEmployee(Employee emp) {
		currEmp = emp;
		return "editEmployee";
	}
	
	/**
	 * Create a new employee.
	 * @return String to redirect to the edit employee page with an empty new employee.
	 */
	public String addEmployee() {
		currEmp = new Employee();
		currEmp.setBasicInfo(new EmployeeEntity());
		
		//Set credentials for employee and it's entity.
		currEmp.setCredential(new CredentialEntity());
		currEmp.getCredential().setRole("Employee");
		currEmp.getBasicInfo().setCredentials(currEmp.getCredential());
		currEmp.getBasicInfo().setActive(true);
		currEmp.getCredential().setEmployee(currEmp.getBasicInfo());
		currEmp.getCredential().setSalt("");
		currEmp.getCredential().setPassword("password");
		currEmp.getCredential().setUserName("");
		
		return "editEmployee";
	}
	
	/**
	 * @return the currEmp
	 */
	public Employee getCurrEmp() {
		return currEmp;
	}

	/**
	 * @param currEmp the currEmp to set
	 */
	public void setCurrEmp(Employee currEmp) {
		this.currEmp = currEmp;
	}
	
	public void createEmployee(Employee emp) {
		empDAO.persist(emp);
	}
	
	public void updateEmployee(Employee emp) {
		empDAO.merge(emp);
	}
	
	public void deleteEmployee(Employee emp) {
		empDAO.delete(emp);
	}

	public List<Employee> getEmployees(){
		if(allEmployees == null)
		{
			allEmployees = new ArrayList<Employee>();
			allEmployees.addAll(empDAO.getEmployees());
		}
		return allEmployees;
	}
	
	public List<EmployeeEntity> getEmployeeEntities() 
	{
		allEmployeeEntities = new ArrayList<EmployeeEntity>();
		List<Employee> employees = getEmployees();
		for(Employee e : employees)
		{
			allEmployeeEntities.add(e.getBasicInfo());
		}
		return allEmployeeEntities;
	}
	
	public void setEmployees(List<Employee> employees) {
		allEmployees = employees;
	}
	
	public List<Employee> getFilteredEmployees() {
		return filteredEmployees;
	}
	
	public void setFilteredEmployees(List<Employee> filteredEmployees) {
		this.filteredEmployees = filteredEmployees;
	}


	public String getNewUserName() {
		return currEmp.getBasicInfo().getCredentials().getUserName();
	}


	public void setNewUserName(String newUserName) {
		this.newUserName = newUserName;
	}


	public String getNewPassword() {
		return newPassword;
	}


	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}