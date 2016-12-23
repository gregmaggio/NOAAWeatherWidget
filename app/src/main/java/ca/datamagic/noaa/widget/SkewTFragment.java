package ca.datamagic.noaa.widget;

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
public class SkewTFragment extends Fragment {
    private ImageView _skewTView = null;
    private String _skewTUrl = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.skewt_main, container, false);
        _skewTView = (ImageView)view.findViewById(R.id.skewTView);
        render(_skewTUrl);
        return view;
    }

    public void render(String skewTUrl) {
        _skewTUrl = skewTUrl;
        if (_skewTView != null) {
            ImageTask imageTask = new ImageTask(_skewTUrl, _skewTView);
            imageTask.execute((Void[]) null);
        }
    }
}
