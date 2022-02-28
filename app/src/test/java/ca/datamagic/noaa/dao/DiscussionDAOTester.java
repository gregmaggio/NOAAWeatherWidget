package ca.datamagic.noaa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;

/**
 * Created by Greg on 1/2/2016.
 */
@RunWith(JUnit4.class)
public class DiscussionDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        DiscussionDAO dao = new DiscussionDAO();
        String discussion = dao.load("LZK");
        System.out.println(discussion);
    }
}
