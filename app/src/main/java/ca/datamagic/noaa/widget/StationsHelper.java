package ca.datamagic.noaa.widget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.quadtree.Station;

public class StationsHelper extends SQLiteOpenHelper {
    private static Logger _logger = LogFactory.getLogger(StationsHelper.class);
    private static int _version = 1;
    private static String _dbName = "stations_new.db";
    private static String _createSQL = "CREATE TABLE station (station_id TEXT, station_name TEXT, state TEXT, wfo TEXT, radar TEXT, latitude NUMERIC, longitude NUMERIC)";
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

    public void writeStations(List<Station> stations) {
        _logger.info("writeStations: " + stations.size());
        SQLiteDatabase db = getWritableDatabase();
        db.delete("station", null, null);
        for (int ii = 0; ii < stations.size(); ii++) {
            try {
                Station station = stations.get(ii);
                ContentValues values = new ContentValues();
                String stationId = station.getStationId();
                if ((stationId == null) || stationId.length() < 1) {
                    continue;
                }
                String stationName = station.getStationName();
                if ((stationName == null) || stationName.length() < 1) {
                    continue;
                }
                String state = station.getState();
                if ((state == null) || state.length() < 1) {
                    continue;
                }
                String wfo = station.getWFO();
                if ((wfo == null) || wfo.length() < 1) {
                    continue;
                }
                String radar = station.getRadar();
                if ((radar == null) || radar.length() < 1) {
                    continue;
                }
                values.put("station_id", stationId);
                values.put("station_name", stationName);
                values.put("state", state);
                values.put("wfo", wfo);
                values.put("radar", radar);
                values.put("latitude", station.getLatitude());
                values.put("longitude", station.getLongitude());
                db.insert("station", null, values);
            } catch (Throwable t) {
                _logger.warning("Error in write stations. Exception: " + t.getMessage());
            }
        }
    }

    public List<Station> readStations() {
        Cursor cursor = null;
        try {
            HashMap<String, Station> stations = new HashMap<String, Station>();

            SQLiteDatabase db = getReadableDatabase();

            String tableName = "station";
            String[] projection = {"station_id", "station_name", "state", "wfo", "radar", "latitude", "longitude" };
            String sortOrder = "station_name ASC";
            cursor = db.query(tableName, projection, null, null, null, null, sortOrder);
            while (cursor.moveToNext()) {
                try {
                    String stationId = cursor.getString(cursor.getColumnIndexOrThrow("station_id"));
                    if ((stationId == null) || stationId.length() < 1) {
                        continue;
                    }
                    String stationName = cursor.getString(cursor.getColumnIndexOrThrow("station_name"));
                    if ((stationName == null) || stationName.length() < 1) {
                        continue;
                    }
                    String state = cursor.getString(cursor.getColumnIndexOrThrow("state"));
                    if ((state == null) || state.length() < 1) {
                        continue;
                    }
                    String wfo = cursor.getString(cursor.getColumnIndexOrThrow("wfo"));
                    if ((wfo == null) || wfo.length() < 1) {
                        continue;
                    }
                    String radar = cursor.getString(cursor.getColumnIndexOrThrow("radar"));
                    if ((radar == null) || radar.length() < 1) {
                        continue;
                    }
                    double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                    double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
                    if (!stations.containsKey(stationId.toUpperCase())) {
                        Station station = new Station();
                        station.setStationId(stationId);
                        station.setStationName(stationName);
                        station.setState(state);
                        station.setWFO(wfo);
                        station.setRadar(radar);
                        station.setLatitude(latitude);
                        station.setLongitude(longitude);
                        stations.put(stationId.toUpperCase(), station);
                    }
                } catch (Throwable t) {
                    _logger.warning("Write Stations Error. Exception: " + t.getMessage());
                }
            }
            return new ArrayList<Station>(stations.values());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
