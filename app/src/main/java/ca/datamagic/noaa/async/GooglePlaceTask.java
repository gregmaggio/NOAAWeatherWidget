package ca.datamagic.noaa.async;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONObject;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.GooglePlacesDAO;
import ca.datamagic.noaa.dto.PlaceDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 3/24/2018.
 */

public class GooglePlaceTask extends AsyncTaskBase<Void, Void, PlaceDTO> {
    private Logger _logger = LogFactory.getLogger(GooglePlaceTask.class);
    private Context _context = null;
    private CacheHelper _cacheHelper = null;
    private GooglePlacesDAO _googlePlacesDAO = null;
    private String _placeId = null;

    public GooglePlaceTask(Context context, String placeId) {
        _context = context;
        _cacheHelper = new CacheHelper(context);
        _googlePlacesDAO = new GooglePlacesDAO();
        _placeId = placeId;
    }

    @Override
    protected AsyncTaskResult<PlaceDTO> doInBackground(Void... voids) {
        try {
            _logger.info("Loading place...");
            PlaceDTO place = null;
            String json = readPlace(_placeId);
            if ((json != null) && (json.length() > 0)) {
                _logger.info("Loading place from cache...");
                place = new PlaceDTO(new JSONObject(json));
            }
            if (place == null) {
                _logger.info("Loading place from api...");
                place = _googlePlacesDAO.loadPlace(_placeId);
                writePlace(_placeId, place.toString());
            }
            return new AsyncTaskResult<PlaceDTO>(place);
        } catch (Throwable t) {
            return new AsyncTaskResult<PlaceDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<PlaceDTO> result) {
        _logger.info("...Place loaded.");
        fireCompleted(result);
    }

    private String readPlace(String placeId) {
        try {
            return _cacheHelper.readPlace(placeId);
        } catch (Throwable t) {
            _logger.warning("ReadPredictions: " + t.getMessage());
        }
        return null;
    }

    private void writePlace(String placeId, String placeJSON) {
        try {
            _cacheHelper.writePlace(placeId, placeJSON);
        } catch (Throwable t) {
            _logger.warning("WritePredictions: " + t.getMessage());
        }
    }

    private static class CacheHelper extends SQLiteOpenHelper {
        private static final int _version = 1;
        private static final String _dbName = "google_places.db";

        public CacheHelper(Context context) {
            super(context, _dbName, null, _version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE google_places (place_id TEXT NOT NULL, place_json TEXT NOT NULL);");
            db.execSQL("CREATE INDEX google_places_idx_place_id ON google_places(place_id COLLATE NOCASE);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        public void writePlace(String placeId, String placeJSON) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("place_id", placeId);
            values.put("place_json", placeJSON);
            db.insert("google_places", null, values);
        }

        public String readPlace(String placeId) {
            SQLiteDatabase db = getReadableDatabase();
            String tableName = "google_places";
            String[] projection = {"place_json"};
            String whereClause = "place_id = ?";
            String[] whereArgs = new String[] { placeId };
            Cursor cursor = db.query(tableName, projection, whereClause, whereArgs, null, null, null);
            if (cursor.moveToNext()) {
                int columnIndex = cursor.getColumnIndex("place_json");
                if (columnIndex > -1) {
                    return cursor.getString(columnIndex);
                }
            }
            return null;
        }
    }
}
