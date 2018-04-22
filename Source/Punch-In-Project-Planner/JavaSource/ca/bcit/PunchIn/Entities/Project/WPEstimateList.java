package ca.bcit.PunchIn.Entities.Project;

import java.util.Date;

public class WPEstimateList {
    
    private Date date;

    private String title;
    
    private String wpNum;
    
    private String projectName;
    
    private int projectNum;
    
    public WPEstimateList() {
    }
    
    public WPEstimateList(Date date, String title, String wpNum, String projectName, int projectNum) {
        this.date = date;
        this.title = title;
        this.wpNum = wpNum;
        this.projectName = projectName;
        this.projectNum = projectNum;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the wpNum
     */
    public String getWpNum() {
        return wpNum;
    }

    /**
     * @param wpNum the wpNum to set
     */
    public void setWpNum(String wpNum) {
        this.wpNum = wpNum;
    }

    /**
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public int getProjectNum() {
        return projectNum;
    }
}
