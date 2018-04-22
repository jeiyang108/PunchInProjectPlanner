package ca.bcit.PunchIn.ArqullianTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CredentialDAOTests.class, EmployeeDAOTests.class, EmployeeDriverTests.class,
		HrManagementDriverTests.class, LoginBeanTests.class, ProjectDAOTests.class, ProjectDriverTests.class,
		ReportBeanTests.class, TimesheetDAOTests.class, TimesheetDriverTests.class, UiDriverTests.class })
public class AllTests {

}
