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

/**
 * Created by Greg on 2/11/2017.
 */

public class LocationsHelper extends SQLiteOpenHelper {
    private static Logger _logger = LogFactory.getLogger(LocationsHelper.class);
    private static int _version = 1;
    private static String _dbName = "locations.db";
    private static String _createSQL = "CREATE TABLE location (description TEXT, city TEXT, state_code TEXT, latitude NUMERIC, longitude NUMERIC)";
    private static String _deleteSQL = "DROP TABLE IF EXISTS location";
    private String _databaseFileName = null;

    public LocationsHelper(Context context) {
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
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {
            HashMap<String, Station> stations = new HashMap<String, Station>();
            db = getReadableDatabase();
            String tableName = "location";
            String[] projection = {"description", "city", "state_code", "latitude", "longitude" };
            String sortOrder = "description ASC";
            cursor = db.query(tableName, projection, null, null, null, null, sortOrder);
            while (cursor.moveToNext()) {
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String stateCode = cursor.getString(cursor.getColumnIndexOrThrow("state_code"));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
                if (!stations.containsKey(description.toUpperCase())) {
                    Station station = new Station();
                    station.setStationName(description);
                    station.setState(stateCode);
                    station.setLatitude(latitude);
                    station.setLongitude(longitude);
                    stations.put(description.toUpperCase(), station);
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
