package ca.datamagic.noaa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.UUID;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.dto.PlaceDTO;
import ca.datamagic.noaa.dto.PredictionDTO;
import ca.datamagic.noaa.dto.PredictionListDTO;

/**
 * Created by Greg on 3/21/2018.
 */
@RunWith(JUnit4.class)
public class GooglePlacesDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        GooglePlacesDAO.setApiKey("Put Key Here");
        GooglePlacesDAO dao = new GooglePlacesDAO();
        PredictionListDTO predictions = dao.loadAutoCompletePredictions("Haymarket", UUID.randomUUID().toString().toUpperCase());
        System.out.println("predictions: " + predictions);
        PlaceDTO place = dao.loadPlace(predictions.get(0).getPlaceId());
        System.out.println("place: " + place);
        System.out.println("Name: " + place.getName());
        System.out.println("Latitude: " + place.getLatitude());
        System.out.println("Longitude: " + place.getLongitude());
        System.out.println("City: " + place.getCity());
        System.out.println("State: " + place.getState());
    }

    @Test
    public void test2() throws Throwable {
        GooglePlacesDAO.setApiKey("Put Key Here");
        GooglePlacesDAO dao = new GooglePlacesDAO();
        PredictionListDTO predictions = dao.loadAutoCompletePredictions("Colville,WA", UUID.randomUUID().toString().toUpperCase());
        System.out.println("predictions: " + predictions);
        PlaceDTO place = dao.loadPlace(predictions.get(0).getPlaceId());
        System.out.println("place: " + place);
        System.out.println("Name: " + place.getName());
        System.out.println("Latitude: " + place.getLatitude());
        System.out.println("Longitude: " + place.getLongitude());
        System.out.println("City: " + place.getCity());
        System.out.println("State: " + place.getState());
    }
}
