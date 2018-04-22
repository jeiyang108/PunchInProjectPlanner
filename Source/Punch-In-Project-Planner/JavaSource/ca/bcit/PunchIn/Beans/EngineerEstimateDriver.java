package ca.bcit.PunchIn.Beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.PunchIn.DAOs.ProjectDAO;
import ca.bcit.PunchIn.Entities.Project.ResponsibleEngineerEstimate;
import ca.bcit.PunchIn.Entities.Project.WPEstimateList;
import ca.bcit.PunchIn.Entities.Project.WorkPackageEntity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * Drives the viewing and editing 
 * of responsible engineer work package estimates.
 * @author yasharnesvaderani
 * @version 1.0
 */
@SessionScoped
@Named("estimateDriver")
public class EngineerEstimateDriver implements Serializable {
    
    /**
     * DAO for Projects.
     */
    @Inject 
    private ProjectDAO projectDAO;
    
    /**
     * Bean that handles currently logged in user.
     */
    @Inject
    private LoginBean loginBean;
    
    /**
     * List of the responsible engineer's work packages.
     */
    private List<WPEstimateList> workpackages;
    
    /**
     * The current work package selected to estimate work for.
     */
    private WorkPackageEntity currentWorkPackage;
    
    /**
     * The new estimate to add to the work package.
     */
    private ResponsibleEngineerEstimate estimateToAdd;
    
  
    /**
     * Initializes the responsible engineer's list of work packages.
     */
    @PostConstruct
    public void init() {
        workpackages = 
                projectDAO.findEngineerPackages(loginBean.getEmployeeId());
    }
    
    /**
     * Sets the selected work package to view details for
     *  and returns navigation string to details page.
     * @param wp The selected workpackge.
     * @return Navigation string to estimate details page.
     */
    public String viewEstimateDetails(WPEstimateList wp) {
        currentWorkPackage = projectDAO.findWorkPackageWithEstimates(wp);
        return "estimate_details";
    }
    
    /**
     * Returns the navigation string to the add estimate page.
     * @return Navigation string to new page for adding an estimate.
     */
    public String addEstimate() {
        estimateToAdd = new ResponsibleEngineerEstimate();
        return "add_estimate";
    }
    
    /**
     * Saves the estimate to the database and 
     * returns a navigation string to the estimate details page.
     * @return The navigation string to the estimate details page.
     */
    public String saveEstimate() {
        estimateToAdd.setEstimateDate(Calendar.getInstance());
        estimateToAdd.setWorkPackage(currentWorkPackage);
        
        projectDAO.persistEngineerEstimate(estimateToAdd);
        currentWorkPackage.getResponsibleEngineerEstimates().add(estimateToAdd);
        estimateToAdd = null;
        
        return "estimate_details";
    }


    /**
     * Returns the list of work packages for the responsible engineer.
     * @return the workpackages
     */
    public List<WPEstimateList> getWorkpackages() {
        return workpackages;
    }
    
    /**
     * Returns the currently selected work package.
     * @return The currently selected work package.
     */
    public WorkPackageEntity getCurrentWorkPackage() {
        return currentWorkPackage;
    }
    
    /**
     * Returns the estimate to add.
     * @return The estimateToAdd.
     * @return
     */
    public ResponsibleEngineerEstimate getEstimateToAdd() {
        return estimateToAdd;
    }
}
