package ca.datamagic.noaa.dao;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.quadtree.Quad;
import ca.datamagic.quadtree.Station;

public class QuadTreeTester extends BaseTester {
    private static final double distance = 75;
    private static final String units = "statute miles";

    @Test
    public void test1() throws Throwable {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("C:/Dev/Android/NOAAWeatherWidget/app/src/test/res/rawtree.ser"));
        Quad tree = (Quad)inputStream.readObject();
        inputStream.close();
        Station station = tree.readNearest(31.6135868,-97.2284829, distance, units);
        Assert.assertNotNull(station);
    }
}
