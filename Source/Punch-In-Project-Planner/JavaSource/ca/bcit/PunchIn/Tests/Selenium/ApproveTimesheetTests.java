package ca.bcit.PunchIn.Tests.Selenium;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

/**
 * This test class includes test cases for Timesheet Approval process.
 * User should be logged in as a Timesheet Approver to go over this process.
 * @author jeiyang
 * @version 1.1
 * Updated on April 7
 */
public class ApproveTimesheetTests extends TestHelper{
    //ApproveTimesheet01: Timesheet Approver views a list of timesheets. 
    @Test
    public void approveTimesheet01viewList() {
        setProperty();
        login(browser, timesheetApproverUsername, passwordDefault);
        browser.findElement(By.linkText("Timesheets")).click();
        //A list of timesheets displayed.
        assertEquals("Timesheets Ready for Approval", browser.findElement(By.id("summarytitle")).getText());
        browser.findElement(By.linkText("Logout")).click();
        browser.close();      
    }
    //ApproveTimesheet02: Employee cannot view the Timesheets page.
    @Test
    public void approveTimesheet02ApproverOnly() {
        setProperty();
        login(browser, employeeUsername2, passwordDefault);
        browser.findElement(By.linkText("Timesheets")).click();
        //The Timesheets approval page is not displayed. The page being displayed contains the title 'Summary for the Week'
        assertNotEquals("Timesheets Ready for Approval", browser.findElement(By.id("summarytitle")).getText());
        browser.findElement(By.linkText("Logout")).click();
        browser.close();       
    }
    //ApproveTimesheet03: Timesheet Approver staff reviews the timesheet entered by employee (precondition: AddTimesheet03 as employee, Login01 as Timesheet Approver staff)
    @Test
    public void approveTimesheet03Details() throws InterruptedException {
        setProperty();
        login(browser, timesheetApproverUsername, passwordDefault);
        browser.findElement(By.linkText("Timesheets")).click();
        //Click on View icon.
        browser.findElement(By.xpath("//a[@id='j_idt33:projectTable:2:view']/img")).click();
        Thread.sleep(8000);
        //Details displayed.
        assertEquals("Unexpected result. (System might be too slow)", "Approve or Reject Timesheet", browser.findElement(By.id("summarytitle")).getText());
        browser.findElement(By.linkText("Logout")).click();
        browser.close();       
    }
    //ApproveTimesheet04: Timesheet Approver can search for timesheet with employee username.
    @Test
    public void approveTimesheet04Search() throws InterruptedException {
        setProperty();
        login(browser, timesheetApproverUsername, passwordDefault);
        browser.findElement(By.linkText("Timesheets")).click();
        browser.findElement(By.id("j_idt33:projectTable:j_idt35:filter")).click();//
        browser.findElement(By.id("j_idt33:projectTable:j_idt35:filter")).clear();
        browser.findElement(By.id("j_idt33:projectTable:j_idt35:filter")).sendKeys("cadleme");
        Thread.sleep(1000);
        assertTrue("Username Search failed.", browser.findElement(By.id("j_idt33:projectTable_data")).getText().contains(("cadleme"))); 
        browser.findElement(By.linkText("Logout")).click();
        browser.close();       
    }
    //ApproveTimesheet05: Timesheet Approver can search for timesheet with employee name.
    @Test
    public void approveTimesheet05Search() throws InterruptedException {
        setProperty();
        login(browser, timesheetApproverUsername, passwordDefault);
        browser.findElement(By.linkText("Timesheets")).click();
        browser.findElement(By.id("j_idt33:projectTable:j_idt40:filter")).click();//
        browser.findElement(By.id("j_idt33:projectTable:j_idt40:filter")).clear();
        browser.findElement(By.id("j_idt33:projectTable:j_idt40:filter")).sendKeys("Judi Varah");
        Thread.sleep(1000);
        assertTrue("Name Search failed.", browser.findElement(By.id("j_idt33:projectTable_data")).getText().contains(("Judi Varah"))); //Vousden
        browser.findElement(By.linkText("Logout")).click();
        browser.close();       
    }
    //ApproveTimesheet06: Timesheet Approver can search for timesheet with week ending.
    @Test
    public void approveTimesheet06Search() throws InterruptedException {
        setProperty();
        login(browser, timesheetApproverUsername, passwordDefault);
        browser.findElement(By.linkText("Timesheets")).click();
        browser.findElement(By.id("j_idt33:projectTable:j_idt45:filter")).click();
        browser.findElement(By.id("j_idt33:projectTable:j_idt45:filter")).clear();
        browser.findElement(By.id("j_idt33:projectTable:j_idt45:filter")).sendKeys("2017-11-24");
        Thread.sleep(1000);
        assertTrue("WeekEnding Search failed.", browser.findElement(By.id("j_idt33:projectTable_data")).getText().contains(("2017-11-24")));
        browser.findElement(By.linkText("Logout")).click();
        browser.close();       
    }
    
    
    
    
    
    
    /**
     * Manually tested due to object recognition issues with selenium.
     */
    //ApproveTimesheet07: Timesheet Approver staff approves the timesheet entered by employee (precondition: AddTimesheet03 as employee, Login01 as Timesheet Approver staff)
    
    /**
     * Manually tested due to object recognition issues with selenium.
     */
    //ApproveTimesheet08: Unupproved timesheets remain 'unapproved' in the system. (precondition: AddTimesheet03 as employee, Login01 as Timesheet Approver staff)
    
    /**
     * Manually tested due to object recognition issues with selenium.
     */
    //ApproveTimesheet09: Timesheet is not approved and returned back to the employee. (precondition: AddTimesheet03 as employee, Login01 as Timesheet Approver staff)

    
}
