package ca.datamagic.noaa.widget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.datamagic.noaa.dto.StationDTO;

/**
 * Created by Greg on 2/11/2017.
 */

public class StationsHelper extends SQLiteOpenHelper {
    private static int _version = 1;
    private static String _dbName = "stations.db";
    private static String _createSQL = "CREATE TABLE station (station_id TEXT, station_name TEXT, street_number TEXT, street_name TEXT, city TEXT, state_code TEXT, state_name TEXT, zip TEXT, country_code TEXT, country_name TEXT, latitude NUMERIC, longitude NUMERIC, has_radiosonde TEXT)";
    private static String _deleteSQL = "DROP TABLE IF EXISTS station";

    public StationsHelper(Context context) {
        super(context, _dbName, null, _version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(_createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(_deleteSQL);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void writeStations(List<StationDTO> stations) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("station", null, null);
        for (int ii = 0; ii < stations.size(); ii++) {
            StationDTO station = stations.get(ii);
            ContentValues values = new ContentValues();
            values.put("station_id", station.getStationId());
            values.put("station_name", station.getStationName());
            values.put("street_number", station.getStreetNumber());
            values.put("street_name", station.getStreetName());
            values.put("city", station.getCity());
            values.put("state_code", station.getStateCode());
            values.put("state_name", station.getStateName());
            values.put("zip", station.getZip());
            values.put("country_code", station.getCountryCode());
            values.put("country_name", station.getCountryName());
            values.put("latitude", station.getLatitude());
            values.put("longitude", station.getLongitude());
            values.put("has_radiosonde", (station.getHasRadiosonde() ? "Y" : "N"));
            db.insert("station", null, values);
        }
    }

    public List<StationDTO> readStations() {
        Cursor cursor = null;
        try {
            HashMap<String, StationDTO> stations = new HashMap<String, StationDTO>();

            SQLiteDatabase db = getReadableDatabase();

            String tableName = "station";
            String[] projection = {"station_id", "station_name", "street_number", "street_name", "city", "state_code", "state_name", "zip", "country_code", "country_name", "latitude", "longitude", "has_radiosonde"};
            String sortOrder = "station_name ASC";
            cursor = db.query(tableName, projection, null, null, null, null, sortOrder);
            while (cursor.moveToNext()) {
                String stationId = cursor.getString(cursor.getColumnIndexOrThrow("station_id"));
                String stationName = cursor.getString(cursor.getColumnIndexOrThrow("station_name"));
                String streetNumber = cursor.getString(cursor.getColumnIndexOrThrow("street_number"));
                String streetName = cursor.getString(cursor.getColumnIndexOrThrow("street_name"));
                String city = cursor.getString(cursor.getColumnIndexOrThrow("city"));
                String stateCode = cursor.getString(cursor.getColumnIndexOrThrow("state_code"));
                String stateName = cursor.getString(cursor.getColumnIndexOrThrow("state_name"));
                String zip = cursor.getString(cursor.getColumnIndexOrThrow("zip"));
                String countryCode = cursor.getString(cursor.getColumnIndexOrThrow("country_code"));
                String countryName = cursor.getString(cursor.getColumnIndexOrThrow("country_name"));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
                String hasRadiosonde = cursor.getString(cursor.getColumnIndexOrThrow("has_radiosonde"));
                if (!stations.containsKey(stationId.toUpperCase())) {
                    StationDTO station = new StationDTO();
                    station.setStationId(stationId);
                    station.setStationName(stationName);
                    station.setStreetNumber(streetNumber);
                    station.setStreetName(streetName);
                    station.setCity(city);
                    station.setStateCode(stateCode);
                    station.setStateName(stateName);
                    station.setZip(zip);
                    station.setCountryCode(countryCode);
                    station.setCountryName(countryName);
                    station.setLatitude(latitude);
                    station.setLongitude(longitude);
                    station.setHasRadiosonde((hasRadiosonde.compareToIgnoreCase("Y") == 0));
                    stations.put(stationId.toUpperCase(), station);
                }
            }
            return new ArrayList<StationDTO>(stations.values());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
