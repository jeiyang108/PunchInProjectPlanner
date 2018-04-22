package ca.bcit.PunchIn.Tests.Selenium;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
/**
 * ViewTimesheet test cases(01-02)
 * Employee view their timesheet.
 * @author jeiyang
 * @version 1.1
 * Updated on April 3. 5:48 pm.
 */
public class ViewTimesheetTests extends TestHelper{

    //ViewTimesheet01: Employee can see the list of previously entered timesheets. (precondition: ApproveTimesheet01, Login as employee)
    @Test
    public void viewTimesheetTest01Display() {
        setProperty();
        login(browser, employeeUsername2, passwordDefault);
        try {
            browser.findElement(By.linkText("Timesheets")).click();
            browser.findElement(By.id("timesheet")).isDisplayed(); //Timesheet table exists in the page.
            browser.findElement(By.linkText("Logout")).click();
            System.out.println("ViewTimesheetTest01: Timesheet ID column is found! <PASS>");
            browser.close();
        } catch (NoSuchElementException ex) { 
            /* Do nothing. The link is not present. Assert is passed */    
            fail("Timesheets page is not displayed. Timesheet ID column doesn't appear in the page. <FAIL>");   
        }  
    }
    
    //ViewTimesheet02: Employee can select and view a previously entered timesheet from the [View Timesheet] page. (precodition: ViewTimesheet01)
    @Test
    public void viewTimesheetTest02Detail() {
        setProperty();
        login(browser, employeeUsername2, passwordDefault);
        try {
            browser.findElement(By.linkText("Timesheets")).click();
            browser.findElement(By.xpath("//a[@id='j_idt50:j_idt52:0:view']/img")).click(); //Click [View] button.
            browser.findElement(By.xpath("//div[@id='content']/div")).isDisplayed(); //Check if table header is displayed.
            browser.findElement(By.linkText("Logout")).click();
            System.out.println("ViewTimesheetTest02: Timesheet ID column is found! <PASS>");
            browser.close();
        } catch (NoSuchElementException ex) { 
            /* Do nothing. The link is not present. Assert is passed */    
            fail("Timesheet detail is not displayed. Table header not found. <FAIL> ");   
        }  
    }


}
