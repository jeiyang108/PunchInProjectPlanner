/**
 * 
 */
package ca.bcit.PunchIn.DAOs;

import ca.bcit.PunchIn.Entities.Credential.CredentialEntity;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;


/**
 * @author eboow
 *
 */
@Stateless
@Dependent
public class CredentialDAO extends BaseDAO {
    
    /**
     * Default UID.
     */
    private static final long serialVersionUID = 1L;

        
	/**
	 * Save user data to database.
	 * 
	 * @param credential
	 *            employee's credential
	 */
	public void persist(CredentialEntity credential) {
		em.persist(credential);
	}

	/**
	 * Update credential data to database.
	 * 
	 * @param credential
	 *            employee's credential
	 */
	public void merge(CredentialEntity credential) {
		//CredentialEntity credentialEntity = new CredentialEntity();
		em.merge(credential);
	}

	/**
	 * Find employee entity in database.
	 * 
	 * @param username
	 *            employee's user name
	 * @return the Employee entity.
	 */
	public CredentialEntity find(String username) {
		return em.find(CredentialEntity.class, username);
	}

	/**
	 * Remove employee from database.
	 * 
	 * @param username
	 *            employee's user name.
	 */
	public void remove(String username) {

		em.remove(find(username));
	}
	
	public boolean validate(String username, String password) {
		CredentialEntity credential = find(username);
		
		if (credential != null) {
		    return credential.getPassword().equals(password);
		} 
		
		return false;
	}
}
