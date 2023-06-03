package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.widget.MainActivity;
import ca.datamagic.noaa.widget.Renderer;

public class RenderTask extends AsyncTaskBase<Void, Void, Void> {
    private Logger _logger = LogFactory.getLogger(RenderTask.class);
    private Renderer _renderer = null;

    public RenderTask(Renderer renderer) {
        _renderer = renderer;
    }

    @Override
    protected AsyncTaskResult<Void> doInBackground(Void... params) {
        _logger.info("Rendering...");
        try {
            Thread.sleep(250);
            MainActivity.getThisInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        _renderer.render();
                    } catch (Throwable t) {
                        _logger.warning("Throwable: " + t.getMessage());
                    }
                }
            });

            return new AsyncTaskResult<Void>();
        } catch (Throwable t) {
            return new AsyncTaskResult<Void>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Void> result) {
        _logger.info("...views refreshed.");
        fireCompleted(result);
    }
}
