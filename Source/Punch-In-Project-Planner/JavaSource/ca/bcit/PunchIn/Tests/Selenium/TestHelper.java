package ca.bcit.PunchIn.Tests.Selenium;

import java.io.File;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * TestHelper.
 * @author jeiyang
 * @version 1.1
 * TestHelper class contains setPropety and login methods which can be used in any test.
 * Modify usernames and default password that will be used in test cases.
 */
public class TestHelper {
    
    /** 
     * Modify the username and default password below. 
     * 
     */
  
    String employeeUsername1 = "estienham6";
    String employeeUsername2 = "jjanmano"; 
    String adminUsername = "admin"; //Master Admin
    String timesheetApproverUsername = "doggo";
    String projectManagerUsername = "koi"; //for Timesheet approval / Project Management process
    String responsibleEngineerUsername = "ghost"; //for Estimates
    String hrUsername = "pebbles"; //HR
    String passwordDefault = "password";
    
    /**
     * Browser and URL of the index page.
     */
    WebDriver browser;
    String url = "http://localhost:8080/Punch-In-Project-Planner/";
    
    /**
     * Set Property
     */
	public void setProperty() {
		File file = new File("Resources/chromedriver");
		String filePath = file.getAbsolutePath();
		System.setProperty("webdriver.chrome.driver", filePath);
		browser = new ChromeDriver();
	}
	
	/**
	 * 
	 * @param browser WebDriver such as Chrome or FireFox.
	 * @param username Username used to log into the system.
	 * @param password Password of the user.
	 */
	public void login(WebDriver browser, String username, String password) {
	    
		browser.get(url);
		browser.findElement(By.xpath("//*[@id='j_idt11:username']")).sendKeys(username); //id=j_idt11:username
		browser.findElement(By.xpath("//*[@id='j_idt11:password']")).sendKeys(password);
		browser.findElement(By.xpath("//*[text()='Login']")).click();
	}
	
}
