package ca.datamagic.noaa.dao;

import android.graphics.Bitmap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by Greg on 1/1/2016.
 */
@RunWith(RobolectricTestRunner.class)
public class ImageDAOTester {
    @Test
    public void test1() throws Throwable {
        ImageDAO dao = new ImageDAO();
        Bitmap bitmap = dao.load("http://forecast.weather.gov/newimages/medium/sct.png");
    }
}
