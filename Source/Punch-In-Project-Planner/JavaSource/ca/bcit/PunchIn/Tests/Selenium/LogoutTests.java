
package ca.bcit.PunchIn.Tests.Selenium;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
/**
 * Logout related test cases. (LogoutTest01~05)
 * @author jeiyang
 * @version 1.0
 * Tested on April 3. 5:40 pm
 */
public class LogoutTests extends TestHelper{

    //Logout01: Logout button not accessible by non-user.
    @Test
    public void logoutTest01Nonuser() {
        setProperty();
        browser.get(url);  
        try {
            browser.findElement(By.linkText("Logout"));
            fail("Link with text <Logout> is present");
        } catch (NoSuchElementException ex) { 
            /* Do nothing. The link is not present. Assert is passed */
            System.out.println("LogoutTest01: Catched the exception! <PASS>");
            browser.close();
        }  
    }
    //Logout02: Logout button not rendered after user logs out of the system.
    @Test
    public void logoutTest02ButtonRendered() {
        setProperty();
        //WebDriver browser = new ChromeDriver();
        login(browser, employeeUsername1, passwordDefault);
        browser.findElement(By.linkText("Logout")).click();
        /*The user is logged out*/
        try {
            browser.findElement(By.linkText("Logout"));
            fail("Link with text <Logout> is present");        
        } catch (NoSuchElementException ex) { 
            /* Do nothing. The link is not present. Assert is passed */ 
            System.out.println("LogoutTest02: Catched the exception! <PASS>");
            browser.close();
        }
    }
    //Logout03: Logging out redirects page to login page.
    @Test
    public void logoutTest03RedirectPage() {
        setProperty();
        //WebDriver browser = new ChromeDriver();
        login(browser, employeeUsername1, passwordDefault);
        browser.findElement(By.linkText("Logout")).click();
        /*The user is logged out and should be redirected to login page.*/
        //The main logo is displayed before log in.
        assertTrue("logoutTest03: Main page logo not found. User may be logged into the system.", browser.findElement(By.xpath("//div[@id='login']/img")).isDisplayed());
        browser.close();
    }

    //Logout05: User-related pages do not render when accessed by non-user.
    @Test
    public void logoutTest05UserOnlyPage() {
        setProperty();
        browser.get(url); 
        login(browser, employeeUsername1, passwordDefault);
        browser.findElement(By.linkText("Logout")).click();  
        try {
            browser.get("http://localhost:8080/Punch-In-Project-Planner/faces/sections/employee/timesheets.xhtml");
            browser.findElement(By.id("summarytitle"));
            fail("The page is not supposed to be accessed by non-user.");
        } catch (NoSuchElementException ex) { 
            /* Do nothing. The link is not present. Assert is passed */
            System.out.println("LogoutTest05: Catched the exception! <PASS>");         
            browser.close();
        }  
    }
}
