package ca.datamagic.noaa.dao;

import android.graphics.Bitmap;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileOutputStream;
import java.util.List;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.dto.RadarDTO;
import ca.datamagic.noaa.dto.RadarImageMetaDataDTO;
import ca.datamagic.noaa.dto.RadarTimeDTO;

public class RadarDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        RadarDAO dao = new RadarDAO();
        RadarDTO radar = dao.loadNearest(39.0045396, -76.9066824);
        System.out.println("radar: " + mapper.writeValueAsString(radar));
        String urlType = dao.loadUrlType(radar.getICAO());
        System.out.println("urlType: " + urlType);
        String[] urls = dao.loadUrls(radar.getICAO());
        System.out.println("urls: " + mapper.writeValueAsString(urls));
        String imageUrl = urls[0];
        Bitmap bitmap = dao.loadImage(imageUrl);
        Assert.assertNotNull(bitmap);
    }
}
