package ca.bcit.PunchIn.Tests.Selenium;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
/**
 * NEEDS UPDATE LATER
 * @author jeiyang
 * @version 1.0
 */
public class ViewReportTests extends TestHelper { 

    //ViewReport01: Project manager views weekly reports. (precondition: Login01 as Project manager)
    @Test
    public void viewReportTest01() {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Reports")).click();
        //List of reports displayed.
        assertTrue("Error: ", browser.findElement(By.id("summarytitle")).getText().contains("View Project Reports"));
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    //ViewReport02: Project manager searches for report with Project Name keyword
    @Test
    public void viewReportTest02Search() throws InterruptedException {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Reports")).click();
        //Search report
        browser.findElement(By.id("j_idt34:projectTable:j_idt36:filter")).click();
        browser.findElement(By.id("j_idt34:projectTable:j_idt36:filter")).clear();
        browser.findElement(By.id("j_idt34:projectTable:j_idt36:filter")).sendKeys("Sons");
        Thread.sleep(1000);
        assertTrue("Error: " + browser.findElement(By.id("j_idt34:projectTable_data")).getText(), browser.findElement(By.id("j_idt34:projectTable_data")).getText().contains("Sons"));
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    
    //ViewReport03: Project manager searches for report with Project Manager Name keyword
    @Test
    public void viewReportTest03Search() throws InterruptedException {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Reports")).click();
        //Search report
        browser.findElement(By.id("j_idt34:projectTable:j_idt39:filter")).click();
        browser.findElement(By.id("j_idt34:projectTable:j_idt39:filter")).clear();
        browser.findElement(By.id("j_idt34:projectTable:j_idt39:filter")).sendKeys("Claudette");
        
        Thread.sleep(1000);
        assertTrue("Error: " + browser.findElement(By.id("j_idt34:projectTable_data")).getText(), browser.findElement(By.id("j_idt34:projectTable_data")).getText().contains("Claudette"));
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    //ViewReport04: View a summary report.
    @Test
    public void viewReportTest04Sum() throws InterruptedException {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Reports")).click();
        browser.findElement(By.xpath("//a[@id='j_idt34:projectTable:0:sum']/img")).click();
        Thread.sleep(500);
        assertEquals("Reinger-Champlin Summary Report", browser.findElement(By.id("summarytitle")).getText());
        
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    //ViewReport05WP: View weekly report
    @Test
    public void viewReportTest05Weekly() throws InterruptedException {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Reports")).click();
        
        browser.findElement(By.xpath("//a[@id='j_idt34:projectTable:0:week']/img")).click();
        Thread.sleep(500);
        assertEquals("Reinger-Champlin Weekly Report", browser.findElement(By.id("summarytitle")).getText());
        
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
    //ViewReport06WP: View earned report
    @Test
    public void viewReportTest06Earn() throws InterruptedException {
        setProperty();
        login(browser, adminUsername, passwordDefault);
        browser.findElement(By.linkText("Reports")).click();

        browser.findElement(By.xpath("//a[@id='j_idt34:projectTable:0:earn']/img")).click();
        Thread.sleep(500);
        assertEquals("Reinger-Champlin Earned Value Report", browser.findElement(By.id("summarytitle")).getText());
        
        browser.findElement(By.linkText("Logout")).click();
        browser.close();
    }
}
