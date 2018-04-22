package ca.bcit.PunchIn.Tests.Selenium;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.Select;
/**
 * AddTimesheet(01-03)/AddTimesheetRow(01-2)/DeleteTimesheetRow(01) Tests
 * @author jeiyang
 * Updated April 1. 1am
 * MAKE SURE TO DELETE THE CURRENT WEEK"S TIMESHEET ON DATABASE.
 * DELETE FROM TimesheetDB.Timesheet where (EmployeeID = 7 OR EmployeeID = 25) and WeekEnd >= '2018-04-05'; (or today's date)
 */
public class AddTimesheetTests extends TestHelper{

    //AddTimesheet01: Verify timesheet is created when timesheet creation button is clicked.
    @Test
    public void addTimesheetTest01() throws StaleElementReferenceException{
        setProperty();
        login(browser, employeeUsername2, passwordDefault);
        try {
            browser.findElement(By.linkText("Timesheets")).click();
            addNewTimesheet();

            //Check if the new timesheet is displayed.
            browser.findElement(By.id("j_idt50")).isDisplayed();
            //browser.findElement(By.xpath("//form[@id='j_idt45']/a[2]/img")).isDisplayed(); //new row
            System.out.println("AddTimesheetTest01 <PASS>");
            
            browser.findElement(By.linkText("Logout")).click(); //link=Logout
            browser.close();
            
        } catch (NoSuchElementException ex) { 
            fail("AddTimesheetTest01 Failed." + ex);
        }  
    }
    
    //AddTimesheet02: Try creating a timesheet with invalid input.
    @Test
    public void addTimesheetTest02Invalid()  {
        setProperty();
        login(browser, employeeUsername2, passwordDefault);
        browser.findElement(By.linkText("Timesheets")).click();
        addNewTimesheet();
        addNewRow();

        browser.findElement(By.name("j_idt50:j_idt64:0:j_idt108")).click(); //Wednesday field
        browser.findElement(By.name("j_idt50:j_idt64:0:j_idt108")).clear();
        browser.findElement(By.name("j_idt50:j_idt64:0:j_idt108")).sendKeys("-3"); //Enter negative value.
        //Save button
        browser.findElement(By.xpath("//a[@id='j_idt50:save']/img")).click();
        assertTrue("Error message not displayed", browser.findElement(By.id("j_idt50:j_idt144")).getText().contains("Wednesday must be between 0 and 24"));
        //Close error msg
        browser.findElement(By.xpath("//div[@id='j_idt50:j_idt144']/div/a/span")).click();
        browser.findElement(By.linkText("Logout")).click(); //link=Logout
        browser.close();
    }
    //AddTimesheet03: Try creating a timesheet with blank input.
    @Test
    public void addTimesheetTest03Blank() {
        setProperty();
        login(browser, employeeUsername2, passwordDefault);
        browser.findElement(By.linkText("Timesheets")).click();
        addNewTimesheet();
        addNewRow();
        //Left the project number field blank to check error msg.
        browser.findElement(By.name("j_idt50:j_idt64:0:j_idt108")).click(); //Wednesday field
        browser.findElement(By.name("j_idt50:j_idt64:0:j_idt108")).clear();
        browser.findElement(By.name("j_idt50:j_idt64:0:j_idt108")).sendKeys("3"); //Enter negative value.
        //Save button.
        browser.findElement(By.xpath("//a[@id='j_idt50:save']/img")).click();
        assertTrue("Error message not displayed", browser.findElement(By.id("j_idt50:j_idt144")).getText().contains("Project Number must not be empty."));
        //Close error msg
        browser.findElement(By.xpath("//div[@id='j_idt50:j_idt144']/div/a/span")).click();
        browser.findElement(By.linkText("Logout")).click(); //link=Logout
        browser.close();
    }
    
    
    //AddTimesheet04: Timesheet is created and sent to a Timesheet Approver staff. 
    /**
     * Manually tested due to object recognition issues with Selenium.
     */
    //PRECONDITION: if a timesheet for the week has already been created, you must delete the record before running the test.
    //DELETE FROM TimesheetDB.Timesheet where EmployeeID = 7 and WeekEnd >= '2018-04-05'(or today);
    /*
     @Test
    public void addTimesheetTest04() throws StaleElementReferenceException {
        setProperty();
        login(browser, employeeUsername1, passwordDefault);
        browser.findElement(By.linkText("Timesheets")).click();
        
        addNewTimesheet();
        addNewRow();
        //Enter valid inputs
        //45:59 -> 47:61
        new Select(browser.findElement(By.id("j_idt50:j_idt64:0:projectMenu"))).selectByVisibleText("Kautzer Inc");
        //new Select(browser.findElement(By.name("j_idt50:j_idt64:0:j_idt78"))).selectByValue("DDPL"); //Work Package
        Select ddpl = new Select(browser.findElement(By.id("j_idt50:j_idt64:0:j_idt78")));
        ddpl.selectByVisibleText("DDPL");
        //browser.findElement(By.xpath("//option[@value='DDPL']")).click();
        //Tuesday 3
        browser.findElement(By.name("j_idt50:j_idt64:0:j_idt96")).click();
        browser.findElement(By.name("j_idt50:j_idt64:0:j_idt96")).clear();
        browser.findElement(By.name("j_idt50:j_idt64:0:j_idt96")).sendKeys("3");
        //Note: Test
        browser.findElement(By.name("j_idt50:j_idt64:0:j_idt132")).click();
        browser.findElement(By.name("j_idt50:j_idt64:0:j_idt132")).clear();
        browser.findElement(By.name("j_idt50:j_idt64:0:j_idt132")).sendKeys("AddTimesheetTest04");
        //Click on Save
        browser.findElement(By.xpath("//a[@id='j_idt50:save']/img")).click();
        //View the newly added timesheet.
        browser.findElement(By.linkText("Timesheets")).click();
        //Path can be By.xpath("//div[@id='timesheet']/table/tbody/tr/td[6]/a/img"). Change the value if the test fails. tr[2]
        browser.findElement(By.xpath("//div[@id='timesheet']/table/tbody/tr[2]/td[6]/a/img")).click(); ////
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Check if the Total hour is 3.00
        //System.out.println("Content: " + browser.findElement(By.id("timesheet")).getText());
        assertTrue("Unexpected value in total field", browser.findElement(By.id("content")).getText().contains("3.00"));
        //assertTrue("Unexpected value in project field", browser.findElement(By.xpath("id='j_idt50:j_idt64:0:j_idt132'")).getText().contains("AddTimesheetTest04"));
        
        

         
             //EditTimesheetRow Part
         

        browser.findElement(By.linkText("Timesheets")).click();
        //View Timesheet
        //Path can be By.xpath("//div[@id='timesheet']/table/tbody/tr/td[6]/a/img"). Change the value if the test fails.
        browser.findElement(By.xpath("//div[@id='timesheet']/table/tbody/tr/td[6]/a/img")).click();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Note: Test
        browser.findElement(By.name("j_idt50:j_idt64:0:j_idt132")).click();
        browser.findElement(By.name("j_idt50:j_idt64:0:j_idt132")).clear();
        browser.findElement(By.name("j_idt50:j_idt64:0:j_idt132")).sendKeys("AddTimesheetRowTest02");
        //Click on Save
        browser.findElement(By.xpath("//a[@id='j_idt50:save']/img")).click();
        //View Timesheet
        //Path can be By.xpath("//div[@id='timesheet']/table/tbody/tr/td[6]/a/img"). Change the value if the test fails.
        browser.findElement(By.linkText("Timesheets")).click();
        browser.findElement(By.xpath("//div[@id='timesheet']/table/tbody/tr/td[6]/a/img")).click();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Check if Note="Test"
        assertEquals("Unexpected value in Note field", browser.findElement(By.id("j_idt50:j_idt64:0:j_idt132")).getAttribute("value"), "AddTimesheetRowTest02");
        

         
             //DeleteTimesheetRow Part
         

        browser.findElement(By.linkText("Timesheets")).click();
        //View Timesheet
        browser.findElement(By.xpath("//div[@id='timesheet']/table/tbody/tr/td[6]/a/img")).click();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Save the data before delete
        String prj = browser.findElement(By.id("j_idt50:j_idt64:0:projectMenu")).getAttribute("value");
        //browser.findElement(By.xpath("//option[@value='1']")).click(); 
        String wp = browser.findElement(By.name("j_idt50:j_idt64:0:j_idt78")).getAttribute("value");
        System.out.println(prj + " - " + wp);
        //Delete button. 
        browser.findElement(By.xpath("//div[@id='timesheet']/table/tbody/tr/td[11]/a/img")).click();
        
        //Check if the timesheet row still exists or not.
        assertNotEquals("Project still exists: DeleteTimesheetRow01 fails.", browser.findElement(By.xpath("//div[@id='timesheet']/table/tbody/tr/td[2]")).getAttribute("value"), prj);
        
        
        browser.findElement(By.linkText("Logout")).click(); //link=Logout
        browser.close();

    }
     
     */
    
    
    
    
    //AddTimesheetRow01: Verify new row is created when time sheet row button is clicked.
    @Test
    public void addTimesheetRowTest01newTimesheet() {
        setProperty();
        login(browser, employeeUsername2, passwordDefault);
        try {
            browser.findElement(By.linkText("Timesheets")).click();
            addNewTimesheet();
            addNewRow();
            //Check if an empty row is created.
            //assertEquals("Fail ProjectNo", browser.findElement(By.id("j_idt45:j_idt59:0:projectMenu")).getAttribute("value"), "");
            assertEquals("Fail Sun", browser.findElement(By.name("j_idt50:j_idt64:0:j_idt90")).getAttribute("value"), "0");
            assertEquals("Fail Mon", browser.findElement(By.name("j_idt50:j_idt64:0:j_idt96")).getAttribute("value"), "0");

            browser.findElement(By.linkText("Logout")).click();
            browser.close();
        } catch (NoSuchElementException ex) { 
            fail("AddTimesheetTest01 Failed.");
        }  
    }
    
    
    public void addNewTimesheet() {
        browser.findElement(By.xpath("//a[@id='j_idt50:add']/img")).click(); //Add a new timesheet 
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void addNewRow() {
        browser.findElement(By.id("j_idt50:add")).click(); //Add a new timesheet row 
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
