package ca.datamagic.noaa.widget;

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

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.quadtree.Station;

public class OldStationsHelper extends SQLiteOpenHelper {
    private static Logger _logger = LogFactory.getLogger(OldStationsHelper.class);
    private static int _version = 1;
    private static String _dbName = "stations.db";
    private static String _createSQL = "CREATE TABLE station (station_id TEXT, station_name TEXT, street_number TEXT, street_name TEXT, city TEXT, state_code TEXT, state_name TEXT, zip TEXT, country_code TEXT, country_name TEXT, latitude NUMERIC, longitude NUMERIC, has_radiosonde TEXT)";
    private static String _deleteSQL = "DROP TABLE IF EXISTS station";
    private String _databaseFileName = null;

    public OldStationsHelper(Context context) {
        super(context, _dbName, null, _version);
        _databaseFileName = MessageFormat.format("{0}/{1}", context.getFilesDir().getAbsolutePath(), _dbName);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Do Nothing!!!
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do Nothing!!!
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do Nothing!!!
    }

    public List<Station> readStations() {
        File file = new File(_databaseFileName);
        if (!file.exists()) {
            return null;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            HashMap<String, Station> stations = new HashMap<String, Station>();
            db = getReadableDatabase();
            String tableName = "station";
            String[] projection = {"station_id", "station_name", "street_number", "street_name", "city", "state_code", "state_name", "zip", "country_code", "country_name", "latitude", "longitude", "has_radiosonde"};
            String sortOrder = "station_name ASC";
            cursor = db.query(tableName, projection, null, null, null, null, sortOrder);
            while (cursor.moveToNext()) {
                String stationId = cursor.getString(cursor.getColumnIndexOrThrow("station_id"));
                String stationName = cursor.getString(cursor.getColumnIndexOrThrow("station_name"));
                String stateCode = cursor.getString(cursor.getColumnIndexOrThrow("state_code"));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
                if (!stations.containsKey(stationId.toUpperCase())) {
                    Station station = new Station();
                    station.setStationId(stationId);
                    station.setStationName(stationName);
                    station.setState(stateCode);
                    station.setLatitude(latitude);
                    station.setLongitude(longitude);
                    stations.put(stationId.toUpperCase(), station);
                }
            }
            return new ArrayList<Station>(stations.values());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
            file.delete();
        }
    }
}
