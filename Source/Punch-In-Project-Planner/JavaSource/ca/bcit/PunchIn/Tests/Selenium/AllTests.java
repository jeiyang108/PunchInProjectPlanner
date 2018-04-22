package ca.bcit.PunchIn.Tests.Selenium;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author jeiyang
 * @version 1.1
 * Update: April 7.
 * 
 * Make sure to update DB with the following scripts to delete exisitng records before re-running test cases.
 * 
DELETE FROM timesheetDB.Employee WHERE firstname = 'John' AND lastname = 'wildsmith';
DELETE FROM TimesheetDB.Timesheet where (EmployeeID = 7 OR EmployeeID = 25) and WeekEnd >= '2018-04-05'; 
 *
 */
@RunWith(Suite.class)

/*
 * Please check the 'TestLog' documentation to see the detailed information regarding expected test results,
 * actual outputs, and test items that are manually tested.
 */
@SuiteClasses({ LoginTests.class, LogoutTests.class, AddEmployeeTests.class, AddProjectTests.class, AddTimesheetTests.class, 
    ViewEmployeeTests.class, ViewProjectTests.class, ViewReportTests.class, ViewTimesheetTests.class})
public class AllTests {

}
