package ca.bcit.PunchIn.DAOs;


import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author eboow
 *
 */
public class BaseDAO implements Serializable {
    /**
     * Default UID.
     */
    private static final long serialVersionUID = 1L;

	/** Entity Manager. **/
	@PersistenceContext(unitName = "PunchIn-jpa")
	protected EntityManager em;
}
