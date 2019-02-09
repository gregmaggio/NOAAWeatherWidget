package ca.datamagic.noaa.widget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.dto.LocationDTO;
import ca.datamagic.noaa.dto.PointDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 2/11/2017.
 */

public class LocationsHelper extends SQLiteOpenHelper {
    private static Logger _logger = LogFactory.getLogger(LocationsHelper.class);
    private static int _version = 1;
    private static String _dbName = "locations.db";
    private static String _createSQL = "CREATE TABLE location (description TEXT, city TEXT, state_code TEXT, latitude NUMERIC, longitude NUMERIC)";
    private static String _deleteSQL = "DROP TABLE IF EXISTS location";
    private String _filesPath = null;

    public LocationsHelper(Context context) {
        super(context, _dbName, null, _version);
        _filesPath = context.getFilesDir().getAbsolutePath();
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

    public void writeLocations(List<LocationDTO> locations) {
        SQLiteDatabase db = null;
        try {
            _logger.info("writeLocations: " + locations.size());
            db = getWritableDatabase();
            db.delete("location", null, null);
            for (int ii = 0; ii < locations.size(); ii++) {
                LocationDTO location = locations.get(ii);
                _logger.info("location: " + location);
                ContentValues values = new ContentValues();
                values.put("description", location.getDescription());
                values.put("city", location.getCity());
                values.put("state_code", location.getState());
                values.put("latitude", location.getPoint().getLatitude());
                values.put("longitude", location.getPoint().getLongitude());
                db.insert("location", null, values);
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public List<LocationDTO> readLocations() {
        Cursor cursor = null;
        SQLiteDatabase stationsDb = null;
        SQLiteDatabase db = null;
        try {
            HashMap<String, LocationDTO> locations = new HashMap<String, LocationDTO>();

            String stationsDatabase = MessageFormat.format("{0}/stations.db", _filesPath);
            File stationsFile = new File(stationsDatabase);
            if (stationsFile.exists()) {
                stationsDb = SQLiteDatabase.openDatabase(stationsDatabase, null, 0);
                String tableName = "station";
                String[] projection = {"station_id", "station_name", "street_number", "street_name", "city", "state_code", "state_name", "zip", "country_code", "country_name", "latitude", "longitude", "has_radiosonde"};
                String sortOrder = "station_name ASC";
                cursor = stationsDb.query(tableName, projection, null, null, null, null, sortOrder);
                while (cursor.moveToNext()) {
                    String stationName = cursor.getString(cursor.getColumnIndexOrThrow("station_name"));
                    String city = cursor.getString(cursor.getColumnIndexOrThrow("city"));
                    String stateCode = cursor.getString(cursor.getColumnIndexOrThrow("state_code"));
                    double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                    double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
                    if (!locations.containsKey(stationName.toUpperCase())) {
                        LocationDTO location = new LocationDTO();
                        _logger.info("location: " + location);
                        location.setDescription(stationName);
                        location.setCity(city);
                        location.setState(stateCode);
                        location.setPoint(new PointDTO(latitude, longitude));
                        locations.put(stationName.toUpperCase(), location);
                    }
                }
                cursor.close();
                cursor = null;
                stationsDb.close();
                stationsDb = null;
                stationsFile.delete();
            }

            db = getReadableDatabase();

            String tableName = "location";
            String[] projection = {"description", "city", "state_code", "latitude", "longitude" };
            String sortOrder = "description ASC";
            cursor = db.query(tableName, projection, null, null, null, null, sortOrder);
            while (cursor.moveToNext()) {
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String city = cursor.getString(cursor.getColumnIndexOrThrow("city"));
                String stateCode = cursor.getString(cursor.getColumnIndexOrThrow("state_code"));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
                if (!locations.containsKey(description.toUpperCase())) {
                    LocationDTO location = new LocationDTO();
                    _logger.info("location: " + location);
                    location.setDescription(description);
                    location.setCity(city);
                    location.setState(stateCode);
                    location.setPoint(new PointDTO(latitude, longitude));
                    locations.put(description.toUpperCase(), location);
                }
            }
            return new ArrayList<LocationDTO>(locations.values());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (stationsDb != null) {
                stationsDb.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }
}
