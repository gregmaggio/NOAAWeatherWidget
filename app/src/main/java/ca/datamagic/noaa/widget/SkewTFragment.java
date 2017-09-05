package ca.datamagic.noaa.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
    private ImageView _skewTView = null;
    private Bitmap _blank = null;
    private String _skewTUrl = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.skewt_main, container, false);
        _skewTView = (ImageView)view.findViewById(R.id.skewTView);
        _blank = BitmapFactory.decodeResource(getResources(), R.drawable._blank);
        return view;
    }

    public String getSkewTUrl() {
        return _skewTUrl;
    }

    public void setSkewTUrl(String newVal) {
        _skewTUrl = newVal;
    }

    @Override
    public void render() {
        if (_skewTView != null) {
            if ((_skewTUrl != null) && (_skewTUrl.length() > 0)) {
                ImageTask imageTask = new ImageTask(_skewTUrl, _skewTView);
                imageTask.execute((Void[]) null);
            } else {
                _skewTView.setImageBitmap(_blank);
            }
        }
    }
}
