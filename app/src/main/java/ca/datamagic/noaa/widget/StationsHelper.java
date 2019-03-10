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

import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.logging.LogFactory;

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

    public void writeStations(List<StationDTO> stations) {
        _logger.info("writeStations: " + stations.size());
        SQLiteDatabase db = getWritableDatabase();
        db.delete("station", null, null);
        for (int ii = 0; ii < stations.size(); ii++) {
            StationDTO station = stations.get(ii);
            ContentValues values = new ContentValues();
            values.put("station_id", station.getStationId());
            values.put("station_name", station.getStationName());
            values.put("state", station.getState());
            values.put("wfo", station.getWFO());
            values.put("radar", station.getRadar());
            values.put("latitude", station.getLatitude());
            values.put("longitude", station.getLongitude());
            db.insert("station", null, values);
        }
    }

    public List<StationDTO> readStations() {
        Cursor cursor = null;
        try {
            HashMap<String, StationDTO> stations = new HashMap<String, StationDTO>();

            SQLiteDatabase db = getReadableDatabase();

            String tableName = "station";
            String[] projection = {"station_id", "station_name", "state", "wfo", "radar", "latitude", "longitude" };
            String sortOrder = "station_name ASC";
            cursor = db.query(tableName, projection, null, null, null, null, sortOrder);
            while (cursor.moveToNext()) {
                String stationId = cursor.getString(cursor.getColumnIndexOrThrow("station_id"));
                String stationName = cursor.getString(cursor.getColumnIndexOrThrow("station_name"));
                String state = cursor.getString(cursor.getColumnIndexOrThrow("state"));
                String wfo = cursor.getString(cursor.getColumnIndexOrThrow("wfo"));
                String radar = cursor.getString(cursor.getColumnIndexOrThrow("radar"));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
                if (!stations.containsKey(stationId.toUpperCase())) {
                    StationDTO station = new StationDTO();
                    station.setStationId(stationId);
                    station.setStationName(stationName);
                    station.setState(state);
                    station.setWFO(wfo);
                    station.setRadar(radar);
                    station.setLatitude(latitude);
                    station.setLongitude(longitude);
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
