package ca.bcit.PunchIn.Beans;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import ca.bcit.PunchIn.PasswordHash;
import ca.bcit.PunchIn.DAOs.CredentialDAO;
import ca.bcit.PunchIn.DAOs.ProjectDAO;
import ca.bcit.PunchIn.Entities.Credential.CredentialEntity;
import ca.bcit.PunchIn.Models.Employee.Employee;

import java.io.Serializable;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;


@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private Long employeeId;
    private String username;
    private String password;
    private Employee currEmp;
    private Boolean isPM;
    private Boolean isRE;
    
    @Inject 
    private CredentialDAO credentialDAO;
    
    @Inject 
    private ProjectDAO projDAO;
    
    @Inject
    private EmployeeDriver empDriver;
    
    @Inject
    HttpServletRequest request;


    public LoginBean() {
    }

    /**
     * @return the employeeId
     */
    public Long getEmployeeId() {
        return employeeId;
    }
    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String newValue) {
        username = newValue;
    }
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Checks if employee is a project manager.
     */
    public boolean isProjectManager() {
    	if (isPM == null) {
    		isPM = new Boolean(false);
    		if (currEmp.isAdmin()) {
    			isPM = true;
    		} else {
    			isPM = projDAO.isProjectManager(currEmp.getBasicInfo());
    		}
    	}
    	return isPM;
    }
    
    /**
     * @param password the password to set
     */
    public boolean isResponsibleEngineer() {
        if (isRE == null) {
            isRE = new Boolean(false);
            if (currEmp.isAdmin()) {
                isRE = true;
            } else {
                isRE = projDAO.isResponsibleEngineer(currEmp.getBasicInfo());
            }
        }
        return isRE;
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

	public String login() {
        CredentialEntity credentials = credentialDAO.find(username); 
        
        if (credentials == null) {
            return null;
        }
        
        // user is already logged in
        if (currEmp != null) {
            return "sections/employee/workPackages.xhtml?faces-redirect=true";
        }
        
        try {
            String hashedPassword = PasswordHash.getSecurePassword(password,
                    DatatypeConverter.parseBase64Binary(credentials.getSalt()));
            
            request.login(username, hashedPassword);
            
            if (request.authenticate((HttpServletResponse)FacesContext.
                    getCurrentInstance().getExternalContext().getResponse())) {
            	
                employeeId = credentials.getEmployee().getEmployeeID();
                currEmp = empDriver.setCurrentEmp();
                
                if (currEmp.getBasicInfo().isActive()) {
                    return "sections/employee/workPackages.xhtml?faces-redirect=true";
                } else {
                    return null;
                }
            } else {
            		return null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String logout() {
        
        try {
            request.getSession().invalidate();
            return "logout";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "logout";
    }

    
}
