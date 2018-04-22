/**
 * 
 */
package ca.bcit.PunchIn.Entities.Credential;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;

/**
 * @author eboow
 *
 */

@Entity
@Table(name = "Credential")
public class CredentialEntity implements Serializable {

	@Id
	@Column(name="UserName", length=25)	
	private String userName;

	@OneToOne(optional=false)
	@JoinColumn(name="EmployeeID", columnDefinition="bigint")
	private EmployeeEntity employee;
	
	@Column(name="Password", length=255, nullable=false)
	private String password;
	
	@Column(name="Salt", length=45, nullable=false)
	private String salt;
	
	@Column(name="Role", length=30, nullable=false)
	private String role;

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the employeeID
	 */
	public EmployeeEntity getEmployee() {
		return employee;
	}

	/**
	 * @param employeeID the employeeID to set
	 */
	public void setEmployee(EmployeeEntity employee) {
		this.employee = employee;
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
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * @param salt the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}
	

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CredentialEntity [userName=" + userName + ", employeeID=" + employee + ", password=" + password
                + ", salt=" + salt + "]";
    }

}
