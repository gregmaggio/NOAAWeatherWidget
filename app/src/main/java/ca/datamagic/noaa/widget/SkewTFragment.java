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

import ca.datamagic.noaa.async.ImageTask;

/**
 * Created by Greg on 12/14/2016.
 */
public class SkewTFragment extends Fragment implements Renderer {
    private String _skewTUrl = null;

    public void setSkewTUrl(String newVal) {
        _skewTUrl = newVal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.skewt_main, container, false);
        ImageView skewTView = (ImageView)view.findViewById(R.id.skewTView);
        if ((_skewTUrl != null) && (_skewTUrl.length() > 0)) {
            ImageTask imageTask = new ImageTask(_skewTUrl, skewTView);
            imageTask.execute((Void[]) null);
        } else {
            Bitmap blank = BitmapFactory.decodeResource(getResources(), R.drawable._blank);
            skewTView.setImageBitmap(blank);
        }
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            String skewTUrl = savedInstanceState.getString("skewTUrl");
            _skewTUrl = skewTUrl;
        } catch (NullPointerException ex) {
            // Do Nothing
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("skewTUrl", ((_skewTUrl == null) ? "" : _skewTUrl));
    }

    @Override
    public void render() {
        View view = getView();
        if (view != null) {
            ImageView skewTView = (ImageView)view.findViewById(R.id.skewTView);
            if ((_skewTUrl != null) && (_skewTUrl.length() > 0)) {
                ImageTask imageTask = new ImageTask(_skewTUrl, skewTView);
                imageTask.execute((Void[]) null);
            } else {
                Bitmap blank = BitmapFactory.decodeResource(getResources(), R.drawable._blank);
                skewTView.setImageBitmap(blank);
            }
        }
    }
}
