package ca.bcit.PunchIn.Tests.Selenium;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
/**
 * @version 1.1
 * @author jeiyang
 * Updated April 3 4:55pm
 */
public class ViewProjectTests extends TestHelper {

    //ViewProjects01: Manager views a list of projects.
    @Test
    public void viewProjectTest01() {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Projects")).click();
        assertTrue(browser.findElement(By.id("projects:projectTable_data")).getText().contains("TODO"));
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    //ViewProjects02: Other users cannot see the list of projects.
    @Test
    public void viewProjectTest02() {
        setProperty();
        login(browser, employeeUsername1, passwordDefault);

        //browser.findElement(By.linkText("Projects")).click();
        assertFalse("Projects tab shouldn't be accessible by employee", browser.findElement(By.id("j_idt9")).getText().contains("Projects"));

        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    //ViewProjects03: Search for a project using name.
    @Test
    public void viewProjectTest03() {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Projects")).click();
        
        browser.findElement(By.id("projects:projectTable:j_idt34:filter")).click();
        browser.findElement(By.id("projects:projectTable:j_idt34:filter")).clear();
        browser.findElement(By.id("projects:projectTable:j_idt34:filter")).sendKeys("Kautzer");
        assertTrue(browser.findElement(By.id("projects:projectTable_data")).getText().contains(("Kautzer")));
        
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    //ViewProjects04: Search for a project using project manager.
    @Test
    public void viewProjectTest04() {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Projects")).click();
        
        browser.findElement(By.id("projects:projectTable:j_idt39:filter")).click();
        browser.findElement(By.id("projects:projectTable:j_idt39:filter")).clear();
        browser.findElement(By.id("projects:projectTable:j_idt39:filter")).sendKeys("Claudette");
        assertTrue(browser.findElement(By.id("projects:projectTable_data")).getText().contains(("Claudette")));
        
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    //ViewProjects05: Display details of the selected project.
    @Test
    public void viewProjectTest05() throws InterruptedException {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Projects")).click();
        
        browser.findElement(By.xpath("//tbody[@id='projects:projectTable_data']/tr/td[7]/a/img")).click();
        Thread.sleep(1000);
        assertTrue(browser.findElement(By.id("project_details")).getText().contains(("Project Details")));
        
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }

}
