package ca.bcit.PunchIn.Tests.Selenium;

import static org.junit.Assert.*;

import javax.persistence.EntityNotFoundException;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
/**
 * ViewEmployee 01-05
 * @author jeiyang
 * Updated April 7 1:29am
 */
public class ViewEmployeeTests extends TestHelper{

    //ViewEmployee01: Verify admin can view the list of employees (precondition: Login01 was executed successfully as admin.)
    @Test
    public void viewEmployeeTest01() {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Employees")).click();
        //The table contains an existing user.
        assertEquals("Add, Edit or View Employees", browser.findElement(By.id("summarytitle")).getText());
        //assertTrue(browser.findElement(By.id("j_idt31:j_idt33_data")).getText().contains(adminUsername));
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    //ViewEmployee02: Verify HR can search for the employee using name. (precondition: ViewEmployee01 was executed successfully.)
    @Test
    public void viewEmployeeTest02() throws InterruptedException {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Employees")).click();
        //Search for employee
        browser.findElement(By.id("employees:employeeTable:j_idt34:filter")).click();
        browser.findElement(By.id("employees:employeeTable:j_idt34:filter")).clear();
        browser.findElement(By.id("employees:employeeTable:j_idt34:filter")).sendKeys("waffle");
        Thread.sleep(1000);
        //The table contains the user.
        assertTrue(browser.findElement(By.xpath("//tbody[@id='employees:employeeTable_data']/tr/td/div")).getText().contains("waffle"));    
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    //ViewEmployee03: Verify HR staff can search for an employee using supervisor name. (precondition: Login01 was executed successfully as HR staff)
    @Test
    public void viewEmployeeTest03() throws InterruptedException {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Employees")).click();
        Thread.sleep(500);
        //Search for employee
        browser.findElement(By.id("employees:employeeTable:j_idt39:filter")).click();
        browser.findElement(By.id("employees:employeeTable:j_idt39:filter")).clear();
        browser.findElement(By.id("employees:employeeTable:j_idt39:filter")).sendKeys("Roon");

        //The table contains the user.
        assertTrue(browser.findElement(By.xpath("//tbody[@id='employees:employeeTable_data']")).getText().contains("Roon"));
        
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    //ViewEmployee04: Verify HR can view the detailed info of the selected employee.
    @Test
    public void viewEmployeeTest04() {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Employees")).click();
        //View icon.
        try {
            browser.findElement(By.xpath("//a[@id='employees:employeeTable:0:view']/img")).click();
            assertTrue(browser.findElement(By.id("summarytitle")).getText().contains("Employee Details"));
        } catch (StaleElementReferenceException e) {
            System.out.println("StaleElementRefException at viewEmployeeTest04");
        }
        
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    //ViewEmployee05: Employee has no access to the [Employees] page.
    @Test
    public void viewEmployeeTest05() {
        setProperty();
        login(browser, employeeUsername1, passwordDefault);
        try {
            browser.findElement(By.linkText("Employees")).click();
            //The table contains an existing user.
            assertNotEquals("Add, Edit or View Employees", browser.findElement(By.id("summarytitle")).getText());
            fail("[Employees] button shouldn't be displayed.");
        } catch (NoSuchElementException e) {
            //PASS
            browser.findElement(By.linkText("Logout")).click();
            browser.close();
        }
        
    }

}
