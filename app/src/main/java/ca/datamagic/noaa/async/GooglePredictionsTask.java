package ca.datamagic.noaa.async;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.GooglePlacesDAO;
import ca.datamagic.noaa.dto.PredictionListDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 3/24/2018.
 */

public class GooglePredictionsTask extends AsyncTaskBase<Void, Void, PredictionListDTO> {
    private Logger _logger = LogFactory.getLogger(GooglePredictionsTask.class);
    private Context _context = null;
    private CacheHelper _cacheHelper = null;
    private GooglePlacesDAO _dao = null;
    private String _searchText = null;
    private String _sessionToken = null;

    public GooglePredictionsTask(Context context, String searchText, String sessionToken) {
        _context = context;
        _cacheHelper = new CacheHelper(context);
        _dao = new GooglePlacesDAO();
        _searchText = searchText;
        _sessionToken = sessionToken;
    }

    @Override
    protected AsyncTaskResult<PredictionListDTO> doInBackground(Void... voids) {
        try {
            _logger.info("Loading predictions...");
            PredictionListDTO predictions = null;
            String json = readPredictions(_searchText);
            if ((json != null) && (json.length() > 0)) {
                _logger.info("Loading predictions from cache...");
                predictions = new PredictionListDTO(new JSONArray(json));
            }
            if (predictions == null) {
                _logger.info("Loading predictions from api...");
                predictions = _dao.loadAutoCompletePredictions(_searchText, _sessionToken);
                writePredictions(_searchText, predictions.toString());
            }
            return new AsyncTaskResult<PredictionListDTO>(predictions);
        } catch (Throwable t) {
            return new AsyncTaskResult<PredictionListDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<PredictionListDTO> result) {
        _logger.info("...Predictions loaded.");
        fireCompleted(result);
    }

    private String readPredictions(String searchText) {
        try {
            return _cacheHelper.readPredictions(searchText);
        } catch (Throwable t) {
            _logger.warning("ReadPredictions: " + t.getMessage());
        }
        return null;
    }

    private void writePredictions(String searchText, String predictions) {
        try {
            _cacheHelper.writePredictions(searchText, predictions);
        } catch (Throwable t) {
            _logger.warning("WritePredictions: " + t.getMessage());
        }
    }

    private static class CacheHelper extends SQLiteOpenHelper {
        private static final int _version = 1;
        private static final String _dbName = "google_predictions.db";

        public CacheHelper(Context context) {
            super(context, _dbName, null, _version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE google_predictions (search_text TEXT NOT NULL, results_json TEXT NOT NULL);");
            db.execSQL("CREATE INDEX google_predictions_idx_search_text ON google_predictions(search_text COLLATE NOCASE);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        public void writePredictions(String searchText, String predictions) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("search_text", searchText);
            values.put("results_json", predictions);
            db.insert("google_predictions", null, values);
        }

        public String readPredictions(String searchText) {
            SQLiteDatabase db = getReadableDatabase();
            String tableName = "google_predictions";
            String[] projection = {"results_json"};
            String whereClause = "search_text = ?";
            String[] whereArgs = new String[] { searchText };
            Cursor cursor = db.query(tableName, projection, whereClause, whereArgs, null, null, null);
            if (cursor.moveToNext()) {
                int columnIndex = cursor.getColumnIndex("results_json");
                if (columnIndex > -1) {
                    return cursor.getString(columnIndex);
                }
            }
            return null;
        }
    }
}
