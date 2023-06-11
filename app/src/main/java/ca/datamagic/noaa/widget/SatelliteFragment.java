package ca.datamagic.noaa.widget;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.logging.Logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ca.datamagic.noaa.async.AccountingTask;
import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.RenderTask;
import ca.datamagic.noaa.async.SatelliteTask;
import ca.datamagic.noaa.current.CurrentStation;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class SatelliteFragment extends Fragment implements Renderer, NonSwipeableFragment {
    private static final Logger _logger = LogFactory.getLogger(SatelliteFragment.class);
    private ImageButton _zoomInButton = null;
    private ImageButton _zoomOutButton = null;
    private ImageButton _resetZoomButton = null;
    private SatelliteView _satelliteView = null;

    public static SatelliteFragment newInstance() {
        SatelliteFragment fragment = new SatelliteFragment();
        return fragment;
    }

    public static int getLayoutId() {
        return R.layout.satellite_main;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.satellite_main, container, false);
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        _logger.info("onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        _logger.info("onSaveInstanceState");
        super.onSaveInstanceState(outState);
        cleanup();
    }

    @Override
    public void render() {
        try {
            if (!MainActivity.getThisInstance().isFragmentActive(this)) {
                return;
            }
            View view = getView();
            if (view != null) {
                render(view);
            } else {
                RenderTask renderTask = new RenderTask(this);
                renderTask.execute();
            }
            (new AccountingTask("Satellite", "Render")).execute();
        } catch (IllegalStateException ex) {
            _logger.warning("IllegalStateException: " + ex.getMessage());
            RenderTask renderTask = new RenderTask(this);
            renderTask.execute();
        }
    }

    @Override
    public void cleanup() {
        try {
            if (_satelliteView != null) {
                _satelliteView.setSatelliteBitmap(null);
                _satelliteView.resetScale();
            }
        } catch (Throwable t) {
            _logger.warning("Radar view reset scale error: " + t.getMessage());
        }
        _satelliteView = null;
        _zoomInButton = null;
        _zoomOutButton = null;
        _resetZoomButton = null;
    }

    @Override
    public boolean canSwipe(float x, float y) {
        if (_satelliteView != null) {
            Rect outRect = new Rect();
            int[] location = new int[2];
            _satelliteView.getDrawingRect(outRect);
            _satelliteView.getLocationOnScreen(location);
            outRect.offset(location[0], location[1]);
            return !outRect.contains((int)x,(int)y);
        }
        return true;
    }

    private void render(View view) {
        _zoomInButton = view.findViewById(R.id.zoomInButton);
        _zoomInButton.setOnClickListener(new SatelliteFragment.ZoomInButtonListener());
        _zoomOutButton = view.findViewById(R.id.zoomOutButton);
        _zoomOutButton.setOnClickListener(new SatelliteFragment.ZoomOutButtonListener());
        _resetZoomButton = view.findViewById(R.id.resetZoomButton);
        _resetZoomButton.setOnClickListener(new SatelliteFragment.ResetZoomButtonListener());
        _satelliteView = view.findViewById(R.id.satelliteView);
        LinearLayout satelliteLayout = view.findViewById(R.id.satelliteLayout);
        TextView satelliteViewNotAvailable = view.findViewById(R.id.satelliteViewNotAvailable);
        PreferencesDAO preferencesDAO = new PreferencesDAO(getContext());
        PreferencesDTO preferencesDTO = preferencesDAO.read();
        if ((preferencesDTO.isTextOnly() != null) && preferencesDTO.isTextOnly().booleanValue()) {
            satelliteViewNotAvailable.setVisibility(View.VISIBLE);
            satelliteLayout.setVisibility(View.GONE);
            return;
        } else {
            satelliteViewNotAvailable.setVisibility(View.GONE);
            satelliteLayout.setVisibility(View.VISIBLE);
        }
        String state = null;
        StationDTO station = CurrentStation.getStation();
        if (station != null) {
            state = station.getState();
        }
        SatelliteTask task = new SatelliteTask(state);
        task.addListener(new AsyncTaskListener<Bitmap>() {
            @Override
            public void completed(AsyncTaskResult<Bitmap> result) {
                MainActivity.getThisInstance().stopBusy();
                Bitmap bitmap = result.getResult();
                if (bitmap != null) {
                    double width = bitmap.getWidth();
                    double height = bitmap.getHeight();
                    double aspectRatio = width / height;
                    _satelliteView.setAspectRatio(aspectRatio);
                    _satelliteView.setSatelliteBitmap(bitmap);
                } else {
                    _satelliteView.setSatelliteBitmap(null);
                }
                _satelliteView.invalidate();
            }
        });
        task.execute();
        MainActivity.getThisInstance().startBusy();
    }

    private class ZoomInButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                _satelliteView.zoomIn();
            } catch (Throwable t) {
                if ((t != null) && (t.getMessage() != null)) {
                    _logger.warning(t.getMessage());
                }
                _logger.warning("Unexpected Exception in ZoomInButtonListener.onClick.");
            }
        }
    }

    private class ZoomOutButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                _satelliteView.zoomOut();
            } catch (Throwable t) {
                if ((t != null) && (t.getMessage() != null)) {
                    _logger.warning(t.getMessage());
                }
                _logger.warning("Unexpected Exception in ZoomOutButtonListener.onClick.");
            }
        }
    }

    private class ResetZoomButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                _satelliteView.resetZoom();
            } catch (Throwable t) {
                if ((t != null) && (t.getMessage() != null)) {
                    _logger.warning(t.getMessage());
                }
                _logger.warning("Unexpected Exception in ResetZoomButtonListener.onClick.");
            }
        }
    }
}
