package ca.datamagic.noaa.async;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.logging.Logger;

import ca.datamagic.noaa.dao.GoogleTimeZoneDAO;
import ca.datamagic.noaa.dto.TimeZoneDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class GoogleTimeZoneTask extends AsyncTaskBase<TimeZoneDTO> {
    private Logger _logger = LogFactory.getLogger(GoogleTimeZoneTask.class);
    private Context _context = null;
    private CacheHelper _cacheHelper = null;
    private GoogleTimeZoneDAO _googleTimeZoneDAO = null;
    private Double _latitude = null;
    private Double _longitude = null;
    private String _sessionToken = null;

    public GoogleTimeZoneTask(Context context, Double latitude, Double longitude, String sessionToken) {
        _context = context;
        _cacheHelper = new CacheHelper(context);
        _googleTimeZoneDAO = new GoogleTimeZoneDAO();
        _latitude = latitude;
        _longitude = longitude;
        _sessionToken = sessionToken;
    }

    @Override
    protected AsyncTaskResult<TimeZoneDTO> doInBackground() {
        try {
            _logger.info("Loading TimeZone...");
            TimeZoneDTO timeZone = null;
            String json = readTimeZone(_latitude, _longitude);
            if ((json != null) && (json.length() > 0)) {
                _logger.info("Loading time zone from cache...");
                timeZone = new TimeZoneDTO(new JSONObject(json));
            }
            if (timeZone == null) {
                _logger.info("Loading time zone from api...");
                timeZone = _googleTimeZoneDAO.loadTimeZone(_latitude, _longitude, _sessionToken);
                writePlace(_latitude, _longitude, timeZone.toString());
            }
            return new AsyncTaskResult<TimeZoneDTO>(timeZone);
        } catch (Throwable t) {
            return new AsyncTaskResult<TimeZoneDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<TimeZoneDTO> result) {
        _logger.info("...TimeZone loaded.");
        fireCompleted(result);
    }

    private String readTimeZone(Double latitude, Double longitude) {
        try {
            return _cacheHelper.readTimeZone(latitude, longitude);
        } catch (Throwable t) {
            _logger.warning("ReadTimeZone: " + t.getMessage());
        }
        return null;
    }

    private void writePlace(Double latitude, Double longitude, String timeZoneJSON) {
        try {
            _cacheHelper.writeTimeZone(latitude, longitude, timeZoneJSON);
        } catch (Throwable t) {
            _logger.warning("WriteTimeZone: " + t.getMessage());
        }
    }

    private static class CacheHelper extends SQLiteOpenHelper {
        private static final int _version = 1;
        private static final String _dbName = "google_timezones.db";
        private static DecimalFormat _latLonFormat = new DecimalFormat("0.00000");

        public CacheHelper(Context context) {
            super(context, _dbName, null, _version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE google_timezones (lat_lon_id TEXT NOT NULL, timezone_json TEXT NOT NULL);");
            db.execSQL("CREATE INDEX google_timezones_idx_lat_lon_id ON google_timezones(lat_lon_id COLLATE NOCASE);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        private static String getLatLonId(Double latitude, Double longitude) {
            StringBuilder builder = new StringBuilder();
            builder.append(_latLonFormat.format(latitude));
            builder.append("-");
            builder.append(_latLonFormat.format(longitude));
            return builder.toString();
        }

        public void writeTimeZone(Double latitude, Double longitude, String timeZoneJSON) {
            String latLonId = getLatLonId(latitude, longitude);
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("lat_lon_id", latLonId);
            values.put("timezone_json", timeZoneJSON);
            db.insert("google_timezones", null, values);
        }

        public String readTimeZone(Double latitude, Double longitude) {
            String latLonId = getLatLonId(latitude, longitude);
            SQLiteDatabase db = getReadableDatabase();
            String tableName = "google_timezones";
            String[] projection = {"timezone_json"};
            String whereClause = "lat_lon_id = ?";
            String[] whereArgs = new String[] { latLonId };
            Cursor cursor = db.query(tableName, projection, whereClause, whereArgs, null, null, null);
            if (cursor.moveToNext()) {
                int columnIndex = cursor.getColumnIndex("timezone_json");
                if (columnIndex > -1) {
                    return cursor.getString(columnIndex);
                }
            }
            return null;
        }
    }
}
