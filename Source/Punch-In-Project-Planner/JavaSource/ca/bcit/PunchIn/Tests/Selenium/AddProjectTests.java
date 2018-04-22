package ca.bcit.PunchIn.Tests.Selenium;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
/**
 * 
 * @author jeiyang
 * Tested manually.
 */
public class AddProjectTests extends TestHelper {

    //AddProject01: Project manager add a new project. (precondition: Login01 as project manager)
    /**
     * Manually tested due to object recognition issues with selenium.
     */
    //AddProject02: Adding a new project with missing field. (This test case should be expanded in the future, based on the input requirements.)
    /**
     * Manually tested due to object recognition issues with selenium.
     */
    //AddProject03: Adding a new project with wrong format. (This test case should be expanded in the future, based on the input requirements.)
    /**
     * Manually tested due to object recognition issues with selenium.
     */

    //AddProject04: New Project form is displayed when user press 'Add'(+) button.
    @Test
    public void addProjectTest04() {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Projects")).click();
        browser.findElement(By.xpath("//a[@id='projects:add']/img")).click(); //click add button
        try {
            assertTrue(browser.findElement(By.xpath("//table[@id='project_create_edit:j_idt34']/tbody/tr/td")).isDisplayed());
        } catch (NoSuchElementException e) {
            fail("addProjectTest04 failed: form title not found.");
        }
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    
    //AddProject05: Saving a project with no fields
    /**
     * Manually tested due to object recognition issues with selenium.
     */
    /*@Test
    public void addProjectTest05() throws InterruptedException {
    		setProperty();
    		login(browser, adminUsername, passwordDefault);
    		browser.findElement(By.linkText("Projects")).click();
    	    browser.findElement(By.xpath("//a[@id='projects:add']/img")).click(); //click add button
    	    browser.findElement(By.xpath("//a[@id='project_create_edit:save']/img")).click();
    	    try {
    	    		browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);	
    	    	
    	    		WebElement error = browser.findElement(By.xpath("//*[@id='project_create_edit:msgDialog']"));

    	    		String display = error.getCssValue("display");
    	    		
    	    		
    	    		assertEquals("block", display);
        } catch (NoSuchElementException e) {
        		fail("addProjectTest05 failed: error pop-up not found.");
        }
    	    browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }*/

}
