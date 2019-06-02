package ca.datamagic.noaa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;

@RunWith(JUnit4.class)
public class AccountingDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        AccountingDAO dao = new AccountingDAO();
        dao.post(39.0045396, -76.9066824, "Test", "Test");
    }
}
