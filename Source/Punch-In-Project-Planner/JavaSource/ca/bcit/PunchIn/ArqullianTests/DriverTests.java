package ca.bcit.PunchIn.ArqullianTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ EmployeeDriverTests.class, HrManagementDriverTests.class, ProjectDriverTests.class,
		ReportBeanTests.class, TimesheetDriverTests.class, UiDriverTests.class, LoginBeanTests.class })
public class DriverTests {

}
