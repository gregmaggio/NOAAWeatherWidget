package ca.datamagic.noaa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.dto.PlaceDTO;
import ca.datamagic.noaa.dto.PredictionDTO;

/**
 * Created by Greg on 3/21/2018.
 */
@RunWith(JUnit4.class)
public class GooglePlacesDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        GooglePlacesDAO dao = new GooglePlacesDAO();
        List<PredictionDTO> predictions = dao.loadAutoCompletePredictions("Haymarket");
        PlaceDTO place = dao.loadPlace(predictions.get(0).getPlaceId());
        System.out.println("Name: " + place.getName());
        System.out.println("Latitude: " + place.getLatitude());
        System.out.println("Longitude: " + place.getLongitude());
        System.out.println("City: " + place.getCity());
        System.out.println("State: " + place.getState());
    }

    @Test
    public void test2() throws Throwable {
        GooglePlacesDAO dao = new GooglePlacesDAO();
        List<PredictionDTO> predictions = dao.loadAutoCompletePredictions("Colville,WA");
        PlaceDTO place = dao.loadPlace(predictions.get(0).getPlaceId());
        System.out.println("Name: " + place.getName());
        System.out.println("Latitude: " + place.getLatitude());
        System.out.println("Longitude: " + place.getLongitude());
        System.out.println("City: " + place.getCity());
        System.out.println("State: " + place.getState());
    }
}
