package ca.datamagic.noaa.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.logging.Logger;

import ca.datamagic.noaa.async.ImageTask;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 12/14/2016.
 */
public class SkewTFragment extends Fragment implements Renderer {
    private static Logger _logger = LogFactory.getLogger(SkewTFragment.class);

    public String getSkewTUrl() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            return arguments.getString("skewTUrl", null);
        }
        return null;
    }

    public void setSkewTUrl(String newVal) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            arguments.putString("skewTUrl", newVal);
        }
    }

    public static SkewTFragment newInstance() {
        return newInstance(null);
    }

    public static SkewTFragment newInstance(String skewTUrl) {
        SkewTFragment fragment = new SkewTFragment();
        Bundle bundle = new Bundle();
        bundle.putString("skewTUrl", skewTUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static int getLayoutId() {
        return R.layout.skewt_main;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.skewt_main, container, false);
        ImageView skewTView = (ImageView)view.findViewById(R.id.skewTView);
        String skewTUrl = getSkewTUrl();
        if ((skewTUrl != null) && (skewTUrl.length() > 0)) {
            ImageTask imageTask = new ImageTask(skewTUrl, skewTView);
            imageTask.execute((Void[]) null);
        } else {
            Bitmap blank = BitmapFactory.decodeResource(getResources(), R.drawable._blank);
            skewTView.setImageBitmap(blank);
        }
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        _logger.info("onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
        String skewTUrl = getSkewTUrl();
        _logger.info("skewTUrl: " + skewTUrl);
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
            ImageView skewTView = (ImageView)view.findViewById(R.id.skewTView);
            String skewTUrl = getSkewTUrl();
            if ((skewTUrl != null) && (skewTUrl.length() > 0)) {
                ImageTask imageTask = new ImageTask(skewTUrl, skewTView);
                imageTask.execute((Void[]) null);
            } else {
                Bitmap blank = BitmapFactory.decodeResource(getResources(), R.drawable._blank);
                skewTView.setImageBitmap(blank);
            }
        }
    }
}
