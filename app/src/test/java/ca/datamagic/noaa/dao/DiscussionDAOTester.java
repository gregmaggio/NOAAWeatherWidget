package ca.datamagic.noaa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by Greg on 1/2/2016.
 */
@RunWith(JUnit4.class)
public class DiscussionDAOTester {
    @Test
    public void test1() throws Exception {
        DiscussionDAO dao = new DiscussionDAO();
        String discussion = dao.load("EAX");
        System.out.println(discussion);
    }
}
