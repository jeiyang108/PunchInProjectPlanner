package ca.bcit.PunchIn.ArqullianTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CredentialDAOTests.class, EmployeeDAOTests.class, ProjectDAOTests.class, TimesheetDAOTests.class })
public class DAOTests {

}
