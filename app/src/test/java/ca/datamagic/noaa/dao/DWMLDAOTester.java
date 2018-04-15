package ca.datamagic.noaa.dao;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.dom.DWMLHandler;
import ca.datamagic.noaa.dto.DWMLDTO;

/**
 * Created by Greg on 2/18/2017.
 */
@RunWith(JUnit4.class)
public class DWMLDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        DWMLDAO.setFilesPath(getFilesPath());
        DWMLDAO dao = new DWMLDAO();
        DWMLDTO dwml = dao.load(33.81167, -118.14639, "e");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dwml);
        System.out.println("json: " + json);
        String xml = DWMLDAO.read();
        DWMLHandler handler = new DWMLHandler();
        DWMLDTO readDWML = handler.parse(xml);
        String readJSON = mapper.writeValueAsString(readDWML);
        Assert.assertEquals(json, readJSON);
    }

    @Test
    public void test2() throws Throwable {
        DWMLDAO.setFilesPath(getFilesPath());
        DWMLDAO dao = new DWMLDAO();
        DWMLDTO dwml = dao.load(67.1, -157.85, "e");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dwml);
        System.out.println("json: " + json);
        String xml = DWMLDAO.read();
        DWMLHandler handler = new DWMLHandler();
        DWMLDTO readDWML = handler.parse(xml);
        String readJSON = mapper.writeValueAsString(readDWML);
        Assert.assertEquals(json, readJSON);
    }
}
