package ca.bcit.PunchIn.Tests.Selenium;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.Select;

/**
 * EditEmployee01-02
 * AssignEmployee01-02
 * 
 * @author jeiyang
 * @version 1.0
 * Updated April 7. 2:13am
 */
public class EditEmployeeTests extends TestHelper {
    
    /**
     * Manually tested due to object recognition issues with selenium.
     */
    /*
     
     
    //EditEmployee01: Verify admin can edit an employee. Edit page is displayed. (precondition: Login01 was executed successfully as admin.)
    @Test
    public void editEmployeeTest01() throws InterruptedException, StaleElementReferenceException {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Employees")).click();
        
        //Search for employee
        searchName("Roon");
        
        //View icon.
        browser.findElement(By.xpath("//a[@id='employees:employeeTable:0:view']/img")).click();
        //Edit icon.
        browser.findElement(By.xpath("//a[@id='editEmployee:edit']/img")).click();
        //Page is displayed.
        assertTrue(browser.findElement(By.id("summarytitle")).getText().contains("Edit Employee"));
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    
    
    //EditEmployee02: Employee info is edited after 'save'. (FirstName)
    @Test
    public void editEmployeeTest02() throws InterruptedException {
        String previousName;
        
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Employees")).click();
        
        //Search for employee
        searchName("Roon"); 
        
        previousName = editFirstName("Chaddie");
        Thread.sleep(4000);
        
        //Search for the employee and assert.
        searchName("Chaddie");

        //Check if name has been changed from Chadd to Chaddie.
        //assertEquals(previousName, "Chadd");
        assertTrue(browser.findElement(By.xpath("//tbody[@id='employees:employeeTable_data']/tr/td[2]/div")).getText().contains("Chaddie"));
        
        //Change back to the previous name.
        editFirstName("Chadd");
        
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    //AssignEmployee01: HR can assign employee to a manager. (precondition: Login01 as HR staff)
    @Test
    public void assignEmployeeTest01() throws InterruptedException, StaleElementReferenceException {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Employees")).click();
        
        //Search for employee (Cori Boch)
        searchName("Cori");
        
        //View icon.
        browser.findElement(By.xpath("//a[@id='employees:employeeTable:0:view']/img")).click();
        //Edit icon.
        browser.findElement(By.xpath("//a[@id='editEmployee:edit']/img")).click();
        
        //Page is displayed.
        
        //Manager(Supervisor)
        browser.findElement(By.id("editEmployee:empManagerSelector")).click();
        new Select(browser.findElement(By.id("editEmployee:empManagerSelector"))).selectByVisibleText("Isaac Rafe");
        browser.findElement(By.xpath("//option[@value='Isaac Rafe']")).click();

        //Save.
        browser.findElement(By.xpath("//a[@id='editEmployee:save']/img")).click();
        Thread.sleep(4000);
        searchName("Cori");
        //assert emp manager
        assertTrue(browser.findElement(By.xpath("//tbody[@id='employees:employeeTable_data']/tr/td[3]/div")).getText().contains("Isaac Rafe"));
        
        ////////BACK to previous data
        //View icon.
        browser.findElement(By.xpath("//a[@id='employees:employeeTable:0:view']/img")).click();
        //Edit icon.
        browser.findElement(By.xpath("//a[@id='editEmployee:edit']/img")).click();
        //Manager
        browser.findElement(By.id("editEmployee:empManagerSelector")).click();
        new Select(browser.findElement(By.id("editEmployee:empManagerSelector"))).selectByVisibleText("Chadd Roon");
        //browser.findElement(By.xpath("//option[@value='Chadd Roon']")).click();

        //Save.
        browser.findElement(By.xpath("//a[@id='editEmployee:save']/img")).click();
        
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    
    //AssignEmployee02: HR can assign employee to timesheet approver.
    @Test
    public void assignEmployeeTest02() throws InterruptedException, StaleElementReferenceException {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Employees")).click();
        
        //Search for employee (Cori Boch)
        searchName("Cori");
        
        //View icon.
        browser.findElement(By.xpath("//a[@id='employees:employeeTable:0:view']/img")).click();
        //Edit icon.
        browser.findElement(By.xpath("//a[@id='editEmployee:edit']/img")).click();
        //Page is displayed.
        
        //Timesheet approver
        browser.findElement(By.id("editEmployee:empTimesheetSelector")).click();
        new Select(browser.findElement(By.id("editEmployee:empTimesheetSelector"))).selectByVisibleText("Shannon Lawrenson");
        browser.findElement(By.xpath("(//option[@value='Shannon Lawrenson'])[2]")).click();
        //Save.
        browser.findElement(By.xpath("//a[@id='editEmployee:save']/img")).click();
        Thread.sleep(4000);
        searchName("Cori");
        // assert TS approver
        browser.findElement(By.xpath("//tbody[@id='employees:employeeTable_data']/tr/td[4]/div")).getText().contains("Shannon Lawrenson");
        
        ///////BACK to original data
        //View icon.
        browser.findElement(By.xpath("//a[@id='employees:employeeTable:0:view']/img")).click();
        //Edit icon.
        browser.findElement(By.xpath("//a[@id='editEmployee:edit']/img")).click();
        //Page is displayed.
        //Timesheet approver
        browser.findElement(By.id("editEmployee:empTimesheetSelector")).click();
        new Select(browser.findElement(By.id("editEmployee:empTimesheetSelector"))).selectByVisibleText("Leroy Padgett");
        //browser.findElement(By.xpath("(//option[@value='Shannon Lawrenson'])[2]")).click();
        //Save.
        browser.findElement(By.xpath("//a[@id='editEmployee:save']/img")).click();
        
        //browser.findElement(By.linkText("Logout")).click();
        //browser.close();
    }
    
    */
    
    
    /**
     * 
     * @param newName
     * @return previous name.
     * @throws InterruptedException 
     */
    private String editFirstName(String newName) throws InterruptedException {
      //View icon.
        browser.findElement(By.xpath("//a[@id='employees:employeeTable:0:view']/img")).click();
        System.out.println("StaleElementRefException at editEmployeeTest01");
        //Edit icon.
        browser.findElement(By.xpath("//a[@id='editEmployee:edit']/img")).click();
        //Page is displayed.
        assertTrue(browser.findElement(By.id("summarytitle")).getText().contains("Edit Employee"));
        
        
        String previousName = browser.findElement(By.id("editEmployee:j_idt49")).getAttribute("value");
        
        //Edit Employee Info.
        browser.findElement(By.id("editEmployee:j_idt49")).click(); //First Name
        browser.findElement(By.id("editEmployee:j_idt49")).clear();
        browser.findElement(By.id("editEmployee:j_idt49")).sendKeys(newName); 

        //Save.
        browser.findElement(By.xpath("//a[@id='editEmployee:save']/img")).click();
        Thread.sleep(500);
        //Will be used for assertion.
        return previousName;
    }
    
    private void searchName(String name) throws InterruptedException {
        browser.findElement(By.id("employees:employeeTable:j_idt39:filter")).click();
        browser.findElement(By.id("employees:employeeTable:j_idt39:filter")).clear();
        browser.findElement(By.id("employees:employeeTable:j_idt39:filter")).sendKeys(name);
        Thread.sleep(500);
    }
      
      
     
     
    
}
