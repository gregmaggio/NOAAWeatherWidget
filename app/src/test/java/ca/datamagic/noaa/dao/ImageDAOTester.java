package ca.datamagic.noaa.dao;

import android.graphics.Bitmap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import ca.datamagic.noaa.widget.BuildConfig;

/**
 * Created by Greg on 1/1/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ImageDAOTester {
    @Test
    public void test1() throws Throwable {
        ImageDAO dao = new ImageDAO();
        Bitmap bitmap = dao.load("http://forecast.weather.gov/newimages/medium/sct.png");
    }
}
