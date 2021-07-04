package ca.datamagic.noaa.dao;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.util.List;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.dto.RadarTimeDTO;

public class RadarDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        RadarDAO dao = new RadarDAO();
        List<RadarTimeDTO> radarTimes = dao.getTimeStamps();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(radarTimes);
        System.out.println(json);
    }
}
