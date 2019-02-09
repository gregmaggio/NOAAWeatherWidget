package ca.datamagic.noaa.async;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;

public class WFOTask extends AsyncTaskBase<Void, Void, String> {
    private static Logger _logger = LogFactory.getLogger(WFOTask.class);
    private String _url = null;

    public WFOTask() {

    }

    public void setUrl(String newVal) {
        _url = newVal;
    }

    @Override
    protected AsyncTaskResult<String> doInBackground(Void... params) {
        try {
            _logger.info("Loading WFO...");
            _logger.info("url: " + _url);
            if ((_url == null) || (_url.length() < 1)) {
                return new AsyncTaskResult<String>("");
            }
            URL url = new URL(_url);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            _logger.info("responseCode: " + responseCode);
            if (responseCode == 200) {
                String spec = _url;
                if (spec.endsWith("/")) {
                    spec = spec.substring(0, spec.length() - 1);
                }
                int index = spec.lastIndexOf('/');
                String wfo = spec.substring(index + 1).toUpperCase();
                _logger.info("wfo: " + wfo);
                return new AsyncTaskResult<String>(wfo);
            }
            String responseMessage = connection.getResponseMessage();
            _logger.info("responseMessage: " + responseMessage);
            Map<String, List<String>> headerFields = connection.getHeaderFields();
            Set<String> keys = headerFields.keySet();
            for (String key : keys) {
                _logger.info("key: " + key);
                if ((key != null) && (key.compareToIgnoreCase("location") == 0)) {
                    List<String> values = headerFields.get(key);
                    if (values.size() > 0) {
                        String spec = values.get(0);
                        if (spec.endsWith("/")) {
                            spec = spec.substring(0, spec.length() - 1);
                        }
                        int index = spec.lastIndexOf('/');
                        String wfo = spec.substring(index + 1).toUpperCase();
                        _logger.info("wfo: " + wfo);
                        return new AsyncTaskResult<String>(wfo);
                    }
                }
            }
            return new AsyncTaskResult<String>("");
        } catch (Throwable t) {
            return new AsyncTaskResult<String>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<String> result) {
        _logger.info("...wfo loaded.");
        fireCompleted(result);
    }
}
