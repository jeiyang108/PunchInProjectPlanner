package ca.bcit.PunchIn.Entities.Project;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class WorkPackageId implements Serializable {
    
    /**
	 * Default UID.
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="WorkpackageID", columnDefinition="varchar", length=10)
    private String workpackageID;
	
	@Column(name="ProjectNum")
    private int projectNum;
    
    public String getWorkpackageID() {
        return workpackageID;
    }
    
    public void setWorkpackageID(String workpackageID) {
        this.workpackageID = workpackageID;
    }
    
    public int getProjectNum() {
        return projectNum;
    }
    
    public void setProjectNum(int projectNum) {
        this.projectNum = projectNum;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + projectNum;
        result = prime * result + ((workpackageID == null) ? 0 : workpackageID.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WorkPackageId other = (WorkPackageId) obj;
        if (projectNum != other.projectNum)
            return false;
        if (workpackageID == null) {
            if (other.workpackageID != null)
                return false;
        } else if (!workpackageID.equals(other.workpackageID))
            return false;
        return true;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WorkPackageId [workpackageID=" + workpackageID + ", projectNum=" + projectNum + "]";
	}
    
    
}
