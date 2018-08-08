package ca.datamagic.noaa.widget;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.logging.Logger;

import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.SkewTTask;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 12/14/2016.
 */
public class SkewTFragment extends Fragment implements Renderer {
    private static Logger _logger = LogFactory.getLogger(SkewTFragment.class);
    private boolean _downloadingSkewTBitmap = false;

    public String getSkewTStation() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            return arguments.getString("skewTStation");
        }
        return null;
    }

    public void setSkewTStation(String newVal) {
        boolean skewTStationSet = false;
        Bundle arguments = getArguments();
        if (arguments != null) {
            String curVal = arguments.getString("skewTStation");
            if ((curVal != null) && (newVal != null)) {
                if (curVal != newVal) {
                    arguments.putString("skewTStation", newVal);
                    skewTStationSet = true;
                }
            } else if ((curVal == null) && (newVal != null)) {
                arguments.putString("skewTStation", newVal);
                skewTStationSet = true;
            } else if ((curVal != null) && (newVal == null)) {
                arguments.putString("skewTStation", newVal);
                skewTStationSet = true;
            }

        }
        if (skewTStationSet) {
            setSkewTBitmap(null);
        }
    }

    public Bitmap getSkewTBitmap() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            return arguments.getParcelable("skewTBitmap");
        }
        return null;
    }

    public void setSkewTBitmap(Bitmap newVal) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            arguments.putParcelable("skewTBitmap", newVal);
        }
        try {
            View view = getView();
            SkewTView skewTView = (SkewTView)view.findViewById(R.id.skewTView);
            skewTView.setSkewTBitmap(null);
            skewTView.invalidate();
        } catch (Throwable t) {
            _logger.warning("Exception: " + t.getMessage());
        }
    }

    public static SkewTFragment newInstance() {
        return newInstance(null);
    }

    public static SkewTFragment newInstance(String skewTStation) {
        SkewTFragment fragment = new SkewTFragment();
        Bundle bundle = new Bundle();
        bundle.putString("skewTStation", skewTStation);
        bundle.putParcelable("skewTBitmap", null);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static int getLayoutId() {
        return R.layout.skewt_main;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.skewt_main, container, false);
        //SkewTView skewTView = (SkewTView)view.findViewById(R.id.skewTView);
        //skewTView.setSkewTBitmap(getSkewTBitmap());
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
    }

    @Override
    public void render() {
        View view = getView();
        if (view != null) {
            String skewTStation = getSkewTStation();
            Bitmap skewTBitmap = getSkewTBitmap();
            if ((skewTBitmap == null) && (skewTStation != null)) {
                initializeSkewTBitmap(skewTStation);
            } else if (skewTBitmap != null) {
                SkewTView skewTView = (SkewTView)view.findViewById(R.id.skewTView);
                skewTView.setSkewTBitmap(skewTBitmap);
                skewTView.invalidate();
            }
        }
    }

    private void initializeSkewTBitmap(String skewTStation) {
        if (_downloadingSkewTBitmap) {
            return;
        }
        _downloadingSkewTBitmap = true;
        MainActivity.getThisInstance().startBusy();
        SkewTTask task = new SkewTTask(skewTStation);
        task.addListener(new AsyncTaskListener<Bitmap>() {
            @Override
            public void completed(AsyncTaskResult<Bitmap> result) {
                _downloadingSkewTBitmap = false;
                MainActivity.getThisInstance().stopBusy();
                if (result.getThrowable() != null) {
                    _logger.warning("Exception: " + result.getThrowable().getMessage());
                } else {
                    setSkewTBitmap(result.getResult());
                    try {
                        View view = getView();
                        SkewTView skewTView = (SkewTView)view.findViewById(R.id.skewTView);
                        skewTView.setSkewTBitmap(getSkewTBitmap());
                        skewTView.invalidate();
                    } catch (Throwable t) {
                        _logger.warning("Exception: " + result.getThrowable().getMessage());
                    }
                }
            }
        });
        task.execute((Void)null);
    }
}
