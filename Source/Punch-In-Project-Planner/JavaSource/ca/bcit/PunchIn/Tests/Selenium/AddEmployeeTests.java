package ca.bcit.PunchIn.Tests.Selenium;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.Select;

/**
 * PRECONDITION: Delete the Employee with the following script from the Database!
 * DELETE FROM timesheetDB.Employee WHERE firstname = 'John' AND lastname = 'wildsmith';
 * @author jeiyang
 * @version 1.0
 * Updated April 7 4am
 */
public class AddEmployeeTests extends TestHelper {
    

    //AddEmployee01: Click [Add Employee]. The user is directed to the page where the user can add employees. 
    @Test
    public void addEmployee01() throws InterruptedException, StaleElementReferenceException {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Employees")).click();
        //Add icon.
        browser.findElement(By.xpath("//a[@id='employees:add']/img")).click();
        Thread.sleep(2000);
        assertTrue(browser.findElement(By.id("summarytitle")).getText().contains("Edit Employee"));
        //Logout
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    
    
    
    
    
    
    
    
    
    
    /**
     * Manually tested due to object recognition issues with selenium.
     */
    //AddEmployee02: Enter new employee name and credentials into the input boxes. Click [Save]. 
    //The new employee is added to the database.
    // Run the following script before running this test.
    // DELETE FROM timesheetDB.Employee WHERE firstname = 'John' AND lastname = 'wildsmith';
    
    /*
     
      @Test
    public void addEmployee02() throws InterruptedException {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Employees")).click();
        //Add icon.
        browser.findElement(By.xpath("//a[@id='employees:add']/img")).click();
       
        //Enter info
        browser.findElement(By.id("editEmployee:j_idt39")).click();
        browser.findElement(By.id("editEmployee:j_idt39")).clear();
        browser.findElement(By.id("editEmployee:j_idt39")).sendKeys("1234"); //ID
        browser.findElement(By.id("editEmployee:j_idt44")).clear();
        browser.findElement(By.id("editEmployee:j_idt44")).sendKeys("jwildsmith"); //Username
        browser.findElement(By.id("editEmployee:j_idt49")).clear();
        browser.findElement(By.id("editEmployee:j_idt49")).sendKeys("John"); //Firstname
        browser.findElement(By.id("editEmployee:j_idt54")).clear();
        browser.findElement(By.id("editEmployee:j_idt54")).sendKeys("Wildsmith"); //Lastname
        browser.findElement(By.id("editEmployee:empManagerSelector")).click();
        new Select(browser.findElement(By.id("editEmployee:empManagerSelector"))).selectByVisibleText("Robbin Kanzler");
        //browser.findElement(By.xpath("//option[@value='Robbin Kanzler']")).click();
        //Save icon.
        browser.findElement(By.xpath("//a[@id='editEmployee:save']/img")).click();
        Thread.sleep(3000);
        browser.findElement(By.id("employees:employeeTable:j_idt34:filter")).click();
        browser.findElement(By.id("employees:employeeTable:j_idt34:filter")).clear();
        browser.findElement(By.id("employees:employeeTable:j_idt34:filter")).sendKeys("jwildsmith");
        
        Thread.sleep(500);
        assertTrue(browser.findElement(By.xpath("//tbody[@id='employees:employeeTable_data']/tr/td/div")).getText().contains("jwildsmith"));
        
        //Logout
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
     
     */
    
 
}
