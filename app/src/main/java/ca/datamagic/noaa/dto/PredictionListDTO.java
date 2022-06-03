package ca.datamagic.noaa.dto;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PredictionListDTO {
    private List<PredictionDTO> _items = new ArrayList<PredictionDTO>();
    private String _json = null;

    public PredictionListDTO(JSONArray predictions) throws JSONException {
        for (int ii = 0; ii < predictions.length(); ii++) {
            PredictionDTO prediction = new PredictionDTO(predictions.getJSONObject(ii));
            if ((prediction.getDescription() != null) && (prediction.getDescription().length() > 0) && (prediction.getDescription().toUpperCase().contains("USA"))) {
                _items.add(prediction);
            }
        }
        _json = predictions.toString();
    }

    public PredictionDTO get(int index) {
        return _items.get(index);
    }

    public int size() {
        return _items.size();
    }

    @Override
    public String toString() {
        return  _json;
    }
}
